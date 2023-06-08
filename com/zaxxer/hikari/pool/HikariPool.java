/*     */ package com.zaxxer.hikari.pool;
/*     */ 
/*     */ import com.codahale.metrics.MetricRegistry;
/*     */ import com.codahale.metrics.health.HealthCheckRegistry;
/*     */ import com.zaxxer.hikari.HikariConfig;
/*     */ import com.zaxxer.hikari.HikariPoolMXBean;
/*     */ import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
/*     */ import com.zaxxer.hikari.metrics.PoolStats;
/*     */ import com.zaxxer.hikari.metrics.dropwizard.CodahaleHealthChecker;
/*     */ import com.zaxxer.hikari.metrics.dropwizard.CodahaleMetricsTrackerFactory;
/*     */ import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
/*     */ import com.zaxxer.hikari.util.ClockSource;
/*     */ import com.zaxxer.hikari.util.ConcurrentBag;
/*     */ import com.zaxxer.hikari.util.SuspendResumeLock;
/*     */ import com.zaxxer.hikari.util.UtilityElf;
/*     */ import io.micrometer.core.instrument.MeterRegistry;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLTransientConnectionException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.sql.DataSource;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HikariPool
/*     */   extends PoolBase
/*     */   implements HikariPoolMXBean, ConcurrentBag.IBagStateListener
/*     */ {
/*  73 */   private final Logger logger = LoggerFactory.getLogger(HikariPool.class);
/*     */   
/*     */   public static final int POOL_NORMAL = 0;
/*     */   
/*     */   public static final int POOL_SUSPENDED = 1;
/*     */   
/*     */   public static final int POOL_SHUTDOWN = 2;
/*     */   public volatile int poolState;
/*  81 */   private final long aliveBypassWindowMs = Long.getLong("com.zaxxer.hikari.aliveBypassWindowMs", TimeUnit.MILLISECONDS.toMillis(500L)).longValue();
/*  82 */   private final long housekeepingPeriodMs = Long.getLong("com.zaxxer.hikari.housekeeping.periodMs", TimeUnit.SECONDS.toMillis(30L)).longValue();
/*     */   
/*     */   private static final String EVICTED_CONNECTION_MESSAGE = "(connection was evicted)";
/*     */   
/*     */   private static final String DEAD_CONNECTION_MESSAGE = "(connection is dead)";
/*  87 */   private final PoolEntryCreator poolEntryCreator = new PoolEntryCreator(null);
/*  88 */   private final PoolEntryCreator postFillPoolEntryCreator = new PoolEntryCreator("After adding ");
/*     */   
/*     */   private final Collection<Runnable> addConnectionQueue;
/*     */   
/*     */   private final ThreadPoolExecutor addConnectionExecutor;
/*     */   
/*     */   private final ThreadPoolExecutor closeConnectionExecutor;
/*     */   
/*     */   private final ConcurrentBag<PoolEntry> connectionBag;
/*     */   
/*     */   private final ProxyLeakTaskFactory leakTaskFactory;
/*     */   
/*     */   private final SuspendResumeLock suspendResumeLock;
/*     */   
/*     */   private final ScheduledExecutorService houseKeepingExecutorService;
/*     */   
/*     */   private ScheduledFuture<?> houseKeeperTask;
/*     */ 
/*     */   
/*     */   public HikariPool(HikariConfig paramHikariConfig) {
/* 108 */     super(paramHikariConfig);
/*     */     
/* 110 */     this.connectionBag = new ConcurrentBag(this);
/* 111 */     this.suspendResumeLock = paramHikariConfig.isAllowPoolSuspension() ? new SuspendResumeLock() : SuspendResumeLock.FAUX_LOCK;
/*     */     
/* 113 */     this.houseKeepingExecutorService = initializeHouseKeepingExecutorService();
/*     */     
/* 115 */     checkFailFast();
/*     */     
/* 117 */     if (paramHikariConfig.getMetricsTrackerFactory() != null) {
/* 118 */       setMetricsTrackerFactory(paramHikariConfig.getMetricsTrackerFactory());
/*     */     } else {
/*     */       
/* 121 */       setMetricRegistry(paramHikariConfig.getMetricRegistry());
/*     */     } 
/*     */     
/* 124 */     setHealthCheckRegistry(paramHikariConfig.getHealthCheckRegistry());
/*     */     
/* 126 */     handleMBeans(this, true);
/*     */     
/* 128 */     ThreadFactory threadFactory = paramHikariConfig.getThreadFactory();
/*     */     
/* 130 */     LinkedBlockingQueue<? extends Runnable> linkedBlockingQueue = new LinkedBlockingQueue(paramHikariConfig.getMaximumPoolSize());
/* 131 */     this.addConnectionQueue = Collections.unmodifiableCollection(linkedBlockingQueue);
/* 132 */     this.addConnectionExecutor = UtilityElf.createThreadPoolExecutor(linkedBlockingQueue, this.poolName + " connection adder", threadFactory, new ThreadPoolExecutor.DiscardPolicy());
/* 133 */     this.closeConnectionExecutor = UtilityElf.createThreadPoolExecutor(paramHikariConfig.getMaximumPoolSize(), this.poolName + " connection closer", threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
/*     */     
/* 135 */     this.leakTaskFactory = new ProxyLeakTaskFactory(paramHikariConfig.getLeakDetectionThreshold(), this.houseKeepingExecutorService);
/*     */     
/* 137 */     this.houseKeeperTask = this.houseKeepingExecutorService.scheduleWithFixedDelay(new HouseKeeper(), 100L, this.housekeepingPeriodMs, TimeUnit.MILLISECONDS);
/*     */     
/* 139 */     if (Boolean.getBoolean("com.zaxxer.hikari.blockUntilFilled") && paramHikariConfig.getInitializationFailTimeout() > 1L) {
/* 140 */       this.addConnectionExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
/* 141 */       this.addConnectionExecutor.setMaximumPoolSize(Runtime.getRuntime().availableProcessors());
/*     */       
/* 143 */       long l = ClockSource.currentTime();
/* 144 */       while (ClockSource.elapsedMillis(l) < paramHikariConfig.getInitializationFailTimeout() && getTotalConnections() < paramHikariConfig.getMinimumIdle()) {
/* 145 */         UtilityElf.quietlySleep(TimeUnit.MILLISECONDS.toMillis(100L));
/*     */       }
/*     */       
/* 148 */       this.addConnectionExecutor.setCorePoolSize(1);
/* 149 */       this.addConnectionExecutor.setMaximumPoolSize(1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() {
/* 161 */     return getConnection(this.connectionTimeout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection(long paramLong) {
/* 173 */     this.suspendResumeLock.acquire();
/* 174 */     long l = ClockSource.currentTime();
/*     */     
/*     */     try {
/* 177 */       long l1 = paramLong;
/*     */       do {
/* 179 */         PoolEntry poolEntry = (PoolEntry)this.connectionBag.borrow(l1, TimeUnit.MILLISECONDS);
/* 180 */         if (poolEntry == null) {
/*     */           break;
/*     */         }
/*     */         
/* 184 */         long l2 = ClockSource.currentTime();
/* 185 */         if (poolEntry.isMarkedEvicted() || (ClockSource.elapsedMillis(poolEntry.lastAccessed, l2) > this.aliveBypassWindowMs && !isConnectionAlive(poolEntry.connection))) {
/* 186 */           closeConnection(poolEntry, poolEntry.isMarkedEvicted() ? "(connection was evicted)" : "(connection is dead)");
/* 187 */           l1 = paramLong - ClockSource.elapsedMillis(l);
/*     */         } else {
/*     */           
/* 190 */           this.metricsTracker.recordBorrowStats(poolEntry, l);
/* 191 */           return poolEntry.createProxyConnection(this.leakTaskFactory.schedule(poolEntry), l2);
/*     */         } 
/* 193 */       } while (l1 > 0L);
/*     */       
/* 195 */       this.metricsTracker.recordBorrowTimeoutStats(l);
/* 196 */       throw createTimeoutException(l);
/*     */     }
/* 198 */     catch (InterruptedException interruptedException) {
/* 199 */       Thread.currentThread().interrupt();
/* 200 */       throw new SQLException(this.poolName + " - Interrupted during connection acquisition", interruptedException);
/*     */     } finally {
/*     */       
/* 203 */       this.suspendResumeLock.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void shutdown() {
/*     */     try {
/* 216 */       this.poolState = 2;
/*     */       
/* 218 */       if (this.addConnectionExecutor == null) {
/*     */         return;
/*     */       }
/*     */       
/* 222 */       logPoolState(new String[] { "Before shutdown " });
/*     */       
/* 224 */       if (this.houseKeeperTask != null) {
/* 225 */         this.houseKeeperTask.cancel(false);
/* 226 */         this.houseKeeperTask = null;
/*     */       } 
/*     */       
/* 229 */       softEvictConnections();
/*     */       
/* 231 */       this.addConnectionExecutor.shutdown();
/* 232 */       this.addConnectionExecutor.awaitTermination(getLoginTimeout(), TimeUnit.SECONDS);
/*     */       
/* 234 */       destroyHouseKeepingExecutorService();
/*     */       
/* 236 */       this.connectionBag.close();
/*     */       
/* 238 */       ThreadPoolExecutor threadPoolExecutor = UtilityElf.createThreadPoolExecutor(this.config.getMaximumPoolSize(), this.poolName + " connection assassinator", this.config
/* 239 */           .getThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
/*     */       try {
/* 241 */         long l = ClockSource.currentTime();
/*     */         do {
/* 243 */           abortActiveConnections(threadPoolExecutor);
/* 244 */           softEvictConnections();
/* 245 */         } while (getTotalConnections() > 0 && ClockSource.elapsedMillis(l) < TimeUnit.SECONDS.toMillis(10L));
/*     */       } finally {
/*     */         
/* 248 */         threadPoolExecutor.shutdown();
/* 249 */         threadPoolExecutor.awaitTermination(10L, TimeUnit.SECONDS);
/*     */       } 
/*     */       
/* 252 */       shutdownNetworkTimeoutExecutor();
/* 253 */       this.closeConnectionExecutor.shutdown();
/* 254 */       this.closeConnectionExecutor.awaitTermination(10L, TimeUnit.SECONDS);
/*     */     } finally {
/*     */       
/* 257 */       logPoolState(new String[] { "After shutdown " });
/* 258 */       handleMBeans(this, false);
/* 259 */       this.metricsTracker.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void evictConnection(Connection paramConnection) {
/* 270 */     ProxyConnection proxyConnection = (ProxyConnection)paramConnection;
/* 271 */     proxyConnection.cancelLeakTask();
/*     */     
/*     */     try {
/* 274 */       softEvictConnection(proxyConnection.getPoolEntry(), "(connection evicted by user)", !paramConnection.isClosed());
/*     */     }
/* 276 */     catch (SQLException sQLException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetricRegistry(Object paramObject) {
/* 289 */     if (paramObject != null && UtilityElf.safeIsAssignableFrom(paramObject, "com.codahale.metrics.MetricRegistry")) {
/* 290 */       setMetricsTrackerFactory((MetricsTrackerFactory)new CodahaleMetricsTrackerFactory((MetricRegistry)paramObject));
/*     */     }
/* 292 */     else if (paramObject != null && UtilityElf.safeIsAssignableFrom(paramObject, "io.micrometer.core.instrument.MeterRegistry")) {
/* 293 */       setMetricsTrackerFactory((MetricsTrackerFactory)new MicrometerMetricsTrackerFactory((MeterRegistry)paramObject));
/*     */     } else {
/*     */       
/* 296 */       setMetricsTrackerFactory((MetricsTrackerFactory)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetricsTrackerFactory(MetricsTrackerFactory paramMetricsTrackerFactory) {
/* 307 */     if (paramMetricsTrackerFactory != null) {
/* 308 */       this.metricsTracker = new PoolBase.MetricsTrackerDelegate(paramMetricsTrackerFactory.create(this.config.getPoolName(), getPoolStats()));
/*     */     } else {
/*     */       
/* 311 */       this.metricsTracker = new PoolBase.NopMetricsTrackerDelegate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHealthCheckRegistry(Object paramObject) {
/* 323 */     if (paramObject != null) {
/* 324 */       CodahaleHealthChecker.registerHealthChecks(this, this.config, (HealthCheckRegistry)paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBagItem(int paramInt) {
/* 336 */     boolean bool = (paramInt - this.addConnectionQueue.size() >= 0) ? true : false;
/* 337 */     if (bool) {
/* 338 */       this.addConnectionExecutor.submit(this.poolEntryCreator);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getActiveConnections() {
/* 350 */     return this.connectionBag.getCount(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIdleConnections() {
/* 357 */     return this.connectionBag.getCount(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalConnections() {
/* 364 */     return this.connectionBag.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getThreadsAwaitingConnection() {
/* 371 */     return this.connectionBag.getWaitingThreadCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void softEvictConnections() {
/* 378 */     this.connectionBag.values().forEach(paramPoolEntry -> softEvictConnection(paramPoolEntry, "(connection evicted)", false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void suspendPool() {
/* 385 */     if (this.suspendResumeLock == SuspendResumeLock.FAUX_LOCK) {
/* 386 */       throw new IllegalStateException(this.poolName + " - is not suspendable");
/*     */     }
/* 388 */     if (this.poolState != 1) {
/* 389 */       this.suspendResumeLock.suspend();
/* 390 */       this.poolState = 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void resumePool() {
/* 398 */     if (this.poolState == 1) {
/* 399 */       this.poolState = 0;
/* 400 */       fillPool();
/* 401 */       this.suspendResumeLock.resume();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void logPoolState(String... paramVarArgs) {
/* 416 */     if (this.logger.isDebugEnabled()) {
/* 417 */       this.logger.debug("{} - {}stats (total={}, active={}, idle={}, waiting={})", new Object[] { this.poolName, (paramVarArgs.length > 0) ? paramVarArgs[0] : "", 
/*     */             
/* 419 */             Integer.valueOf(getTotalConnections()), Integer.valueOf(getActiveConnections()), Integer.valueOf(getIdleConnections()), Integer.valueOf(getThreadsAwaitingConnection()) });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void recycle(PoolEntry paramPoolEntry) {
/* 431 */     this.metricsTracker.recordConnectionUsage(paramPoolEntry);
/*     */     
/* 433 */     this.connectionBag.requite(paramPoolEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void closeConnection(PoolEntry paramPoolEntry, String paramString) {
/* 444 */     if (this.connectionBag.remove(paramPoolEntry)) {
/* 445 */       Connection connection = paramPoolEntry.close();
/* 446 */       this.closeConnectionExecutor.execute(() -> {
/*     */             quietlyCloseConnection(paramConnection, paramString);
/*     */             if (this.poolState == 0) {
/*     */               fillPool();
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int[] getPoolStateCounts() {
/* 458 */     return this.connectionBag.getStateCounts();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PoolEntry createPoolEntry() {
/*     */     try {
/* 473 */       PoolEntry poolEntry = newPoolEntry();
/*     */       
/* 475 */       long l = this.config.getMaxLifetime();
/* 476 */       if (l > 0L) {
/*     */         
/* 478 */         long l1 = (l > 10000L) ? ThreadLocalRandom.current().nextLong(l / 40L) : 0L;
/* 479 */         long l2 = l - l1;
/* 480 */         poolEntry.setFutureEol(this.houseKeepingExecutorService.schedule(() -> { if (softEvictConnection(paramPoolEntry, "(connection has passed maxLifetime)", false)) addBagItem(this.connectionBag.getWaitingThreadCount());  }l2, TimeUnit.MILLISECONDS));
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 489 */       return poolEntry;
/*     */     }
/* 491 */     catch (ConnectionSetupException connectionSetupException) {
/* 492 */       if (this.poolState == 0) {
/* 493 */         this.logger.error("{} - Error thrown while acquiring connection from data source", this.poolName, connectionSetupException.getCause());
/* 494 */         this.lastConnectionFailure.set(connectionSetupException);
/*     */       } 
/* 496 */       return null;
/*     */     }
/* 498 */     catch (SQLException sQLException) {
/* 499 */       if (this.poolState == 0) {
/* 500 */         this.logger.debug("{} - Cannot acquire connection from data source", this.poolName, sQLException);
/* 501 */         this.lastConnectionFailure.set(new PoolBase.ConnectionSetupException(sQLException));
/*     */       } 
/* 503 */       return null;
/*     */     }
/* 505 */     catch (Exception exception) {
/* 506 */       if (this.poolState == 0) {
/* 507 */         this.logger.error("{} - Error thrown while acquiring connection from data source", this.poolName, exception);
/* 508 */         this.lastConnectionFailure.set(new PoolBase.ConnectionSetupException(exception));
/*     */       } 
/* 510 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void fillPool() {
/* 520 */     int i = Math.min(this.config.getMaximumPoolSize() - getTotalConnections(), this.config.getMinimumIdle() - getIdleConnections()) - this.addConnectionQueue.size();
/* 521 */     for (byte b = 0; b < i; b++) {
/* 522 */       this.addConnectionExecutor.submit((b < i - 1) ? this.poolEntryCreator : this.postFillPoolEntryCreator);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void abortActiveConnections(ExecutorService paramExecutorService) {
/* 533 */     for (PoolEntry poolEntry : this.connectionBag.values(1)) {
/* 534 */       Connection connection = poolEntry.close();
/*     */       try {
/* 536 */         connection.abort(paramExecutorService);
/*     */       }
/* 538 */       catch (Throwable throwable) {
/* 539 */         quietlyCloseConnection(connection, "(connection aborted during shutdown)");
/*     */       } finally {
/*     */         
/* 542 */         this.connectionBag.remove(poolEntry);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkFailFast() {
/* 555 */     long l1 = this.config.getInitializationFailTimeout();
/* 556 */     if (l1 < 0L) {
/*     */       return;
/*     */     }
/*     */     
/* 560 */     long l2 = ClockSource.currentTime();
/*     */     do {
/* 562 */       PoolEntry poolEntry = createPoolEntry();
/* 563 */       if (poolEntry != null) {
/* 564 */         if (this.config.getMinimumIdle() > 0) {
/* 565 */           this.connectionBag.add(poolEntry);
/* 566 */           this.logger.debug("{} - Added connection {}", this.poolName, poolEntry.connection);
/*     */         } else {
/*     */           
/* 569 */           quietlyCloseConnection(poolEntry.close(), "(initialization check complete and minimumIdle is zero)");
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 575 */       if (getLastConnectionFailure() instanceof PoolBase.ConnectionSetupException) {
/* 576 */         throwPoolInitializationException(getLastConnectionFailure().getCause());
/*     */       }
/*     */       
/* 579 */       UtilityElf.quietlySleep(TimeUnit.SECONDS.toMillis(1L));
/* 580 */     } while (ClockSource.elapsedMillis(l2) < l1);
/*     */     
/* 582 */     if (l1 > 0L) {
/* 583 */       throwPoolInitializationException(getLastConnectionFailure());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void throwPoolInitializationException(Throwable paramThrowable) {
/* 595 */     this.logger.error("{} - Exception during pool initialization.", this.poolName, paramThrowable);
/* 596 */     destroyHouseKeepingExecutorService();
/* 597 */     throw new PoolInitializationException(paramThrowable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean softEvictConnection(PoolEntry paramPoolEntry, String paramString, boolean paramBoolean) {
/* 615 */     paramPoolEntry.markEvicted();
/* 616 */     if (paramBoolean || this.connectionBag.reserve(paramPoolEntry)) {
/* 617 */       closeConnection(paramPoolEntry, paramString);
/* 618 */       return true;
/*     */     } 
/*     */     
/* 621 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ScheduledExecutorService initializeHouseKeepingExecutorService() {
/* 633 */     if (this.config.getScheduledExecutor() == null) {
/* 634 */       ThreadFactory threadFactory = Optional.<ThreadFactory>ofNullable(this.config.getThreadFactory()).orElseGet(() -> new UtilityElf.DefaultThreadFactory(this.poolName + " housekeeper", true));
/* 635 */       ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, threadFactory, new ThreadPoolExecutor.DiscardPolicy());
/* 636 */       scheduledThreadPoolExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
/* 637 */       scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
/* 638 */       return scheduledThreadPoolExecutor;
/*     */     } 
/*     */     
/* 641 */     return this.config.getScheduledExecutor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void destroyHouseKeepingExecutorService() {
/* 650 */     if (this.config.getScheduledExecutor() == null) {
/* 651 */       this.houseKeepingExecutorService.shutdownNow();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PoolStats getPoolStats() {
/* 662 */     return new PoolStats(TimeUnit.SECONDS.toMillis(1L))
/*     */       {
/*     */         protected void update() {
/* 665 */           this.pendingThreads = HikariPool.this.getThreadsAwaitingConnection();
/* 666 */           this.idleConnections = HikariPool.this.getIdleConnections();
/* 667 */           this.totalConnections = HikariPool.this.getTotalConnections();
/* 668 */           this.activeConnections = HikariPool.this.getActiveConnections();
/* 669 */           this.maxConnections = HikariPool.this.config.getMaximumPoolSize();
/* 670 */           this.minConnections = HikariPool.this.config.getMinimumIdle();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SQLException createTimeoutException(long paramLong) {
/* 689 */     logPoolState(new String[] { "Timeout failure " });
/* 690 */     this.metricsTracker.recordConnectionTimeout();
/*     */     
/* 692 */     String str = null;
/* 693 */     Exception exception = getLastConnectionFailure();
/* 694 */     if (exception instanceof SQLException) {
/* 695 */       str = ((SQLException)exception).getSQLState();
/*     */     }
/* 697 */     SQLTransientConnectionException sQLTransientConnectionException = new SQLTransientConnectionException(this.poolName + " - Connection is not available, request timed out after " + ClockSource.elapsedMillis(paramLong) + "ms.", str, exception);
/* 698 */     if (exception instanceof SQLException) {
/* 699 */       sQLTransientConnectionException.setNextException((SQLException)exception);
/*     */     }
/*     */     
/* 702 */     return sQLTransientConnectionException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class PoolEntryCreator
/*     */     implements Callable<Boolean>
/*     */   {
/*     */     private final String loggingPrefix;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     PoolEntryCreator(String param1String) {
/* 719 */       this.loggingPrefix = param1String;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Boolean call() {
/* 725 */       long l = 250L;
/* 726 */       while (HikariPool.this.poolState == 0 && shouldCreateAnotherConnection()) {
/* 727 */         PoolEntry poolEntry = HikariPool.this.createPoolEntry();
/* 728 */         if (poolEntry != null) {
/* 729 */           HikariPool.this.connectionBag.add(poolEntry);
/* 730 */           HikariPool.this.logger.debug("{} - Added connection {}", HikariPool.this.poolName, poolEntry.connection);
/* 731 */           if (this.loggingPrefix != null) {
/* 732 */             HikariPool.this.logPoolState(new String[] { this.loggingPrefix });
/*     */           }
/* 734 */           return Boolean.TRUE;
/*     */         } 
/*     */ 
/*     */         
/* 738 */         UtilityElf.quietlySleep(l);
/* 739 */         l = Math.min(TimeUnit.SECONDS.toMillis(10L), Math.min(HikariPool.this.connectionTimeout, (long)(l * 1.5D)));
/*     */       } 
/*     */       
/* 742 */       return Boolean.FALSE;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private synchronized boolean shouldCreateAnotherConnection() {
/* 752 */       return (HikariPool.this.getTotalConnections() < HikariPool.this.config.getMaximumPoolSize() && (HikariPool.this
/* 753 */         .connectionBag.getWaitingThreadCount() > 0 || HikariPool.this.getIdleConnections() < HikariPool.this.config.getMinimumIdle()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class HouseKeeper
/*     */     implements Runnable
/*     */   {
/* 762 */     private volatile long previous = ClockSource.plusMillis(ClockSource.currentTime(), -HikariPool.this.housekeepingPeriodMs);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 769 */         HikariPool.this.connectionTimeout = HikariPool.this.config.getConnectionTimeout();
/* 770 */         HikariPool.this.validationTimeout = HikariPool.this.config.getValidationTimeout();
/* 771 */         HikariPool.this.leakTaskFactory.updateLeakDetectionThreshold(HikariPool.this.config.getLeakDetectionThreshold());
/* 772 */         HikariPool.this.catalog = (HikariPool.this.config.getCatalog() != null && !HikariPool.this.config.getCatalog().equals(HikariPool.this.catalog)) ? HikariPool.this.config.getCatalog() : HikariPool.this.catalog;
/*     */         
/* 774 */         long l1 = HikariPool.this.config.getIdleTimeout();
/* 775 */         long l2 = ClockSource.currentTime();
/*     */ 
/*     */         
/* 778 */         if (ClockSource.plusMillis(l2, 128L) < ClockSource.plusMillis(this.previous, HikariPool.this.housekeepingPeriodMs)) {
/* 779 */           HikariPool.this.logger.warn("{} - Retrograde clock change detected (housekeeper delta={}), soft-evicting connections from pool.", HikariPool.this.poolName, 
/* 780 */               ClockSource.elapsedDisplayString(this.previous, l2));
/* 781 */           this.previous = l2;
/* 782 */           HikariPool.this.softEvictConnections();
/*     */           return;
/*     */         } 
/* 785 */         if (l2 > ClockSource.plusMillis(this.previous, 3L * HikariPool.this.housekeepingPeriodMs / 2L))
/*     */         {
/* 787 */           HikariPool.this.logger.warn("{} - Thread starvation or clock leap detected (housekeeper delta={}).", HikariPool.this.poolName, ClockSource.elapsedDisplayString(this.previous, l2));
/*     */         }
/*     */         
/* 790 */         this.previous = l2;
/*     */         
/* 792 */         String str = "Pool ";
/* 793 */         if (l1 > 0L && HikariPool.this.config.getMinimumIdle() < HikariPool.this.config.getMaximumPoolSize()) {
/* 794 */           HikariPool.this.logPoolState(new String[] { "Before cleanup " });
/* 795 */           str = "After cleanup  ";
/*     */           
/* 797 */           List list = HikariPool.this.connectionBag.values(0);
/* 798 */           int i = list.size() - HikariPool.this.config.getMinimumIdle();
/* 799 */           for (PoolEntry poolEntry : list) {
/* 800 */             if (i > 0 && ClockSource.elapsedMillis(poolEntry.lastAccessed, l2) > l1 && HikariPool.this.connectionBag.reserve(poolEntry)) {
/* 801 */               HikariPool.this.closeConnection(poolEntry, "(connection has passed idleTimeout)");
/* 802 */               i--;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 807 */         HikariPool.this.logPoolState(new String[] { str });
/*     */         
/* 809 */         HikariPool.this.fillPool();
/*     */       }
/* 811 */       catch (Exception exception) {
/* 812 */         HikariPool.this.logger.error("Unexpected exception in housekeeping task", exception);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private HouseKeeper() {}
/*     */   }
/*     */ 
/*     */   
/*     */   public static class PoolInitializationException
/*     */     extends RuntimeException
/*     */   {
/*     */     private static final long serialVersionUID = 929872118275916520L;
/*     */     
/*     */     public PoolInitializationException(Throwable param1Throwable) {
/* 827 */       super("Failed to initialize pool: " + param1Throwable.getMessage(), param1Throwable);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\pool\HikariPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
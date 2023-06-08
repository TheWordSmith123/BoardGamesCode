/*     */ package com.zaxxer.hikari.pool;
/*     */ 
/*     */ import com.zaxxer.hikari.HikariConfig;
/*     */ import com.zaxxer.hikari.metrics.IMetricsTracker;
/*     */ import com.zaxxer.hikari.util.ClockSource;
/*     */ import com.zaxxer.hikari.util.DriverDataSource;
/*     */ import com.zaxxer.hikari.util.PropertyElf;
/*     */ import com.zaxxer.hikari.util.UtilityElf;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
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
/*     */ abstract class PoolBase
/*     */ {
/*  53 */   private final Logger logger = LoggerFactory.getLogger(PoolBase.class);
/*     */   
/*     */   public final HikariConfig config;
/*     */   
/*     */   IMetricsTrackerDelegate metricsTracker;
/*     */   
/*     */   protected final String poolName;
/*     */   
/*     */   volatile String catalog;
/*     */   
/*     */   final AtomicReference<Exception> lastConnectionFailure;
/*     */   long connectionTimeout;
/*     */   long validationTimeout;
/*  66 */   private static final String[] RESET_STATES = new String[] { "readOnly", "autoCommit", "isolation", "catalog", "netTimeout", "schema" };
/*     */   
/*     */   private static final int UNINITIALIZED = -1;
/*     */   
/*     */   private static final int TRUE = 1;
/*     */   
/*     */   private static final int FALSE = 0;
/*     */   
/*     */   private int networkTimeout;
/*     */   
/*     */   private int isNetworkTimeoutSupported;
/*     */   private int isQueryTimeoutSupported;
/*     */   private int defaultTransactionIsolation;
/*     */   private int transactionIsolation;
/*     */   private Executor netTimeoutExecutor;
/*     */   private DataSource dataSource;
/*     */   private final String schema;
/*     */   private final boolean isReadOnly;
/*     */   private final boolean isAutoCommit;
/*     */   private final boolean isUseJdbc4Validation;
/*     */   private final boolean isIsolateInternalQueries;
/*     */   private volatile boolean isValidChecked;
/*     */   
/*     */   PoolBase(HikariConfig paramHikariConfig) {
/*  90 */     this.config = paramHikariConfig;
/*     */     
/*  92 */     this.networkTimeout = -1;
/*  93 */     this.catalog = paramHikariConfig.getCatalog();
/*  94 */     this.schema = paramHikariConfig.getSchema();
/*  95 */     this.isReadOnly = paramHikariConfig.isReadOnly();
/*  96 */     this.isAutoCommit = paramHikariConfig.isAutoCommit();
/*  97 */     this.transactionIsolation = UtilityElf.getTransactionIsolation(paramHikariConfig.getTransactionIsolation());
/*     */     
/*  99 */     this.isQueryTimeoutSupported = -1;
/* 100 */     this.isNetworkTimeoutSupported = -1;
/* 101 */     this.isUseJdbc4Validation = (paramHikariConfig.getConnectionTestQuery() == null);
/* 102 */     this.isIsolateInternalQueries = paramHikariConfig.isIsolateInternalQueries();
/*     */     
/* 104 */     this.poolName = paramHikariConfig.getPoolName();
/* 105 */     this.connectionTimeout = paramHikariConfig.getConnectionTimeout();
/* 106 */     this.validationTimeout = paramHikariConfig.getValidationTimeout();
/* 107 */     this.lastConnectionFailure = new AtomicReference<>();
/*     */     
/* 109 */     initializeDataSource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     return this.poolName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void recycle(PoolEntry paramPoolEntry);
/*     */ 
/*     */ 
/*     */   
/*     */   void quietlyCloseConnection(Connection paramConnection, String paramString) {
/* 127 */     if (paramConnection != null) {
/*     */       try {
/* 129 */         this.logger.debug("{} - Closing connection {}: {}", new Object[] { this.poolName, paramConnection, paramString });
/*     */         
/*     */         try {
/* 132 */           setNetworkTimeout(paramConnection, TimeUnit.SECONDS.toMillis(15L));
/*     */         }
/* 134 */         catch (SQLException sQLException) {
/*     */ 
/*     */         
/*     */         } finally {
/* 138 */           paramConnection.close();
/*     */         }
/*     */       
/* 141 */       } catch (Exception exception) {
/* 142 */         this.logger.debug("{} - Closing connection {} failed", new Object[] { this.poolName, paramConnection, exception });
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConnectionAlive(Connection paramConnection) {
/*     */     try {
/*     */       try {
/* 151 */         setNetworkTimeout(paramConnection, this.validationTimeout);
/*     */         
/* 153 */         int i = (int)Math.max(1000L, this.validationTimeout) / 1000;
/*     */         
/* 155 */         if (this.isUseJdbc4Validation) {
/* 156 */           return paramConnection.isValid(i);
/*     */         }
/*     */         
/* 159 */         try (Statement null = paramConnection.createStatement()) {
/* 160 */           if (this.isNetworkTimeoutSupported != 1) {
/* 161 */             setQueryTimeout(statement, i);
/*     */           }
/*     */           
/* 164 */           statement.execute(this.config.getConnectionTestQuery());
/*     */         } 
/*     */       } finally {
/*     */         
/* 168 */         setNetworkTimeout(paramConnection, this.networkTimeout);
/*     */         
/* 170 */         if (this.isIsolateInternalQueries && !this.isAutoCommit) {
/* 171 */           paramConnection.rollback();
/*     */         }
/*     */       } 
/*     */       
/* 175 */       return true;
/*     */     }
/* 177 */     catch (Exception exception) {
/* 178 */       this.lastConnectionFailure.set(exception);
/* 179 */       this.logger.warn("{} - Failed to validate connection {} ({}). Possibly consider using a shorter maxLifetime value.", new Object[] { this.poolName, paramConnection, exception
/* 180 */             .getMessage() });
/* 181 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   Exception getLastConnectionFailure() {
/* 187 */     return this.lastConnectionFailure.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataSource getUnwrappedDataSource() {
/* 192 */     return this.dataSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PoolEntry newPoolEntry() {
/* 201 */     return new PoolEntry(newConnection(), this, this.isReadOnly, this.isAutoCommit);
/*     */   }
/*     */ 
/*     */   
/*     */   void resetConnectionState(Connection paramConnection, ProxyConnection paramProxyConnection, int paramInt) {
/* 206 */     int i = 0;
/*     */     
/* 208 */     if ((paramInt & 0x1) != 0 && paramProxyConnection.getReadOnlyState() != this.isReadOnly) {
/* 209 */       paramConnection.setReadOnly(this.isReadOnly);
/* 210 */       i |= 0x1;
/*     */     } 
/*     */     
/* 213 */     if ((paramInt & 0x2) != 0 && paramProxyConnection.getAutoCommitState() != this.isAutoCommit) {
/* 214 */       paramConnection.setAutoCommit(this.isAutoCommit);
/* 215 */       i |= 0x2;
/*     */     } 
/*     */     
/* 218 */     if ((paramInt & 0x4) != 0 && paramProxyConnection.getTransactionIsolationState() != this.transactionIsolation) {
/* 219 */       paramConnection.setTransactionIsolation(this.transactionIsolation);
/* 220 */       i |= 0x4;
/*     */     } 
/*     */     
/* 223 */     if ((paramInt & 0x8) != 0 && this.catalog != null && !this.catalog.equals(paramProxyConnection.getCatalogState())) {
/* 224 */       paramConnection.setCatalog(this.catalog);
/* 225 */       i |= 0x8;
/*     */     } 
/*     */     
/* 228 */     if ((paramInt & 0x10) != 0 && paramProxyConnection.getNetworkTimeoutState() != this.networkTimeout) {
/* 229 */       setNetworkTimeout(paramConnection, this.networkTimeout);
/* 230 */       i |= 0x10;
/*     */     } 
/*     */     
/* 233 */     if ((paramInt & 0x20) != 0 && this.schema != null && !this.schema.equals(paramProxyConnection.getSchemaState())) {
/* 234 */       paramConnection.setSchema(this.schema);
/* 235 */       i |= 0x20;
/*     */     } 
/*     */     
/* 238 */     if (i != 0 && this.logger.isDebugEnabled()) {
/* 239 */       this.logger.debug("{} - Reset ({}) on connection {}", new Object[] { this.poolName, stringFromResetBits(i), paramConnection });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void shutdownNetworkTimeoutExecutor() {
/* 245 */     if (this.netTimeoutExecutor instanceof ThreadPoolExecutor) {
/* 246 */       ((ThreadPoolExecutor)this.netTimeoutExecutor).shutdownNow();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   long getLoginTimeout() {
/*     */     try {
/* 253 */       return (this.dataSource != null) ? this.dataSource.getLoginTimeout() : TimeUnit.SECONDS.toSeconds(5L);
/* 254 */     } catch (SQLException sQLException) {
/* 255 */       return TimeUnit.SECONDS.toSeconds(5L);
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
/*     */   void handleMBeans(HikariPool paramHikariPool, boolean paramBoolean) {
/* 270 */     if (!this.config.isRegisterMbeans()) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 275 */       MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
/*     */       
/* 277 */       ObjectName objectName1 = new ObjectName("com.zaxxer.hikari:type=PoolConfig (" + this.poolName + ")");
/* 278 */       ObjectName objectName2 = new ObjectName("com.zaxxer.hikari:type=Pool (" + this.poolName + ")");
/* 279 */       if (paramBoolean) {
/* 280 */         if (!mBeanServer.isRegistered(objectName1)) {
/* 281 */           mBeanServer.registerMBean(this.config, objectName1);
/* 282 */           mBeanServer.registerMBean(paramHikariPool, objectName2);
/*     */         } else {
/* 284 */           this.logger.error("{} - JMX name ({}) is already registered.", this.poolName, this.poolName);
/*     */         }
/*     */       
/* 287 */       } else if (mBeanServer.isRegistered(objectName1)) {
/* 288 */         mBeanServer.unregisterMBean(objectName1);
/* 289 */         mBeanServer.unregisterMBean(objectName2);
/*     */       }
/*     */     
/* 292 */     } catch (Exception exception) {
/* 293 */       this.logger.warn("{} - Failed to {} management beans.", new Object[] { this.poolName, paramBoolean ? "register" : "unregister", exception });
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
/*     */   private void initializeDataSource() {
/* 306 */     String str1 = this.config.getJdbcUrl();
/* 307 */     String str2 = this.config.getUsername();
/* 308 */     String str3 = this.config.getPassword();
/* 309 */     String str4 = this.config.getDataSourceClassName();
/* 310 */     String str5 = this.config.getDriverClassName();
/* 311 */     String str6 = this.config.getDataSourceJNDI();
/* 312 */     Properties properties = this.config.getDataSourceProperties();
/*     */     
/* 314 */     DataSource dataSource = this.config.getDataSource();
/* 315 */     if (str4 != null && dataSource == null) {
/* 316 */       dataSource = (DataSource)UtilityElf.createInstance(str4, DataSource.class, new Object[0]);
/* 317 */       PropertyElf.setTargetFromProperties(dataSource, properties);
/*     */     } else {
/* 319 */       DriverDataSource driverDataSource; if (str1 != null && dataSource == null) {
/* 320 */         driverDataSource = new DriverDataSource(str1, str5, properties, str2, str3);
/*     */       }
/* 322 */       else if (str6 != null && driverDataSource == null) {
/*     */         try {
/* 324 */           InitialContext initialContext = new InitialContext();
/* 325 */           dataSource = (DataSource)initialContext.lookup(str6);
/* 326 */         } catch (NamingException namingException) {
/* 327 */           throw new HikariPool.PoolInitializationException(namingException);
/*     */         } 
/*     */       } 
/*     */     } 
/* 331 */     if (dataSource != null) {
/* 332 */       setLoginTimeout(dataSource);
/* 333 */       createNetworkTimeoutExecutor(dataSource, str4, str1);
/*     */     } 
/*     */     
/* 336 */     this.dataSource = dataSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Connection newConnection() {
/* 346 */     long l = ClockSource.currentTime();
/*     */     
/* 348 */     Connection connection = null;
/*     */     try {
/* 350 */       String str1 = this.config.getUsername();
/* 351 */       String str2 = this.config.getPassword();
/*     */       
/* 353 */       connection = (str1 == null) ? this.dataSource.getConnection() : this.dataSource.getConnection(str1, str2);
/*     */       
/* 355 */       setupConnection(connection);
/* 356 */       this.lastConnectionFailure.set(null);
/* 357 */       return connection;
/*     */     }
/* 359 */     catch (Exception exception) {
/* 360 */       if (connection != null) {
/* 361 */         quietlyCloseConnection(connection, "(Failed to create/setup connection)");
/*     */       }
/* 363 */       else if (getLastConnectionFailure() == null) {
/* 364 */         this.logger.debug("{} - Failed to create/setup connection: {}", this.poolName, exception.getMessage());
/*     */       } 
/*     */       
/* 367 */       this.lastConnectionFailure.set(exception);
/* 368 */       throw exception;
/*     */     }
/*     */     finally {
/*     */       
/* 372 */       if (this.metricsTracker != null) {
/* 373 */         this.metricsTracker.recordConnectionCreated(ClockSource.elapsedMillis(l));
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
/*     */   private void setupConnection(Connection paramConnection) {
/*     */     try {
/* 387 */       if (this.networkTimeout == -1) {
/* 388 */         this.networkTimeout = getAndSetNetworkTimeout(paramConnection, this.validationTimeout);
/*     */       } else {
/*     */         
/* 391 */         setNetworkTimeout(paramConnection, this.validationTimeout);
/*     */       } 
/*     */       
/* 394 */       if (paramConnection.isReadOnly() != this.isReadOnly) {
/* 395 */         paramConnection.setReadOnly(this.isReadOnly);
/*     */       }
/*     */       
/* 398 */       if (paramConnection.getAutoCommit() != this.isAutoCommit) {
/* 399 */         paramConnection.setAutoCommit(this.isAutoCommit);
/*     */       }
/*     */       
/* 402 */       checkDriverSupport(paramConnection);
/*     */       
/* 404 */       if (this.transactionIsolation != this.defaultTransactionIsolation) {
/* 405 */         paramConnection.setTransactionIsolation(this.transactionIsolation);
/*     */       }
/*     */       
/* 408 */       if (this.catalog != null) {
/* 409 */         paramConnection.setCatalog(this.catalog);
/*     */       }
/*     */       
/* 412 */       if (this.schema != null) {
/* 413 */         paramConnection.setSchema(this.schema);
/*     */       }
/*     */       
/* 416 */       executeSql(paramConnection, this.config.getConnectionInitSql(), true);
/*     */       
/* 418 */       setNetworkTimeout(paramConnection, this.networkTimeout);
/*     */     }
/* 420 */     catch (SQLException sQLException) {
/* 421 */       throw new ConnectionSetupException(sQLException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkDriverSupport(Connection paramConnection) {
/* 432 */     if (!this.isValidChecked) {
/* 433 */       checkValidationSupport(paramConnection);
/* 434 */       checkDefaultIsolation(paramConnection);
/*     */       
/* 436 */       this.isValidChecked = true;
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
/*     */   private void checkValidationSupport(Connection paramConnection) {
/*     */     try {
/* 449 */       if (this.isUseJdbc4Validation) {
/* 450 */         paramConnection.isValid(1);
/*     */       } else {
/*     */         
/* 453 */         executeSql(paramConnection, this.config.getConnectionTestQuery(), false);
/*     */       }
/*     */     
/* 456 */     } catch (Exception|AbstractMethodError exception) {
/* 457 */       this.logger.error("{} - Failed to execute{} connection test query ({}).", new Object[] { this.poolName, this.isUseJdbc4Validation ? " isValid() for connection, configure" : "", exception.getMessage() });
/* 458 */       throw exception;
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
/*     */   private void checkDefaultIsolation(Connection paramConnection) {
/*     */     try {
/* 471 */       this.defaultTransactionIsolation = paramConnection.getTransactionIsolation();
/* 472 */       if (this.transactionIsolation == -1) {
/* 473 */         this.transactionIsolation = this.defaultTransactionIsolation;
/*     */       }
/*     */     }
/* 476 */     catch (SQLException sQLException) {
/* 477 */       this.logger.warn("{} - Default transaction isolation level detection failed ({}).", this.poolName, sQLException.getMessage());
/* 478 */       if (sQLException.getSQLState() != null && !sQLException.getSQLState().startsWith("08")) {
/* 479 */         throw sQLException;
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
/*     */   private void setQueryTimeout(Statement paramStatement, int paramInt) {
/* 492 */     if (this.isQueryTimeoutSupported != 0) {
/*     */       try {
/* 494 */         paramStatement.setQueryTimeout(paramInt);
/* 495 */         this.isQueryTimeoutSupported = 1;
/*     */       }
/* 497 */       catch (Exception exception) {
/* 498 */         if (this.isQueryTimeoutSupported == -1) {
/* 499 */           this.isQueryTimeoutSupported = 0;
/* 500 */           this.logger.info("{} - Failed to set query timeout for statement. ({})", this.poolName, exception.getMessage());
/*     */         } 
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
/*     */ 
/*     */   
/*     */   private int getAndSetNetworkTimeout(Connection paramConnection, long paramLong) {
/* 516 */     if (this.isNetworkTimeoutSupported != 0) {
/*     */       try {
/* 518 */         int i = paramConnection.getNetworkTimeout();
/* 519 */         paramConnection.setNetworkTimeout(this.netTimeoutExecutor, (int)paramLong);
/* 520 */         this.isNetworkTimeoutSupported = 1;
/* 521 */         return i;
/*     */       }
/* 523 */       catch (Exception|AbstractMethodError exception) {
/* 524 */         if (this.isNetworkTimeoutSupported == -1) {
/* 525 */           this.isNetworkTimeoutSupported = 0;
/*     */           
/* 527 */           this.logger.info("{} - Driver does not support get/set network timeout for connections. ({})", this.poolName, exception.getMessage());
/* 528 */           if (this.validationTimeout < TimeUnit.SECONDS.toMillis(1L)) {
/* 529 */             this.logger.warn("{} - A validationTimeout of less than 1 second cannot be honored on drivers without setNetworkTimeout() support.", this.poolName);
/*     */           }
/* 531 */           else if (this.validationTimeout % TimeUnit.SECONDS.toMillis(1L) != 0L) {
/* 532 */             this.logger.warn("{} - A validationTimeout with fractional second granularity cannot be honored on drivers without setNetworkTimeout() support.", this.poolName);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 538 */     return 0;
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
/*     */   private void setNetworkTimeout(Connection paramConnection, long paramLong) {
/* 551 */     if (this.isNetworkTimeoutSupported == 1) {
/* 552 */       paramConnection.setNetworkTimeout(this.netTimeoutExecutor, (int)paramLong);
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
/*     */   private void executeSql(Connection paramConnection, String paramString, boolean paramBoolean) {
/* 566 */     if (paramString != null) {
/* 567 */       try (Statement null = paramConnection.createStatement()) {
/*     */         
/* 569 */         statement.execute(paramString);
/*     */       } 
/*     */       
/* 572 */       if (this.isIsolateInternalQueries && !this.isAutoCommit) {
/* 573 */         if (paramBoolean) {
/* 574 */           paramConnection.commit();
/*     */         } else {
/*     */           
/* 577 */           paramConnection.rollback();
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createNetworkTimeoutExecutor(DataSource paramDataSource, String paramString1, String paramString2) {
/* 586 */     if ((paramString1 != null && paramString1.contains("Mysql")) || (paramString2 != null && paramString2
/* 587 */       .contains("mysql")) || (paramDataSource != null && paramDataSource
/* 588 */       .getClass().getName().contains("Mysql"))) {
/* 589 */       this.netTimeoutExecutor = new SynchronousExecutor();
/*     */     } else {
/*     */       
/* 592 */       ThreadFactory threadFactory = this.config.getThreadFactory();
/* 593 */       threadFactory = (threadFactory != null) ? threadFactory : (ThreadFactory)new UtilityElf.DefaultThreadFactory(this.poolName + " network timeout executor", true);
/* 594 */       ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newCachedThreadPool(threadFactory);
/* 595 */       threadPoolExecutor.setKeepAliveTime(15L, TimeUnit.SECONDS);
/* 596 */       threadPoolExecutor.allowCoreThreadTimeOut(true);
/* 597 */       this.netTimeoutExecutor = threadPoolExecutor;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setLoginTimeout(DataSource paramDataSource) {
/* 608 */     if (this.connectionTimeout != 2147483647L) {
/*     */       try {
/* 610 */         paramDataSource.setLoginTimeout(Math.max(1, (int)TimeUnit.MILLISECONDS.toSeconds(500L + this.connectionTimeout)));
/*     */       }
/* 612 */       catch (Exception exception) {
/* 613 */         this.logger.info("{} - Failed to set login timeout for data source. ({})", this.poolName, exception.getMessage());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String stringFromResetBits(int paramInt) {
/* 630 */     StringBuilder stringBuilder = new StringBuilder();
/* 631 */     for (byte b = 0; b < RESET_STATES.length; b++) {
/* 632 */       if ((paramInt & 1 << b) != 0) {
/* 633 */         stringBuilder.append(RESET_STATES[b]).append(", ");
/*     */       }
/*     */     } 
/*     */     
/* 637 */     stringBuilder.setLength(stringBuilder.length() - 2);
/* 638 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class ConnectionSetupException
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 929872118275916521L;
/*     */ 
/*     */ 
/*     */     
/*     */     ConnectionSetupException(Throwable param1Throwable) {
/* 651 */       super(param1Throwable);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SynchronousExecutor
/*     */     implements Executor
/*     */   {
/*     */     private SynchronousExecutor() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void execute(Runnable param1Runnable) {
/*     */       try {
/* 666 */         param1Runnable.run();
/*     */       }
/* 668 */       catch (Exception exception) {
/* 669 */         LoggerFactory.getLogger(PoolBase.class).debug("Failed to execute: {}", param1Runnable, exception);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static interface IMetricsTrackerDelegate
/*     */     extends AutoCloseable
/*     */   {
/*     */     default void recordConnectionUsage(PoolEntry poolEntry) {}
/*     */ 
/*     */     
/*     */     default void recordConnectionCreated(long connectionCreatedMillis) {}
/*     */ 
/*     */     
/*     */     default void recordBorrowTimeoutStats(long startTime) {}
/*     */ 
/*     */     
/*     */     default void recordBorrowStats(PoolEntry poolEntry, long startTime) {}
/*     */ 
/*     */     
/*     */     default void recordConnectionTimeout() {}
/*     */     
/*     */     default void close() {}
/*     */   }
/*     */   
/*     */   static class MetricsTrackerDelegate
/*     */     implements IMetricsTrackerDelegate
/*     */   {
/*     */     final IMetricsTracker tracker;
/*     */     
/*     */     MetricsTrackerDelegate(IMetricsTracker param1IMetricsTracker) {
/* 701 */       this.tracker = param1IMetricsTracker;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordConnectionUsage(PoolEntry param1PoolEntry) {
/* 707 */       this.tracker.recordConnectionUsageMillis(param1PoolEntry.getMillisSinceBorrowed());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordConnectionCreated(long param1Long) {
/* 713 */       this.tracker.recordConnectionCreatedMillis(param1Long);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordBorrowTimeoutStats(long param1Long) {
/* 719 */       this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(param1Long));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordBorrowStats(PoolEntry param1PoolEntry, long param1Long) {
/* 725 */       long l = ClockSource.currentTime();
/* 726 */       param1PoolEntry.lastBorrowed = l;
/* 727 */       this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(param1Long, l));
/*     */     }
/*     */ 
/*     */     
/*     */     public void recordConnectionTimeout() {
/* 732 */       this.tracker.recordConnectionTimeout();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {
/* 738 */       this.tracker.close();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class NopMetricsTrackerDelegate implements IMetricsTrackerDelegate {}
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\pool\PoolBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.zaxxer.hikari;
/*     */ 
/*     */ import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
/*     */ import com.zaxxer.hikari.pool.HikariPool;
/*     */ import java.io.Closeable;
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.logging.Logger;
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
/*     */ public class HikariDataSource
/*     */   extends HikariConfig
/*     */   implements DataSource, Closeable
/*     */ {
/*  42 */   private static final Logger LOGGER = LoggerFactory.getLogger(HikariDataSource.class);
/*     */   
/*  44 */   private final AtomicBoolean isShutdown = new AtomicBoolean();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final HikariPool fastPathPool;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile HikariPool pool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HikariDataSource() {
/*  62 */     this.fastPathPool = null;
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
/*     */   public HikariDataSource(HikariConfig paramHikariConfig) {
/*  77 */     paramHikariConfig.validate();
/*  78 */     paramHikariConfig.copyStateTo(this);
/*     */     
/*  80 */     LOGGER.info("{} - Starting...", paramHikariConfig.getPoolName());
/*  81 */     this.pool = this.fastPathPool = new HikariPool(this);
/*  82 */     LOGGER.info("{} - Start completed.", paramHikariConfig.getPoolName());
/*     */     
/*  84 */     seal();
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
/*  95 */     if (isClosed()) {
/*  96 */       throw new SQLException("HikariDataSource " + this + " has been closed.");
/*     */     }
/*     */     
/*  99 */     if (this.fastPathPool != null) {
/* 100 */       return this.fastPathPool.getConnection();
/*     */     }
/*     */ 
/*     */     
/* 104 */     HikariPool hikariPool = this.pool;
/* 105 */     if (hikariPool == null) {
/* 106 */       synchronized (this) {
/* 107 */         hikariPool = this.pool;
/* 108 */         if (hikariPool == null) {
/* 109 */           validate();
/* 110 */           LOGGER.info("{} - Starting...", getPoolName());
/*     */           try {
/* 112 */             this.pool = hikariPool = new HikariPool(this);
/* 113 */             seal();
/*     */           }
/* 115 */           catch (com.zaxxer.hikari.pool.HikariPool.PoolInitializationException poolInitializationException) {
/* 116 */             if (poolInitializationException.getCause() instanceof SQLException) {
/* 117 */               throw (SQLException)poolInitializationException.getCause();
/*     */             }
/*     */             
/* 120 */             throw poolInitializationException;
/*     */           } 
/*     */           
/* 123 */           LOGGER.info("{} - Start completed.", getPoolName());
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 128 */     return hikariPool.getConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection(String paramString1, String paramString2) {
/* 135 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() {
/* 142 */     HikariPool hikariPool = this.pool;
/* 143 */     return (hikariPool != null) ? hikariPool.getUnwrappedDataSource().getLogWriter() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter paramPrintWriter) {
/* 150 */     HikariPool hikariPool = this.pool;
/* 151 */     if (hikariPool != null) {
/* 152 */       hikariPool.getUnwrappedDataSource().setLogWriter(paramPrintWriter);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoginTimeout(int paramInt) {
/* 160 */     HikariPool hikariPool = this.pool;
/* 161 */     if (hikariPool != null) {
/* 162 */       hikariPool.getUnwrappedDataSource().setLoginTimeout(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLoginTimeout() {
/* 170 */     HikariPool hikariPool = this.pool;
/* 171 */     return (hikariPool != null) ? hikariPool.getUnwrappedDataSource().getLoginTimeout() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getParentLogger() {
/* 178 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> paramClass) {
/* 186 */     if (paramClass.isInstance(this)) {
/* 187 */       return (T)this;
/*     */     }
/*     */     
/* 190 */     HikariPool hikariPool = this.pool;
/* 191 */     if (hikariPool != null) {
/* 192 */       DataSource dataSource = hikariPool.getUnwrappedDataSource();
/* 193 */       if (paramClass.isInstance(dataSource)) {
/* 194 */         return (T)dataSource;
/*     */       }
/*     */       
/* 197 */       if (dataSource != null) {
/* 198 */         return dataSource.unwrap(paramClass);
/*     */       }
/*     */     } 
/*     */     
/* 202 */     throw new SQLException("Wrapped DataSource is not an instance of " + paramClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> paramClass) {
/* 209 */     if (paramClass.isInstance(this)) {
/* 210 */       return true;
/*     */     }
/*     */     
/* 213 */     HikariPool hikariPool = this.pool;
/* 214 */     if (hikariPool != null) {
/* 215 */       DataSource dataSource = hikariPool.getUnwrappedDataSource();
/* 216 */       if (paramClass.isInstance(dataSource)) {
/* 217 */         return true;
/*     */       }
/*     */       
/* 220 */       if (dataSource != null) {
/* 221 */         return dataSource.isWrapperFor(paramClass);
/*     */       }
/*     */     } 
/*     */     
/* 225 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetricRegistry(Object paramObject) {
/* 236 */     boolean bool = (getMetricRegistry() != null) ? true : false;
/* 237 */     super.setMetricRegistry(paramObject);
/*     */     
/* 239 */     HikariPool hikariPool = this.pool;
/* 240 */     if (hikariPool != null) {
/* 241 */       if (bool) {
/* 242 */         throw new IllegalStateException("MetricRegistry can only be set one time");
/*     */       }
/*     */       
/* 245 */       hikariPool.setMetricRegistry(getMetricRegistry());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetricsTrackerFactory(MetricsTrackerFactory paramMetricsTrackerFactory) {
/* 254 */     boolean bool = (getMetricsTrackerFactory() != null) ? true : false;
/* 255 */     super.setMetricsTrackerFactory(paramMetricsTrackerFactory);
/*     */     
/* 257 */     HikariPool hikariPool = this.pool;
/* 258 */     if (hikariPool != null) {
/* 259 */       if (bool) {
/* 260 */         throw new IllegalStateException("MetricsTrackerFactory can only be set one time");
/*     */       }
/*     */       
/* 263 */       hikariPool.setMetricsTrackerFactory(getMetricsTrackerFactory());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHealthCheckRegistry(Object paramObject) {
/* 272 */     boolean bool = (getHealthCheckRegistry() != null) ? true : false;
/* 273 */     super.setHealthCheckRegistry(paramObject);
/*     */     
/* 275 */     HikariPool hikariPool = this.pool;
/* 276 */     if (hikariPool != null) {
/* 277 */       if (bool) {
/* 278 */         throw new IllegalStateException("HealthCheckRegistry can only be set one time");
/*     */       }
/*     */       
/* 281 */       hikariPool.setHealthCheckRegistry(getHealthCheckRegistry());
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
/*     */   public boolean isRunning() {
/* 297 */     return (this.pool != null && this.pool.poolState == 0);
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
/*     */   public HikariPoolMXBean getHikariPoolMXBean() {
/* 309 */     return (HikariPoolMXBean)this.pool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HikariConfigMXBean getHikariConfigMXBean() {
/* 319 */     return this;
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
/*     */   public void evictConnection(Connection paramConnection) {
/*     */     HikariPool hikariPool;
/* 332 */     if (!isClosed() && (hikariPool = this.pool) != null && paramConnection.getClass().getName().startsWith("com.zaxxer.hikari")) {
/* 333 */       hikariPool.evictConnection(paramConnection);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 343 */     if (this.isShutdown.getAndSet(true)) {
/*     */       return;
/*     */     }
/*     */     
/* 347 */     HikariPool hikariPool = this.pool;
/* 348 */     if (hikariPool != null) {
/*     */       try {
/* 350 */         LOGGER.info("{} - Shutdown initiated...", getPoolName());
/* 351 */         hikariPool.shutdown();
/* 352 */         LOGGER.info("{} - Shutdown completed.", getPoolName());
/*     */       }
/* 354 */       catch (InterruptedException interruptedException) {
/* 355 */         LOGGER.warn("{} - Interrupted during closing", getPoolName(), interruptedException);
/* 356 */         Thread.currentThread().interrupt();
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
/*     */   public boolean isClosed() {
/* 368 */     return this.isShutdown.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 375 */     return "HikariDataSource (" + this.pool + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\HikariDataSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
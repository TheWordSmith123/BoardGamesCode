/*     */ package com.zaxxer.hikari.metrics.dropwizard;
/*     */ 
/*     */ import com.codahale.metrics.Metric;
/*     */ import com.codahale.metrics.MetricRegistry;
/*     */ import com.codahale.metrics.Timer;
/*     */ import com.codahale.metrics.health.HealthCheck;
/*     */ import com.codahale.metrics.health.HealthCheckRegistry;
/*     */ import com.zaxxer.hikari.HikariConfig;
/*     */ import com.zaxxer.hikari.pool.HikariPool;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.SortedMap;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CodahaleHealthChecker
/*     */ {
/*     */   public static void registerHealthChecks(HikariPool paramHikariPool, HikariConfig paramHikariConfig, HealthCheckRegistry paramHealthCheckRegistry) {
/*  60 */     Properties properties = paramHikariConfig.getHealthCheckProperties();
/*  61 */     MetricRegistry metricRegistry = (MetricRegistry)paramHikariConfig.getMetricRegistry();
/*     */     
/*  63 */     long l1 = Long.parseLong(properties.getProperty("connectivityCheckTimeoutMs", String.valueOf(paramHikariConfig.getConnectionTimeout())));
/*  64 */     paramHealthCheckRegistry.register(MetricRegistry.name(paramHikariConfig.getPoolName(), new String[] { "pool", "ConnectivityCheck" }), new ConnectivityHealthCheck(paramHikariPool, l1));
/*     */     
/*  66 */     long l2 = Long.parseLong(properties.getProperty("expected99thPercentileMs", "0"));
/*  67 */     if (metricRegistry != null && l2 > 0L) {
/*  68 */       SortedMap sortedMap = metricRegistry.getTimers((paramString, paramMetric) -> paramString.equals(MetricRegistry.name(paramHikariConfig.getPoolName(), new String[] { "pool", "Wait" })));
/*     */       
/*  70 */       if (!sortedMap.isEmpty()) {
/*  71 */         Timer timer = (Timer)((Map.Entry)sortedMap.entrySet().iterator().next()).getValue();
/*  72 */         paramHealthCheckRegistry.register(MetricRegistry.name(paramHikariConfig.getPoolName(), new String[] { "pool", "Connection99Percent" }), new Connection99Percent(timer, l2));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConnectivityHealthCheck
/*     */     extends HealthCheck
/*     */   {
/*     */     private final HikariPool pool;
/*     */ 
/*     */     
/*     */     private final long checkTimeoutMs;
/*     */ 
/*     */     
/*     */     ConnectivityHealthCheck(HikariPool param1HikariPool, long param1Long) {
/*  89 */       this.pool = param1HikariPool;
/*  90 */       this.checkTimeoutMs = (param1Long > 0L && param1Long != 2147483647L) ? param1Long : TimeUnit.SECONDS.toMillis(10L);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected HealthCheck.Result check() {
/*  97 */       try (Connection null = this.pool.getConnection(this.checkTimeoutMs)) {
/*  98 */         return HealthCheck.Result.healthy();
/*     */       }
/* 100 */       catch (SQLException sQLException) {
/* 101 */         return HealthCheck.Result.unhealthy(sQLException);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Connection99Percent
/*     */     extends HealthCheck
/*     */   {
/*     */     private final Timer waitTimer;
/*     */     private final long expected99thPercentile;
/*     */     
/*     */     Connection99Percent(Timer param1Timer, long param1Long) {
/* 113 */       this.waitTimer = param1Timer;
/* 114 */       this.expected99thPercentile = param1Long;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected HealthCheck.Result check() {
/* 121 */       long l = TimeUnit.NANOSECONDS.toMillis(Math.round(this.waitTimer.getSnapshot().get99thPercentile()));
/* 122 */       return (l <= this.expected99thPercentile) ? HealthCheck.Result.healthy() : HealthCheck.Result.unhealthy("99th percentile connection wait time of %dms exceeds the threshold %dms", new Object[] { Long.valueOf(l), Long.valueOf(this.expected99thPercentile) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\metrics\dropwizard\CodahaleHealthChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
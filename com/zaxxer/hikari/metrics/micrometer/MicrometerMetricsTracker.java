/*     */ package com.zaxxer.hikari.metrics.micrometer;
/*     */ 
/*     */ import com.zaxxer.hikari.metrics.IMetricsTracker;
/*     */ import com.zaxxer.hikari.metrics.PoolStats;
/*     */ import io.micrometer.core.instrument.Counter;
/*     */ import io.micrometer.core.instrument.Gauge;
/*     */ import io.micrometer.core.instrument.MeterRegistry;
/*     */ import io.micrometer.core.instrument.Timer;
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
/*     */ public class MicrometerMetricsTracker
/*     */   implements IMetricsTracker
/*     */ {
/*     */   public static final String HIKARI_METRIC_NAME_PREFIX = "hikaricp";
/*     */   private static final String METRIC_CATEGORY = "pool";
/*     */   private static final String METRIC_NAME_WAIT = "hikaricp.connections.acquire";
/*     */   private static final String METRIC_NAME_USAGE = "hikaricp.connections.usage";
/*     */   private static final String METRIC_NAME_CONNECT = "hikaricp.connections.creation";
/*     */   private static final String METRIC_NAME_TIMEOUT_RATE = "hikaricp.connections.timeout";
/*     */   private static final String METRIC_NAME_TOTAL_CONNECTIONS = "hikaricp.connections";
/*     */   private static final String METRIC_NAME_IDLE_CONNECTIONS = "hikaricp.connections.idle";
/*     */   private static final String METRIC_NAME_ACTIVE_CONNECTIONS = "hikaricp.connections.active";
/*     */   private static final String METRIC_NAME_PENDING_CONNECTIONS = "hikaricp.connections.pending";
/*     */   private static final String METRIC_NAME_MAX_CONNECTIONS = "hikaricp.connections.max";
/*     */   private static final String METRIC_NAME_MIN_CONNECTIONS = "hikaricp.connections.min";
/*     */   private final Timer connectionObtainTimer;
/*     */   private final Counter connectionTimeoutCounter;
/*     */   private final Timer connectionUsage;
/*     */   private final Timer connectionCreation;
/*     */   private final Gauge totalConnectionGauge;
/*     */   private final Gauge idleConnectionGauge;
/*     */   private final Gauge activeConnectionGauge;
/*     */   private final Gauge pendingConnectionGauge;
/*     */   private final Gauge maxConnectionGauge;
/*     */   private final Gauge minConnectionGauge;
/*     */   private final PoolStats poolStats;
/*     */   
/*     */   MicrometerMetricsTracker(String paramString, PoolStats paramPoolStats, MeterRegistry paramMeterRegistry) {
/*  73 */     this.poolStats = paramPoolStats;
/*     */     
/*  75 */     this
/*     */ 
/*     */       
/*  78 */       .connectionObtainTimer = Timer.builder("hikaricp.connections.acquire").description("Connection acquire time").tags(new String[] { "pool", paramString }).register(paramMeterRegistry);
/*     */     
/*  80 */     this
/*     */ 
/*     */       
/*  83 */       .connectionCreation = Timer.builder("hikaricp.connections.creation").description("Connection creation time").tags(new String[] { "pool", paramString }).register(paramMeterRegistry);
/*     */     
/*  85 */     this
/*     */ 
/*     */       
/*  88 */       .connectionUsage = Timer.builder("hikaricp.connections.usage").description("Connection usage time").tags(new String[] { "pool", paramString }).register(paramMeterRegistry);
/*     */     
/*  90 */     this
/*     */ 
/*     */       
/*  93 */       .connectionTimeoutCounter = Counter.builder("hikaricp.connections.timeout").description("Connection timeout total count").tags(new String[] { "pool", paramString }).register(paramMeterRegistry);
/*     */     
/*  95 */     this
/*     */ 
/*     */       
/*  98 */       .totalConnectionGauge = Gauge.builder("hikaricp.connections", paramPoolStats, PoolStats::getTotalConnections).description("Total connections").tags(new String[] { "pool", paramString }).register(paramMeterRegistry);
/*     */     
/* 100 */     this
/*     */ 
/*     */       
/* 103 */       .idleConnectionGauge = Gauge.builder("hikaricp.connections.idle", paramPoolStats, PoolStats::getIdleConnections).description("Idle connections").tags(new String[] { "pool", paramString }).register(paramMeterRegistry);
/*     */     
/* 105 */     this
/*     */ 
/*     */       
/* 108 */       .activeConnectionGauge = Gauge.builder("hikaricp.connections.active", paramPoolStats, PoolStats::getActiveConnections).description("Active connections").tags(new String[] { "pool", paramString }).register(paramMeterRegistry);
/*     */     
/* 110 */     this
/*     */ 
/*     */       
/* 113 */       .pendingConnectionGauge = Gauge.builder("hikaricp.connections.pending", paramPoolStats, PoolStats::getPendingThreads).description("Pending threads").tags(new String[] { "pool", paramString }).register(paramMeterRegistry);
/*     */     
/* 115 */     this
/*     */ 
/*     */       
/* 118 */       .maxConnectionGauge = Gauge.builder("hikaricp.connections.max", paramPoolStats, PoolStats::getMaxConnections).description("Max connections").tags(new String[] { "pool", paramString }).register(paramMeterRegistry);
/*     */     
/* 120 */     this
/*     */ 
/*     */       
/* 123 */       .minConnectionGauge = Gauge.builder("hikaricp.connections.min", paramPoolStats, PoolStats::getMinConnections).description("Min connections").tags(new String[] { "pool", paramString }).register(paramMeterRegistry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordConnectionAcquiredNanos(long paramLong) {
/* 131 */     this.connectionObtainTimer.record(paramLong, TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordConnectionUsageMillis(long paramLong) {
/* 138 */     this.connectionUsage.record(paramLong, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordConnectionTimeout() {
/* 144 */     this.connectionTimeoutCounter.increment();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordConnectionCreatedMillis(long paramLong) {
/* 150 */     this.connectionCreation.record(paramLong, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\metrics\micrometer\MicrometerMetricsTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
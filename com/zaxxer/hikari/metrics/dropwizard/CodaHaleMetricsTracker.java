/*     */ package com.zaxxer.hikari.metrics.dropwizard;
/*     */ 
/*     */ import com.codahale.metrics.Histogram;
/*     */ import com.codahale.metrics.Meter;
/*     */ import com.codahale.metrics.Metric;
/*     */ import com.codahale.metrics.MetricRegistry;
/*     */ import com.codahale.metrics.Timer;
/*     */ import com.zaxxer.hikari.metrics.IMetricsTracker;
/*     */ import com.zaxxer.hikari.metrics.PoolStats;
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
/*     */ public final class CodaHaleMetricsTracker
/*     */   implements IMetricsTracker
/*     */ {
/*     */   private final String poolName;
/*     */   private final Timer connectionObtainTimer;
/*     */   private final Histogram connectionUsage;
/*     */   private final Histogram connectionCreation;
/*     */   private final Meter connectionTimeoutMeter;
/*     */   private final MetricRegistry registry;
/*     */   private static final String METRIC_CATEGORY = "pool";
/*     */   private static final String METRIC_NAME_WAIT = "Wait";
/*     */   private static final String METRIC_NAME_USAGE = "Usage";
/*     */   private static final String METRIC_NAME_CONNECT = "ConnectionCreation";
/*     */   private static final String METRIC_NAME_TIMEOUT_RATE = "ConnectionTimeoutRate";
/*     */   private static final String METRIC_NAME_TOTAL_CONNECTIONS = "TotalConnections";
/*     */   private static final String METRIC_NAME_IDLE_CONNECTIONS = "IdleConnections";
/*     */   private static final String METRIC_NAME_ACTIVE_CONNECTIONS = "ActiveConnections";
/*     */   private static final String METRIC_NAME_PENDING_CONNECTIONS = "PendingConnections";
/*     */   private static final String METRIC_NAME_MAX_CONNECTIONS = "MaxConnections";
/*     */   private static final String METRIC_NAME_MIN_CONNECTIONS = "MinConnections";
/*     */   
/*     */   CodaHaleMetricsTracker(String paramString, PoolStats paramPoolStats, MetricRegistry paramMetricRegistry) {
/*  52 */     this.poolName = paramString;
/*  53 */     this.registry = paramMetricRegistry;
/*  54 */     this.connectionObtainTimer = paramMetricRegistry.timer(MetricRegistry.name(paramString, new String[] { "pool", "Wait" }));
/*  55 */     this.connectionUsage = paramMetricRegistry.histogram(MetricRegistry.name(paramString, new String[] { "pool", "Usage" }));
/*  56 */     this.connectionCreation = paramMetricRegistry.histogram(MetricRegistry.name(paramString, new String[] { "pool", "ConnectionCreation" }));
/*  57 */     this.connectionTimeoutMeter = paramMetricRegistry.meter(MetricRegistry.name(paramString, new String[] { "pool", "ConnectionTimeoutRate" }));
/*     */     
/*  59 */     paramMetricRegistry.register(MetricRegistry.name(paramString, new String[] { "pool", "TotalConnections" }), (Metric)paramPoolStats::getTotalConnections);
/*     */ 
/*     */     
/*  62 */     paramMetricRegistry.register(MetricRegistry.name(paramString, new String[] { "pool", "IdleConnections" }), (Metric)paramPoolStats::getIdleConnections);
/*     */ 
/*     */     
/*  65 */     paramMetricRegistry.register(MetricRegistry.name(paramString, new String[] { "pool", "ActiveConnections" }), (Metric)paramPoolStats::getActiveConnections);
/*     */ 
/*     */     
/*  68 */     paramMetricRegistry.register(MetricRegistry.name(paramString, new String[] { "pool", "PendingConnections" }), (Metric)paramPoolStats::getPendingThreads);
/*     */ 
/*     */     
/*  71 */     paramMetricRegistry.register(MetricRegistry.name(paramString, new String[] { "pool", "MaxConnections" }), (Metric)paramPoolStats::getMaxConnections);
/*     */ 
/*     */     
/*  74 */     paramMetricRegistry.register(MetricRegistry.name(paramString, new String[] { "pool", "MinConnections" }), (Metric)paramPoolStats::getMinConnections);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*  82 */     this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "Wait" }));
/*  83 */     this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "Usage" }));
/*  84 */     this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "ConnectionCreation" }));
/*  85 */     this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "ConnectionTimeoutRate" }));
/*  86 */     this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "TotalConnections" }));
/*  87 */     this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "IdleConnections" }));
/*  88 */     this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "ActiveConnections" }));
/*  89 */     this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "PendingConnections" }));
/*  90 */     this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "MaxConnections" }));
/*  91 */     this.registry.remove(MetricRegistry.name(this.poolName, new String[] { "pool", "MinConnections" }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordConnectionAcquiredNanos(long paramLong) {
/*  98 */     this.connectionObtainTimer.update(paramLong, TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordConnectionUsageMillis(long paramLong) {
/* 105 */     this.connectionUsage.update(paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordConnectionTimeout() {
/* 111 */     this.connectionTimeoutMeter.mark();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordConnectionCreatedMillis(long paramLong) {
/* 117 */     this.connectionCreation.update(paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public Timer getConnectionAcquisitionTimer() {
/* 122 */     return this.connectionObtainTimer;
/*     */   }
/*     */ 
/*     */   
/*     */   public Histogram getConnectionDurationHistogram() {
/* 127 */     return this.connectionUsage;
/*     */   }
/*     */ 
/*     */   
/*     */   public Histogram getConnectionCreationHistogram() {
/* 132 */     return this.connectionCreation;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\metrics\dropwizard\CodaHaleMetricsTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
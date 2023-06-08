/*    */ package com.zaxxer.hikari.metrics.dropwizard;
/*    */ 
/*    */ import com.codahale.metrics.MetricRegistry;
/*    */ import com.zaxxer.hikari.metrics.IMetricsTracker;
/*    */ import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
/*    */ import com.zaxxer.hikari.metrics.PoolStats;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CodahaleMetricsTrackerFactory
/*    */   implements MetricsTrackerFactory
/*    */ {
/*    */   private final MetricRegistry registry;
/*    */   
/*    */   public CodahaleMetricsTrackerFactory(MetricRegistry paramMetricRegistry) {
/* 30 */     this.registry = paramMetricRegistry;
/*    */   }
/*    */ 
/*    */   
/*    */   public MetricRegistry getRegistry() {
/* 35 */     return this.registry;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public IMetricsTracker create(String paramString, PoolStats paramPoolStats) {
/* 41 */     return new CodaHaleMetricsTracker(paramString, paramPoolStats, this.registry);
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\metrics\dropwizard\CodahaleMetricsTrackerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.zaxxer.hikari.metrics.prometheus;
/*    */ 
/*    */ import com.zaxxer.hikari.metrics.IMetricsTracker;
/*    */ import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
/*    */ import com.zaxxer.hikari.metrics.PoolStats;
/*    */ import io.prometheus.client.CollectorRegistry;
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
/*    */ public class PrometheusHistogramMetricsTrackerFactory
/*    */   implements MetricsTrackerFactory
/*    */ {
/*    */   private HikariCPCollector collector;
/*    */   private CollectorRegistry collectorRegistry;
/*    */   
/*    */   public PrometheusHistogramMetricsTrackerFactory() {
/* 41 */     this.collectorRegistry = CollectorRegistry.defaultRegistry;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PrometheusHistogramMetricsTrackerFactory(CollectorRegistry paramCollectorRegistry) {
/* 49 */     this.collectorRegistry = paramCollectorRegistry;
/*    */   }
/*    */ 
/*    */   
/*    */   public IMetricsTracker create(String paramString, PoolStats paramPoolStats) {
/* 54 */     getCollector().add(paramString, paramPoolStats);
/* 55 */     return new PrometheusHistogramMetricsTracker(paramString, this.collectorRegistry);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private HikariCPCollector getCollector() {
/* 62 */     if (this.collector == null) {
/* 63 */       this.collector = (HikariCPCollector)(new HikariCPCollector()).register(this.collectorRegistry);
/*    */     }
/* 65 */     return this.collector;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\metrics\prometheus\PrometheusHistogramMetricsTrackerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
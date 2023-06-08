/*    */ package com.zaxxer.hikari.metrics.micrometer;
/*    */ 
/*    */ import com.zaxxer.hikari.metrics.IMetricsTracker;
/*    */ import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
/*    */ import com.zaxxer.hikari.metrics.PoolStats;
/*    */ import io.micrometer.core.instrument.MeterRegistry;
/*    */ 
/*    */ 
/*    */ public class MicrometerMetricsTrackerFactory
/*    */   implements MetricsTrackerFactory
/*    */ {
/*    */   private final MeterRegistry registry;
/*    */   
/*    */   public MicrometerMetricsTrackerFactory(MeterRegistry paramMeterRegistry) {
/* 15 */     this.registry = paramMeterRegistry;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public IMetricsTracker create(String paramString, PoolStats paramPoolStats) {
/* 21 */     return new MicrometerMetricsTracker(paramString, paramPoolStats, this.registry);
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\metrics\micrometer\MicrometerMetricsTrackerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.zaxxer.hikari.metrics.prometheus;
/*    */ 
/*    */ import com.zaxxer.hikari.metrics.PoolStats;
/*    */ import io.prometheus.client.Collector;
/*    */ import io.prometheus.client.GaugeMetricFamily;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.function.Function;
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
/*    */ class HikariCPCollector
/*    */   extends Collector
/*    */ {
/* 33 */   private static final List<String> LABEL_NAMES = Collections.singletonList("pool");
/*    */   
/* 35 */   private final Map<String, PoolStats> poolStatsMap = new ConcurrentHashMap<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Collector.MetricFamilySamples> collect() {
/* 40 */     return Arrays.asList(new Collector.MetricFamilySamples[] { (Collector.MetricFamilySamples)
/* 41 */           createGauge("hikaricp_active_connections", "Active connections", PoolStats::getActiveConnections), (Collector.MetricFamilySamples)
/*    */           
/* 43 */           createGauge("hikaricp_idle_connections", "Idle connections", PoolStats::getIdleConnections), (Collector.MetricFamilySamples)
/*    */           
/* 45 */           createGauge("hikaricp_pending_threads", "Pending threads", PoolStats::getPendingThreads), (Collector.MetricFamilySamples)
/*    */           
/* 47 */           createGauge("hikaricp_connections", "The number of current connections", PoolStats::getTotalConnections), (Collector.MetricFamilySamples)
/*    */           
/* 49 */           createGauge("hikaricp_max_connections", "Max connections", PoolStats::getMaxConnections), (Collector.MetricFamilySamples)
/*    */           
/* 51 */           createGauge("hikaricp_min_connections", "Min connections", PoolStats::getMinConnections) });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void add(String paramString, PoolStats paramPoolStats) {
/* 58 */     this.poolStatsMap.put(paramString, paramPoolStats);
/*    */   }
/*    */ 
/*    */   
/*    */   void remove(String paramString) {
/* 63 */     this.poolStatsMap.remove(paramString);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private GaugeMetricFamily createGauge(String paramString1, String paramString2, Function<PoolStats, Integer> paramFunction) {
/* 69 */     GaugeMetricFamily gaugeMetricFamily = new GaugeMetricFamily(paramString1, paramString2, LABEL_NAMES);
/* 70 */     this.poolStatsMap.forEach((paramString, paramPoolStats) -> paramGaugeMetricFamily.addMetric(Collections.singletonList(paramString), ((Integer)paramFunction.apply(paramPoolStats)).intValue()));
/*    */ 
/*    */ 
/*    */     
/* 74 */     return gaugeMetricFamily;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\metrics\prometheus\HikariCPCollector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
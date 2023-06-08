/*    */ package com.zaxxer.hikari.metrics.prometheus;
/*    */ 
/*    */ import com.zaxxer.hikari.metrics.IMetricsTracker;
/*    */ import io.prometheus.client.CollectorRegistry;
/*    */ import io.prometheus.client.Counter;
/*    */ import io.prometheus.client.Histogram;
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
/*    */ class PrometheusHistogramMetricsTracker
/*    */   implements IMetricsTracker
/*    */ {
/* 33 */   private static final Counter CONNECTION_TIMEOUT_COUNTER = ((Counter.Builder)((Counter.Builder)((Counter.Builder)Counter.build()
/* 34 */     .name("hikaricp_connection_timeout_total"))
/* 35 */     .labelNames(new String[] { "pool"
/* 36 */       })).help("Connection timeout total count"))
/* 37 */     .create();
/*    */ 
/*    */   
/* 40 */   private static final Histogram ELAPSED_ACQUIRED_HISTOGRAM = registerHistogram("hikaricp_connection_acquired_nanos", "Connection acquired time (ns)", 1000.0D);
/*    */ 
/*    */   
/* 43 */   private static final Histogram ELAPSED_BORROWED_HISTOGRAM = registerHistogram("hikaricp_connection_usage_millis", "Connection usage (ms)", 1.0D);
/*    */ 
/*    */   
/* 46 */   private static final Histogram ELAPSED_CREATION_HISTOGRAM = registerHistogram("hikaricp_connection_creation_millis", "Connection creation (ms)", 1.0D);
/*    */   private final Counter.Child connectionTimeoutCounterChild;
/*    */   private final Histogram.Child elapsedAcquiredHistogramChild;
/*    */   
/*    */   private static Histogram registerHistogram(String paramString1, String paramString2, double paramDouble) {
/* 51 */     return ((Histogram.Builder)((Histogram.Builder)((Histogram.Builder)Histogram.build()
/* 52 */       .name(paramString1))
/* 53 */       .labelNames(new String[] { "pool"
/* 54 */         })).help(paramString2))
/* 55 */       .exponentialBuckets(paramDouble, 2.0D, 11)
/* 56 */       .create();
/*    */   }
/*    */ 
/*    */   
/*    */   private final Histogram.Child elapsedBorrowedHistogramChild;
/*    */   private final Histogram.Child elapsedCreationHistogramChild;
/*    */   
/*    */   PrometheusHistogramMetricsTracker(String paramString, CollectorRegistry paramCollectorRegistry) {
/* 64 */     registerMetrics(paramCollectorRegistry);
/* 65 */     this.connectionTimeoutCounterChild = (Counter.Child)CONNECTION_TIMEOUT_COUNTER.labels(new String[] { paramString });
/* 66 */     this.elapsedAcquiredHistogramChild = (Histogram.Child)ELAPSED_ACQUIRED_HISTOGRAM.labels(new String[] { paramString });
/* 67 */     this.elapsedBorrowedHistogramChild = (Histogram.Child)ELAPSED_BORROWED_HISTOGRAM.labels(new String[] { paramString });
/* 68 */     this.elapsedCreationHistogramChild = (Histogram.Child)ELAPSED_CREATION_HISTOGRAM.labels(new String[] { paramString });
/*    */   }
/*    */   
/*    */   private void registerMetrics(CollectorRegistry paramCollectorRegistry) {
/* 72 */     CONNECTION_TIMEOUT_COUNTER.register(paramCollectorRegistry);
/* 73 */     ELAPSED_ACQUIRED_HISTOGRAM.register(paramCollectorRegistry);
/* 74 */     ELAPSED_BORROWED_HISTOGRAM.register(paramCollectorRegistry);
/* 75 */     ELAPSED_CREATION_HISTOGRAM.register(paramCollectorRegistry);
/*    */   }
/*    */ 
/*    */   
/*    */   public void recordConnectionAcquiredNanos(long paramLong) {
/* 80 */     this.elapsedAcquiredHistogramChild.observe(paramLong);
/*    */   }
/*    */ 
/*    */   
/*    */   public void recordConnectionUsageMillis(long paramLong) {
/* 85 */     this.elapsedBorrowedHistogramChild.observe(paramLong);
/*    */   }
/*    */ 
/*    */   
/*    */   public void recordConnectionCreatedMillis(long paramLong) {
/* 90 */     this.elapsedCreationHistogramChild.observe(paramLong);
/*    */   }
/*    */ 
/*    */   
/*    */   public void recordConnectionTimeout() {
/* 95 */     this.connectionTimeoutCounterChild.inc();
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\metrics\prometheus\PrometheusHistogramMetricsTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
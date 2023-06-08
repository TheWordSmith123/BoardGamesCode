/*    */ package com.zaxxer.hikari.pool;
/*    */ 
/*    */ import java.util.concurrent.ScheduledExecutorService;
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
/*    */ class ProxyLeakTaskFactory
/*    */ {
/*    */   private ScheduledExecutorService executorService;
/*    */   private long leakDetectionThreshold;
/*    */   
/*    */   ProxyLeakTaskFactory(long paramLong, ScheduledExecutorService paramScheduledExecutorService) {
/* 34 */     this.executorService = paramScheduledExecutorService;
/* 35 */     this.leakDetectionThreshold = paramLong;
/*    */   }
/*    */ 
/*    */   
/*    */   ProxyLeakTask schedule(PoolEntry paramPoolEntry) {
/* 40 */     return (this.leakDetectionThreshold == 0L) ? ProxyLeakTask.NO_LEAK : scheduleNewTask(paramPoolEntry);
/*    */   }
/*    */ 
/*    */   
/*    */   void updateLeakDetectionThreshold(long paramLong) {
/* 45 */     this.leakDetectionThreshold = paramLong;
/*    */   }
/*    */   
/*    */   private ProxyLeakTask scheduleNewTask(PoolEntry paramPoolEntry) {
/* 49 */     ProxyLeakTask proxyLeakTask = new ProxyLeakTask(paramPoolEntry);
/* 50 */     proxyLeakTask.schedule(this.executorService, this.leakDetectionThreshold);
/*    */     
/* 52 */     return proxyLeakTask;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\pool\ProxyLeakTaskFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
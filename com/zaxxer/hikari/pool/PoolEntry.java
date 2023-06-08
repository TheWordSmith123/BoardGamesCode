/*     */ package com.zaxxer.hikari.pool;
/*     */ 
/*     */ import com.zaxxer.hikari.util.ClockSource;
/*     */ import com.zaxxer.hikari.util.ConcurrentBag;
/*     */ import com.zaxxer.hikari.util.FastList;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Statement;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*     */ final class PoolEntry
/*     */   implements ConcurrentBag.IConcurrentBagEntry
/*     */ {
/*  38 */   private static final Logger LOGGER = LoggerFactory.getLogger(PoolEntry.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private volatile int state = 0;
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
/*  59 */   private static final AtomicIntegerFieldUpdater<PoolEntry> stateUpdater = AtomicIntegerFieldUpdater.newUpdater(PoolEntry.class, "state");
/*     */   Connection connection;
/*     */   long lastAccessed;
/*     */   
/*     */   PoolEntry(Connection paramConnection, PoolBase paramPoolBase, boolean paramBoolean1, boolean paramBoolean2) {
/*  64 */     this.connection = paramConnection;
/*  65 */     this.hikariPool = (HikariPool)paramPoolBase;
/*  66 */     this.isReadOnly = paramBoolean1;
/*  67 */     this.isAutoCommit = paramBoolean2;
/*  68 */     this.lastAccessed = ClockSource.currentTime();
/*  69 */     this.openStatements = new FastList(Statement.class, 16);
/*     */   }
/*     */   long lastBorrowed; private volatile boolean evict;
/*     */   private volatile ScheduledFuture<?> endOfLife;
/*     */   private final FastList<Statement> openStatements;
/*     */   private final HikariPool hikariPool;
/*     */   private final boolean isReadOnly;
/*     */   private final boolean isAutoCommit;
/*     */   
/*     */   void recycle(long paramLong) {
/*  79 */     if (this.connection != null) {
/*  80 */       this.lastAccessed = paramLong;
/*  81 */       this.hikariPool.recycle(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setFutureEol(ScheduledFuture<?> paramScheduledFuture) {
/*  92 */     this.endOfLife = paramScheduledFuture;
/*     */   }
/*     */ 
/*     */   
/*     */   Connection createProxyConnection(ProxyLeakTask paramProxyLeakTask, long paramLong) {
/*  97 */     return ProxyFactory.getProxyConnection(this, this.connection, this.openStatements, paramProxyLeakTask, paramLong, this.isReadOnly, this.isAutoCommit);
/*     */   }
/*     */ 
/*     */   
/*     */   void resetConnectionState(ProxyConnection paramProxyConnection, int paramInt) {
/* 102 */     this.hikariPool.resetConnectionState(this.connection, paramProxyConnection, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   String getPoolName() {
/* 107 */     return this.hikariPool.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isMarkedEvicted() {
/* 112 */     return this.evict;
/*     */   }
/*     */ 
/*     */   
/*     */   void markEvicted() {
/* 117 */     this.evict = true;
/*     */   }
/*     */ 
/*     */   
/*     */   void evict(String paramString) {
/* 122 */     this.hikariPool.closeConnection(this, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   long getMillisSinceBorrowed() {
/* 128 */     return ClockSource.elapsedMillis(this.lastBorrowed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 135 */     long l = ClockSource.currentTime();
/* 136 */     return this.connection + ", accessed " + 
/* 137 */       ClockSource.elapsedDisplayString(this.lastAccessed, l) + " ago, " + 
/* 138 */       stateToString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getState() {
/* 149 */     return stateUpdater.get(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean compareAndSet(int paramInt1, int paramInt2) {
/* 156 */     return stateUpdater.compareAndSet(this, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setState(int paramInt) {
/* 163 */     stateUpdater.set(this, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   Connection close() {
/* 168 */     ScheduledFuture<?> scheduledFuture = this.endOfLife;
/* 169 */     if (scheduledFuture != null && !scheduledFuture.isDone() && !scheduledFuture.cancel(false)) {
/* 170 */       LOGGER.warn("{} - maxLifeTime expiration task cancellation unexpectedly returned false for connection {}", getPoolName(), this.connection);
/*     */     }
/*     */     
/* 173 */     Connection connection = this.connection;
/* 174 */     this.connection = null;
/* 175 */     this.endOfLife = null;
/* 176 */     return connection;
/*     */   }
/*     */ 
/*     */   
/*     */   private String stateToString() {
/* 181 */     switch (this.state) {
/*     */       case 1:
/* 183 */         return "IN_USE";
/*     */       case 0:
/* 185 */         return "NOT_IN_USE";
/*     */       case -1:
/* 187 */         return "REMOVED";
/*     */       case -2:
/* 189 */         return "RESERVED";
/*     */     } 
/* 191 */     return "Invalid";
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\pool\PoolEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
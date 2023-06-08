/*     */ package com.zaxxer.hikari.util;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.LockSupport;
/*     */ import java.util.stream.Collectors;
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
/*     */ public class ConcurrentBag<T extends ConcurrentBag.IConcurrentBagEntry>
/*     */   implements AutoCloseable
/*     */ {
/*  67 */   private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentBag.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CopyOnWriteArrayList<T> sharedList;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean weakThreadLocals;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ThreadLocal<List<Object>> threadList;
/*     */ 
/*     */ 
/*     */   
/*     */   private final IBagStateListener listener;
/*     */ 
/*     */ 
/*     */   
/*     */   private final AtomicInteger waiters;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean closed;
/*     */ 
/*     */ 
/*     */   
/*     */   private final SynchronousQueue<T> handoffQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentBag(IBagStateListener paramIBagStateListener) {
/* 103 */     this.listener = paramIBagStateListener;
/* 104 */     this.weakThreadLocals = useWeakThreadLocals();
/*     */     
/* 106 */     this.handoffQueue = new SynchronousQueue<>(true);
/* 107 */     this.waiters = new AtomicInteger();
/* 108 */     this.sharedList = new CopyOnWriteArrayList<>();
/* 109 */     if (this.weakThreadLocals) {
/* 110 */       this.threadList = ThreadLocal.withInitial(() -> new ArrayList(16));
/*     */     } else {
/*     */       
/* 113 */       this.threadList = ThreadLocal.withInitial(() -> new FastList(IConcurrentBagEntry.class, 16));
/*     */     } 
/*     */   }
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
/*     */   public T borrow(long paramLong, TimeUnit paramTimeUnit) {
/* 129 */     List<Object> list = this.threadList.get(); int i;
/* 130 */     for (i = list.size() - 1; i >= 0; i--) {
/* 131 */       WeakReference weakReference = (WeakReference)list.remove(i);
/*     */       
/* 133 */       IConcurrentBagEntry iConcurrentBagEntry = this.weakThreadLocals ? ((WeakReference<IConcurrentBagEntry>)weakReference).get() : (IConcurrentBagEntry)weakReference;
/* 134 */       if (iConcurrentBagEntry != null && iConcurrentBagEntry.compareAndSet(0, 1)) {
/* 135 */         return (T)iConcurrentBagEntry;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 140 */     i = this.waiters.incrementAndGet();
/*     */     try {
/* 142 */       for (IConcurrentBagEntry iConcurrentBagEntry : this.sharedList) {
/* 143 */         if (iConcurrentBagEntry.compareAndSet(0, 1)) {
/*     */           
/* 145 */           if (i > 1) {
/* 146 */             this.listener.addBagItem(i - 1);
/*     */           }
/* 148 */           return (T)iConcurrentBagEntry;
/*     */         } 
/*     */       } 
/*     */       
/* 152 */       this.listener.addBagItem(i);
/*     */       
/* 154 */       paramLong = paramTimeUnit.toNanos(paramLong);
/*     */       do {
/* 156 */         long l = ClockSource.currentTime();
/* 157 */         IConcurrentBagEntry iConcurrentBagEntry = (IConcurrentBagEntry)this.handoffQueue.poll(paramLong, TimeUnit.NANOSECONDS);
/* 158 */         if (iConcurrentBagEntry == null || iConcurrentBagEntry.compareAndSet(0, 1)) {
/* 159 */           return (T)iConcurrentBagEntry;
/*     */         }
/*     */         
/* 162 */         paramLong -= ClockSource.elapsedNanos(l);
/* 163 */       } while (paramLong > 10000L);
/*     */       
/* 165 */       return null;
/*     */     } finally {
/*     */       
/* 168 */       this.waiters.decrementAndGet();
/*     */     } 
/*     */   }
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
/*     */   public void requite(T paramT) {
/* 183 */     paramT.setState(0);
/*     */     
/* 185 */     for (byte b = 0; this.waiters.get() > 0; b++) {
/* 186 */       if (paramT.getState() != 0 || this.handoffQueue.offer(paramT)) {
/*     */         return;
/*     */       }
/* 189 */       if ((b & 0xFF) == 255) {
/* 190 */         LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(10L));
/*     */       } else {
/*     */         
/* 193 */         Thread.yield();
/*     */       } 
/*     */     } 
/*     */     
/* 197 */     List list = this.threadList.get();
/* 198 */     if (list.size() < 50) {
/* 199 */       list.add(this.weakThreadLocals ? new WeakReference<>(paramT) : paramT);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(T paramT) {
/* 210 */     if (this.closed) {
/* 211 */       LOGGER.info("ConcurrentBag has been closed, ignoring add()");
/* 212 */       throw new IllegalStateException("ConcurrentBag has been closed, ignoring add()");
/*     */     } 
/*     */     
/* 215 */     this.sharedList.add(paramT);
/*     */ 
/*     */     
/* 218 */     while (this.waiters.get() > 0 && paramT.getState() == 0 && !this.handoffQueue.offer(paramT)) {
/* 219 */       Thread.yield();
/*     */     }
/*     */   }
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
/*     */   public boolean remove(T paramT) {
/* 234 */     if (!paramT.compareAndSet(1, -1) && !paramT.compareAndSet(-2, -1) && !this.closed) {
/* 235 */       LOGGER.warn("Attempt to remove an object from the bag that was not borrowed or reserved: {}", paramT);
/* 236 */       return false;
/*     */     } 
/*     */     
/* 239 */     boolean bool = this.sharedList.remove(paramT);
/* 240 */     if (!bool && !this.closed) {
/* 241 */       LOGGER.warn("Attempt to remove an object from the bag that does not exist: {}", paramT);
/*     */     }
/*     */     
/* 244 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 253 */     this.closed = true;
/*     */   }
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
/*     */   public List<T> values(int paramInt) {
/* 267 */     List<?> list = (List)this.sharedList.stream().filter(paramIConcurrentBagEntry -> (paramIConcurrentBagEntry.getState() == paramInt)).collect(Collectors.toList());
/* 268 */     Collections.reverse(list);
/* 269 */     return (List)list;
/*     */   }
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
/*     */   public List<T> values() {
/* 283 */     return (List<T>)this.sharedList.clone();
/*     */   }
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
/*     */   public boolean reserve(T paramT) {
/* 300 */     return paramT.compareAndSet(0, -2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unreserve(T paramT) {
/* 311 */     if (paramT.compareAndSet(-2, 0)) {
/*     */       
/* 313 */       while (this.waiters.get() > 0 && !this.handoffQueue.offer(paramT)) {
/* 314 */         Thread.yield();
/*     */       }
/*     */     } else {
/*     */       
/* 318 */       LOGGER.warn("Attempt to relinquish an object to the bag that was not reserved: {}", paramT);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWaitingThreadCount() {
/* 330 */     return this.waiters.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCount(int paramInt) {
/* 341 */     byte b = 0;
/* 342 */     for (IConcurrentBagEntry iConcurrentBagEntry : this.sharedList) {
/* 343 */       if (iConcurrentBagEntry.getState() == paramInt) {
/* 344 */         b++;
/*     */       }
/*     */     } 
/* 347 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getStateCounts() {
/* 352 */     int[] arrayOfInt = new int[6];
/* 353 */     for (IConcurrentBagEntry iConcurrentBagEntry : this.sharedList) {
/* 354 */       arrayOfInt[iConcurrentBagEntry.getState()] = arrayOfInt[iConcurrentBagEntry.getState()] + 1;
/*     */     }
/* 356 */     arrayOfInt[4] = this.sharedList.size();
/* 357 */     arrayOfInt[5] = this.waiters.get();
/*     */     
/* 359 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 369 */     return this.sharedList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dumpState() {
/* 374 */     this.sharedList.forEach(paramIConcurrentBagEntry -> LOGGER.info(paramIConcurrentBagEntry.toString()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useWeakThreadLocals() {
/*     */     try {
/* 387 */       if (System.getProperty("com.zaxxer.hikari.useWeakReferences") != null) {
/* 388 */         return Boolean.getBoolean("com.zaxxer.hikari.useWeakReferences");
/*     */       }
/*     */       
/* 391 */       return (getClass().getClassLoader() != ClassLoader.getSystemClassLoader());
/*     */     }
/* 393 */     catch (SecurityException securityException) {
/* 394 */       return true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface IBagStateListener {
/*     */     void addBagItem(int param1Int);
/*     */   }
/*     */   
/*     */   public static interface IConcurrentBagEntry {
/*     */     public static final int STATE_NOT_IN_USE = 0;
/*     */     public static final int STATE_IN_USE = 1;
/*     */     public static final int STATE_REMOVED = -1;
/*     */     public static final int STATE_RESERVED = -2;
/*     */     
/*     */     boolean compareAndSet(int param1Int1, int param1Int2);
/*     */     
/*     */     void setState(int param1Int);
/*     */     
/*     */     int getState();
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikar\\util\ConcurrentBag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
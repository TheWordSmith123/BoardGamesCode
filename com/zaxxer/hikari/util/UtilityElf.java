/*     */ package com.zaxxer.hikari.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
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
/*     */ public final class UtilityElf
/*     */ {
/*     */   public static String getNullIfEmpty(String paramString) {
/*  38 */     return (paramString == null) ? null : (paramString.trim().isEmpty() ? null : paramString.trim());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void quietlySleep(long paramLong) {
/*     */     try {
/*  49 */       Thread.sleep(paramLong);
/*     */     }
/*  51 */     catch (InterruptedException interruptedException) {
/*     */       
/*  53 */       Thread.currentThread().interrupt();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean safeIsAssignableFrom(Object paramObject, String paramString) {
/*     */     try {
/*  65 */       Class<?> clazz = Class.forName(paramString);
/*  66 */       return clazz.isAssignableFrom(paramObject.getClass());
/*  67 */     } catch (ClassNotFoundException classNotFoundException) {
/*  68 */       return false;
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
/*     */   public static <T> T createInstance(String paramString, Class<T> paramClass, Object... paramVarArgs) {
/*  84 */     if (paramString == null) {
/*  85 */       return null;
/*     */     }
/*     */     
/*     */     try {
/*  89 */       Class<?> clazz = UtilityElf.class.getClassLoader().loadClass(paramString);
/*  90 */       if (paramVarArgs.length == 0) {
/*  91 */         return paramClass.cast(clazz.newInstance());
/*     */       }
/*     */       
/*  94 */       Class[] arrayOfClass = new Class[paramVarArgs.length];
/*  95 */       for (byte b = 0; b < paramVarArgs.length; b++) {
/*  96 */         arrayOfClass[b] = paramVarArgs[b].getClass();
/*     */       }
/*  98 */       Constructor<?> constructor = clazz.getConstructor(arrayOfClass);
/*  99 */       return paramClass.cast(constructor.newInstance(paramVarArgs));
/*     */     }
/* 101 */     catch (Exception exception) {
/* 102 */       throw new RuntimeException(exception);
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
/*     */   public static ThreadPoolExecutor createThreadPoolExecutor(int paramInt, String paramString, ThreadFactory paramThreadFactory, RejectedExecutionHandler paramRejectedExecutionHandler) {
/* 117 */     if (paramThreadFactory == null) {
/* 118 */       paramThreadFactory = new DefaultThreadFactory(paramString, true);
/*     */     }
/*     */     
/* 121 */     LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue(paramInt);
/* 122 */     ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, linkedBlockingQueue, paramThreadFactory, paramRejectedExecutionHandler);
/* 123 */     threadPoolExecutor.allowCoreThreadTimeOut(true);
/* 124 */     return threadPoolExecutor;
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
/*     */   public static ThreadPoolExecutor createThreadPoolExecutor(BlockingQueue<Runnable> paramBlockingQueue, String paramString, ThreadFactory paramThreadFactory, RejectedExecutionHandler paramRejectedExecutionHandler) {
/* 138 */     if (paramThreadFactory == null) {
/* 139 */       paramThreadFactory = new DefaultThreadFactory(paramString, true);
/*     */     }
/*     */     
/* 142 */     ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, paramBlockingQueue, paramThreadFactory, paramRejectedExecutionHandler);
/* 143 */     threadPoolExecutor.allowCoreThreadTimeOut(true);
/* 144 */     return threadPoolExecutor;
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
/*     */   public static int getTransactionIsolation(String paramString) {
/* 159 */     if (paramString != null) {
/*     */       
/*     */       try {
/* 162 */         String str = paramString.toUpperCase(Locale.ENGLISH);
/* 163 */         return IsolationLevel.valueOf(str).getLevelId();
/* 164 */       } catch (IllegalArgumentException illegalArgumentException) {
/*     */         
/*     */         try {
/* 167 */           int i = Integer.parseInt(paramString);
/* 168 */           for (IsolationLevel isolationLevel : IsolationLevel.values()) {
/* 169 */             if (isolationLevel.getLevelId() == i) {
/* 170 */               return isolationLevel.getLevelId();
/*     */             }
/*     */           } 
/*     */           
/* 174 */           throw new IllegalArgumentException("Invalid transaction isolation value: " + paramString);
/*     */         }
/* 176 */         catch (NumberFormatException numberFormatException) {
/* 177 */           throw new IllegalArgumentException("Invalid transaction isolation value: " + paramString, numberFormatException);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 182 */     return -1;
/*     */   }
/*     */   
/*     */   public static final class DefaultThreadFactory
/*     */     implements ThreadFactory {
/*     */     private final String threadName;
/*     */     private final boolean daemon;
/*     */     
/*     */     public DefaultThreadFactory(String param1String, boolean param1Boolean) {
/* 191 */       this.threadName = param1String;
/* 192 */       this.daemon = param1Boolean;
/*     */     }
/*     */ 
/*     */     
/*     */     public Thread newThread(Runnable param1Runnable) {
/* 197 */       Thread thread = new Thread(param1Runnable, this.threadName);
/* 198 */       thread.setDaemon(this.daemon);
/* 199 */       return thread;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikar\\util\UtilityElf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
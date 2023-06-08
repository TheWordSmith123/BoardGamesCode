/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.spi.LoggerFactoryBinder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StaticLoggerBinder
/*    */   implements LoggerFactoryBinder
/*    */ {
/* 43 */   private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final StaticLoggerBinder getSingleton() {
/* 51 */     return SINGLETON;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public static String REQUESTED_API_VERSION = "1.6.99";
/*    */   
/* 61 */   private static final String loggerFactoryClassStr = JDK14LoggerFactory.class.getName();
/*    */ 
/*    */ 
/*    */   
/*    */   private final ILoggerFactory loggerFactory;
/*    */ 
/*    */ 
/*    */   
/*    */   private StaticLoggerBinder() {
/* 70 */     this.loggerFactory = new JDK14LoggerFactory();
/*    */   }
/*    */   
/*    */   public ILoggerFactory getLoggerFactory() {
/* 74 */     return this.loggerFactory;
/*    */   }
/*    */   
/*    */   public String getLoggerFactoryClassStr() {
/* 78 */     return loggerFactoryClassStr;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\org\slf4j\impl\StaticLoggerBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
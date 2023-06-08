/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import java.util.logging.Logger;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.Logger;
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
/*    */ public class JDK14LoggerFactory
/*    */   implements ILoggerFactory
/*    */ {
/*    */   ConcurrentMap<String, Logger> loggerMap;
/*    */   
/*    */   public JDK14LoggerFactory() {
/* 45 */     this.loggerMap = new ConcurrentHashMap<String, Logger>();
/*    */ 
/*    */     
/* 48 */     Logger.getLogger("");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Logger getLogger(String paramString) {
/* 58 */     if (paramString.equalsIgnoreCase("ROOT")) {
/* 59 */       paramString = "";
/*    */     }
/*    */     
/* 62 */     Logger logger1 = this.loggerMap.get(paramString);
/* 63 */     if (logger1 != null) {
/* 64 */       return logger1;
/*    */     }
/* 66 */     Logger logger = Logger.getLogger(paramString);
/* 67 */     JDK14LoggerAdapter jDK14LoggerAdapter = new JDK14LoggerAdapter(logger);
/* 68 */     Logger logger2 = (Logger)this.loggerMap.putIfAbsent(paramString, jDK14LoggerAdapter);
/* 69 */     return (logger2 == null) ? (Logger)jDK14LoggerAdapter : logger2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\org\slf4j\impl\JDK14LoggerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
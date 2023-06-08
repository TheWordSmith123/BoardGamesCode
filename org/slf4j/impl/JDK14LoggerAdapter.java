/*     */ package org.slf4j.impl;
/*     */ 
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogRecord;
/*     */ import java.util.logging.Logger;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.event.LoggingEvent;
/*     */ import org.slf4j.helpers.FormattingTuple;
/*     */ import org.slf4j.helpers.MarkerIgnoringBase;
/*     */ import org.slf4j.helpers.MessageFormatter;
/*     */ import org.slf4j.spi.LocationAwareLogger;
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
/*     */ public final class JDK14LoggerAdapter
/*     */   extends MarkerIgnoringBase
/*     */   implements LocationAwareLogger
/*     */ {
/*     */   private static final long serialVersionUID = -8053026990503422791L;
/*     */   final transient Logger logger;
/*     */   
/*     */   JDK14LoggerAdapter(Logger paramLogger) {
/*  57 */     this.logger = paramLogger;
/*  58 */     this.name = paramLogger.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTraceEnabled() {
/*  67 */     return this.logger.isLoggable(Level.FINEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String paramString) {
/*  77 */     if (this.logger.isLoggable(Level.FINEST)) {
/*  78 */       log(SELF, Level.FINEST, paramString, (Throwable)null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String paramString, Object paramObject) {
/*  97 */     if (this.logger.isLoggable(Level.FINEST)) {
/*  98 */       FormattingTuple formattingTuple = MessageFormatter.format(paramString, paramObject);
/*  99 */       log(SELF, Level.FINEST, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String paramString, Object paramObject1, Object paramObject2) {
/* 120 */     if (this.logger.isLoggable(Level.FINEST)) {
/* 121 */       FormattingTuple formattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
/* 122 */       log(SELF, Level.FINEST, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String paramString, Object... paramVarArgs) {
/* 141 */     if (this.logger.isLoggable(Level.FINEST)) {
/* 142 */       FormattingTuple formattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
/* 143 */       log(SELF, Level.FINEST, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */   public void trace(String paramString, Throwable paramThrowable) {
/* 156 */     if (this.logger.isLoggable(Level.FINEST)) {
/* 157 */       log(SELF, Level.FINEST, paramString, paramThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDebugEnabled() {
/* 167 */     return this.logger.isLoggable(Level.FINE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String paramString) {
/* 177 */     if (this.logger.isLoggable(Level.FINE)) {
/* 178 */       log(SELF, Level.FINE, paramString, (Throwable)null);
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
/*     */ 
/*     */   
/*     */   public void debug(String paramString, Object paramObject) {
/* 196 */     if (this.logger.isLoggable(Level.FINE)) {
/* 197 */       FormattingTuple formattingTuple = MessageFormatter.format(paramString, paramObject);
/* 198 */       log(SELF, Level.FINE, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String paramString, Object paramObject1, Object paramObject2) {
/* 219 */     if (this.logger.isLoggable(Level.FINE)) {
/* 220 */       FormattingTuple formattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
/* 221 */       log(SELF, Level.FINE, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String paramString, Object... paramVarArgs) {
/* 240 */     if (this.logger.isLoggable(Level.FINE)) {
/* 241 */       FormattingTuple formattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
/* 242 */       log(SELF, Level.FINE, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */   public void debug(String paramString, Throwable paramThrowable) {
/* 255 */     if (this.logger.isLoggable(Level.FINE)) {
/* 256 */       log(SELF, Level.FINE, paramString, paramThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInfoEnabled() {
/* 266 */     return this.logger.isLoggable(Level.INFO);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String paramString) {
/* 276 */     if (this.logger.isLoggable(Level.INFO)) {
/* 277 */       log(SELF, Level.INFO, paramString, (Throwable)null);
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
/*     */ 
/*     */   
/*     */   public void info(String paramString, Object paramObject) {
/* 295 */     if (this.logger.isLoggable(Level.INFO)) {
/* 296 */       FormattingTuple formattingTuple = MessageFormatter.format(paramString, paramObject);
/* 297 */       log(SELF, Level.INFO, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String paramString, Object paramObject1, Object paramObject2) {
/* 318 */     if (this.logger.isLoggable(Level.INFO)) {
/* 319 */       FormattingTuple formattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
/* 320 */       log(SELF, Level.INFO, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String paramString, Object... paramVarArgs) {
/* 339 */     if (this.logger.isLoggable(Level.INFO)) {
/* 340 */       FormattingTuple formattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
/* 341 */       log(SELF, Level.INFO, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */   public void info(String paramString, Throwable paramThrowable) {
/* 355 */     if (this.logger.isLoggable(Level.INFO)) {
/* 356 */       log(SELF, Level.INFO, paramString, paramThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWarnEnabled() {
/* 367 */     return this.logger.isLoggable(Level.WARNING);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String paramString) {
/* 377 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 378 */       log(SELF, Level.WARNING, paramString, (Throwable)null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String paramString, Object paramObject) {
/* 397 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 398 */       FormattingTuple formattingTuple = MessageFormatter.format(paramString, paramObject);
/* 399 */       log(SELF, Level.WARNING, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String paramString, Object paramObject1, Object paramObject2) {
/* 420 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 421 */       FormattingTuple formattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
/* 422 */       log(SELF, Level.WARNING, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String paramString, Object... paramVarArgs) {
/* 441 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 442 */       FormattingTuple formattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
/* 443 */       log(SELF, Level.WARNING, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */   public void warn(String paramString, Throwable paramThrowable) {
/* 457 */     if (this.logger.isLoggable(Level.WARNING)) {
/* 458 */       log(SELF, Level.WARNING, paramString, paramThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isErrorEnabled() {
/* 468 */     return this.logger.isLoggable(Level.SEVERE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String paramString) {
/* 478 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 479 */       log(SELF, Level.SEVERE, paramString, (Throwable)null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String paramString, Object paramObject) {
/* 498 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 499 */       FormattingTuple formattingTuple = MessageFormatter.format(paramString, paramObject);
/* 500 */       log(SELF, Level.SEVERE, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String paramString, Object paramObject1, Object paramObject2) {
/* 521 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 522 */       FormattingTuple formattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
/* 523 */       log(SELF, Level.SEVERE, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String paramString, Object... paramVarArgs) {
/* 542 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 543 */       FormattingTuple formattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
/* 544 */       log(SELF, Level.SEVERE, formattingTuple.getMessage(), formattingTuple.getThrowable());
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
/*     */   public void error(String paramString, Throwable paramThrowable) {
/* 558 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 559 */       log(SELF, Level.SEVERE, paramString, paramThrowable);
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
/*     */   
/*     */   private void log(String paramString1, Level paramLevel, String paramString2, Throwable paramThrowable) {
/* 576 */     LogRecord logRecord = new LogRecord(paramLevel, paramString2);
/* 577 */     logRecord.setLoggerName(getName());
/* 578 */     logRecord.setThrown(paramThrowable);
/*     */ 
/*     */     
/* 581 */     fillCallerData(paramString1, logRecord);
/* 582 */     this.logger.log(logRecord);
/*     */   }
/*     */   
/* 585 */   static String SELF = JDK14LoggerAdapter.class.getName();
/* 586 */   static String SUPER = MarkerIgnoringBase.class.getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void fillCallerData(String paramString, LogRecord paramLogRecord) {
/* 595 */     StackTraceElement[] arrayOfStackTraceElement = (new Throwable()).getStackTrace();
/*     */     
/* 597 */     int i = -1; int j;
/* 598 */     for (j = 0; j < arrayOfStackTraceElement.length; j++) {
/* 599 */       String str = arrayOfStackTraceElement[j].getClassName();
/* 600 */       if (str.equals(paramString) || str.equals(SUPER)) {
/* 601 */         i = j;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 606 */     j = -1;
/* 607 */     for (int k = i + 1; k < arrayOfStackTraceElement.length; k++) {
/* 608 */       String str = arrayOfStackTraceElement[k].getClassName();
/* 609 */       if (!str.equals(paramString) && !str.equals(SUPER)) {
/* 610 */         j = k;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 615 */     if (j != -1) {
/* 616 */       StackTraceElement stackTraceElement = arrayOfStackTraceElement[j];
/*     */ 
/*     */       
/* 619 */       paramLogRecord.setSourceClassName(stackTraceElement.getClassName());
/* 620 */       paramLogRecord.setSourceMethodName(stackTraceElement.getMethodName());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void log(Marker paramMarker, String paramString1, int paramInt, String paramString2, Object[] paramArrayOfObject, Throwable paramThrowable) {
/* 625 */     Level level = slf4jLevelIntToJULLevel(paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 631 */     if (this.logger.isLoggable(level)) {
/* 632 */       log(paramString1, level, paramString2, paramThrowable);
/*     */     }
/*     */   }
/*     */   
/*     */   private Level slf4jLevelIntToJULLevel(int paramInt) {
/*     */     Level level;
/* 638 */     switch (paramInt) {
/*     */       case 0:
/* 640 */         level = Level.FINEST;
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
/* 657 */         return level;case 10: level = Level.FINE; return level;case 20: level = Level.INFO; return level;case 30: level = Level.WARNING; return level;case 40: level = Level.SEVERE; return level;
/*     */     } 
/*     */     throw new IllegalStateException("Level number " + paramInt + " is not recognized.");
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(LoggingEvent paramLoggingEvent) {
/* 664 */     Level level = slf4jLevelIntToJULLevel(paramLoggingEvent.getLevel().toInt());
/* 665 */     if (this.logger.isLoggable(level)) {
/* 666 */       LogRecord logRecord = eventToRecord(paramLoggingEvent, level);
/* 667 */       this.logger.log(logRecord);
/*     */     } 
/*     */   }
/*     */   
/*     */   private LogRecord eventToRecord(LoggingEvent paramLoggingEvent, Level paramLevel) {
/* 672 */     String str = paramLoggingEvent.getMessage();
/* 673 */     Object[] arrayOfObject = paramLoggingEvent.getArgumentArray();
/* 674 */     FormattingTuple formattingTuple = MessageFormatter.arrayFormat(str, arrayOfObject);
/* 675 */     if (formattingTuple.getThrowable() != null && paramLoggingEvent.getThrowable() != null) {
/* 676 */       throw new IllegalArgumentException("both last element in argument array and last argument are of type Throwable");
/*     */     }
/*     */     
/* 679 */     Throwable throwable = paramLoggingEvent.getThrowable();
/* 680 */     if (formattingTuple.getThrowable() != null) {
/* 681 */       throwable = formattingTuple.getThrowable();
/* 682 */       throw new IllegalStateException("fix above code");
/*     */     } 
/*     */     
/* 685 */     LogRecord logRecord = new LogRecord(paramLevel, formattingTuple.getMessage());
/* 686 */     logRecord.setLoggerName(paramLoggingEvent.getLoggerName());
/* 687 */     logRecord.setMillis(paramLoggingEvent.getTimeStamp());
/* 688 */     logRecord.setSourceClassName("NA/SubstituteLogger");
/* 689 */     logRecord.setSourceMethodName("NA/SubstituteLogger");
/*     */     
/* 691 */     logRecord.setThrown(throwable);
/* 692 */     return logRecord;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\org\slf4j\impl\JDK14LoggerAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
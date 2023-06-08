/*     */ package org.slf4j.event;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.helpers.SubstituteLogger;
/*     */ 
/*     */ public class EventRecodingLogger
/*     */   implements Logger
/*     */ {
/*     */   String name;
/*     */   SubstituteLogger logger;
/*     */   Queue<SubstituteLoggingEvent> eventQueue;
/*     */   
/*     */   public EventRecodingLogger(SubstituteLogger paramSubstituteLogger, Queue<SubstituteLoggingEvent> paramQueue) {
/*  16 */     this.logger = paramSubstituteLogger;
/*  17 */     this.name = paramSubstituteLogger.getName();
/*  18 */     this.eventQueue = paramQueue;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  22 */     return this.name;
/*     */   }
/*     */   
/*     */   private void recordEvent(Level paramLevel, String paramString, Object[] paramArrayOfObject, Throwable paramThrowable) {
/*  26 */     recordEvent(paramLevel, null, paramString, paramArrayOfObject, paramThrowable);
/*     */   }
/*     */ 
/*     */   
/*     */   private void recordEvent(Level paramLevel, Marker paramMarker, String paramString, Object[] paramArrayOfObject, Throwable paramThrowable) {
/*  31 */     SubstituteLoggingEvent substituteLoggingEvent = new SubstituteLoggingEvent();
/*  32 */     substituteLoggingEvent.setTimeStamp(System.currentTimeMillis());
/*  33 */     substituteLoggingEvent.setLevel(paramLevel);
/*  34 */     substituteLoggingEvent.setLogger(this.logger);
/*  35 */     substituteLoggingEvent.setLoggerName(this.name);
/*  36 */     substituteLoggingEvent.setMarker(paramMarker);
/*  37 */     substituteLoggingEvent.setMessage(paramString);
/*  38 */     substituteLoggingEvent.setArgumentArray(paramArrayOfObject);
/*  39 */     substituteLoggingEvent.setThrowable(paramThrowable);
/*  40 */     substituteLoggingEvent.setThreadName(Thread.currentThread().getName());
/*  41 */     this.eventQueue.add(substituteLoggingEvent);
/*     */   }
/*     */   
/*     */   public boolean isTraceEnabled() {
/*  45 */     return true;
/*     */   }
/*     */   
/*     */   public void trace(String paramString) {
/*  49 */     recordEvent(Level.TRACE, paramString, null, null);
/*     */   }
/*     */   
/*     */   public void trace(String paramString, Object paramObject) {
/*  53 */     recordEvent(Level.TRACE, paramString, new Object[] { paramObject }, null);
/*     */   }
/*     */   
/*     */   public void trace(String paramString, Object paramObject1, Object paramObject2) {
/*  57 */     recordEvent(Level.TRACE, paramString, new Object[] { paramObject1, paramObject2 }, null);
/*     */   }
/*     */   
/*     */   public void trace(String paramString, Object... paramVarArgs) {
/*  61 */     recordEvent(Level.TRACE, paramString, paramVarArgs, null);
/*     */   }
/*     */   
/*     */   public void trace(String paramString, Throwable paramThrowable) {
/*  65 */     recordEvent(Level.TRACE, paramString, null, paramThrowable);
/*     */   }
/*     */   
/*     */   public boolean isTraceEnabled(Marker paramMarker) {
/*  69 */     return true;
/*     */   }
/*     */   
/*     */   public void trace(Marker paramMarker, String paramString) {
/*  73 */     recordEvent(Level.TRACE, paramMarker, paramString, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(Marker paramMarker, String paramString, Object paramObject) {
/*  78 */     recordEvent(Level.TRACE, paramMarker, paramString, new Object[] { paramObject }, null);
/*     */   }
/*     */   
/*     */   public void trace(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2) {
/*  82 */     recordEvent(Level.TRACE, paramMarker, paramString, new Object[] { paramObject1, paramObject2 }, null);
/*     */   }
/*     */   
/*     */   public void trace(Marker paramMarker, String paramString, Object... paramVarArgs) {
/*  86 */     recordEvent(Level.TRACE, paramMarker, paramString, paramVarArgs, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(Marker paramMarker, String paramString, Throwable paramThrowable) {
/*  91 */     recordEvent(Level.TRACE, paramMarker, paramString, null, paramThrowable);
/*     */   }
/*     */   
/*     */   public boolean isDebugEnabled() {
/*  95 */     return true;
/*     */   }
/*     */   
/*     */   public void debug(String paramString) {
/*  99 */     recordEvent(Level.TRACE, paramString, null, null);
/*     */   }
/*     */   
/*     */   public void debug(String paramString, Object paramObject) {
/* 103 */     recordEvent(Level.DEBUG, paramString, new Object[] { paramObject }, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String paramString, Object paramObject1, Object paramObject2) {
/* 108 */     recordEvent(Level.DEBUG, paramString, new Object[] { paramObject1, paramObject2 }, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String paramString, Object... paramVarArgs) {
/* 113 */     recordEvent(Level.DEBUG, paramString, paramVarArgs, null);
/*     */   }
/*     */   
/*     */   public void debug(String paramString, Throwable paramThrowable) {
/* 117 */     recordEvent(Level.DEBUG, paramString, null, paramThrowable);
/*     */   }
/*     */   
/*     */   public boolean isDebugEnabled(Marker paramMarker) {
/* 121 */     return true;
/*     */   }
/*     */   
/*     */   public void debug(Marker paramMarker, String paramString) {
/* 125 */     recordEvent(Level.DEBUG, paramMarker, paramString, null, null);
/*     */   }
/*     */   
/*     */   public void debug(Marker paramMarker, String paramString, Object paramObject) {
/* 129 */     recordEvent(Level.DEBUG, paramMarker, paramString, new Object[] { paramObject }, null);
/*     */   }
/*     */   
/*     */   public void debug(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2) {
/* 133 */     recordEvent(Level.DEBUG, paramMarker, paramString, new Object[] { paramObject1, paramObject2 }, null);
/*     */   }
/*     */   
/*     */   public void debug(Marker paramMarker, String paramString, Object... paramVarArgs) {
/* 137 */     recordEvent(Level.DEBUG, paramMarker, paramString, paramVarArgs, null);
/*     */   }
/*     */   
/*     */   public void debug(Marker paramMarker, String paramString, Throwable paramThrowable) {
/* 141 */     recordEvent(Level.DEBUG, paramMarker, paramString, null, paramThrowable);
/*     */   }
/*     */   
/*     */   public boolean isInfoEnabled() {
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   public void info(String paramString) {
/* 149 */     recordEvent(Level.INFO, paramString, null, null);
/*     */   }
/*     */   
/*     */   public void info(String paramString, Object paramObject) {
/* 153 */     recordEvent(Level.INFO, paramString, new Object[] { paramObject }, null);
/*     */   }
/*     */   
/*     */   public void info(String paramString, Object paramObject1, Object paramObject2) {
/* 157 */     recordEvent(Level.INFO, paramString, new Object[] { paramObject1, paramObject2 }, null);
/*     */   }
/*     */   
/*     */   public void info(String paramString, Object... paramVarArgs) {
/* 161 */     recordEvent(Level.INFO, paramString, paramVarArgs, null);
/*     */   }
/*     */   
/*     */   public void info(String paramString, Throwable paramThrowable) {
/* 165 */     recordEvent(Level.INFO, paramString, null, paramThrowable);
/*     */   }
/*     */   
/*     */   public boolean isInfoEnabled(Marker paramMarker) {
/* 169 */     return true;
/*     */   }
/*     */   
/*     */   public void info(Marker paramMarker, String paramString) {
/* 173 */     recordEvent(Level.INFO, paramMarker, paramString, null, null);
/*     */   }
/*     */   
/*     */   public void info(Marker paramMarker, String paramString, Object paramObject) {
/* 177 */     recordEvent(Level.INFO, paramMarker, paramString, new Object[] { paramObject }, null);
/*     */   }
/*     */   
/*     */   public void info(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2) {
/* 181 */     recordEvent(Level.INFO, paramMarker, paramString, new Object[] { paramObject1, paramObject2 }, null);
/*     */   }
/*     */   
/*     */   public void info(Marker paramMarker, String paramString, Object... paramVarArgs) {
/* 185 */     recordEvent(Level.INFO, paramMarker, paramString, paramVarArgs, null);
/*     */   }
/*     */   
/*     */   public void info(Marker paramMarker, String paramString, Throwable paramThrowable) {
/* 189 */     recordEvent(Level.INFO, paramMarker, paramString, null, paramThrowable);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWarnEnabled() {
/* 194 */     return true;
/*     */   }
/*     */   
/*     */   public void warn(String paramString) {
/* 198 */     recordEvent(Level.WARN, paramString, null, null);
/*     */   }
/*     */   
/*     */   public void warn(String paramString, Object paramObject) {
/* 202 */     recordEvent(Level.WARN, paramString, new Object[] { paramObject }, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(String paramString, Object paramObject1, Object paramObject2) {
/* 207 */     recordEvent(Level.WARN, paramString, new Object[] { paramObject1, paramObject2 }, null);
/*     */   }
/*     */   
/*     */   public void warn(String paramString, Object... paramVarArgs) {
/* 211 */     recordEvent(Level.WARN, paramString, paramVarArgs, null);
/*     */   }
/*     */   
/*     */   public void warn(String paramString, Throwable paramThrowable) {
/* 215 */     recordEvent(Level.WARN, paramString, null, paramThrowable);
/*     */   }
/*     */   
/*     */   public boolean isWarnEnabled(Marker paramMarker) {
/* 219 */     return true;
/*     */   }
/*     */   
/*     */   public void warn(Marker paramMarker, String paramString) {
/* 223 */     recordEvent(Level.WARN, paramString, null, null);
/*     */   }
/*     */   
/*     */   public void warn(Marker paramMarker, String paramString, Object paramObject) {
/* 227 */     recordEvent(Level.WARN, paramString, new Object[] { paramObject }, null);
/*     */   }
/*     */   
/*     */   public void warn(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2) {
/* 231 */     recordEvent(Level.WARN, paramMarker, paramString, new Object[] { paramObject1, paramObject2 }, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(Marker paramMarker, String paramString, Object... paramVarArgs) {
/* 236 */     recordEvent(Level.WARN, paramMarker, paramString, paramVarArgs, null);
/*     */   }
/*     */   
/*     */   public void warn(Marker paramMarker, String paramString, Throwable paramThrowable) {
/* 240 */     recordEvent(Level.WARN, paramMarker, paramString, null, paramThrowable);
/*     */   }
/*     */   
/*     */   public boolean isErrorEnabled() {
/* 244 */     return true;
/*     */   }
/*     */   
/*     */   public void error(String paramString) {
/* 248 */     recordEvent(Level.ERROR, paramString, null, null);
/*     */   }
/*     */   
/*     */   public void error(String paramString, Object paramObject) {
/* 252 */     recordEvent(Level.ERROR, paramString, new Object[] { paramObject }, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String paramString, Object paramObject1, Object paramObject2) {
/* 257 */     recordEvent(Level.ERROR, paramString, new Object[] { paramObject1, paramObject2 }, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String paramString, Object... paramVarArgs) {
/* 262 */     recordEvent(Level.ERROR, paramString, paramVarArgs, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String paramString, Throwable paramThrowable) {
/* 267 */     recordEvent(Level.ERROR, paramString, null, paramThrowable);
/*     */   }
/*     */   
/*     */   public boolean isErrorEnabled(Marker paramMarker) {
/* 271 */     return true;
/*     */   }
/*     */   
/*     */   public void error(Marker paramMarker, String paramString) {
/* 275 */     recordEvent(Level.ERROR, paramMarker, paramString, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(Marker paramMarker, String paramString, Object paramObject) {
/* 280 */     recordEvent(Level.ERROR, paramMarker, paramString, new Object[] { paramObject }, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2) {
/* 285 */     recordEvent(Level.ERROR, paramMarker, paramString, new Object[] { paramObject1, paramObject2 }, null);
/*     */   }
/*     */   
/*     */   public void error(Marker paramMarker, String paramString, Object... paramVarArgs) {
/* 289 */     recordEvent(Level.ERROR, paramMarker, paramString, paramVarArgs, null);
/*     */   }
/*     */   
/*     */   public void error(Marker paramMarker, String paramString, Throwable paramThrowable) {
/* 293 */     recordEvent(Level.ERROR, paramMarker, paramString, null, paramThrowable);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\org\slf4j\event\EventRecodingLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
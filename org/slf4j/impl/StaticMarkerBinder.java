/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import org.slf4j.IMarkerFactory;
/*    */ import org.slf4j.helpers.BasicMarkerFactory;
/*    */ import org.slf4j.spi.MarkerFactoryBinder;
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
/*    */ public class StaticMarkerBinder
/*    */   implements MarkerFactoryBinder
/*    */ {
/* 44 */   public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();
/*    */   
/* 46 */   final IMarkerFactory markerFactory = (IMarkerFactory)new BasicMarkerFactory();
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
/*    */   public static StaticMarkerBinder getSingleton() {
/* 58 */     return SINGLETON;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IMarkerFactory getMarkerFactory() {
/* 66 */     return this.markerFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMarkerFactoryClassStr() {
/* 74 */     return BasicMarkerFactory.class.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\org\slf4j\impl\StaticMarkerBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
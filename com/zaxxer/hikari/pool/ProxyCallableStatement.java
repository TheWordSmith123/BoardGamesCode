/*    */ package com.zaxxer.hikari.pool;
/*    */ 
/*    */ import java.sql.CallableStatement;
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
/*    */ public abstract class ProxyCallableStatement
/*    */   extends ProxyPreparedStatement
/*    */   implements CallableStatement
/*    */ {
/*    */   protected ProxyCallableStatement(ProxyConnection paramProxyConnection, CallableStatement paramCallableStatement) {
/* 30 */     super(paramProxyConnection, paramCallableStatement);
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\pool\ProxyCallableStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
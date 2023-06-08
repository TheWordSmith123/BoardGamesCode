/*     */ package com.zaxxer.hikari.pool;
/*     */ 
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
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
/*     */ public abstract class ProxyResultSet
/*     */   implements ResultSet
/*     */ {
/*     */   protected final ProxyConnection connection;
/*     */   protected final ProxyStatement statement;
/*     */   final ResultSet delegate;
/*     */   
/*     */   protected ProxyResultSet(ProxyConnection paramProxyConnection, ProxyStatement paramProxyStatement, ResultSet paramResultSet) {
/*  36 */     this.connection = paramProxyConnection;
/*  37 */     this.statement = paramProxyStatement;
/*  38 */     this.delegate = paramResultSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final SQLException checkException(SQLException paramSQLException) {
/*  44 */     return this.connection.checkException(paramSQLException);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  51 */     return getClass().getSimpleName() + '@' + System.identityHashCode(this) + " wrapping " + this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Statement getStatement() {
/*  62 */     return this.statement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateRow() {
/*  69 */     this.connection.markCommitStateDirty();
/*  70 */     this.delegate.updateRow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertRow() {
/*  77 */     this.connection.markCommitStateDirty();
/*  78 */     this.delegate.insertRow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteRow() {
/*  85 */     this.connection.markCommitStateDirty();
/*  86 */     this.delegate.deleteRow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T unwrap(Class<T> paramClass) {
/*  94 */     if (paramClass.isInstance(this.delegate)) {
/*  95 */       return (T)this.delegate;
/*     */     }
/*  97 */     if (this.delegate != null) {
/*  98 */       return this.delegate.unwrap(paramClass);
/*     */     }
/*     */     
/* 101 */     throw new SQLException("Wrapped ResultSet is not an instance of " + paramClass);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\pool\ProxyResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
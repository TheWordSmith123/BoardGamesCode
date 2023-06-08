/*     */ package com.zaxxer.hikari.pool;
/*     */ 
/*     */ import java.sql.Connection;
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
/*     */ 
/*     */ public abstract class ProxyStatement
/*     */   implements Statement
/*     */ {
/*     */   protected final ProxyConnection connection;
/*     */   final Statement delegate;
/*     */   private boolean isClosed;
/*     */   private ResultSet proxyResultSet;
/*     */   
/*     */   ProxyStatement(ProxyConnection paramProxyConnection, Statement paramStatement) {
/*  39 */     this.connection = paramProxyConnection;
/*  40 */     this.delegate = paramStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final SQLException checkException(SQLException paramSQLException) {
/*  46 */     return this.connection.checkException(paramSQLException);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/*  53 */     String str = this.delegate.toString();
/*  54 */     return getClass().getSimpleName() + '@' + System.identityHashCode(this) + " wrapping " + str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void close() {
/*  65 */     synchronized (this) {
/*  66 */       if (this.isClosed) {
/*     */         return;
/*     */       }
/*     */       
/*  70 */       this.isClosed = true;
/*     */     } 
/*     */     
/*  73 */     this.connection.untrackStatement(this.delegate);
/*     */     
/*     */     try {
/*  76 */       this.delegate.close();
/*     */     }
/*  78 */     catch (SQLException sQLException) {
/*  79 */       throw this.connection.checkException(sQLException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() {
/*  87 */     return this.connection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute(String paramString) {
/*  94 */     this.connection.markCommitStateDirty();
/*  95 */     return this.delegate.execute(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute(String paramString, int paramInt) {
/* 102 */     this.connection.markCommitStateDirty();
/* 103 */     return this.delegate.execute(paramString, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSet executeQuery(String paramString) {
/* 110 */     this.connection.markCommitStateDirty();
/* 111 */     ResultSet resultSet = this.delegate.executeQuery(paramString);
/* 112 */     return ProxyFactory.getProxyResultSet(this.connection, this, resultSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeUpdate(String paramString) {
/* 119 */     this.connection.markCommitStateDirty();
/* 120 */     return this.delegate.executeUpdate(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] executeBatch() {
/* 127 */     this.connection.markCommitStateDirty();
/* 128 */     return this.delegate.executeBatch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeUpdate(String paramString, int paramInt) {
/* 135 */     this.connection.markCommitStateDirty();
/* 136 */     return this.delegate.executeUpdate(paramString, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeUpdate(String paramString, int[] paramArrayOfint) {
/* 143 */     this.connection.markCommitStateDirty();
/* 144 */     return this.delegate.executeUpdate(paramString, paramArrayOfint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeUpdate(String paramString, String[] paramArrayOfString) {
/* 151 */     this.connection.markCommitStateDirty();
/* 152 */     return this.delegate.executeUpdate(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute(String paramString, int[] paramArrayOfint) {
/* 159 */     this.connection.markCommitStateDirty();
/* 160 */     return this.delegate.execute(paramString, paramArrayOfint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute(String paramString, String[] paramArrayOfString) {
/* 167 */     this.connection.markCommitStateDirty();
/* 168 */     return this.delegate.execute(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long[] executeLargeBatch() {
/* 175 */     this.connection.markCommitStateDirty();
/* 176 */     return this.delegate.executeLargeBatch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long executeLargeUpdate(String paramString) {
/* 183 */     this.connection.markCommitStateDirty();
/* 184 */     return this.delegate.executeLargeUpdate(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long executeLargeUpdate(String paramString, int paramInt) {
/* 191 */     this.connection.markCommitStateDirty();
/* 192 */     return this.delegate.executeLargeUpdate(paramString, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long executeLargeUpdate(String paramString, int[] paramArrayOfint) {
/* 199 */     this.connection.markCommitStateDirty();
/* 200 */     return this.delegate.executeLargeUpdate(paramString, paramArrayOfint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long executeLargeUpdate(String paramString, String[] paramArrayOfString) {
/* 207 */     this.connection.markCommitStateDirty();
/* 208 */     return this.delegate.executeLargeUpdate(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSet getResultSet() {
/* 214 */     ResultSet resultSet = this.delegate.getResultSet();
/* 215 */     if (resultSet != null) {
/* 216 */       if (this.proxyResultSet == null || ((ProxyResultSet)this.proxyResultSet).delegate != resultSet) {
/* 217 */         this.proxyResultSet = ProxyFactory.getProxyResultSet(this.connection, this, resultSet);
/*     */       }
/*     */     } else {
/*     */       
/* 221 */       this.proxyResultSet = null;
/*     */     } 
/* 223 */     return this.proxyResultSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSet getGeneratedKeys() {
/* 230 */     ResultSet resultSet = this.delegate.getGeneratedKeys();
/* 231 */     if (this.proxyResultSet == null || ((ProxyResultSet)this.proxyResultSet).delegate != resultSet) {
/* 232 */       this.proxyResultSet = ProxyFactory.getProxyResultSet(this.connection, this, resultSet);
/*     */     }
/* 234 */     return this.proxyResultSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T unwrap(Class<T> paramClass) {
/* 242 */     if (paramClass.isInstance(this.delegate)) {
/* 243 */       return (T)this.delegate;
/*     */     }
/* 245 */     if (this.delegate != null) {
/* 246 */       return this.delegate.unwrap(paramClass);
/*     */     }
/*     */     
/* 249 */     throw new SQLException("Wrapped statement is not an instance of " + paramClass);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\pool\ProxyStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
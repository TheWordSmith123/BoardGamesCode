/*     */ package com.zaxxer.hikari.pool;
/*     */ 
/*     */ import com.zaxxer.hikari.util.ClockSource;
/*     */ import com.zaxxer.hikari.util.FastList;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Savepoint;
/*     */ import java.sql.Statement;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
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
/*     */ public abstract class ProxyConnection
/*     */   implements Connection
/*     */ {
/*     */   static final int DIRTY_BIT_READONLY = 1;
/*     */   static final int DIRTY_BIT_AUTOCOMMIT = 2;
/*     */   static final int DIRTY_BIT_ISOLATION = 4;
/*     */   static final int DIRTY_BIT_CATALOG = 8;
/*     */   static final int DIRTY_BIT_NETTIMEOUT = 16;
/*     */   static final int DIRTY_BIT_SCHEMA = 32;
/*  70 */   private static final Logger LOGGER = LoggerFactory.getLogger(ProxyConnection.class);
/*     */   
/*  72 */   private static final Set<String> ERROR_STATES = new HashSet<>(); static {
/*  73 */     ERROR_STATES.add("0A000");
/*  74 */     ERROR_STATES.add("57P01");
/*  75 */     ERROR_STATES.add("57P02");
/*  76 */     ERROR_STATES.add("57P03");
/*  77 */     ERROR_STATES.add("01002");
/*  78 */     ERROR_STATES.add("JZ0C0");
/*  79 */     ERROR_STATES.add("JZ0C1");
/*     */   }
/*  81 */   private static final Set<Integer> ERROR_CODES = new HashSet<>(); protected Connection delegate; private final PoolEntry poolEntry; private final ProxyLeakTask leakTask; private final FastList<Statement> openStatements; private int dirtyBits; private long lastAccess; static {
/*  82 */     ERROR_CODES.add(Integer.valueOf(500150));
/*  83 */     ERROR_CODES.add(Integer.valueOf(2399));
/*     */   }
/*     */   private boolean isCommitStateDirty; private boolean isReadOnly; private boolean isAutoCommit; private int networkTimeout; private int transactionIsolation; private String dbcatalog; private String dbschema;
/*     */   protected ProxyConnection(PoolEntry paramPoolEntry, Connection paramConnection, FastList<Statement> paramFastList, ProxyLeakTask paramProxyLeakTask, long paramLong, boolean paramBoolean1, boolean paramBoolean2) {
/*  87 */     this.poolEntry = paramPoolEntry;
/*  88 */     this.delegate = paramConnection;
/*  89 */     this.openStatements = paramFastList;
/*  90 */     this.leakTask = paramProxyLeakTask;
/*  91 */     this.lastAccess = paramLong;
/*  92 */     this.isReadOnly = paramBoolean1;
/*  93 */     this.isAutoCommit = paramBoolean2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 100 */     return getClass().getSimpleName() + '@' + System.identityHashCode(this) + " wrapping " + this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean getAutoCommitState() {
/* 109 */     return this.isAutoCommit;
/*     */   }
/*     */ 
/*     */   
/*     */   final String getCatalogState() {
/* 114 */     return this.dbcatalog;
/*     */   }
/*     */ 
/*     */   
/*     */   final String getSchemaState() {
/* 119 */     return this.dbschema;
/*     */   }
/*     */ 
/*     */   
/*     */   final int getTransactionIsolationState() {
/* 124 */     return this.transactionIsolation;
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean getReadOnlyState() {
/* 129 */     return this.isReadOnly;
/*     */   }
/*     */ 
/*     */   
/*     */   final int getNetworkTimeoutState() {
/* 134 */     return this.networkTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final PoolEntry getPoolEntry() {
/* 143 */     return this.poolEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   final SQLException checkException(SQLException paramSQLException) {
/* 148 */     SQLException sQLException = paramSQLException;
/* 149 */     for (byte b = 0; this.delegate != ClosedConnection.CLOSED_CONNECTION && sQLException != null && b < 10; b++) {
/* 150 */       String str = sQLException.getSQLState();
/* 151 */       if ((str != null && str.startsWith("08")) || sQLException instanceof java.sql.SQLTimeoutException || ERROR_STATES
/*     */         
/* 153 */         .contains(str) || ERROR_CODES
/* 154 */         .contains(Integer.valueOf(sQLException.getErrorCode()))) {
/*     */ 
/*     */         
/* 157 */         LOGGER.warn("{} - Connection {} marked as broken because of SQLSTATE({}), ErrorCode({})", new Object[] { this.poolEntry
/* 158 */               .getPoolName(), this.delegate, str, Integer.valueOf(sQLException.getErrorCode()), sQLException });
/* 159 */         this.leakTask.cancel();
/* 160 */         this.poolEntry.evict("(connection is broken)");
/* 161 */         this.delegate = ClosedConnection.CLOSED_CONNECTION;
/*     */       } else {
/*     */         
/* 164 */         sQLException = sQLException.getNextException();
/*     */       } 
/*     */     } 
/*     */     
/* 168 */     return paramSQLException;
/*     */   }
/*     */ 
/*     */   
/*     */   final synchronized void untrackStatement(Statement paramStatement) {
/* 173 */     this.openStatements.remove(paramStatement);
/*     */   }
/*     */ 
/*     */   
/*     */   final void markCommitStateDirty() {
/* 178 */     if (this.isAutoCommit) {
/* 179 */       this.lastAccess = ClockSource.currentTime();
/*     */     } else {
/*     */       
/* 182 */       this.isCommitStateDirty = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void cancelLeakTask() {
/* 188 */     this.leakTask.cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized <T extends Statement> T trackStatement(T paramT) {
/* 193 */     this.openStatements.add(paramT);
/*     */     
/* 195 */     return paramT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void closeStatements() {
/* 201 */     int i = this.openStatements.size();
/* 202 */     if (i > 0) {
/* 203 */       for (byte b = 0; b < i && this.delegate != ClosedConnection.CLOSED_CONNECTION; b++) { try {
/* 204 */           Statement statement = (Statement)this.openStatements.get(b); Throwable throwable = null;
/*     */           
/* 206 */           if (statement != null) if (throwable != null) { try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  } else { statement.close(); }  
/* 207 */         } catch (SQLException sQLException) {
/* 208 */           LOGGER.warn("{} - Connection {} marked as broken because of an exception closing open statements during Connection.close()", this.poolEntry
/* 209 */               .getPoolName(), this.delegate);
/* 210 */           this.leakTask.cancel();
/* 211 */           this.poolEntry.evict("(exception closing Statements during Connection.close())");
/* 212 */           this.delegate = ClosedConnection.CLOSED_CONNECTION;
/*     */         }  }
/*     */ 
/*     */       
/* 216 */       this.openStatements.clear();
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
/*     */   public final void close() {
/* 229 */     closeStatements();
/*     */     
/* 231 */     if (this.delegate != ClosedConnection.CLOSED_CONNECTION) {
/* 232 */       this.leakTask.cancel();
/*     */       
/*     */       try {
/* 235 */         if (this.isCommitStateDirty && !this.isAutoCommit) {
/* 236 */           this.delegate.rollback();
/* 237 */           this.lastAccess = ClockSource.currentTime();
/* 238 */           LOGGER.debug("{} - Executed rollback on connection {} due to dirty commit state on close().", this.poolEntry.getPoolName(), this.delegate);
/*     */         } 
/*     */         
/* 241 */         if (this.dirtyBits != 0) {
/* 242 */           this.poolEntry.resetConnectionState(this, this.dirtyBits);
/* 243 */           this.lastAccess = ClockSource.currentTime();
/*     */         } 
/*     */         
/* 246 */         this.delegate.clearWarnings();
/*     */       }
/* 248 */       catch (SQLException sQLException) {
/*     */         
/* 250 */         if (!this.poolEntry.isMarkedEvicted()) {
/* 251 */           throw checkException(sQLException);
/*     */         }
/*     */       } finally {
/*     */         
/* 255 */         this.delegate = ClosedConnection.CLOSED_CONNECTION;
/* 256 */         this.poolEntry.recycle(this.lastAccess);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 266 */     return (this.delegate == ClosedConnection.CLOSED_CONNECTION);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement createStatement() {
/* 273 */     return ProxyFactory.getProxyStatement(this, trackStatement(this.delegate.createStatement()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement createStatement(int paramInt1, int paramInt2) {
/* 280 */     return ProxyFactory.getProxyStatement(this, trackStatement(this.delegate.createStatement(paramInt1, paramInt2)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement createStatement(int paramInt1, int paramInt2, int paramInt3) {
/* 287 */     return ProxyFactory.getProxyStatement(this, trackStatement(this.delegate.createStatement(paramInt1, paramInt2, paramInt3)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String paramString) {
/* 295 */     return ProxyFactory.getProxyCallableStatement(this, trackStatement(this.delegate.prepareCall(paramString)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2) {
/* 302 */     return ProxyFactory.getProxyCallableStatement(this, trackStatement(this.delegate.prepareCall(paramString, paramInt1, paramInt2)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CallableStatement prepareCall(String paramString, int paramInt1, int paramInt2, int paramInt3) {
/* 309 */     return ProxyFactory.getProxyCallableStatement(this, trackStatement(this.delegate.prepareCall(paramString, paramInt1, paramInt2, paramInt3)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String paramString) {
/* 316 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(paramString)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String paramString, int paramInt) {
/* 323 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(paramString, paramInt)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2) {
/* 330 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(paramString, paramInt1, paramInt2)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String paramString, int paramInt1, int paramInt2, int paramInt3) {
/* 337 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(paramString, paramInt1, paramInt2, paramInt3)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String paramString, int[] paramArrayOfint) {
/* 344 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(paramString, paramArrayOfint)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PreparedStatement prepareStatement(String paramString, String[] paramArrayOfString) {
/* 351 */     return ProxyFactory.getProxyPreparedStatement(this, trackStatement(this.delegate.prepareStatement(paramString, paramArrayOfString)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatabaseMetaData getMetaData() {
/* 358 */     markCommitStateDirty();
/* 359 */     return ProxyFactory.getProxyDatabaseMetaData(this, this.delegate.getMetaData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void commit() {
/* 366 */     this.delegate.commit();
/* 367 */     this.isCommitStateDirty = false;
/* 368 */     this.lastAccess = ClockSource.currentTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback() {
/* 375 */     this.delegate.rollback();
/* 376 */     this.isCommitStateDirty = false;
/* 377 */     this.lastAccess = ClockSource.currentTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rollback(Savepoint paramSavepoint) {
/* 384 */     this.delegate.rollback(paramSavepoint);
/* 385 */     this.isCommitStateDirty = false;
/* 386 */     this.lastAccess = ClockSource.currentTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoCommit(boolean paramBoolean) {
/* 393 */     this.delegate.setAutoCommit(paramBoolean);
/* 394 */     this.isAutoCommit = paramBoolean;
/* 395 */     this.dirtyBits |= 0x2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadOnly(boolean paramBoolean) {
/* 402 */     this.delegate.setReadOnly(paramBoolean);
/* 403 */     this.isReadOnly = paramBoolean;
/* 404 */     this.isCommitStateDirty = false;
/* 405 */     this.dirtyBits |= 0x1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransactionIsolation(int paramInt) {
/* 412 */     this.delegate.setTransactionIsolation(paramInt);
/* 413 */     this.transactionIsolation = paramInt;
/* 414 */     this.dirtyBits |= 0x4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatalog(String paramString) {
/* 421 */     this.delegate.setCatalog(paramString);
/* 422 */     this.dbcatalog = paramString;
/* 423 */     this.dirtyBits |= 0x8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNetworkTimeout(Executor paramExecutor, int paramInt) {
/* 430 */     this.delegate.setNetworkTimeout(paramExecutor, paramInt);
/* 431 */     this.networkTimeout = paramInt;
/* 432 */     this.dirtyBits |= 0x10;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSchema(String paramString) {
/* 439 */     this.delegate.setSchema(paramString);
/* 440 */     this.dbschema = paramString;
/* 441 */     this.dirtyBits |= 0x20;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isWrapperFor(Class<?> paramClass) {
/* 448 */     return (paramClass.isInstance(this.delegate) || (this.delegate != null && this.delegate.isWrapperFor(paramClass)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T unwrap(Class<T> paramClass) {
/* 456 */     if (paramClass.isInstance(this.delegate)) {
/* 457 */       return (T)this.delegate;
/*     */     }
/* 459 */     if (this.delegate != null) {
/* 460 */       return this.delegate.unwrap(paramClass);
/*     */     }
/*     */     
/* 463 */     throw new SQLException("Wrapped connection is not an instance of " + paramClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ClosedConnection
/*     */   {
/* 472 */     static final Connection CLOSED_CONNECTION = getClosedConnection();
/*     */ 
/*     */     
/*     */     private static Connection getClosedConnection() {
/* 476 */       InvocationHandler invocationHandler = (param1Object, param1Method, param1ArrayOfObject) -> {
/*     */           String str = param1Method.getName();
/*     */           
/*     */           if ("isClosed".equals(str)) {
/*     */             return Boolean.TRUE;
/*     */           }
/*     */           
/*     */           if ("isValid".equals(str)) {
/*     */             return Boolean.FALSE;
/*     */           }
/*     */           if ("abort".equals(str)) {
/*     */             return void.class;
/*     */           }
/*     */           if ("close".equals(str)) {
/*     */             return void.class;
/*     */           }
/*     */           if ("toString".equals(str)) {
/*     */             return ClosedConnection.class.getCanonicalName();
/*     */           }
/*     */           throw new SQLException("Connection is closed");
/*     */         };
/* 497 */       return (Connection)Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, invocationHandler);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\pool\ProxyConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
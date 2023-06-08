/*     */ package com.zaxxer.hikari.pool;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ProxyDatabaseMetaData
/*     */   implements DatabaseMetaData
/*     */ {
/*     */   protected final ProxyConnection connection;
/*     */   protected final DatabaseMetaData delegate;
/*     */   
/*     */   ProxyDatabaseMetaData(ProxyConnection paramProxyConnection, DatabaseMetaData paramDatabaseMetaData) {
/*  17 */     this.connection = paramProxyConnection;
/*  18 */     this.delegate = paramDatabaseMetaData;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final SQLException checkException(SQLException paramSQLException) {
/*  24 */     return this.connection.checkException(paramSQLException);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/*  31 */     String str = this.delegate.toString();
/*  32 */     return getClass().getSimpleName() + '@' + System.identityHashCode(this) + " wrapping " + str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Connection getConnection() {
/*  43 */     return this.connection;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProcedures(String paramString1, String paramString2, String paramString3) {
/*  48 */     ResultSet resultSet = this.delegate.getProcedures(paramString1, paramString2, paramString3);
/*  49 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/*  50 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProcedureColumns(String paramString1, String paramString2, String paramString3, String paramString4) {
/*  55 */     ResultSet resultSet = this.delegate.getProcedureColumns(paramString1, paramString2, paramString3, paramString4);
/*  56 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/*  57 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getTables(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
/*  62 */     ResultSet resultSet = this.delegate.getTables(paramString1, paramString2, paramString3, paramArrayOfString);
/*  63 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/*  64 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getSchemas() {
/*  69 */     ResultSet resultSet = this.delegate.getSchemas();
/*  70 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/*  71 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getCatalogs() {
/*  76 */     ResultSet resultSet = this.delegate.getCatalogs();
/*  77 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/*  78 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getTableTypes() {
/*  83 */     ResultSet resultSet = this.delegate.getTableTypes();
/*  84 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/*  85 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getColumns(String paramString1, String paramString2, String paramString3, String paramString4) {
/*  90 */     ResultSet resultSet = this.delegate.getColumns(paramString1, paramString2, paramString3, paramString4);
/*  91 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/*  92 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getColumnPrivileges(String paramString1, String paramString2, String paramString3, String paramString4) {
/*  97 */     ResultSet resultSet = this.delegate.getColumnPrivileges(paramString1, paramString2, paramString3, paramString4);
/*  98 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/*  99 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getTablePrivileges(String paramString1, String paramString2, String paramString3) {
/* 104 */     ResultSet resultSet = this.delegate.getTablePrivileges(paramString1, paramString2, paramString3);
/* 105 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 106 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getBestRowIdentifier(String paramString1, String paramString2, String paramString3, int paramInt, boolean paramBoolean) {
/* 111 */     ResultSet resultSet = this.delegate.getBestRowIdentifier(paramString1, paramString2, paramString3, paramInt, paramBoolean);
/* 112 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 113 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getVersionColumns(String paramString1, String paramString2, String paramString3) {
/* 118 */     ResultSet resultSet = this.delegate.getVersionColumns(paramString1, paramString2, paramString3);
/* 119 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 120 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getPrimaryKeys(String paramString1, String paramString2, String paramString3) {
/* 125 */     ResultSet resultSet = this.delegate.getPrimaryKeys(paramString1, paramString2, paramString3);
/* 126 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 127 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getImportedKeys(String paramString1, String paramString2, String paramString3) {
/* 132 */     ResultSet resultSet = this.delegate.getImportedKeys(paramString1, paramString2, paramString3);
/* 133 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 134 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getExportedKeys(String paramString1, String paramString2, String paramString3) {
/* 139 */     ResultSet resultSet = this.delegate.getExportedKeys(paramString1, paramString2, paramString3);
/* 140 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 141 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getCrossReference(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) {
/* 146 */     ResultSet resultSet = this.delegate.getCrossReference(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6);
/* 147 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 148 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getTypeInfo() {
/* 153 */     ResultSet resultSet = this.delegate.getTypeInfo();
/* 154 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 155 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getIndexInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2) {
/* 160 */     ResultSet resultSet = this.delegate.getIndexInfo(paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2);
/* 161 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 162 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getUDTs(String paramString1, String paramString2, String paramString3, int[] paramArrayOfint) {
/* 167 */     ResultSet resultSet = this.delegate.getUDTs(paramString1, paramString2, paramString3, paramArrayOfint);
/* 168 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 169 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getSuperTypes(String paramString1, String paramString2, String paramString3) {
/* 174 */     ResultSet resultSet = this.delegate.getSuperTypes(paramString1, paramString2, paramString3);
/* 175 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 176 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getSuperTables(String paramString1, String paramString2, String paramString3) {
/* 181 */     ResultSet resultSet = this.delegate.getSuperTables(paramString1, paramString2, paramString3);
/* 182 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 183 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getAttributes(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 188 */     ResultSet resultSet = this.delegate.getAttributes(paramString1, paramString2, paramString3, paramString4);
/* 189 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 190 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getSchemas(String paramString1, String paramString2) {
/* 195 */     ResultSet resultSet = this.delegate.getSchemas(paramString1, paramString2);
/* 196 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 197 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getClientInfoProperties() {
/* 202 */     ResultSet resultSet = this.delegate.getClientInfoProperties();
/* 203 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 204 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getFunctions(String paramString1, String paramString2, String paramString3) {
/* 209 */     ResultSet resultSet = this.delegate.getFunctions(paramString1, paramString2, paramString3);
/* 210 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 211 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getFunctionColumns(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 216 */     ResultSet resultSet = this.delegate.getFunctionColumns(paramString1, paramString2, paramString3, paramString4);
/* 217 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 218 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getPseudoColumns(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 223 */     ResultSet resultSet = this.delegate.getPseudoColumns(paramString1, paramString2, paramString3, paramString4);
/* 224 */     ProxyStatement proxyStatement = (ProxyStatement)ProxyFactory.getProxyStatement(this.connection, resultSet.getStatement());
/* 225 */     return ProxyFactory.getProxyResultSet(this.connection, proxyStatement, resultSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T unwrap(Class<T> paramClass) {
/* 233 */     if (paramClass.isInstance(this.delegate)) {
/* 234 */       return (T)this.delegate;
/*     */     }
/* 236 */     if (this.delegate != null) {
/* 237 */       return this.delegate.unwrap(paramClass);
/*     */     }
/*     */     
/* 240 */     throw new SQLException("Wrapped DatabaseMetaData is not an instance of " + paramClass);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\pool\ProxyDatabaseMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
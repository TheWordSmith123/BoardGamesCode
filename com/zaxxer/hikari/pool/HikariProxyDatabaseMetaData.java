package com.zaxxer.hikari.pool;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.Wrapper;

public class HikariProxyDatabaseMetaData extends ProxyDatabaseMetaData implements Wrapper, DatabaseMetaData {
  public boolean isWrapperFor(Class<?> paramClass) {
    try {
      return this.delegate.isWrapperFor(paramClass);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean allProceduresAreCallable() {
    try {
      return this.delegate.allProceduresAreCallable();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean allTablesAreSelectable() {
    try {
      return this.delegate.allTablesAreSelectable();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getURL() {
    try {
      return this.delegate.getURL();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getUserName() {
    try {
      return this.delegate.getUserName();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean isReadOnly() {
    try {
      return this.delegate.isReadOnly();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean nullsAreSortedHigh() {
    try {
      return this.delegate.nullsAreSortedHigh();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean nullsAreSortedLow() {
    try {
      return this.delegate.nullsAreSortedLow();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean nullsAreSortedAtStart() {
    try {
      return this.delegate.nullsAreSortedAtStart();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean nullsAreSortedAtEnd() {
    try {
      return this.delegate.nullsAreSortedAtEnd();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getDatabaseProductName() {
    try {
      return this.delegate.getDatabaseProductName();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getDatabaseProductVersion() {
    try {
      return this.delegate.getDatabaseProductVersion();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getDriverName() {
    try {
      return this.delegate.getDriverName();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getDriverVersion() {
    try {
      return this.delegate.getDriverVersion();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getDriverMajorVersion() {
    return this.delegate.getDriverMajorVersion();
  }
  
  public int getDriverMinorVersion() {
    return this.delegate.getDriverMinorVersion();
  }
  
  public boolean usesLocalFiles() {
    try {
      return this.delegate.usesLocalFiles();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean usesLocalFilePerTable() {
    try {
      return this.delegate.usesLocalFilePerTable();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsMixedCaseIdentifiers() {
    try {
      return this.delegate.supportsMixedCaseIdentifiers();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean storesUpperCaseIdentifiers() {
    try {
      return this.delegate.storesUpperCaseIdentifiers();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean storesLowerCaseIdentifiers() {
    try {
      return this.delegate.storesLowerCaseIdentifiers();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean storesMixedCaseIdentifiers() {
    try {
      return this.delegate.storesMixedCaseIdentifiers();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsMixedCaseQuotedIdentifiers() {
    try {
      return this.delegate.supportsMixedCaseQuotedIdentifiers();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean storesUpperCaseQuotedIdentifiers() {
    try {
      return this.delegate.storesUpperCaseQuotedIdentifiers();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean storesLowerCaseQuotedIdentifiers() {
    try {
      return this.delegate.storesLowerCaseQuotedIdentifiers();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean storesMixedCaseQuotedIdentifiers() {
    try {
      return this.delegate.storesMixedCaseQuotedIdentifiers();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getIdentifierQuoteString() {
    try {
      return this.delegate.getIdentifierQuoteString();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getSQLKeywords() {
    try {
      return this.delegate.getSQLKeywords();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getNumericFunctions() {
    try {
      return this.delegate.getNumericFunctions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getStringFunctions() {
    try {
      return this.delegate.getStringFunctions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getSystemFunctions() {
    try {
      return this.delegate.getSystemFunctions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getTimeDateFunctions() {
    try {
      return this.delegate.getTimeDateFunctions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getSearchStringEscape() {
    try {
      return this.delegate.getSearchStringEscape();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getExtraNameCharacters() {
    try {
      return this.delegate.getExtraNameCharacters();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsAlterTableWithAddColumn() {
    try {
      return this.delegate.supportsAlterTableWithAddColumn();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsAlterTableWithDropColumn() {
    try {
      return this.delegate.supportsAlterTableWithDropColumn();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsColumnAliasing() {
    try {
      return this.delegate.supportsColumnAliasing();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean nullPlusNonNullIsNull() {
    try {
      return this.delegate.nullPlusNonNullIsNull();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsConvert() {
    try {
      return this.delegate.supportsConvert();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsConvert(int paramInt1, int paramInt2) {
    try {
      return this.delegate.supportsConvert(paramInt1, paramInt2);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsTableCorrelationNames() {
    try {
      return this.delegate.supportsTableCorrelationNames();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsDifferentTableCorrelationNames() {
    try {
      return this.delegate.supportsDifferentTableCorrelationNames();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsExpressionsInOrderBy() {
    try {
      return this.delegate.supportsExpressionsInOrderBy();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsOrderByUnrelated() {
    try {
      return this.delegate.supportsOrderByUnrelated();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsGroupBy() {
    try {
      return this.delegate.supportsGroupBy();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsGroupByUnrelated() {
    try {
      return this.delegate.supportsGroupByUnrelated();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsGroupByBeyondSelect() {
    try {
      return this.delegate.supportsGroupByBeyondSelect();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsLikeEscapeClause() {
    try {
      return this.delegate.supportsLikeEscapeClause();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsMultipleResultSets() {
    try {
      return this.delegate.supportsMultipleResultSets();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsMultipleTransactions() {
    try {
      return this.delegate.supportsMultipleTransactions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsNonNullableColumns() {
    try {
      return this.delegate.supportsNonNullableColumns();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsMinimumSQLGrammar() {
    try {
      return this.delegate.supportsMinimumSQLGrammar();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsCoreSQLGrammar() {
    try {
      return this.delegate.supportsCoreSQLGrammar();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsExtendedSQLGrammar() {
    try {
      return this.delegate.supportsExtendedSQLGrammar();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsANSI92EntryLevelSQL() {
    try {
      return this.delegate.supportsANSI92EntryLevelSQL();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsANSI92IntermediateSQL() {
    try {
      return this.delegate.supportsANSI92IntermediateSQL();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsANSI92FullSQL() {
    try {
      return this.delegate.supportsANSI92FullSQL();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsIntegrityEnhancementFacility() {
    try {
      return this.delegate.supportsIntegrityEnhancementFacility();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsOuterJoins() {
    try {
      return this.delegate.supportsOuterJoins();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsFullOuterJoins() {
    try {
      return this.delegate.supportsFullOuterJoins();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsLimitedOuterJoins() {
    try {
      return this.delegate.supportsLimitedOuterJoins();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getSchemaTerm() {
    try {
      return this.delegate.getSchemaTerm();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getProcedureTerm() {
    try {
      return this.delegate.getProcedureTerm();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getCatalogTerm() {
    try {
      return this.delegate.getCatalogTerm();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean isCatalogAtStart() {
    try {
      return this.delegate.isCatalogAtStart();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public String getCatalogSeparator() {
    try {
      return this.delegate.getCatalogSeparator();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsSchemasInDataManipulation() {
    try {
      return this.delegate.supportsSchemasInDataManipulation();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsSchemasInProcedureCalls() {
    try {
      return this.delegate.supportsSchemasInProcedureCalls();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsSchemasInTableDefinitions() {
    try {
      return this.delegate.supportsSchemasInTableDefinitions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsSchemasInIndexDefinitions() {
    try {
      return this.delegate.supportsSchemasInIndexDefinitions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsSchemasInPrivilegeDefinitions() {
    try {
      return this.delegate.supportsSchemasInPrivilegeDefinitions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsCatalogsInDataManipulation() {
    try {
      return this.delegate.supportsCatalogsInDataManipulation();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsCatalogsInProcedureCalls() {
    try {
      return this.delegate.supportsCatalogsInProcedureCalls();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsCatalogsInTableDefinitions() {
    try {
      return this.delegate.supportsCatalogsInTableDefinitions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsCatalogsInIndexDefinitions() {
    try {
      return this.delegate.supportsCatalogsInIndexDefinitions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsCatalogsInPrivilegeDefinitions() {
    try {
      return this.delegate.supportsCatalogsInPrivilegeDefinitions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsPositionedDelete() {
    try {
      return this.delegate.supportsPositionedDelete();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsPositionedUpdate() {
    try {
      return this.delegate.supportsPositionedUpdate();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsSelectForUpdate() {
    try {
      return this.delegate.supportsSelectForUpdate();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsStoredProcedures() {
    try {
      return this.delegate.supportsStoredProcedures();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsSubqueriesInComparisons() {
    try {
      return this.delegate.supportsSubqueriesInComparisons();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsSubqueriesInExists() {
    try {
      return this.delegate.supportsSubqueriesInExists();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsSubqueriesInIns() {
    try {
      return this.delegate.supportsSubqueriesInIns();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsSubqueriesInQuantifieds() {
    try {
      return this.delegate.supportsSubqueriesInQuantifieds();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsCorrelatedSubqueries() {
    try {
      return this.delegate.supportsCorrelatedSubqueries();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsUnion() {
    try {
      return this.delegate.supportsUnion();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsUnionAll() {
    try {
      return this.delegate.supportsUnionAll();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsOpenCursorsAcrossCommit() {
    try {
      return this.delegate.supportsOpenCursorsAcrossCommit();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsOpenCursorsAcrossRollback() {
    try {
      return this.delegate.supportsOpenCursorsAcrossRollback();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsOpenStatementsAcrossCommit() {
    try {
      return this.delegate.supportsOpenStatementsAcrossCommit();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsOpenStatementsAcrossRollback() {
    try {
      return this.delegate.supportsOpenStatementsAcrossRollback();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxBinaryLiteralLength() {
    try {
      return this.delegate.getMaxBinaryLiteralLength();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxCharLiteralLength() {
    try {
      return this.delegate.getMaxCharLiteralLength();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxColumnNameLength() {
    try {
      return this.delegate.getMaxColumnNameLength();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxColumnsInGroupBy() {
    try {
      return this.delegate.getMaxColumnsInGroupBy();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxColumnsInIndex() {
    try {
      return this.delegate.getMaxColumnsInIndex();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxColumnsInOrderBy() {
    try {
      return this.delegate.getMaxColumnsInOrderBy();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxColumnsInSelect() {
    try {
      return this.delegate.getMaxColumnsInSelect();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxColumnsInTable() {
    try {
      return this.delegate.getMaxColumnsInTable();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxConnections() {
    try {
      return this.delegate.getMaxConnections();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxCursorNameLength() {
    try {
      return this.delegate.getMaxCursorNameLength();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxIndexLength() {
    try {
      return this.delegate.getMaxIndexLength();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxSchemaNameLength() {
    try {
      return this.delegate.getMaxSchemaNameLength();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxProcedureNameLength() {
    try {
      return this.delegate.getMaxProcedureNameLength();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxCatalogNameLength() {
    try {
      return this.delegate.getMaxCatalogNameLength();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxRowSize() {
    try {
      return this.delegate.getMaxRowSize();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean doesMaxRowSizeIncludeBlobs() {
    try {
      return this.delegate.doesMaxRowSizeIncludeBlobs();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxStatementLength() {
    try {
      return this.delegate.getMaxStatementLength();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxStatements() {
    try {
      return this.delegate.getMaxStatements();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxTableNameLength() {
    try {
      return this.delegate.getMaxTableNameLength();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxTablesInSelect() {
    try {
      return this.delegate.getMaxTablesInSelect();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getMaxUserNameLength() {
    try {
      return this.delegate.getMaxUserNameLength();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getDefaultTransactionIsolation() {
    try {
      return this.delegate.getDefaultTransactionIsolation();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsTransactions() {
    try {
      return this.delegate.supportsTransactions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsTransactionIsolationLevel(int paramInt) {
    try {
      return this.delegate.supportsTransactionIsolationLevel(paramInt);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsDataDefinitionAndDataManipulationTransactions() {
    try {
      return this.delegate.supportsDataDefinitionAndDataManipulationTransactions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsDataManipulationTransactionsOnly() {
    try {
      return this.delegate.supportsDataManipulationTransactionsOnly();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean dataDefinitionCausesTransactionCommit() {
    try {
      return this.delegate.dataDefinitionCausesTransactionCommit();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean dataDefinitionIgnoredInTransactions() {
    try {
      return this.delegate.dataDefinitionIgnoredInTransactions();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getProcedures(String paramString1, String paramString2, String paramString3) {
    try {
      return super.getProcedures(paramString1, paramString2, paramString3);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getProcedureColumns(String paramString1, String paramString2, String paramString3, String paramString4) {
    try {
      return super.getProcedureColumns(paramString1, paramString2, paramString3, paramString4);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getTables(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) {
    try {
      return super.getTables(paramString1, paramString2, paramString3, paramArrayOfString);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getSchemas() {
    try {
      return super.getSchemas();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getCatalogs() {
    try {
      return super.getCatalogs();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getTableTypes() {
    try {
      return super.getTableTypes();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getColumns(String paramString1, String paramString2, String paramString3, String paramString4) {
    try {
      return super.getColumns(paramString1, paramString2, paramString3, paramString4);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getColumnPrivileges(String paramString1, String paramString2, String paramString3, String paramString4) {
    try {
      return super.getColumnPrivileges(paramString1, paramString2, paramString3, paramString4);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getTablePrivileges(String paramString1, String paramString2, String paramString3) {
    try {
      return super.getTablePrivileges(paramString1, paramString2, paramString3);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getBestRowIdentifier(String paramString1, String paramString2, String paramString3, int paramInt, boolean paramBoolean) {
    try {
      return super.getBestRowIdentifier(paramString1, paramString2, paramString3, paramInt, paramBoolean);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getVersionColumns(String paramString1, String paramString2, String paramString3) {
    try {
      return super.getVersionColumns(paramString1, paramString2, paramString3);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getPrimaryKeys(String paramString1, String paramString2, String paramString3) {
    try {
      return super.getPrimaryKeys(paramString1, paramString2, paramString3);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getImportedKeys(String paramString1, String paramString2, String paramString3) {
    try {
      return super.getImportedKeys(paramString1, paramString2, paramString3);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getExportedKeys(String paramString1, String paramString2, String paramString3) {
    try {
      return super.getExportedKeys(paramString1, paramString2, paramString3);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getCrossReference(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) {
    try {
      return super.getCrossReference(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getTypeInfo() {
    try {
      return super.getTypeInfo();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getIndexInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2) {
    try {
      return super.getIndexInfo(paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsResultSetType(int paramInt) {
    try {
      return this.delegate.supportsResultSetType(paramInt);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsResultSetConcurrency(int paramInt1, int paramInt2) {
    try {
      return this.delegate.supportsResultSetConcurrency(paramInt1, paramInt2);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean ownUpdatesAreVisible(int paramInt) {
    try {
      return this.delegate.ownUpdatesAreVisible(paramInt);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean ownDeletesAreVisible(int paramInt) {
    try {
      return this.delegate.ownDeletesAreVisible(paramInt);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean ownInsertsAreVisible(int paramInt) {
    try {
      return this.delegate.ownInsertsAreVisible(paramInt);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean othersUpdatesAreVisible(int paramInt) {
    try {
      return this.delegate.othersUpdatesAreVisible(paramInt);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean othersDeletesAreVisible(int paramInt) {
    try {
      return this.delegate.othersDeletesAreVisible(paramInt);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean othersInsertsAreVisible(int paramInt) {
    try {
      return this.delegate.othersInsertsAreVisible(paramInt);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean updatesAreDetected(int paramInt) {
    try {
      return this.delegate.updatesAreDetected(paramInt);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean deletesAreDetected(int paramInt) {
    try {
      return this.delegate.deletesAreDetected(paramInt);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean insertsAreDetected(int paramInt) {
    try {
      return this.delegate.insertsAreDetected(paramInt);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsBatchUpdates() {
    try {
      return this.delegate.supportsBatchUpdates();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getUDTs(String paramString1, String paramString2, String paramString3, int[] paramArrayOfint) {
    try {
      return super.getUDTs(paramString1, paramString2, paramString3, paramArrayOfint);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsSavepoints() {
    try {
      return this.delegate.supportsSavepoints();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsNamedParameters() {
    try {
      return this.delegate.supportsNamedParameters();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsMultipleOpenResults() {
    try {
      return this.delegate.supportsMultipleOpenResults();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsGetGeneratedKeys() {
    try {
      return this.delegate.supportsGetGeneratedKeys();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getSuperTypes(String paramString1, String paramString2, String paramString3) {
    try {
      return super.getSuperTypes(paramString1, paramString2, paramString3);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getSuperTables(String paramString1, String paramString2, String paramString3) {
    try {
      return super.getSuperTables(paramString1, paramString2, paramString3);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getAttributes(String paramString1, String paramString2, String paramString3, String paramString4) {
    try {
      return super.getAttributes(paramString1, paramString2, paramString3, paramString4);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsResultSetHoldability(int paramInt) {
    try {
      return this.delegate.supportsResultSetHoldability(paramInt);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getResultSetHoldability() {
    try {
      return this.delegate.getResultSetHoldability();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getDatabaseMajorVersion() {
    try {
      return this.delegate.getDatabaseMajorVersion();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getDatabaseMinorVersion() {
    try {
      return this.delegate.getDatabaseMinorVersion();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getJDBCMajorVersion() {
    try {
      return this.delegate.getJDBCMajorVersion();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getJDBCMinorVersion() {
    try {
      return this.delegate.getJDBCMinorVersion();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public int getSQLStateType() {
    try {
      return this.delegate.getSQLStateType();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean locatorsUpdateCopy() {
    try {
      return this.delegate.locatorsUpdateCopy();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsStatementPooling() {
    try {
      return this.delegate.supportsStatementPooling();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public RowIdLifetime getRowIdLifetime() {
    try {
      return this.delegate.getRowIdLifetime();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getSchemas(String paramString1, String paramString2) {
    try {
      return super.getSchemas(paramString1, paramString2);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsStoredFunctionsUsingCallSyntax() {
    try {
      return this.delegate.supportsStoredFunctionsUsingCallSyntax();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean autoCommitFailureClosesAllResultSets() {
    try {
      return this.delegate.autoCommitFailureClosesAllResultSets();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getClientInfoProperties() {
    try {
      return super.getClientInfoProperties();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getFunctions(String paramString1, String paramString2, String paramString3) {
    try {
      return super.getFunctions(paramString1, paramString2, paramString3);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getFunctionColumns(String paramString1, String paramString2, String paramString3, String paramString4) {
    try {
      return super.getFunctionColumns(paramString1, paramString2, paramString3, paramString4);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public ResultSet getPseudoColumns(String paramString1, String paramString2, String paramString3, String paramString4) {
    try {
      return super.getPseudoColumns(paramString1, paramString2, paramString3, paramString4);
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean generatedKeyAlwaysReturned() {
    try {
      return this.delegate.generatedKeyAlwaysReturned();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public long getMaxLogicalLobSize() {
    try {
      return this.delegate.getMaxLogicalLobSize();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  public boolean supportsRefCursors() {
    try {
      return this.delegate.supportsRefCursors();
    } catch (SQLException sQLException) {
      throw checkException(sQLException);
    } 
  }
  
  HikariProxyDatabaseMetaData(ProxyConnection paramProxyConnection, DatabaseMetaData paramDatabaseMetaData) {
    super(paramProxyConnection, paramDatabaseMetaData);
  }
}


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\pool\HikariProxyDatabaseMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*      */ package com.zaxxer.hikari;
/*      */ 
/*      */ import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
/*      */ import com.zaxxer.hikari.util.PropertyElf;
/*      */ import com.zaxxer.hikari.util.UtilityElf;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.security.AccessControlException;
/*      */ import java.util.Properties;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.ThreadFactory;
/*      */ import java.util.concurrent.ThreadLocalRandom;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import javax.naming.InitialContext;
/*      */ import javax.naming.NamingException;
/*      */ import javax.sql.DataSource;
/*      */ import org.slf4j.Logger;
/*      */ import org.slf4j.LoggerFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class HikariConfig
/*      */   implements HikariConfigMXBean
/*      */ {
/*   51 */   private static final Logger LOGGER = LoggerFactory.getLogger(HikariConfig.class);
/*      */   
/*   53 */   private static final char[] ID_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
/*   54 */   private static final long CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30L);
/*   55 */   private static final long VALIDATION_TIMEOUT = TimeUnit.SECONDS.toMillis(5L);
/*   56 */   private static final long IDLE_TIMEOUT = TimeUnit.MINUTES.toMillis(10L);
/*   57 */   private static final long MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean unitTest = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  108 */   private Properties dataSourceProperties = new Properties();
/*  109 */   private Properties healthCheckProperties = new Properties();
/*      */   
/*  111 */   private volatile int minIdle = -1;
/*  112 */   private volatile int maxPoolSize = -1;
/*  113 */   private volatile long maxLifetime = MAX_LIFETIME;
/*  114 */   private volatile long connectionTimeout = CONNECTION_TIMEOUT;
/*  115 */   private volatile long validationTimeout = VALIDATION_TIMEOUT;
/*  116 */   private volatile long idleTimeout = IDLE_TIMEOUT; private boolean isAutoCommit = true; private static final int DEFAULT_POOL_SIZE = 10; private volatile String catalog; private volatile long leakDetectionThreshold; private volatile String username; private volatile String password; private String connectionInitSql;
/*  117 */   private long initializationFailTimeout = 1L; private String connectionTestQuery; private String dataSourceClassName; private String dataSourceJndiName; private String driverClassName; private String jdbcUrl; private String poolName;
/*      */   
/*      */   public HikariConfig() {
/*  120 */     String str = System.getProperty("hikaricp.configurationFile");
/*  121 */     if (str != null)
/*  122 */       loadProperties(str); 
/*      */   }
/*      */   private String schema; private String transactionIsolationName; private boolean isReadOnly; private boolean isIsolateInternalQueries; private boolean isRegisterMbeans; private boolean isAllowPoolSuspension; private DataSource dataSource;
/*      */   private ThreadFactory threadFactory;
/*      */   private ScheduledExecutorService scheduledExecutor;
/*      */   private MetricsTrackerFactory metricsTrackerFactory;
/*      */   private Object metricRegistry;
/*      */   private Object healthCheckRegistry;
/*      */   private volatile boolean sealed;
/*      */   
/*      */   public HikariConfig(Properties paramProperties) {
/*  133 */     this();
/*  134 */     PropertyElf.setTargetFromProperties(this, paramProperties);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HikariConfig(String paramString) {
/*  146 */     this();
/*      */     
/*  148 */     loadProperties(paramString);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCatalog() {
/*  159 */     return this.catalog;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCatalog(String paramString) {
/*  166 */     this.catalog = paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getConnectionTimeout() {
/*  174 */     return this.connectionTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionTimeout(long paramLong) {
/*  181 */     if (paramLong == 0L) {
/*  182 */       this.connectionTimeout = 2147483647L;
/*      */     } else {
/*  184 */       if (paramLong < 250L) {
/*  185 */         throw new IllegalArgumentException("connectionTimeout cannot be less than 250ms");
/*      */       }
/*      */       
/*  188 */       this.connectionTimeout = paramLong;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIdleTimeout() {
/*  196 */     return this.idleTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIdleTimeout(long paramLong) {
/*  203 */     if (paramLong < 0L) {
/*  204 */       throw new IllegalArgumentException("idleTimeout cannot be negative");
/*      */     }
/*  206 */     this.idleTimeout = paramLong;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLeakDetectionThreshold() {
/*  213 */     return this.leakDetectionThreshold;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLeakDetectionThreshold(long paramLong) {
/*  220 */     this.leakDetectionThreshold = paramLong;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getMaxLifetime() {
/*  227 */     return this.maxLifetime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxLifetime(long paramLong) {
/*  234 */     this.maxLifetime = paramLong;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaximumPoolSize() {
/*  241 */     return this.maxPoolSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaximumPoolSize(int paramInt) {
/*  248 */     if (paramInt < 1) {
/*  249 */       throw new IllegalArgumentException("maxPoolSize cannot be less than 1");
/*      */     }
/*  251 */     this.maxPoolSize = paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMinimumIdle() {
/*  258 */     return this.minIdle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMinimumIdle(int paramInt) {
/*  265 */     if (paramInt < 0) {
/*  266 */       throw new IllegalArgumentException("minimumIdle cannot be negative");
/*      */     }
/*  268 */     this.minIdle = paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPassword() {
/*  277 */     return this.password;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPassword(String paramString) {
/*  287 */     this.password = paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUsername() {
/*  297 */     return this.username;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUsername(String paramString) {
/*  308 */     this.username = paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getValidationTimeout() {
/*  315 */     return this.validationTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setValidationTimeout(long paramLong) {
/*  322 */     if (paramLong < 250L) {
/*  323 */       throw new IllegalArgumentException("validationTimeout cannot be less than 250ms");
/*      */     }
/*      */     
/*  326 */     this.validationTimeout = paramLong;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConnectionTestQuery() {
/*  340 */     return this.connectionTestQuery;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionTestQuery(String paramString) {
/*  352 */     checkIfSealed();
/*  353 */     this.connectionTestQuery = paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConnectionInitSql() {
/*  364 */     return this.connectionInitSql;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionInitSql(String paramString) {
/*  376 */     checkIfSealed();
/*  377 */     this.connectionInitSql = paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DataSource getDataSource() {
/*  388 */     return this.dataSource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDataSource(DataSource paramDataSource) {
/*  399 */     checkIfSealed();
/*  400 */     this.dataSource = paramDataSource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDataSourceClassName() {
/*  410 */     return this.dataSourceClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDataSourceClassName(String paramString) {
/*  420 */     checkIfSealed();
/*  421 */     this.dataSourceClassName = paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDataSourceProperty(String paramString, Object paramObject) {
/*  439 */     checkIfSealed();
/*  440 */     this.dataSourceProperties.put(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDataSourceJNDI() {
/*  445 */     return this.dataSourceJndiName;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDataSourceJNDI(String paramString) {
/*  450 */     checkIfSealed();
/*  451 */     this.dataSourceJndiName = paramString;
/*      */   }
/*      */ 
/*      */   
/*      */   public Properties getDataSourceProperties() {
/*  456 */     return this.dataSourceProperties;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDataSourceProperties(Properties paramProperties) {
/*  461 */     checkIfSealed();
/*  462 */     this.dataSourceProperties.putAll(paramProperties);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDriverClassName() {
/*  467 */     return this.driverClassName;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDriverClassName(String paramString) {
/*  472 */     checkIfSealed();
/*      */     
/*  474 */     Class<?> clazz = attemptFromContextLoader(paramString);
/*      */     try {
/*  476 */       if (clazz == null) {
/*  477 */         clazz = getClass().getClassLoader().loadClass(paramString);
/*  478 */         LOGGER.debug("Driver class {} found in the HikariConfig class classloader {}", paramString, getClass().getClassLoader());
/*      */       } 
/*  480 */     } catch (ClassNotFoundException classNotFoundException) {
/*  481 */       LOGGER.error("Failed to load driver class {} from HikariConfig class classloader {}", paramString, getClass().getClassLoader());
/*      */     } 
/*      */     
/*  484 */     if (clazz == null) {
/*  485 */       throw new RuntimeException("Failed to load driver class " + paramString + " in either of HikariConfig class loader or Thread context classloader");
/*      */     }
/*      */     
/*      */     try {
/*  489 */       clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*  490 */       this.driverClassName = paramString;
/*      */     }
/*  492 */     catch (Exception exception) {
/*  493 */       throw new RuntimeException("Failed to instantiate class " + paramString, exception);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String getJdbcUrl() {
/*  499 */     return this.jdbcUrl;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setJdbcUrl(String paramString) {
/*  504 */     checkIfSealed();
/*  505 */     this.jdbcUrl = paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutoCommit() {
/*  515 */     return this.isAutoCommit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoCommit(boolean paramBoolean) {
/*  525 */     checkIfSealed();
/*  526 */     this.isAutoCommit = paramBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowPoolSuspension() {
/*  536 */     return this.isAllowPoolSuspension;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowPoolSuspension(boolean paramBoolean) {
/*  548 */     checkIfSealed();
/*  549 */     this.isAllowPoolSuspension = paramBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getInitializationFailTimeout() {
/*  561 */     return this.initializationFailTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInitializationFailTimeout(long paramLong) {
/*  599 */     checkIfSealed();
/*  600 */     this.initializationFailTimeout = paramLong;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIsolateInternalQueries() {
/*  611 */     return this.isIsolateInternalQueries;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIsolateInternalQueries(boolean paramBoolean) {
/*  622 */     checkIfSealed();
/*  623 */     this.isIsolateInternalQueries = paramBoolean;
/*      */   }
/*      */ 
/*      */   
/*      */   public MetricsTrackerFactory getMetricsTrackerFactory() {
/*  628 */     return this.metricsTrackerFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMetricsTrackerFactory(MetricsTrackerFactory paramMetricsTrackerFactory) {
/*  633 */     if (this.metricRegistry != null) {
/*  634 */       throw new IllegalStateException("cannot use setMetricsTrackerFactory() and setMetricRegistry() together");
/*      */     }
/*      */     
/*  637 */     this.metricsTrackerFactory = paramMetricsTrackerFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getMetricRegistry() {
/*  647 */     return this.metricRegistry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMetricRegistry(Object paramObject) {
/*  657 */     if (this.metricsTrackerFactory != null) {
/*  658 */       throw new IllegalStateException("cannot use setMetricRegistry() and setMetricsTrackerFactory() together");
/*      */     }
/*      */     
/*  661 */     if (paramObject != null) {
/*  662 */       paramObject = getObjectOrPerformJndiLookup(paramObject);
/*      */       
/*  664 */       if (!UtilityElf.safeIsAssignableFrom(paramObject, "com.codahale.metrics.MetricRegistry") && 
/*  665 */         !UtilityElf.safeIsAssignableFrom(paramObject, "io.micrometer.core.instrument.MeterRegistry")) {
/*  666 */         throw new IllegalArgumentException("Class must be instance of com.codahale.metrics.MetricRegistry or io.micrometer.core.instrument.MeterRegistry");
/*      */       }
/*      */     } 
/*      */     
/*  670 */     this.metricRegistry = paramObject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getHealthCheckRegistry() {
/*  681 */     return this.healthCheckRegistry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHealthCheckRegistry(Object paramObject) {
/*  692 */     checkIfSealed();
/*      */     
/*  694 */     if (paramObject != null) {
/*  695 */       paramObject = getObjectOrPerformJndiLookup(paramObject);
/*      */       
/*  697 */       if (!(paramObject instanceof com.codahale.metrics.health.HealthCheckRegistry)) {
/*  698 */         throw new IllegalArgumentException("Class must be an instance of com.codahale.metrics.health.HealthCheckRegistry");
/*      */       }
/*      */     } 
/*      */     
/*  702 */     this.healthCheckRegistry = paramObject;
/*      */   }
/*      */ 
/*      */   
/*      */   public Properties getHealthCheckProperties() {
/*  707 */     return this.healthCheckProperties;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHealthCheckProperties(Properties paramProperties) {
/*  712 */     checkIfSealed();
/*  713 */     this.healthCheckProperties.putAll(paramProperties);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addHealthCheckProperty(String paramString1, String paramString2) {
/*  718 */     checkIfSealed();
/*  719 */     this.healthCheckProperties.setProperty(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() {
/*  729 */     return this.isReadOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReadOnly(boolean paramBoolean) {
/*  739 */     checkIfSealed();
/*  740 */     this.isReadOnly = paramBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRegisterMbeans() {
/*  751 */     return this.isRegisterMbeans;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRegisterMbeans(boolean paramBoolean) {
/*  761 */     checkIfSealed();
/*  762 */     this.isRegisterMbeans = paramBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPoolName() {
/*  769 */     return this.poolName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPoolName(String paramString) {
/*  780 */     checkIfSealed();
/*  781 */     this.poolName = paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScheduledExecutorService getScheduledExecutor() {
/*  791 */     return this.scheduledExecutor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setScheduledExecutor(ScheduledExecutorService paramScheduledExecutorService) {
/*  801 */     checkIfSealed();
/*  802 */     this.scheduledExecutor = paramScheduledExecutorService;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTransactionIsolation() {
/*  807 */     return this.transactionIsolationName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSchema() {
/*  816 */     return this.schema;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSchema(String paramString) {
/*  826 */     checkIfSealed();
/*  827 */     this.schema = paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransactionIsolation(String paramString) {
/*  839 */     checkIfSealed();
/*  840 */     this.transactionIsolationName = paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ThreadFactory getThreadFactory() {
/*  850 */     return this.threadFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setThreadFactory(ThreadFactory paramThreadFactory) {
/*  860 */     checkIfSealed();
/*  861 */     this.threadFactory = paramThreadFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   void seal() {
/*  866 */     this.sealed = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyStateTo(HikariConfig paramHikariConfig) {
/*  876 */     for (Field field : HikariConfig.class.getDeclaredFields()) {
/*  877 */       if (!Modifier.isFinal(field.getModifiers())) {
/*  878 */         field.setAccessible(true);
/*      */         try {
/*  880 */           field.set(paramHikariConfig, field.get(this));
/*      */         }
/*  882 */         catch (Exception exception) {
/*  883 */           throw new RuntimeException("Failed to copy HikariConfig state: " + exception.getMessage(), exception);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  888 */     paramHikariConfig.sealed = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Class<?> attemptFromContextLoader(String paramString) {
/*  896 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*  897 */     if (classLoader != null) {
/*      */       try {
/*  899 */         Class<?> clazz = classLoader.loadClass(paramString);
/*  900 */         LOGGER.debug("Driver class {} found in Thread context class loader {}", paramString, classLoader);
/*  901 */         return clazz;
/*  902 */       } catch (ClassNotFoundException classNotFoundException) {
/*  903 */         LOGGER.debug("Driver class {} not found in Thread context class loader {}, trying classloader {}", new Object[] { paramString, classLoader, 
/*  904 */               getClass().getClassLoader() });
/*      */       } 
/*      */     }
/*      */     
/*  908 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void validate() {
/*  914 */     if (this.poolName == null) {
/*  915 */       this.poolName = generatePoolName();
/*      */     }
/*  917 */     else if (this.isRegisterMbeans && this.poolName.contains(":")) {
/*  918 */       throw new IllegalArgumentException("poolName cannot contain ':' when used with JMX");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  923 */     this.catalog = UtilityElf.getNullIfEmpty(this.catalog);
/*  924 */     this.connectionInitSql = UtilityElf.getNullIfEmpty(this.connectionInitSql);
/*  925 */     this.connectionTestQuery = UtilityElf.getNullIfEmpty(this.connectionTestQuery);
/*  926 */     this.transactionIsolationName = UtilityElf.getNullIfEmpty(this.transactionIsolationName);
/*  927 */     this.dataSourceClassName = UtilityElf.getNullIfEmpty(this.dataSourceClassName);
/*  928 */     this.dataSourceJndiName = UtilityElf.getNullIfEmpty(this.dataSourceJndiName);
/*  929 */     this.driverClassName = UtilityElf.getNullIfEmpty(this.driverClassName);
/*  930 */     this.jdbcUrl = UtilityElf.getNullIfEmpty(this.jdbcUrl);
/*      */ 
/*      */     
/*  933 */     if (this.dataSource != null) {
/*  934 */       if (this.dataSourceClassName != null) {
/*  935 */         LOGGER.warn("{} - using dataSource and ignoring dataSourceClassName.", this.poolName);
/*      */       }
/*      */     }
/*  938 */     else if (this.dataSourceClassName != null) {
/*  939 */       if (this.driverClassName != null) {
/*  940 */         LOGGER.error("{} - cannot use driverClassName and dataSourceClassName together.", this.poolName);
/*      */ 
/*      */         
/*  943 */         throw new IllegalStateException("cannot use driverClassName and dataSourceClassName together.");
/*      */       } 
/*  945 */       if (this.jdbcUrl != null) {
/*  946 */         LOGGER.warn("{} - using dataSourceClassName and ignoring jdbcUrl.", this.poolName);
/*      */       }
/*      */     }
/*  949 */     else if (this.jdbcUrl == null && this.dataSourceJndiName == null) {
/*      */ 
/*      */       
/*  952 */       if (this.driverClassName != null) {
/*  953 */         LOGGER.error("{} - jdbcUrl is required with driverClassName.", this.poolName);
/*  954 */         throw new IllegalArgumentException("jdbcUrl is required with driverClassName.");
/*      */       } 
/*      */       
/*  957 */       LOGGER.error("{} - dataSource or dataSourceClassName or jdbcUrl is required.", this.poolName);
/*  958 */       throw new IllegalArgumentException("dataSource or dataSourceClassName or jdbcUrl is required.");
/*      */     } 
/*      */     
/*  961 */     validateNumerics();
/*      */     
/*  963 */     if (LOGGER.isDebugEnabled() || unitTest) {
/*  964 */       logConfiguration();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void validateNumerics() {
/*  970 */     if (this.maxLifetime != 0L && this.maxLifetime < TimeUnit.SECONDS.toMillis(30L)) {
/*  971 */       LOGGER.warn("{} - maxLifetime is less than 30000ms, setting to default {}ms.", this.poolName, Long.valueOf(MAX_LIFETIME));
/*  972 */       this.maxLifetime = MAX_LIFETIME;
/*      */     } 
/*      */     
/*  975 */     if (this.leakDetectionThreshold > 0L && !unitTest && (
/*  976 */       this.leakDetectionThreshold < TimeUnit.SECONDS.toMillis(2L) || (this.leakDetectionThreshold > this.maxLifetime && this.maxLifetime > 0L))) {
/*  977 */       LOGGER.warn("{} - leakDetectionThreshold is less than 2000ms or more than maxLifetime, disabling it.", this.poolName);
/*  978 */       this.leakDetectionThreshold = 0L;
/*      */     } 
/*      */ 
/*      */     
/*  982 */     if (this.connectionTimeout < 250L) {
/*  983 */       LOGGER.warn("{} - connectionTimeout is less than 250ms, setting to {}ms.", this.poolName, Long.valueOf(CONNECTION_TIMEOUT));
/*  984 */       this.connectionTimeout = CONNECTION_TIMEOUT;
/*      */     } 
/*      */     
/*  987 */     if (this.validationTimeout < 250L) {
/*  988 */       LOGGER.warn("{} - validationTimeout is less than 250ms, setting to {}ms.", this.poolName, Long.valueOf(VALIDATION_TIMEOUT));
/*  989 */       this.validationTimeout = VALIDATION_TIMEOUT;
/*      */     } 
/*      */     
/*  992 */     if (this.maxPoolSize < 1) {
/*  993 */       this.maxPoolSize = 10;
/*      */     }
/*      */     
/*  996 */     if (this.minIdle < 0 || this.minIdle > this.maxPoolSize) {
/*  997 */       this.minIdle = this.maxPoolSize;
/*      */     }
/*      */     
/* 1000 */     if (this.idleTimeout + TimeUnit.SECONDS.toMillis(1L) > this.maxLifetime && this.maxLifetime > 0L && this.minIdle < this.maxPoolSize) {
/* 1001 */       LOGGER.warn("{} - idleTimeout is close to or more than maxLifetime, disabling it.", this.poolName);
/* 1002 */       this.idleTimeout = 0L;
/*      */     }
/* 1004 */     else if (this.idleTimeout != 0L && this.idleTimeout < TimeUnit.SECONDS.toMillis(10L) && this.minIdle < this.maxPoolSize) {
/* 1005 */       LOGGER.warn("{} - idleTimeout is less than 10000ms, setting to default {}ms.", this.poolName, Long.valueOf(IDLE_TIMEOUT));
/* 1006 */       this.idleTimeout = IDLE_TIMEOUT;
/*      */     }
/* 1008 */     else if (this.idleTimeout != IDLE_TIMEOUT && this.idleTimeout != 0L && this.minIdle == this.maxPoolSize) {
/* 1009 */       LOGGER.warn("{} - idleTimeout has been set but has no effect because the pool is operating as a fixed size pool.", this.poolName);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkIfSealed() {
/* 1015 */     if (this.sealed) throw new IllegalStateException("The configuration of the pool is sealed once started. Use HikariConfigMXBean for runtime changes.");
/*      */   
/*      */   }
/*      */   
/*      */   private void logConfiguration() {
/* 1020 */     LOGGER.debug("{} - configuration:", this.poolName);
/* 1021 */     TreeSet treeSet = new TreeSet(PropertyElf.getPropertyNames(HikariConfig.class));
/* 1022 */     for (String str : treeSet) {
/*      */       try {
/* 1024 */         Object object = PropertyElf.getProperty(str, this);
/* 1025 */         if ("dataSourceProperties".equals(str)) {
/* 1026 */           Properties properties = PropertyElf.copyProperties(this.dataSourceProperties);
/* 1027 */           properties.setProperty("password", "<masked>");
/* 1028 */           object = properties;
/*      */         } 
/*      */         
/* 1031 */         if ("initializationFailTimeout".equals(str) && this.initializationFailTimeout == Long.MAX_VALUE) {
/* 1032 */           object = "infinite";
/*      */         }
/* 1034 */         else if ("transactionIsolation".equals(str) && this.transactionIsolationName == null) {
/* 1035 */           object = "default";
/*      */         }
/* 1037 */         else if (str.matches("scheduledExecutorService|threadFactory") && object == null) {
/* 1038 */           object = "internal";
/*      */         }
/* 1040 */         else if (str.contains("jdbcUrl") && object instanceof String) {
/* 1041 */           object = ((String)object).replaceAll("([?&;]password=)[^&#;]*(.*)", "$1<masked>$2");
/*      */         }
/* 1043 */         else if (str.contains("password")) {
/* 1044 */           object = "<masked>";
/*      */         }
/* 1046 */         else if (object instanceof String) {
/* 1047 */           object = "\"" + object + "\"";
/*      */         }
/* 1049 */         else if (object == null) {
/* 1050 */           object = "none";
/*      */         } 
/* 1052 */         LOGGER.debug((str + "................................................").substring(0, 32) + object);
/*      */       }
/* 1054 */       catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void loadProperties(String paramString) {
/* 1062 */     File file = new File(paramString);
/* 1063 */     try (InputStream null = file.isFile() ? new FileInputStream(file) : getClass().getResourceAsStream(paramString)) {
/* 1064 */       if (inputStream != null) {
/* 1065 */         Properties properties = new Properties();
/* 1066 */         properties.load(inputStream);
/* 1067 */         PropertyElf.setTargetFromProperties(this, properties);
/*      */       } else {
/*      */         
/* 1070 */         throw new IllegalArgumentException("Cannot find property file: " + paramString);
/*      */       }
/*      */     
/* 1073 */     } catch (IOException iOException) {
/* 1074 */       throw new RuntimeException("Failed to read property file", iOException);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private String generatePoolName() {
/* 1080 */     String str = "HikariPool-";
/*      */     
/*      */     try {
/* 1083 */       synchronized (System.getProperties()) {
/* 1084 */         String str1 = String.valueOf(Integer.getInteger("com.zaxxer.hikari.pool_number", 0).intValue() + 1);
/* 1085 */         System.setProperty("com.zaxxer.hikari.pool_number", str1);
/* 1086 */         return "HikariPool-" + str1;
/*      */       } 
/* 1088 */     } catch (AccessControlException accessControlException) {
/*      */ 
/*      */       
/* 1091 */       ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
/* 1092 */       StringBuilder stringBuilder = new StringBuilder("HikariPool-");
/*      */       
/* 1094 */       for (byte b = 0; b < 4; b++) {
/* 1095 */         stringBuilder.append(ID_CHARACTERS[threadLocalRandom.nextInt(62)]);
/*      */       }
/*      */       
/* 1098 */       LOGGER.info("assigned random pool name '{}' (security manager prevented access to system properties)", stringBuilder);
/*      */       
/* 1100 */       return stringBuilder.toString();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private Object getObjectOrPerformJndiLookup(Object paramObject) {
/* 1106 */     if (paramObject instanceof String) {
/*      */       try {
/* 1108 */         InitialContext initialContext = new InitialContext();
/* 1109 */         return initialContext.lookup((String)paramObject);
/*      */       }
/* 1111 */       catch (NamingException namingException) {
/* 1112 */         throw new IllegalArgumentException(namingException);
/*      */       } 
/*      */     }
/* 1115 */     return paramObject;
/*      */   }
/*      */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\HikariConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
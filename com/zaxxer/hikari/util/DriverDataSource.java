/*     */ package com.zaxxer.hikari.util;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Driver;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import javax.sql.DataSource;
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
/*     */ public final class DriverDataSource
/*     */   implements DataSource
/*     */ {
/*  35 */   private static final Logger LOGGER = LoggerFactory.getLogger(DriverDataSource.class);
/*     */   
/*     */   private static final String PASSWORD = "password";
/*     */   
/*     */   private static final String USER = "user";
/*     */   private final String jdbcUrl;
/*     */   private final Properties driverProperties;
/*     */   private Driver driver;
/*     */   
/*     */   public DriverDataSource(String paramString1, String paramString2, Properties paramProperties, String paramString3, String paramString4) {
/*  45 */     this.jdbcUrl = paramString1;
/*  46 */     this.driverProperties = new Properties();
/*     */     
/*  48 */     for (Map.Entry<Object, Object> entry : paramProperties.entrySet()) {
/*  49 */       this.driverProperties.setProperty(entry.getKey().toString(), entry.getValue().toString());
/*     */     }
/*     */     
/*  52 */     if (paramString3 != null) {
/*  53 */       this.driverProperties.put("user", this.driverProperties.getProperty("user", paramString3));
/*     */     }
/*  55 */     if (paramString4 != null) {
/*  56 */       this.driverProperties.put("password", this.driverProperties.getProperty("password", paramString4));
/*     */     }
/*     */     
/*  59 */     if (paramString2 != null) {
/*  60 */       Enumeration<Driver> enumeration = DriverManager.getDrivers();
/*  61 */       while (enumeration.hasMoreElements()) {
/*  62 */         Driver driver = enumeration.nextElement();
/*  63 */         if (driver.getClass().getName().equals(paramString2)) {
/*  64 */           this.driver = driver;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*  69 */       if (this.driver == null) {
/*  70 */         LOGGER.warn("Registered driver with driverClassName={} was not found, trying direct instantiation.", paramString2);
/*  71 */         Class<?> clazz = null;
/*  72 */         ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*     */         try {
/*  74 */           if (classLoader != null) {
/*     */             try {
/*  76 */               clazz = classLoader.loadClass(paramString2);
/*  77 */               LOGGER.debug("Driver class {} found in Thread context class loader {}", paramString2, classLoader);
/*     */             }
/*  79 */             catch (ClassNotFoundException classNotFoundException) {
/*  80 */               LOGGER.debug("Driver class {} not found in Thread context class loader {}, trying classloader {}", new Object[] { paramString2, classLoader, 
/*  81 */                     getClass().getClassLoader() });
/*     */             } 
/*     */           }
/*     */           
/*  85 */           if (clazz == null) {
/*  86 */             clazz = getClass().getClassLoader().loadClass(paramString2);
/*  87 */             LOGGER.debug("Driver class {} found in the HikariConfig class classloader {}", paramString2, getClass().getClassLoader());
/*     */           } 
/*  89 */         } catch (ClassNotFoundException classNotFoundException) {
/*  90 */           LOGGER.debug("Failed to load driver class {} from HikariConfig class classloader {}", paramString2, getClass().getClassLoader());
/*     */         } 
/*     */         
/*  93 */         if (clazz != null) {
/*     */           try {
/*  95 */             this.driver = (Driver)clazz.newInstance();
/*  96 */           } catch (Exception exception) {
/*  97 */             LOGGER.warn("Failed to create instance of driver class {}, trying jdbcUrl resolution", paramString2, exception);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     String str = paramString1.replaceAll("([?&;]password=)[^&#;]*(.*)", "$1<masked>$2");
/*     */     try {
/* 105 */       if (this.driver == null) {
/* 106 */         this.driver = DriverManager.getDriver(paramString1);
/* 107 */         LOGGER.debug("Loaded driver with class name {} for jdbcUrl={}", this.driver.getClass().getName(), str);
/*     */       }
/* 109 */       else if (!this.driver.acceptsURL(paramString1)) {
/* 110 */         throw new RuntimeException("Driver " + paramString2 + " claims to not accept jdbcUrl, " + str);
/*     */       }
/*     */     
/* 113 */     } catch (SQLException sQLException) {
/* 114 */       throw new RuntimeException("Failed to get driver instance for jdbcUrl=" + str, sQLException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() {
/* 121 */     return this.driver.connect(this.jdbcUrl, this.driverProperties);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection(String paramString1, String paramString2) {
/* 127 */     Properties properties = (Properties)this.driverProperties.clone();
/* 128 */     if (paramString1 != null) {
/* 129 */       properties.put("user", paramString1);
/* 130 */       if (properties.containsKey("username")) {
/* 131 */         properties.put("username", paramString1);
/*     */       }
/*     */     } 
/* 134 */     if (paramString2 != null) {
/* 135 */       properties.put("password", paramString2);
/*     */     }
/*     */     
/* 138 */     return this.driver.connect(this.jdbcUrl, properties);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() {
/* 144 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter paramPrintWriter) {
/* 150 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoginTimeout(int paramInt) {
/* 156 */     DriverManager.setLoginTimeout(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLoginTimeout() {
/* 162 */     return DriverManager.getLoginTimeout();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getParentLogger() {
/* 168 */     return this.driver.getParentLogger();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> paramClass) {
/* 174 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> paramClass) {
/* 180 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikar\\util\DriverDataSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
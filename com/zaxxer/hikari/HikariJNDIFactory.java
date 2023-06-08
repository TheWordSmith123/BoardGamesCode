/*    */ package com.zaxxer.hikari;
/*    */ 
/*    */ import com.zaxxer.hikari.util.PropertyElf;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Hashtable;
/*    */ import java.util.Properties;
/*    */ import java.util.Set;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.Name;
/*    */ import javax.naming.RefAddr;
/*    */ import javax.naming.Reference;
/*    */ import javax.naming.spi.ObjectFactory;
/*    */ import javax.sql.DataSource;
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
/*    */ public class HikariJNDIFactory
/*    */   implements ObjectFactory
/*    */ {
/*    */   public synchronized Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable) {
/* 46 */     if (paramObject instanceof Reference && "javax.sql.DataSource".equals(((Reference)paramObject).getClassName())) {
/* 47 */       Reference reference = (Reference)paramObject;
/* 48 */       Set set = PropertyElf.getPropertyNames(HikariConfig.class);
/*    */       
/* 50 */       Properties properties = new Properties();
/* 51 */       Enumeration<RefAddr> enumeration = reference.getAll();
/* 52 */       while (enumeration.hasMoreElements()) {
/* 53 */         RefAddr refAddr = enumeration.nextElement();
/* 54 */         String str = refAddr.getType();
/* 55 */         if (str.startsWith("dataSource.") || set.contains(str)) {
/* 56 */           properties.setProperty(str, refAddr.getContent().toString());
/*    */         }
/*    */       } 
/* 59 */       return createDataSource(properties, paramContext);
/*    */     } 
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   private DataSource createDataSource(Properties paramProperties, Context paramContext) {
/* 66 */     String str = paramProperties.getProperty("dataSourceJNDI");
/* 67 */     if (str != null) {
/* 68 */       return lookupJndiDataSource(paramProperties, paramContext, str);
/*    */     }
/*    */     
/* 71 */     return new HikariDataSource(new HikariConfig(paramProperties));
/*    */   }
/*    */ 
/*    */   
/*    */   private DataSource lookupJndiDataSource(Properties paramProperties, Context paramContext, String paramString) {
/* 76 */     if (paramContext == null) {
/* 77 */       throw new RuntimeException("JNDI context does not found for dataSourceJNDI : " + paramString);
/*    */     }
/*    */     
/* 80 */     DataSource dataSource = (DataSource)paramContext.lookup(paramString);
/* 81 */     if (dataSource == null) {
/* 82 */       InitialContext initialContext = new InitialContext();
/* 83 */       dataSource = (DataSource)initialContext.lookup(paramString);
/* 84 */       initialContext.close();
/*    */     } 
/*    */     
/* 87 */     if (dataSource != null) {
/* 88 */       HikariConfig hikariConfig = new HikariConfig(paramProperties);
/* 89 */       hikariConfig.setDataSource(dataSource);
/* 90 */       return new HikariDataSource(hikariConfig);
/*    */     } 
/*    */     
/* 93 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\HikariJNDIFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.zaxxer.hikari.util;
/*     */ 
/*     */ import com.zaxxer.hikari.HikariConfig;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public final class PropertyElf
/*     */ {
/*  41 */   private static final Pattern GETTER_PATTERN = Pattern.compile("(get|is)[A-Z].+");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setTargetFromProperties(Object paramObject, Properties paramProperties) {
/*  49 */     if (paramObject == null || paramProperties == null) {
/*     */       return;
/*     */     }
/*     */     
/*  53 */     List<Method> list = Arrays.asList(paramObject.getClass().getMethods());
/*  54 */     paramProperties.forEach((paramObject2, paramObject3) -> {
/*     */           if (paramObject1 instanceof HikariConfig && paramObject2.toString().startsWith("dataSource.")) {
/*     */             ((HikariConfig)paramObject1).addDataSourceProperty(paramObject2.toString().substring("dataSource.".length()), paramObject3);
/*     */           } else {
/*     */             setProperty(paramObject1, paramObject2.toString(), paramObject3, paramList);
/*     */           } 
/*     */         });
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
/*     */   public static Set<String> getPropertyNames(Class<?> paramClass) {
/*  72 */     HashSet<String> hashSet = new HashSet();
/*  73 */     Matcher matcher = GETTER_PATTERN.matcher("");
/*  74 */     for (Method method : paramClass.getMethods()) {
/*  75 */       String str = method.getName();
/*  76 */       if ((method.getParameterTypes()).length == 0 && matcher.reset(str).matches()) {
/*  77 */         str = str.replaceFirst("(get|is)", "");
/*     */         try {
/*  79 */           if (paramClass.getMethod("set" + str, new Class[] { method.getReturnType() }) != null) {
/*  80 */             str = Character.toLowerCase(str.charAt(0)) + str.substring(1);
/*  81 */             hashSet.add(str);
/*     */           }
/*     */         
/*  84 */         } catch (Exception exception) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  90 */     return hashSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getProperty(String paramString, Object paramObject) {
/*     */     try {
/*  97 */       String str = "get" + paramString.substring(0, 1).toUpperCase(Locale.ENGLISH) + paramString.substring(1);
/*  98 */       Method method = paramObject.getClass().getMethod(str, new Class[0]);
/*  99 */       return method.invoke(paramObject, new Object[0]);
/*     */     }
/* 101 */     catch (Exception exception) {
/*     */       try {
/* 103 */         String str = "is" + paramString.substring(0, 1).toUpperCase(Locale.ENGLISH) + paramString.substring(1);
/* 104 */         Method method = paramObject.getClass().getMethod(str, new Class[0]);
/* 105 */         return method.invoke(paramObject, new Object[0]);
/*     */       }
/* 107 */       catch (Exception exception1) {
/* 108 */         return null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Properties copyProperties(Properties paramProperties) {
/* 115 */     Properties properties = new Properties();
/* 116 */     paramProperties.forEach((paramObject1, paramObject2) -> paramProperties.setProperty(paramObject1.toString(), paramObject2.toString()));
/* 117 */     return properties;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void setProperty(Object paramObject1, String paramString, Object paramObject2, List<Method> paramList) {
/* 122 */     Logger logger = LoggerFactory.getLogger(PropertyElf.class);
/*     */ 
/*     */     
/* 125 */     String str = "set" + paramString.substring(0, 1).toUpperCase(Locale.ENGLISH) + paramString.substring(1);
/* 126 */     Method method = paramList.stream().filter(paramMethod -> (paramMethod.getName().equals(paramString) && paramMethod.getParameterCount() == 1)).findFirst().orElse(null);
/*     */     
/* 128 */     if (method == null) {
/* 129 */       String str1 = "set" + paramString.toUpperCase(Locale.ENGLISH);
/* 130 */       method = paramList.stream().filter(paramMethod -> (paramMethod.getName().equals(paramString) && paramMethod.getParameterCount() == 1)).findFirst().orElse(null);
/*     */     } 
/*     */     
/* 133 */     if (method == null) {
/* 134 */       logger.error("Property {} does not exist on target {}", paramString, paramObject1.getClass());
/* 135 */       throw new RuntimeException(String.format("Property %s does not exist on target %s", new Object[] { paramString, paramObject1.getClass() }));
/*     */     } 
/*     */     
/*     */     try {
/* 139 */       Class<?> clazz = method.getParameterTypes()[0];
/* 140 */       if (clazz == int.class) {
/* 141 */         method.invoke(paramObject1, new Object[] { Integer.valueOf(Integer.parseInt(paramObject2.toString())) });
/*     */       }
/* 143 */       else if (clazz == long.class) {
/* 144 */         method.invoke(paramObject1, new Object[] { Long.valueOf(Long.parseLong(paramObject2.toString())) });
/*     */       }
/* 146 */       else if (clazz == boolean.class || clazz == Boolean.class) {
/* 147 */         method.invoke(paramObject1, new Object[] { Boolean.valueOf(Boolean.parseBoolean(paramObject2.toString())) });
/*     */       }
/* 149 */       else if (clazz == String.class) {
/* 150 */         method.invoke(paramObject1, new Object[] { paramObject2.toString() });
/*     */       } else {
/*     */         
/*     */         try {
/* 154 */           logger.debug("Try to create a new instance of \"{}\"", paramObject2.toString());
/* 155 */           method.invoke(paramObject1, new Object[] { Class.forName(paramObject2.toString()).newInstance() });
/*     */         }
/* 157 */         catch (InstantiationException|ClassNotFoundException instantiationException) {
/* 158 */           logger.debug("Class \"{}\" not found or could not instantiate it (Default constructor)", paramObject2.toString());
/* 159 */           method.invoke(paramObject1, new Object[] { paramObject2 });
/*     */         }
/*     */       
/*     */       } 
/* 163 */     } catch (Exception exception) {
/* 164 */       logger.error("Failed to set property {} on target {}", new Object[] { paramString, paramObject1.getClass(), exception });
/* 165 */       throw new RuntimeException(exception);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikar\\util\PropertyElf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
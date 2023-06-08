/*     */ package com.zaxxer.hikari.util;
/*     */ 
/*     */ import com.zaxxer.hikari.pool.ProxyCallableStatement;
/*     */ import com.zaxxer.hikari.pool.ProxyConnection;
/*     */ import com.zaxxer.hikari.pool.ProxyDatabaseMetaData;
/*     */ import com.zaxxer.hikari.pool.ProxyPreparedStatement;
/*     */ import com.zaxxer.hikari.pool.ProxyResultSet;
/*     */ import com.zaxxer.hikari.pool.ProxyStatement;
/*     */ import java.lang.reflect.Array;
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javassist.ClassPath;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.CtNewMethod;
/*     */ import javassist.LoaderClassPath;
/*     */ import javassist.NotFoundException;
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
/*     */ public final class JavassistProxyFactory
/*     */ {
/*     */   private static ClassPool classPool;
/*  44 */   private static String genDirectory = "";
/*     */   
/*     */   public static void main(String... paramVarArgs) {
/*  47 */     classPool = new ClassPool();
/*  48 */     classPool.importPackage("java.sql");
/*  49 */     classPool.appendClassPath((ClassPath)new LoaderClassPath(JavassistProxyFactory.class.getClassLoader()));
/*     */     
/*  51 */     if (paramVarArgs.length > 0) {
/*  52 */       genDirectory = paramVarArgs[0];
/*     */     }
/*     */ 
/*     */     
/*  56 */     String str = "{ try { return delegate.method($$); } catch (SQLException e) { throw checkException(e); } }";
/*  57 */     generateProxyClass(Connection.class, ProxyConnection.class.getName(), str);
/*  58 */     generateProxyClass(Statement.class, ProxyStatement.class.getName(), str);
/*  59 */     generateProxyClass(ResultSet.class, ProxyResultSet.class.getName(), str);
/*  60 */     generateProxyClass(DatabaseMetaData.class, ProxyDatabaseMetaData.class.getName(), str);
/*     */ 
/*     */     
/*  63 */     str = "{ try { return ((cast) delegate).method($$); } catch (SQLException e) { throw checkException(e); } }";
/*  64 */     generateProxyClass(PreparedStatement.class, ProxyPreparedStatement.class.getName(), str);
/*  65 */     generateProxyClass(CallableStatement.class, ProxyCallableStatement.class.getName(), str);
/*     */     
/*  67 */     modifyProxyFactory();
/*     */   }
/*     */   
/*     */   private static void modifyProxyFactory() {
/*  71 */     System.out.println("Generating method bodies for com.zaxxer.hikari.proxy.ProxyFactory");
/*     */     
/*  73 */     String str = ProxyConnection.class.getPackage().getName();
/*  74 */     CtClass ctClass = classPool.getCtClass("com.zaxxer.hikari.pool.ProxyFactory");
/*  75 */     for (CtMethod ctMethod : ctClass.getMethods()) {
/*  76 */       switch (ctMethod.getName()) {
/*     */         case "getProxyConnection":
/*  78 */           ctMethod.setBody("{return new " + str + ".HikariProxyConnection($$);}");
/*     */           break;
/*     */         case "getProxyStatement":
/*  81 */           ctMethod.setBody("{return new " + str + ".HikariProxyStatement($$);}");
/*     */           break;
/*     */         case "getProxyPreparedStatement":
/*  84 */           ctMethod.setBody("{return new " + str + ".HikariProxyPreparedStatement($$);}");
/*     */           break;
/*     */         case "getProxyCallableStatement":
/*  87 */           ctMethod.setBody("{return new " + str + ".HikariProxyCallableStatement($$);}");
/*     */           break;
/*     */         case "getProxyResultSet":
/*  90 */           ctMethod.setBody("{return new " + str + ".HikariProxyResultSet($$);}");
/*     */           break;
/*     */         case "getProxyDatabaseMetaData":
/*  93 */           ctMethod.setBody("{return new " + str + ".HikariProxyDatabaseMetaData($$);}");
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 101 */     ctClass.writeFile(genDirectory + "target/classes");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> void generateProxyClass(Class<T> paramClass, String paramString1, String paramString2) {
/* 109 */     String str = paramString1.replaceAll("(.+)\\.(\\w+)", "$1.Hikari$2");
/*     */     
/* 111 */     CtClass ctClass1 = classPool.getCtClass(paramString1);
/* 112 */     CtClass ctClass2 = classPool.makeClass(str, ctClass1);
/* 113 */     ctClass2.setModifiers(16);
/*     */     
/* 115 */     System.out.println("Generating " + str);
/*     */     
/* 117 */     ctClass2.setModifiers(1);
/*     */ 
/*     */     
/* 120 */     HashSet<String> hashSet1 = new HashSet();
/* 121 */     for (CtMethod ctMethod : ctClass1.getMethods()) {
/* 122 */       if ((ctMethod.getModifiers() & 0x10) == 16) {
/* 123 */         hashSet1.add(ctMethod.getName() + ctMethod.getSignature());
/*     */       }
/*     */     } 
/*     */     
/* 127 */     HashSet<String> hashSet2 = new HashSet();
/* 128 */     for (Class<?> clazz : getAllInterfaces(paramClass)) {
/* 129 */       CtClass ctClass = classPool.getCtClass(clazz.getName());
/* 130 */       ctClass2.addInterface(ctClass);
/* 131 */       for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
/* 132 */         String str1 = ctMethod.getName() + ctMethod.getSignature();
/*     */ 
/*     */         
/* 135 */         if (!hashSet1.contains(str1))
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 140 */           if (!hashSet2.contains(str1)) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 145 */             hashSet2.add(str1);
/*     */ 
/*     */             
/* 148 */             CtMethod ctMethod1 = CtNewMethod.copy(ctMethod, ctClass2, null);
/*     */             
/* 150 */             String str2 = paramString2;
/*     */ 
/*     */             
/* 153 */             CtMethod ctMethod2 = ctClass1.getMethod(ctMethod.getName(), ctMethod.getSignature());
/* 154 */             if ((ctMethod2.getModifiers() & 0x400) != 1024 && !isDefaultMethod(clazz, ctMethod)) {
/* 155 */               str2 = str2.replace("((cast) ", "");
/* 156 */               str2 = str2.replace("delegate", "super");
/* 157 */               str2 = str2.replace("super)", "super");
/*     */             } 
/*     */             
/* 160 */             str2 = str2.replace("cast", paramClass.getName());
/*     */ 
/*     */             
/* 163 */             if (isThrowsSqlException(ctMethod)) {
/* 164 */               str2 = str2.replace("method", ctMethod1.getName());
/*     */             } else {
/*     */               
/* 167 */               str2 = "{ return ((cast) delegate).method($$); }".replace("method", ctMethod1.getName()).replace("cast", paramClass.getName());
/*     */             } 
/*     */             
/* 170 */             if (ctMethod1.getReturnType() == CtClass.voidType) {
/* 171 */               str2 = str2.replace("return", "");
/*     */             }
/*     */             
/* 174 */             ctMethod1.setBody(str2);
/* 175 */             ctClass2.addMethod(ctMethod1);
/*     */           }  } 
/*     */       } 
/*     */     } 
/* 179 */     ctClass2.getClassFile().setMajorVersion(52);
/* 180 */     ctClass2.writeFile(genDirectory + "target/classes");
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isThrowsSqlException(CtMethod paramCtMethod) {
/*     */     try {
/* 186 */       for (CtClass ctClass : paramCtMethod.getExceptionTypes()) {
/* 187 */         if (ctClass.getSimpleName().equals("SQLException")) {
/* 188 */           return true;
/*     */         }
/*     */       }
/*     */     
/* 192 */     } catch (NotFoundException notFoundException) {}
/*     */ 
/*     */ 
/*     */     
/* 196 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isDefaultMethod(Class<?> paramClass, CtMethod paramCtMethod) {
/* 201 */     ArrayList<Class<?>> arrayList = new ArrayList();
/*     */     
/* 203 */     for (CtClass ctClass : paramCtMethod.getParameterTypes()) {
/* 204 */       arrayList.add(toJavaClass(ctClass));
/*     */     }
/*     */     
/* 207 */     return paramClass.getDeclaredMethod(paramCtMethod.getName(), (Class[])arrayList.<Class<?>[]>toArray((Class<?>[][])new Class[0])).toString().contains("default ");
/*     */   }
/*     */ 
/*     */   
/*     */   private static Set<Class<?>> getAllInterfaces(Class<?> paramClass) {
/* 212 */     LinkedHashSet<Class<?>> linkedHashSet = new LinkedHashSet();
/* 213 */     for (Class<?> clazz : paramClass.getInterfaces()) {
/* 214 */       if ((clazz.getInterfaces()).length > 0) {
/* 215 */         linkedHashSet.addAll(getAllInterfaces(clazz));
/*     */       }
/* 217 */       linkedHashSet.add(clazz);
/*     */     } 
/* 219 */     if (paramClass.getSuperclass() != null) {
/* 220 */       linkedHashSet.addAll(getAllInterfaces(paramClass.getSuperclass()));
/*     */     }
/*     */     
/* 223 */     if (paramClass.isInterface()) {
/* 224 */       linkedHashSet.add(paramClass);
/*     */     }
/*     */     
/* 227 */     return linkedHashSet;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Class<?> toJavaClass(CtClass paramCtClass) {
/* 232 */     if (paramCtClass.getName().endsWith("[]")) {
/* 233 */       return Array.newInstance(toJavaClass(paramCtClass.getName().replace("[]", "")), 0).getClass();
/*     */     }
/*     */     
/* 236 */     return toJavaClass(paramCtClass.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> toJavaClass(String paramString) {
/* 242 */     switch (paramString) {
/*     */       case "int":
/* 244 */         return int.class;
/*     */       case "long":
/* 246 */         return long.class;
/*     */       case "short":
/* 248 */         return short.class;
/*     */       case "byte":
/* 250 */         return byte.class;
/*     */       case "float":
/* 252 */         return float.class;
/*     */       case "double":
/* 254 */         return double.class;
/*     */       case "boolean":
/* 256 */         return boolean.class;
/*     */       case "char":
/* 258 */         return char.class;
/*     */       case "void":
/* 260 */         return void.class;
/*     */     } 
/* 262 */     return Class.forName(paramString);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikar\\util\JavassistProxyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
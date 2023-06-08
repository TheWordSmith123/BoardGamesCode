/*    */ package water.of.cup.boardgames.extension;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ import java.net.URLClassLoader;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Enumeration;
/*    */ import java.util.List;
/*    */ import java.util.jar.JarEntry;
/*    */ import java.util.jar.JarFile;
/*    */ import java.util.jar.JarInputStream;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.imageio.ImageIO;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExtensionUtil
/*    */ {
/*    */   @Nullable
/*    */   public static <T> Class<? extends T> findClass(@NotNull File paramFile, @NotNull Class<T> paramClass) {
/* 31 */     if (paramFile == null) $$$reportNull$$$0(0);  if (paramClass == null) $$$reportNull$$$0(1);  if (!paramFile.exists()) {
/* 32 */       return null;
/*    */     }
/*    */     
/* 35 */     URL uRL = paramFile.toURI().toURL();
/* 36 */     URLClassLoader uRLClassLoader = new URLClassLoader(new URL[] { uRL }, paramClass.getClassLoader());
/* 37 */     ArrayList<String> arrayList = new ArrayList();
/* 38 */     ArrayList<Class<? extends T>> arrayList1 = new ArrayList();
/*    */     
/* 40 */     JarInputStream jarInputStream = new JarInputStream(uRL.openStream()); 
/*    */     try { JarEntry jarEntry;
/* 42 */       while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
/* 43 */         String str = jarEntry.getName();
/* 44 */         if (str.isEmpty() || !str.endsWith(".class")) {
/*    */           continue;
/*    */         }
/*    */         
/* 48 */         arrayList.add(str.substring(0, str.lastIndexOf('.')).replace('/', '.'));
/*    */       } 
/*    */       
/* 51 */       for (String str : arrayList) {
/*    */         try {
/* 53 */           Class<?> clazz = uRLClassLoader.loadClass(str);
/* 54 */           if (paramClass.isAssignableFrom(clazz)) {
/* 55 */             arrayList1.add(clazz.asSubclass(paramClass));
/*    */           }
/* 57 */         } catch (NoClassDefFoundError noClassDefFoundError) {}
/*    */       } 
/*    */       
/* 60 */       jarInputStream.close(); } catch (Throwable throwable) { try { jarInputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 61 */      if (arrayList1.isEmpty()) {
/* 62 */       uRLClassLoader.close();
/* 63 */       return null;
/*    */     } 
/* 65 */     return arrayList1.get(0);
/*    */   }
/*    */   
/*    */   public static void loadExtensionImages() {
/* 69 */     File file = new File(BoardGames.getInstance().getDataFolder(), "extensions");
/* 70 */     List list = (List)Arrays.<File>stream(file.listFiles((paramFile, paramString) -> paramString.endsWith(".jar"))).collect(Collectors.toList());
/*    */     
/* 72 */     for (File file1 : list) { 
/* 73 */       try { JarFile jarFile = new JarFile(file1); 
/* 74 */         try { Enumeration<JarEntry> enumeration = jarFile.entries();
/* 75 */           while (enumeration.hasMoreElements()) {
/* 76 */             JarEntry jarEntry = enumeration.nextElement();
/* 77 */             if (jarEntry.getName().endsWith(".png")) {
/* 78 */               JarEntry jarEntry1 = jarFile.getJarEntry(jarEntry.getName());
/* 79 */               InputStream inputStream = jarFile.getInputStream(jarEntry1);
/* 80 */               BufferedImage bufferedImage = ImageIO.read(inputStream);
/*    */               
/* 82 */               String str = getFileName(jarEntry.getName());
/* 83 */               BoardGames.getImageManager().addImage(str, bufferedImage);
/*    */             } 
/*    */           } 
/*    */ 
/*    */           
/* 88 */           jarFile.close(); } catch (Throwable throwable) { try { jarFile.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException iOException)
/* 89 */       { iOException.printStackTrace(); }
/*    */        }
/*    */   
/*    */   }
/*    */   
/*    */   private static String getFileName(String paramString) {
/* 95 */     String[] arrayOfString = paramString.split("/");
/* 96 */     if (arrayOfString.length == 0) return ""; 
/* 97 */     String str = arrayOfString[arrayOfString.length - 1];
/* 98 */     return str.substring(0, str.indexOf('.'));
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\extension\ExtensionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package water.of.cup.boardgames.image_handling;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.HashMap;
/*    */ import javax.annotation.Nullable;
/*    */ import javax.imageio.ImageIO;
/*    */ import water.of.cup.boardgames.BoardGames;
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
/*    */ public class ImageManager
/*    */ {
/* 25 */   private final HashMap<String, BufferedImage> images = new HashMap<>();
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BufferedImage getImage(String paramString) {
/* 30 */     if (this.images.containsKey(paramString)) {
/* 31 */       return this.images.get(paramString);
/*    */     }
/*    */ 
/*    */     
/* 35 */     InputStream inputStream = getCustomImage(paramString);
/*    */     
/* 37 */     if (inputStream == null) {
/* 38 */       String str = "water/of/cup/boardgames/images/" + paramString + ".png";
/* 39 */       inputStream = BoardGames.getInstance().getResource(str);
/*    */     } 
/*    */     
/* 42 */     if (inputStream == null) return null;
/*    */     
/*    */     try {
/* 45 */       BufferedImage bufferedImage = ImageIO.read(inputStream);
/* 46 */       this.images.put(paramString, bufferedImage);
/* 47 */       return bufferedImage;
/* 48 */     } catch (IOException iOException) {
/* 49 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void addImage(String paramString, BufferedImage paramBufferedImage) {
/* 54 */     BufferedImage bufferedImage = null;
/* 55 */     if (getCustomImage(paramString) != null) {
/*    */       try {
/* 57 */         bufferedImage = ImageIO.read(getCustomImage(paramString));
/* 58 */       } catch (IOException iOException) {
/* 59 */         iOException.printStackTrace();
/*    */       } 
/*    */     }
/* 62 */     this.images.put(paramString, (bufferedImage != null) ? bufferedImage : paramBufferedImage);
/*    */   }
/*    */   
/*    */   private InputStream getCustomImage(String paramString) {
/* 66 */     File file = new File(BoardGames.getInstance().getDataFolder() + "/custom_images");
/* 67 */     FileInputStream fileInputStream = null;
/* 68 */     if (file.exists()) {
/* 69 */       File file1 = new File(file + "/" + paramString + ".png");
/* 70 */       if (file1.exists() && file1.isFile()) {
/*    */         try {
/* 72 */           fileInputStream = new FileInputStream(file1);
/* 73 */         } catch (FileNotFoundException fileNotFoundException) {
/* 74 */           fileNotFoundException.printStackTrace();
/*    */         } 
/*    */       }
/*    */     } 
/* 78 */     return fileInputStream;
/*    */   }
/*    */   
/*    */   public void clearImages() {
/* 82 */     this.images.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\image_handling\ImageManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
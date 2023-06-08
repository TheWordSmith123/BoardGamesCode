/*     */ package water.of.cup.boardgames.image_handling;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.AffineTransformOp;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ImageObserver;
/*     */ 
/*     */ public class ImageUtils
/*     */ {
/*     */   public static BufferedImage rotateImage(BufferedImage paramBufferedImage, int paramInt) {
/*  18 */     double d1 = paramBufferedImage.getWidth();
/*  19 */     double d2 = paramBufferedImage.getHeight();
/*     */     
/*  21 */     if (paramInt % 2 == 1) {
/*  22 */       d1 = paramBufferedImage.getHeight();
/*  23 */       d2 = paramBufferedImage.getWidth();
/*     */     } 
/*     */     
/*  26 */     AffineTransform affineTransform = new AffineTransform();
/*     */ 
/*     */     
/*  29 */     if (paramInt == 1)
/*  30 */       affineTransform.translate((d1 - d2) / 2.0D, (d1 - d2) / 2.0D); 
/*  31 */     if (paramInt == 3) {
/*  32 */       affineTransform.translate((d2 - d1) / 2.0D, (d2 - d1) / 2.0D);
/*     */     }
/*  34 */     affineTransform.rotate(1.5707963267948966D * paramInt, d1 / 2.0D, d2 / 2.0D);
/*  35 */     AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, 2);
/*  36 */     return affineTransformOp.filter(paramBufferedImage, (BufferedImage)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BufferedImage rotateSquareImage(BufferedImage paramBufferedImage, double paramDouble) {
/*  41 */     double d1 = paramBufferedImage.getWidth();
/*  42 */     double d2 = paramBufferedImage.getHeight();
/*     */     
/*  44 */     AffineTransform affineTransform = new AffineTransform();
/*     */ 
/*     */     
/*  47 */     affineTransform.rotate(Math.PI * paramDouble / 180.0D, d1 / 2.0D, d2 / 2.0D);
/*  48 */     AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, 2);
/*  49 */     return affineTransformOp.filter(paramBufferedImage, (BufferedImage)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BufferedImage copyImage(BufferedImage paramBufferedImage) {
/*  55 */     BufferedImage bufferedImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), paramBufferedImage.getType());
/*  56 */     Graphics graphics = bufferedImage.getGraphics();
/*  57 */     graphics.drawImage(paramBufferedImage, 0, 0, null);
/*  58 */     graphics.dispose();
/*  59 */     return bufferedImage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BufferedImage combineImages(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2, int[] paramArrayOfint) {
/*  68 */     BufferedImage bufferedImage = new BufferedImage(paramBufferedImage1.getWidth(), paramBufferedImage1.getHeight(), paramBufferedImage1.getType());
/*     */ 
/*     */     
/*  71 */     Graphics graphics = bufferedImage.getGraphics();
/*  72 */     graphics.drawImage(paramBufferedImage1, 0, 0, null);
/*  73 */     graphics.drawImage(paramBufferedImage2, paramArrayOfint[0], paramArrayOfint[1], null);
/*     */     
/*  75 */     graphics.dispose();
/*     */     
/*  77 */     return bufferedImage;
/*     */   }
/*     */   
/*     */   public static BufferedImage writeCenterText(BufferedImage paramBufferedImage, int[] paramArrayOfint, String paramString, Font paramFont) {
/*  81 */     return writeCenterText(paramBufferedImage, paramArrayOfint, paramString, paramFont, Color.BLACK);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BufferedImage writeCenterText(BufferedImage paramBufferedImage, int[] paramArrayOfint, String paramString, Font paramFont, Color paramColor) {
/*  86 */     BufferedImage bufferedImage = copyImage(paramBufferedImage);
/*  87 */     Graphics2D graphics2D = (Graphics2D)bufferedImage.getGraphics();
/*  88 */     graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*  89 */     graphics2D.setFont(paramFont);
/*     */ 
/*     */     
/*  92 */     FontMetrics fontMetrics = graphics2D.getFontMetrics(paramFont);
/*     */ 
/*     */     
/*  95 */     int i = fontMetrics.getLeading() + fontMetrics.getAscent();
/*     */ 
/*     */     
/*  98 */     int j = fontMetrics.stringWidth(paramString);
/*     */ 
/*     */     
/* 101 */     int[] arrayOfInt = { paramArrayOfint[0] - j / 2, paramArrayOfint[1] + i / 2 };
/*     */     
/* 103 */     graphics2D.setColor(paramColor);
/* 104 */     graphics2D.drawString(paramString, arrayOfInt[0], arrayOfInt[1]);
/* 105 */     graphics2D.dispose();
/* 106 */     return bufferedImage;
/*     */   }
/*     */   
/*     */   public static BufferedImage resize(BufferedImage paramBufferedImage, double paramDouble) {
/* 110 */     int i = (int)(paramBufferedImage.getWidth() * paramDouble);
/* 111 */     int j = (int)(paramBufferedImage.getHeight() * paramDouble);
/*     */     
/* 113 */     Image image = paramBufferedImage.getScaledInstance(i, j, 8);
/* 114 */     BufferedImage bufferedImage = new BufferedImage(i, j, 2);
/*     */     
/* 116 */     Graphics2D graphics2D = bufferedImage.createGraphics();
/* 117 */     graphics2D.drawImage(image, 0, 0, (ImageObserver)null);
/* 118 */     graphics2D.dispose();
/*     */     
/* 120 */     return bufferedImage;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\image_handling\ImageUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
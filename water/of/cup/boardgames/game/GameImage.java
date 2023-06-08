/*     */ package water.of.cup.boardgames.game;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.image.BufferedImage;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.image_handling.ImageUtils;
/*     */ 
/*     */ 
/*     */ public class GameImage
/*     */ {
/*     */   BufferedImage image;
/*     */   int rotation;
/*     */   
/*     */   public GameImage(String paramString) {
/*  15 */     setImage(paramString);
/*     */   }
/*     */   
/*     */   public GameImage(String paramString, int paramInt) {
/*  19 */     this(paramString);
/*  20 */     this.rotation = paramInt;
/*     */   }
/*     */   
/*     */   public GameImage(BufferedImage paramBufferedImage, int paramInt) {
/*  24 */     assert paramBufferedImage != null;
/*  25 */     this.image = ImageUtils.copyImage(paramBufferedImage);
/*     */     
/*  27 */     this.rotation = paramInt;
/*     */   }
/*     */   
/*     */   public GameImage combine(GameImage paramGameImage, int[] paramArrayOfint) {
/*  31 */     GameImage gameImage = clone();
/*  32 */     gameImage.addGameImage(paramGameImage, paramArrayOfint);
/*  33 */     return gameImage;
/*     */   }
/*     */   
/*     */   public void setImage(String paramString) {
/*  37 */     BufferedImage bufferedImage = BoardGames.getImageManager().getImage(paramString);
/*  38 */     assert bufferedImage != null;
/*  39 */     this.image = ImageUtils.copyImage(bufferedImage);
/*     */   }
/*     */   
/*     */   public void addGameImage(GameImage paramGameImage, int[] paramArrayOfint) {
/*  43 */     this.image = ImageUtils.combineImages(this.image, paramGameImage.getImage(this.rotation), paramArrayOfint);
/*     */   }
/*     */   
/*     */   public BufferedImage getImage() {
/*  47 */     return getImage(0);
/*     */   }
/*     */   
/*     */   public BufferedImage getImage(int paramInt) {
/*  51 */     int i = (this.rotation + paramInt) % 4;
/*  52 */     return ImageUtils.rotateImage(this.image, i);
/*     */   }
/*     */ 
/*     */   
/*     */   public GameImage clone() {
/*  57 */     return new GameImage(this.image, this.rotation);
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
/*     */   public int[] getDimensions() {
/*  69 */     (new int[2])[0] = this.image.getWidth(); (new int[2])[1] = this.image.getHeight(); (new int[2])[0] = this.image.getHeight(); (new int[2])[1] = this.image.getWidth(); return (this.rotation % 2 == 0) ? new int[2] : new int[2];
/*     */   }
/*     */   
/*     */   public void cropMap(int[] paramArrayOfint) {
/*  73 */     this.image = this.image.getSubimage(paramArrayOfint[0] * 128, paramArrayOfint[1] * 128, 128, 128);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeText(String paramString, int[] paramArrayOfint, int paramInt) {
/*  83 */     Font font = new Font("Serif", 0, paramInt);
/*  84 */     this.image = ImageUtils.writeCenterText(this.image, paramArrayOfint, paramString, font);
/*     */   }
/*     */   
/*     */   public void writeText(String paramString, int[] paramArrayOfint, Font paramFont) {
/*  88 */     this.image = ImageUtils.writeCenterText(this.image, paramArrayOfint, paramString, paramFont);
/*     */   }
/*     */   
/*     */   public void setRotation(int paramInt) {
/*  92 */     this.rotation = paramInt;
/*     */   }
/*     */   
/*     */   public void resize(double paramDouble) {
/*  96 */     this.image = ImageUtils.resize(this.image, paramDouble);
/*     */   }
/*     */   
/*     */   public void rotateSquareImage(double paramDouble) {
/* 100 */     this.image = ImageUtils.rotateSquareImage(this.image, paramDouble);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\GameImage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
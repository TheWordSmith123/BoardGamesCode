/*    */ package water.of.cup.boardgames.game.games.uno;
/*    */ 
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.game.GameImage;
/*    */ 
/*    */ public class UnoCard
/*    */ {
/*    */   private String color;
/*    */   private String type;
/*    */   
/*    */   public UnoCard(String paramString1, String paramString2) {
/* 12 */     this.color = paramString2;
/* 13 */     this.type = paramString1;
/*    */   }
/*    */   
/*    */   public String getColor() {
/* 17 */     return this.color;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 21 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setColor(String paramString) {
/* 25 */     this.color = paramString;
/*    */   }
/*    */   
/*    */   public GameImage getGameImage() {
/* 29 */     GameImage gameImage = new GameImage(BoardGames.getImageManager().getImage("UNOCARD_" + this.color), 0);
/* 30 */     gameImage.addGameImage(new GameImage(BoardGames.getImageManager().getImage("UNOCARD_" + this.type), 0), new int[] { 0, 0 });
/* 31 */     return gameImage;
/*    */   }
/*    */   
/*    */   public boolean matches(UnoCard paramUnoCard) {
/* 35 */     if (this.color.equals("ALL") || paramUnoCard.getColor().equals(this.color))
/* 36 */       return true; 
/* 37 */     if (this.type.equals(paramUnoCard.getType()))
/* 38 */       return true; 
/* 39 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getActions() {
/* 48 */     if (this.type.equals("DRAW2")) {
/* 49 */       return new String[] { "DRAW2", "SKIP" };
/*    */     }
/* 51 */     if (this.type.equals("REVERSE")) {
/* 52 */       return new String[] { "REVERSE" };
/*    */     }
/* 54 */     if (this.type.equals("SKIP")) {
/* 55 */       return new String[] { "SKIP" };
/*    */     }
/* 57 */     if (this.type.equals("WILD")) {
/* 58 */       return new String[] { "WILD" };
/*    */     }
/* 60 */     if (this.type.equals("WILDDRAW4")) {
/* 61 */       return new String[] { "WILD", "DRAW4", "SKIP" };
/*    */     }
/*    */     
/* 64 */     return new String[0];
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\game\\uno\UnoCard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
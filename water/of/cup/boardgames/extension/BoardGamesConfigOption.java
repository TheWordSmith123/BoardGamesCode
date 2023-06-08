/*    */ package water.of.cup.boardgames.extension;
/*    */ 
/*    */ import water.of.cup.boardgames.config.ConfigInterface;
/*    */ 
/*    */ public class BoardGamesConfigOption
/*    */   implements ConfigInterface {
/*    */   private final String path;
/*    */   private final String defaultValue;
/*    */   
/*    */   public BoardGamesConfigOption(String paramString1, String paramString2) {
/* 11 */     this.path = paramString1;
/* 12 */     this.defaultValue = paramString2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPath() {
/* 17 */     return this.path;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDefaultValue() {
/* 22 */     return this.defaultValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\extension\BoardGamesConfigOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
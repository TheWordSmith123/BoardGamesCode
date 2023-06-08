/*    */ package water.of.cup.boardgames.game;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import water.of.cup.boardgames.config.GameRecipe;
/*    */ import water.of.cup.boardgames.config.GameSound;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GameConfig
/*    */ {
/*    */   protected final Game game;
/*    */   
/*    */   protected abstract GameRecipe getGameRecipe();
/*    */   
/*    */   public GameConfig(Game paramGame) {
/* 19 */     this.game = paramGame;
/*    */   }
/*    */   
/*    */   protected abstract ArrayList<GameSound> getGameSounds();
/*    */   
/*    */   protected abstract HashMap<String, Object> getCustomValues();
/*    */   
/*    */   protected abstract int getWinAmount();
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\GameConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
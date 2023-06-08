/*    */ package water.of.cup.boardgames.game.games.conways_game_of_life;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.Material;
/*    */ import water.of.cup.boardgames.config.GameRecipe;
/*    */ import water.of.cup.boardgames.config.GameSound;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.GameConfig;
/*    */ 
/*    */ 
/*    */ public class ConwaysGameOfLifeConfig
/*    */   extends GameConfig
/*    */ {
/*    */   public ConwaysGameOfLifeConfig(Game paramGame) {
/* 16 */     super(paramGame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected GameRecipe getGameRecipe() {
/* 21 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*    */     
/* 23 */     hashMap.put("Q", Material.QUARTZ.toString());
/* 24 */     hashMap.put("H", Material.WITHER_SKELETON_SKULL.toString());
/*    */     
/* 26 */     ArrayList<String> arrayList = new ArrayList<String>()
/*    */       {
/*    */       
/*    */       };
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     return new GameRecipe(this.game.getName(), hashMap, arrayList);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ArrayList<GameSound> getGameSounds() {
/* 39 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected HashMap<String, Object> getCustomValues() {
/* 44 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getWinAmount() {
/* 49 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\conways_game_of_life\ConwaysGameOfLifeConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
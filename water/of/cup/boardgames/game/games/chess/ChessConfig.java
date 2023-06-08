/*    */ package water.of.cup.boardgames.game.games.chess;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.Sound;
/*    */ import water.of.cup.boardgames.config.GameRecipe;
/*    */ import water.of.cup.boardgames.config.GameSound;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.GameConfig;
/*    */ 
/*    */ public class ChessConfig
/*    */   extends GameConfig {
/*    */   public ChessConfig(Game paramGame) {
/* 15 */     super(paramGame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected GameRecipe getGameRecipe() {
/* 20 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 21 */     hashMap.put("B", Material.BLACK_DYE.toString());
/* 22 */     hashMap.put("W", Material.WHITE_DYE.toString());
/* 23 */     hashMap.put("L", Material.LEATHER.toString());
/* 24 */     hashMap.put("Q", Material.QUARTZ.toString());
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
/* 39 */     ArrayList<GameSound> arrayList = new ArrayList();
/* 40 */     arrayList.add(new GameSound("click", Sound.BLOCK_WOOD_PLACE));
/* 41 */     return arrayList;
/*    */   }
/*    */ 
/*    */   
/*    */   protected HashMap<String, Object> getCustomValues() {
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getWinAmount() {
/* 51 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\chess\ChessConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package water.of.cup.boardgames.game.games.minesweaper;
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
/*    */ public class MineSweeperConfig
/*    */   extends GameConfig {
/*    */   public MineSweeperConfig(Game paramGame) {
/* 15 */     super(paramGame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected GameRecipe getGameRecipe() {
/* 20 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 21 */     hashMap.put("S", Material.SAND.toString());
/* 22 */     hashMap.put("T", Material.TNT.toString());
/* 23 */     hashMap.put("L", Material.LEATHER.toString());
/*    */     
/* 25 */     ArrayList<String> arrayList = new ArrayList<String>()
/*    */       {
/*    */       
/*    */       };
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 33 */     return new GameRecipe(this.game.getName(), hashMap, arrayList);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ArrayList<GameSound> getGameSounds() {
/* 38 */     ArrayList<GameSound> arrayList = new ArrayList();
/* 39 */     arrayList.add(new GameSound("click", Sound.BLOCK_WOOD_PLACE));
/* 40 */     return arrayList;
/*    */   }
/*    */ 
/*    */   
/*    */   protected HashMap<String, Object> getCustomValues() {
/* 45 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getWinAmount() {
/* 50 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\minesweaper\MineSweeperConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
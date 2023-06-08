/*    */ package water.of.cup.boardgames.game.games.uno;
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
/*    */ public class UnoConfig
/*    */   extends GameConfig {
/*    */   public UnoConfig(Game paramGame) {
/* 15 */     super(paramGame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected GameRecipe getGameRecipe() {
/* 20 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 21 */     hashMap.put("B", Material.BLUE_DYE.toString());
/* 22 */     hashMap.put("G", Material.GREEN_DYE.toString());
/* 23 */     hashMap.put("R", Material.RED_DYE.toString());
/* 24 */     hashMap.put("Y", Material.YELLOW_DYE.toString());
/* 25 */     hashMap.put("L", Material.LEATHER.toString());
/* 26 */     hashMap.put("I", Material.INK_SAC.toString());
/* 27 */     hashMap.put("P", Material.PAPER.toString());
/*    */     
/* 29 */     ArrayList<String> arrayList = new ArrayList<String>()
/*    */       {
/*    */       
/*    */       };
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 37 */     return new GameRecipe(this.game.getName(), hashMap, arrayList);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ArrayList<GameSound> getGameSounds() {
/* 42 */     ArrayList<GameSound> arrayList = new ArrayList();
/* 43 */     arrayList.add(new GameSound("click", Sound.BLOCK_WOOD_PLACE));
/* 44 */     return arrayList;
/*    */   }
/*    */ 
/*    */   
/*    */   protected HashMap<String, Object> getCustomValues() {
/* 49 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 50 */     hashMap.put("middle_card_size", Integer.valueOf(2));
/* 51 */     return (HashMap)hashMap;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getWinAmount() {
/* 56 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\game\\uno\UnoConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
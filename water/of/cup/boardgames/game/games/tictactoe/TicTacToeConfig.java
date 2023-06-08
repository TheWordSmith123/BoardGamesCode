/*    */ package water.of.cup.boardgames.game.games.tictactoe;
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
/*    */ public class TicTacToeConfig
/*    */   extends GameConfig
/*    */ {
/*    */   public TicTacToeConfig(Game paramGame) {
/* 16 */     super(paramGame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected GameRecipe getGameRecipe() {
/* 21 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 22 */     hashMap.put("P", Material.PAPER.toString());
/* 23 */     hashMap.put("B", Material.BLUE_DYE.toString());
/* 24 */     hashMap.put("R", Material.RED_DYE.toString());
/* 25 */     hashMap.put("L", Material.LEATHER.toString());
/*    */     
/* 27 */     ArrayList<String> arrayList = new ArrayList<String>()
/*    */       {
/*    */       
/*    */       };
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     return new GameRecipe(this.game.getName(), hashMap, arrayList);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ArrayList<GameSound> getGameSounds() {
/* 40 */     ArrayList<GameSound> arrayList = new ArrayList();
/* 41 */     arrayList.add(new GameSound("click", Sound.BLOCK_WOOD_PLACE));
/* 42 */     return arrayList;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected HashMap<String, Object> getCustomValues() {
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getWinAmount() {
/* 56 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\tictactoe\TicTacToeConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
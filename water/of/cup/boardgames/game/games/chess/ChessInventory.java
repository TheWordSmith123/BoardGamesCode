/*    */ package water.of.cup.boardgames.game.games.chess;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import org.bukkit.Material;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ import water.of.cup.boardgames.game.GamePlayer;
/*    */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*    */ import water.of.cup.boardgames.game.inventories.GameOption;
/*    */ import water.of.cup.boardgames.game.inventories.GameOptionType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChessInventory
/*    */   extends GameInventory
/*    */ {
/*    */   private final Chess game;
/*    */   
/*    */   public ChessInventory(Chess paramChess) {
/* 23 */     super(paramChess);
/* 24 */     this.game = paramChess;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ArrayList<GameOption> getOptions() {
/* 29 */     ArrayList<GameOption> arrayList = new ArrayList();
/*    */     
/* 31 */     List<String> list1 = Arrays.asList(new String[] { ConfigUtil.GUI_RANKED_OPTION_TEXT.toString(), ConfigUtil.GUI_UNRANKED_OPTION_TEXT.toString() });
/* 32 */     GameOption gameOption1 = new GameOption("ranked", Material.EXPERIENCE_BOTTLE, GameOptionType.TOGGLE, null, list1.get(0), list1);
/*    */     
/* 34 */     if (ConfigUtil.getBoolean("settings.games.Chess.database.rating")) {
/* 35 */       arrayList.add(gameOption1);
/*    */     }
/* 37 */     List<String> list2 = Arrays.asList(new String[] { "1 min", "1 | 1", "2 | 1", "3 min", "3 | 2", "5 min", "10 min", "30 min", "60 min" });
/*    */     
/* 39 */     GameOption gameOption2 = new GameOption("time", Material.LEATHER, GameOptionType.COUNT, null, list2.get(0), list2);
/* 40 */     arrayList.add(gameOption2);
/*    */     
/* 42 */     return arrayList;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getMaxQueue() {
/* 47 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getMaxGame() {
/* 52 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getMinGame() {
/* 57 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasTeamSelect() {
/* 62 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasGameWagers() {
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasWagerScreen() {
/* 72 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasForfeitScreen() {
/* 77 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onGameCreate(HashMap<String, Object> paramHashMap, ArrayList<GamePlayer> paramArrayList) {
/* 82 */     for (GamePlayer gamePlayer : paramArrayList) {
/* 83 */       gamePlayer.getPlayer().sendMessage(ConfigUtil.CHAT_WELCOME_GAME.buildString(this.game.getAltName()));
/*    */     }
/*    */     
/* 86 */     this.game.startGame();
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\chess\ChessInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
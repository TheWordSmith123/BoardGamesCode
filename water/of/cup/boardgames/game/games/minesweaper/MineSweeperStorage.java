/*    */ package water.of.cup.boardgames.game.games.minesweaper;
/*    */ 
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.storage.BoardGamesStorageType;
/*    */ import water.of.cup.boardgames.game.storage.GameStorage;
/*    */ import water.of.cup.boardgames.game.storage.StorageType;
/*    */ 
/*    */ public class MineSweeperStorage extends GameStorage {
/*    */   public MineSweeperStorage(Game paramGame) {
/* 10 */     super(paramGame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTableName() {
/* 15 */     return "minesweeper";
/*    */   }
/*    */ 
/*    */   
/*    */   protected StorageType[] getGameStores() {
/* 20 */     return new StorageType[] { (StorageType)BoardGamesStorageType.BEST_TIME, (StorageType)BoardGamesStorageType.WINS };
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\minesweaper\MineSweeperStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
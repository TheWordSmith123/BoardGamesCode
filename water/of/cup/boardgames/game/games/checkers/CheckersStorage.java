/*    */ package water.of.cup.boardgames.game.games.checkers;
/*    */ 
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.storage.BoardGamesStorageType;
/*    */ import water.of.cup.boardgames.game.storage.GameStorage;
/*    */ import water.of.cup.boardgames.game.storage.StorageType;
/*    */ 
/*    */ public class CheckersStorage extends GameStorage {
/*    */   public CheckersStorage(Game paramGame) {
/* 10 */     super(paramGame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTableName() {
/* 15 */     return "checkers";
/*    */   }
/*    */ 
/*    */   
/*    */   protected StorageType[] getGameStores() {
/* 20 */     return new StorageType[] { (StorageType)BoardGamesStorageType.WINS, (StorageType)BoardGamesStorageType.LOSSES, (StorageType)BoardGamesStorageType.TIES };
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\checkers\CheckersStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
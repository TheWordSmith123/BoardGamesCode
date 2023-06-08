/*    */ package water.of.cup.boardgames.game.games.connectfour;
/*    */ 
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.storage.BoardGamesStorageType;
/*    */ import water.of.cup.boardgames.game.storage.GameStorage;
/*    */ import water.of.cup.boardgames.game.storage.StorageType;
/*    */ 
/*    */ public class ConnectFourStorage extends GameStorage {
/*    */   public ConnectFourStorage(Game paramGame) {
/* 10 */     super(paramGame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTableName() {
/* 15 */     return "connectfour";
/*    */   }
/*    */ 
/*    */   
/*    */   protected StorageType[] getGameStores() {
/* 20 */     return new StorageType[] { (StorageType)BoardGamesStorageType.WINS, (StorageType)BoardGamesStorageType.LOSSES, (StorageType)BoardGamesStorageType.TIES, (StorageType)BoardGamesStorageType.CROSS_WINS };
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\connectfour\ConnectFourStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
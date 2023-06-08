/*    */ package water.of.cup.boardgames.game.games.chess;
/*    */ 
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.storage.BoardGamesStorageType;
/*    */ import water.of.cup.boardgames.game.storage.GameStorage;
/*    */ import water.of.cup.boardgames.game.storage.StorageType;
/*    */ 
/*    */ public class ChessStorage
/*    */   extends GameStorage {
/*    */   public ChessStorage(Game paramGame) {
/* 11 */     super(paramGame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTableName() {
/* 16 */     return "chess";
/*    */   }
/*    */ 
/*    */   
/*    */   protected StorageType[] getGameStores() {
/* 21 */     return new StorageType[] { (StorageType)BoardGamesStorageType.WINS, (StorageType)BoardGamesStorageType.LOSSES, (StorageType)BoardGamesStorageType.TIES, (StorageType)BoardGamesStorageType.Rating, (StorageType)BoardGamesStorageType.RatingDeviation, (StorageType)BoardGamesStorageType.RatingVolatility };
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\chess\ChessStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
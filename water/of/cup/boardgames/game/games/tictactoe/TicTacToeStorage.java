/*    */ package water.of.cup.boardgames.game.games.tictactoe;
/*    */ 
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.storage.BoardGamesStorageType;
/*    */ import water.of.cup.boardgames.game.storage.GameStorage;
/*    */ import water.of.cup.boardgames.game.storage.StorageType;
/*    */ 
/*    */ public class TicTacToeStorage
/*    */   extends GameStorage {
/*    */   public TicTacToeStorage(Game paramGame) {
/* 11 */     super(paramGame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTableName() {
/* 16 */     return "tictactoe";
/*    */   }
/*    */ 
/*    */   
/*    */   protected StorageType[] getGameStores() {
/* 21 */     return new StorageType[] { (StorageType)BoardGamesStorageType.WINS, (StorageType)BoardGamesStorageType.LOSSES, (StorageType)BoardGamesStorageType.TIES };
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\tictactoe\TicTacToeStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
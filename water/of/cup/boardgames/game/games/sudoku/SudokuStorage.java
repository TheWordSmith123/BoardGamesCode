/*    */ package water.of.cup.boardgames.game.games.sudoku;
/*    */ 
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.storage.BoardGamesStorageType;
/*    */ import water.of.cup.boardgames.game.storage.GameStorage;
/*    */ import water.of.cup.boardgames.game.storage.StorageType;
/*    */ 
/*    */ public class SudokuStorage extends GameStorage {
/*    */   public SudokuStorage(Game paramGame) {
/* 10 */     super(paramGame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTableName() {
/* 15 */     return "sudoku";
/*    */   }
/*    */ 
/*    */   
/*    */   protected StorageType[] getGameStores() {
/* 20 */     return new StorageType[] { (StorageType)BoardGamesStorageType.BEST_TIME, (StorageType)BoardGamesStorageType.WINS, (StorageType)BoardGamesStorageType.LOSSES };
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\sudoku\SudokuStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
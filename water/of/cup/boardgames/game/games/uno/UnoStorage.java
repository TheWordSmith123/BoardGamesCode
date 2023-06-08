/*    */ package water.of.cup.boardgames.game.games.uno;
/*    */ 
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.storage.BoardGamesStorageType;
/*    */ import water.of.cup.boardgames.game.storage.GameStorage;
/*    */ import water.of.cup.boardgames.game.storage.StorageType;
/*    */ 
/*    */ public class UnoStorage extends GameStorage {
/*    */   public UnoStorage(Game paramGame) {
/* 10 */     super(paramGame);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTableName() {
/* 15 */     return "uno";
/*    */   }
/*    */ 
/*    */   
/*    */   protected StorageType[] getGameStores() {
/* 20 */     return new StorageType[] { (StorageType)BoardGamesStorageType.WINS, (StorageType)BoardGamesStorageType.LOSSES };
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\game\\uno\UnoStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package water.of.cup.boardgames.placeholder;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import me.clip.placeholderapi.expansion.PlaceholderExpansion;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.storage.StorageType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BoardGamesPlaceholder
/*    */   extends PlaceholderExpansion
/*    */ {
/*    */   private BoardGames plugin;
/*    */   
/*    */   public BoardGamesPlaceholder(BoardGames paramBoardGames) {
/* 19 */     this.plugin = paramBoardGames;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean persist() {
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canRegister() {
/* 29 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAuthor() {
/* 34 */     return this.plugin.getDescription().getAuthors().toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getIdentifier() {
/* 39 */     return "boards";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getVersion() {
/* 44 */     return this.plugin.getDescription().getVersion();
/*    */   }
/*    */ 
/*    */   
/*    */   public String onPlaceholderRequest(Player paramPlayer, String paramString) {
/* 49 */     if (paramPlayer == null || !this.plugin.hasStorage()) {
/* 50 */       return "";
/*    */     }
/*    */     
/* 53 */     String[] arrayOfString = paramString.split("_");
/* 54 */     if (arrayOfString.length != 3) return null;
/*    */ 
/*    */     
/* 57 */     if (arrayOfString[0].equals("stats")) {
/* 58 */       String str1 = arrayOfString[1];
/* 59 */       String str2 = arrayOfString[2];
/*    */       
/* 61 */       Game game = this.plugin.getGameManager().newGame(str1, 0);
/*    */       
/* 63 */       if (game == null) {
/* 64 */         return null;
/*    */       }
/*    */       
/* 67 */       if (!game.hasGameStorage()) {
/* 68 */         return null;
/*    */       }
/*    */       
/* 71 */       if (!game.getGameStore().hasStorageType(str2)) {
/* 72 */         return null;
/*    */       }
/*    */       
/* 75 */       StorageType storageType = BoardGames.getInstance().getStorageManager().getStorageTypeByKey(str2);
/* 76 */       if (storageType == null) {
/* 77 */         return null;
/*    */       }
/*    */       
/* 80 */       LinkedHashMap linkedHashMap = this.plugin.getStorageManager().fetchPlayerStats((OfflinePlayer)paramPlayer, game.getGameStore(), true);
/* 81 */       if (linkedHashMap == null) return null; 
/* 82 */       if (linkedHashMap.get(storageType) == null) return null;
/*    */       
/* 84 */       return (new StringBuilder()).append(linkedHashMap.get(storageType)).append("").toString();
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 89 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\placeholder\BoardGamesPlaceholder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
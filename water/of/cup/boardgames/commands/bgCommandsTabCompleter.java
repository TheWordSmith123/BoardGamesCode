/*    */ package water.of.cup.boardgames.commands;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.command.TabCompleter;
/*    */ import org.bukkit.entity.Player;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.GameManager;
/*    */ import water.of.cup.boardgames.game.storage.GameStorage;
/*    */ import water.of.cup.boardgames.game.storage.StorageType;
/*    */ 
/*    */ public class bgCommandsTabCompleter
/*    */   implements TabCompleter
/*    */ {
/* 21 */   private final BoardGames instance = BoardGames.getInstance();
/* 22 */   private final GameManager gameManager = this.instance.getGameManager();
/*    */ 
/*    */   
/*    */   public List<String> onTabComplete(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
/* 26 */     ArrayList<String> arrayList1 = new ArrayList();
/* 27 */     boolean bool = ConfigUtil.PERMISSIONS_ENABLED.toBoolean();
/* 28 */     if (paramArrayOfString.length == 1) {
/* 29 */       if (bool) {
/* 30 */         for (String str1 : bgCommands.ARG_PERMS.keySet()) {
/* 31 */           if (paramCommandSender.hasPermission(bgCommands.ARG_PERMS.get(str1)))
/* 32 */             arrayList1.add(str1); 
/*    */         } 
/*    */       } else {
/* 35 */         Collections.addAll(bgCommands.ARG_PERMS.keySet(), new String[0]);
/*    */       } 
/* 37 */     } else if (paramArrayOfString.length == 2) {
/* 38 */       if (paramArrayOfString[0].equalsIgnoreCase("board")) {
/* 39 */         Collections.addAll(arrayList1, (String[])this.gameManager.getAltGameNames().toArray((Object[])new String[0]));
/* 40 */       } else if (paramArrayOfString[0].equalsIgnoreCase("leaderboard") || paramArrayOfString[0].equalsIgnoreCase("stats")) {
/*    */         
/* 42 */         for (String str1 : this.gameManager.getGameNames()) {
/* 43 */           Game game = this.instance.getGameManager().newGame(str1, 0);
/*    */           
/* 45 */           if (game != null && game.hasGameStorage()) {
/* 46 */             arrayList1.add(game.getAltName());
/*    */           }
/*    */         } 
/*    */       } 
/* 50 */     } else if (paramArrayOfString.length == 3) {
/* 51 */       if (paramArrayOfString[0].equalsIgnoreCase("leaderboard")) {
/* 52 */         String str1 = paramArrayOfString[1];
/* 53 */         Game game = this.gameManager.newGame(str1, 0);
/*    */         
/* 55 */         if (game != null) {
/* 56 */           GameStorage gameStorage = game.getGameStore();
/* 57 */           for (StorageType storageType : gameStorage.getStorageTypes()) {
/* 58 */             arrayList1.add(storageType.getKey());
/*    */           }
/*    */         } 
/* 61 */       } else if (paramArrayOfString[0].equalsIgnoreCase("stats")) {
/* 62 */         for (Player player : Bukkit.getOnlinePlayers()) {
/* 63 */           arrayList1.add(player.getName());
/*    */         }
/*    */       } 
/* 66 */     } else if (paramArrayOfString.length == 4 && 
/* 67 */       paramArrayOfString[0].equalsIgnoreCase("leaderboard")) {
/* 68 */       String str1 = paramArrayOfString[1];
/* 69 */       Game game = this.gameManager.newGame(str1, 0);
/*    */       
/* 71 */       if (game != null) {
/* 72 */         GameStorage gameStorage = game.getGameStore();
/* 73 */         int i = this.instance.getStorageManager().getGamePlayerTotal(gameStorage) / 10;
/* 74 */         for (byte b = 0; b < i; b++) {
/* 75 */           arrayList1.add((b + 2) + "");
/*    */         }
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 81 */     ArrayList<String> arrayList2 = new ArrayList();
/* 82 */     String str = paramArrayOfString[paramArrayOfString.length - 1];
/* 83 */     for (String str1 : arrayList1) {
/* 84 */       if (str1.startsWith(str))
/* 85 */         arrayList2.add(str1); 
/*    */     } 
/* 87 */     return arrayList2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\commands\bgCommandsTabCompleter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package water.of.cup.boardgames.commands;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.GameManager;
/*     */ import water.of.cup.boardgames.game.storage.GameStorage;
/*     */ import water.of.cup.boardgames.game.storage.StorageType;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class bgCommands
/*     */   implements CommandExecutor
/*     */ {
/*  25 */   private final BoardGames instance = BoardGames.getInstance();
/*  26 */   private final GameManager gameManager = this.instance.getGameManager();
/*     */ 
/*     */   
/*  29 */   public static final HashMap<String, String> ARG_PERMS = new HashMap<>();
/*     */   
/*     */   static {
/*  32 */     ARG_PERMS.put("games", "boardgames.command.games");
/*  33 */     ARG_PERMS.put("board", "boardgames.command.board");
/*  34 */     ARG_PERMS.put("stats", "boardgames.command.stats");
/*  35 */     ARG_PERMS.put("leaderboard", "boardgames.command.leaderboard");
/*  36 */     ARG_PERMS.put("reload", "boardgames.command.reload");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
/*  42 */     if (!(paramCommandSender instanceof Player)) {
/*  43 */       return true;
/*     */     }
/*     */     
/*  46 */     Player player = (Player)paramCommandSender;
/*  47 */     boolean bool = ConfigUtil.PERMISSIONS_ENABLED.toBoolean();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     if (paramCommand.getName().equalsIgnoreCase("bg")) {
/*  53 */       if (bool && !player.hasPermission("boardgames.command")) {
/*  54 */         return false;
/*     */       }
/*  56 */       if (paramArrayOfString.length == 0) {
/*  57 */         sendHelpMessage(player);
/*  58 */         return true;
/*     */       } 
/*     */       
/*  61 */       if (bool && ARG_PERMS.containsKey(paramArrayOfString[0].toLowerCase(Locale.ROOT)) && !player.hasPermission(ARG_PERMS.get(paramArrayOfString[0].toLowerCase(Locale.ROOT)))) {
/*  62 */         return false;
/*     */       }
/*  64 */       if (paramArrayOfString[0].equalsIgnoreCase("games")) {
/*  65 */         player.sendMessage(ConfigUtil.CHAT_GAME_NAMES.toString());
/*  66 */         for (String str : this.instance.getGameManager().getAltGameNames()) {
/*  67 */           player.sendMessage(str);
/*     */         }
/*     */       }
/*  70 */       else if (paramArrayOfString[0].equalsIgnoreCase("board")) {
/*  71 */         if (paramArrayOfString.length == 2) {
/*  72 */           String str = this.gameManager.getGameNameByAlt(paramArrayOfString[1]);
/*  73 */           Game game = this.gameManager.newGame(str, 0);
/*  74 */           if (game != null)
/*  75 */             player.getWorld().dropItem(player.getLocation(), game.getBoardItem()); 
/*     */         } 
/*     */       } else {
/*  78 */         if (paramArrayOfString[0].equalsIgnoreCase("stats")) {
/*  79 */           if (paramArrayOfString.length != 3) {
/*  80 */             sendHelpMessage(player);
/*  81 */             return false;
/*     */           } 
/*     */           
/*  84 */           if (!this.instance.hasStorage()) {
/*  85 */             player.sendMessage(ConfigUtil.CHAT_NO_DB.toString());
/*  86 */             return false;
/*     */           } 
/*     */           
/*  89 */           String str1 = this.gameManager.getGameNameByAlt(paramArrayOfString[1]);
/*  90 */           String str2 = paramArrayOfString[2];
/*     */           
/*  92 */           Game game = this.gameManager.newGame(str1, 0);
/*  93 */           OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(str2);
/*     */           
/*  95 */           if (!offlinePlayer.hasPlayedBefore()) {
/*  96 */             player.sendMessage(ConfigUtil.CHAT_NO_PLAYER.toString());
/*  97 */             return false;
/*     */           } 
/*     */           
/* 100 */           if (game == null) {
/* 101 */             player.sendMessage(ConfigUtil.CHAT_NO_GAME.toString());
/* 102 */             return false;
/*     */           } 
/*     */           
/* 105 */           if (!game.hasGameStorage()) {
/* 106 */             player.sendMessage(ConfigUtil.CHAT_NO_DB.toString());
/* 107 */             return false;
/*     */           } 
/*     */           
/* 110 */           LinkedHashMap linkedHashMap = this.instance.getStorageManager().fetchPlayerStats(offlinePlayer, game.getGameStore(), true);
/* 111 */           if (linkedHashMap == null) {
/* 112 */             player.sendMessage(ConfigUtil.CHAT_NO_PLAYER.toString());
/* 113 */             return false;
/*     */           } 
/*     */           
/* 116 */           player.sendMessage(ConfigUtil.CHAT_STATS_HEADER.buildStringPlayerGame(offlinePlayer.getName(), game.getAltName()));
/* 117 */           for (StorageType storageType : linkedHashMap.keySet()) {
/* 118 */             player.sendMessage(ConfigUtil.CHAT_STATS_FORMAT.buildStatsFormat(StringUtils.capitalize(storageType.getKey()), (new StringBuilder()).append(linkedHashMap.get(storageType)).append("").toString()));
/*     */           }
/*     */           
/* 121 */           return true;
/* 122 */         }  if (paramArrayOfString[0].equalsIgnoreCase("leaderboard")) {
/* 123 */           if (paramArrayOfString.length < 2) {
/* 124 */             sendHelpMessage(player);
/* 125 */             return false;
/*     */           } 
/*     */           
/* 128 */           if (!this.instance.hasStorage()) {
/* 129 */             player.sendMessage(ConfigUtil.CHAT_NO_DB.toString());
/* 130 */             return false;
/*     */           } 
/*     */           
/* 133 */           String str = this.gameManager.getGameNameByAlt(paramArrayOfString[1]);
/* 134 */           Game game = this.gameManager.newGame(str, 0);
/*     */           
/* 136 */           if (game == null) {
/* 137 */             player.sendMessage(ConfigUtil.CHAT_NO_GAME.toString());
/* 138 */             return false;
/*     */           } 
/*     */           
/* 141 */           if (!game.hasGameStorage()) {
/* 142 */             player.sendMessage(ConfigUtil.CHAT_NO_DB.toString());
/* 143 */             return false;
/*     */           } 
/*     */           
/* 146 */           GameStorage gameStorage = game.getGameStore();
/* 147 */           StorageType storageType = gameStorage.getStorageTypes().get(0);
/*     */           
/* 149 */           if (paramArrayOfString.length > 2) {
/* 150 */             String str1 = paramArrayOfString[2];
/* 151 */             StorageType storageType1 = this.instance.getStorageManager().getStorageTypeByKey(str1);
/* 152 */             if (storageType1 != null) {
/* 153 */               storageType = storageType1;
/*     */             }
/*     */           } 
/* 156 */           int i = this.instance.getStorageManager().getGamePlayerTotal(gameStorage);
/*     */           
/* 158 */           int j = 0;
/* 159 */           if (paramArrayOfString.length > 3) {
/*     */             try {
/* 161 */               j = Integer.parseInt(paramArrayOfString[3]) - 1;
/* 162 */             } catch (NumberFormatException numberFormatException) {}
/*     */           }
/*     */ 
/*     */           
/* 166 */           if (j < 0) {
/* 167 */             j = 0;
/*     */           }
/* 169 */           int k = i / 10 + 1;
/*     */           
/* 171 */           if (j > k - 1) {
/* 172 */             j = k - 1;
/*     */           }
/* 174 */           LinkedHashMap linkedHashMap = this.instance.getStorageManager().fetchTopPlayers(gameStorage, storageType, j);
/*     */           
/* 176 */           if (linkedHashMap == null) {
/* 177 */             player.sendMessage(ConfigUtil.CHAT_DB_ERROR.toString());
/* 178 */             return false;
/*     */           } 
/*     */           
/* 181 */           int m = 1 + j * 10;
/*     */           
/* 183 */           player.sendMessage(ConfigUtil.CHAT_LEADERBOARD_HEADER.buildString(game.getAltName(), storageType.getKey()) + " (" + (j + 1) + "/" + k + ")");
/*     */           
/* 185 */           for (OfflinePlayer offlinePlayer : linkedHashMap.keySet()) {
/* 186 */             player.sendMessage(ConfigUtil.CHAT_LEADERBOARD_FORMAT.buildLeaderBoardFormat(m, offlinePlayer.getName(), (new StringBuilder()).append(((LinkedHashMap)linkedHashMap.get(offlinePlayer)).get(storageType)).append("").toString()));
/* 187 */             m++;
/*     */           } 
/* 189 */         } else if (paramArrayOfString[0].equalsIgnoreCase("reload")) {
/* 190 */           player.sendMessage(ConfigUtil.CHAT_RELOAD.toString());
/*     */           
/* 192 */           this.instance.loadConfig();
/*     */         } 
/* 194 */       }  return true;
/*     */     } 
/*     */     
/* 197 */     return false;
/*     */   }
/*     */   
/*     */   private void sendHelpMessage(Player paramPlayer) {
/* 201 */     paramPlayer.sendMessage(ConfigUtil.CHAT_AVAIL_COMMANDS.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\commands\bgCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
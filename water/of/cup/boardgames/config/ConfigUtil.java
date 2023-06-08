/*     */ package water.of.cup.boardgames.config;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.ChatColor;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.game.MathUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ConfigUtil
/*     */   implements ConfigInterface
/*     */ {
/*  14 */   PERMISSIONS_ENABLED("settings.permissions", "true"),
/*  15 */   WAGERS_ENABLED("settings.wagers", "true"),
/*  16 */   ITEM_WAGERS_ENABLED("settings.itemwagers", "true"),
/*  17 */   RECIPE_ENABLED("settings.recipe.enabled", "true"),
/*  18 */   RECIPE_AUTO_DISCOVER_ENABLED("settings.recipe.autodiscover", "true"),
/*  19 */   DB_HOST("settings.database.host", "localhost"),
/*  20 */   DB_PORT("settings.database.port", "3306"),
/*  21 */   DB_NAME("settings.database.database", "boardgames"),
/*  22 */   DB_USERNAME("settings.database.username", "root"),
/*  23 */   DB_PASS("settings.database.password", " "),
/*  24 */   DB_ENABLED("settings.database.enabled", "false"),
/*  25 */   DB_TRANSFERRED("settings.database.chesstransfer", "false"),
/*  26 */   BOARD_CLICK_DELAY("settings.clickdelay", "0"),
/*  27 */   LOAD_SKULLS("settings.loadskulls", "true"),
/*  28 */   PLAYER_DISTANCE_AMOUNT("settings.distance.amount", "5"),
/*  29 */   PLAYER_DISTANCE_TIME("settings.distance.time", "5000"),
/*     */ 
/*     */   
/*  32 */   GUI_NEXT_PAGE("settings.messages.gui.nextpage", "&aNext Page"),
/*  33 */   GUI_CREATE_GAME("settings.messages.gui.creategame", "&aCreate Game"),
/*  34 */   GUI_ACCEPT_PLAYER("settings.messages.gui.acceptplayer", "&2LEFT CLICK - ACCEPT"),
/*  35 */   GUI_DECLINE_PLAYER("settings.messages.gui.declineplayer", "&4RIGHT CLICK - DECLINE"),
/*  36 */   GUI_START_GAME_WITH("settings.messages.gui.startgamewith", "&aStart game with %num% players"),
/*  37 */   GUI_FORFEIT_GAME("settings.messages.gui.forfeitgame", "&cForfeit Game"),
/*  38 */   GUI_WAIT_CREATOR("settings.messages.gui.waitcreator", "Waiting for game creator"),
/*  39 */   GUI_WAIT_PLAYERS("settings.messages.gui.waitplayers", "Waiting for more players"),
/*  40 */   GUI_JOIN_GAME("settings.messages.gui.joingame", "&aJoin Game"),
/*  41 */   GUI_LEAVE_GAME("settings.messages.gui.leavegame", "&cLeave Game"),
/*  42 */   GUI_READY_TEXT("settings.messages.gui.readytext", "&a&lREADY"),
/*  43 */   GUI_UNREADY_TEXT("settings.messages.gui.unreadytext", "&c&lCLICK TO READY"),
/*  44 */   GUI_NOT_READY_TEXT("settings.messages.gui.notreadytext", "&c&lNOT READY"),
/*  45 */   GUI_WAGER_NEXT("settings.messages.gui.wagernext", "Next"),
/*  46 */   GUI_WAGER_BACK("settings.messages.gui.wagerback", "Back"),
/*  47 */   GUI_WAGER_CANCEL("settings.messages.gui.wagercancel", "&cCancel Wager"),
/*  48 */   GUI_WAGER_CREATE("settings.messages.gui.wagercreate", "&aCreate Wager"),
/*  49 */   GUI_WAGER_ACCEPT("settings.messages.gui.wageraccept", "&aAccept Wager"),
/*  50 */   GUI_WAGER_DECLINE("settings.messages.gui.wagerdecline", "&cDecline Wager"),
/*  51 */   GUI_WAGER_NO_MONEY_CREATE("settings.messages.gui.wagernomoneycreate", "&cNot enough money to create wager"),
/*  52 */   GUI_WAGER_NO_MONEY_ACCEPT("settings.messages.gui.wagernomoneyaccept", "&cNot enough money to accept wager"),
/*  53 */   GUI_WAGER_INCREASE("settings.messages.gui.wagerincrease", "&aIncrease Wager"),
/*  54 */   GUI_WAGER_DECREASE("settings.messages.gui.wagerdecrease", "&aDecrease Wager"),
/*  55 */   GUI_WAGER_BETTINGON("settings.messages.gui.bettingon", "&aBetting on %player%"),
/*  56 */   GUI_WAGER_TEXT("settings.messages.gui.wagertext", "&aWagers"),
/*  57 */   GUI_UP_ARROW("settings.messages.gui.uparrow", "&a/\\"),
/*  58 */   GUI_DOWN_ARROW("settings.messages.gui.downarrow", "&a\\/"),
/*  59 */   GUI_RESET_NUMBERS("settings.messages.gui.resetnumbers", "&cRESET"),
/*  60 */   GUI_DONE_TEXT("settings.messages.gui.donetext", "&aSAVE"),
/*  61 */   GUI_NUMBERS_HALF("settings.messages.gui.numhalf", "&a1/2"),
/*  62 */   GUI_NUMBERS_DOUBLE("settings.messages.gui.numdouble", "&a2x"),
/*  63 */   GUI_NUMBERS_MAX("settings.messages.gui.nummax", "&aMax"),
/*  64 */   GUI_GAME_CREATE_TITLE("settings.messages.gui.gamecreatetitle", "%game% | Create Game"),
/*  65 */   GUI_GAME_FORFEIT_TITLE("settings.messages.gui.gameforfeittitle", "%game% | Forfeit Game"),
/*  66 */   GUI_GAME_JOIN_TITLE("settings.messages.gui.gamejointitle", "%game% | Join Game"),
/*  67 */   GUI_GAME_READY_TITLE("settings.messages.gui.gamereadytitle", "%game% | Ready Game"),
/*  68 */   GUI_GAME_WAGER_TITLE("settings.messages.gui.gamewagertitle", "%game% | Wagers"),
/*  69 */   GUI_GAME_TRADE_TITLE("settings.messages.gui.gametradetitle", "%game% | Bet Items"),
/*  70 */   GUI_CREATE_GAME_DATA_COLOR("settings.messages.gui.creategamedatacolor", "&a"),
/*     */ 
/*     */   
/*  73 */   GUI_WAGER_LABEL("settings.messages.gui.wagerlabel", "&2Wager: "),
/*  74 */   GUI_TEAM_LABEL("settings.messages.gui.teamlabel", "&2Team: "),
/*  75 */   GUI_RANKED_OPTION_TEXT("settings.messages.gui.rankedoption", "ranked"),
/*  76 */   GUI_UNRANKED_OPTION_TEXT("settings.messages.gui.unrankedoption", "unranked"),
/*  77 */   GUI_WAGERITEMS_LABEL("settings.messages.gui.wageritemslabel", "&2Wager Items: "),
/*  78 */   GUI_WAGERITEMS_DISABLED_LABEL("settings.messages.gui.wageritemsdisabledlabel", "NO"),
/*  79 */   GUI_WAGERITEMS_ENABLED_LABEL("settings.messages.gui.wageritemsenabledlabel", "YES"),
/*     */   
/*  81 */   GUI_TEAM_RED_TEXT("settings.messages.gui.teamredtext", "RED"),
/*  82 */   GUI_TEAM_BLACK_TEXT("settings.messages.gui.teamblacktext", "BLACK"),
/*  83 */   GUI_TEAM_WHITE_TEXT("settings.messages.gui.teamwhitetext", "WHITE"),
/*  84 */   GUI_TEAM_BLUE_TEXT("settings.messages.gui.teambluetext", "BLUE"),
/*     */ 
/*     */   
/*  87 */   CHAT_GUI_GAME_ALREADY_CREATED("settings.messages.gui.gamealreadycreated", "Game has already been created."),
/*  88 */   CHAT_GUI_GAME_NO_MONEY_CREATE("settings.messages.gui.gamenomoneycreate", "&cNot enough money to create game."),
/*  89 */   CHAT_GUI_GAME_NO_MONEY_ACCEPT("settings.messages.gui.gamenomoneyaccept", "&cPlayer no longer has enough money.."),
/*  90 */   CHAT_GUI_GAME_NO_MONEY_JOIN("settings.messages.gui.gamenomoneyjoin", "&cYou do not have enough money!"),
/*  91 */   CHAT_GUI_GAME_ACCEPT("settings.messages.gui.gameacceptchat", "Accepting %player%"),
/*  92 */   CHAT_GUI_GAME_DECLINE("settings.messages.gui.gamedeclinechat", "Declining %player%"),
/*  93 */   CHAT_GUI_GAME_OWNER_LEFT("settings.messages.gui.gameownerleft", "&cGame owner has left. Game cancelled."),
/*  94 */   CHAT_GUI_GAME_PLAYER_LEFT("settings.messages.gui.gameplayerleft", "Player left ready screen. Game cancelled."),
/*  95 */   CHAT_GUI_GAME_NO_AVAIL_GAME("settings.messages.gui.noavailgame", "No available game to join."),
/*  96 */   CHAT_GUI_GAME_FULL_QUEUE("settings.messages.gui.fullqueue", "Too many players are queuing!"),
/*  97 */   CHAT_GUI_WAGER_ACCEPT("settings.messages.gui.chatwageraccept", "%player% has accepted your wager!"),
/*  98 */   CHAT_GUI_WAGER_ACCEPTED("settings.messages.gui.chatwageraccept", "You have accepted %player%'s wager!"),
/*     */ 
/*     */   
/* 101 */   CHAT_GAME_PLAYER_WIN("settings.messages.chat.playerwin", "%player% has won the %game% game!"),
/* 102 */   CHAT_GAME_PLAYER_LOSE("settings.messages.chat.playerlose", "&aYou lost the %game% game!"),
/* 103 */   CHAT_GAME_TIE("settings.messages.chat.gametie", "&aTie %game% game!"),
/* 104 */   CHAT_GAME_FORCE_JUMP("settings.messages.chat.forcejump", "You must select a piece that can jump if a jump is possible."),
/* 105 */   CHAT_GAME_UNO_FORCE_2("settings.messages.chat.unoforce2", "You were forced to draw 2 cards."),
/* 106 */   CHAT_GAME_UNO_FORCE_4("settings.messages.chat.unoforce4", "You were forced to draw 4 cards."),
/* 107 */   CHAT_GAME_UNO_SKIPPED("settings.messages.chat.unoskipped", "You were skipped."),
/* 108 */   CHAT_GAME_UNO_NOT_YOUR_TURN("settings.messages.chat.unonotyourturn", "It is not your turn."),
/* 109 */   CHAT_GAME_UNO_SELECT_COLOR("settings.messages.chat.unoselectcolor", "Select a color."),
/* 110 */   CHAT_GAME_UNO_FORCE_DRAW("settings.messages.chat.unoforcedraw", "You have no playable cards and were forced to draw a card."),
/* 111 */   CHAT_GAME_UNO_INVALID_CARD("settings.messages.chat.unoinvalidcard", "You can not play that card."),
/* 112 */   CHAT_GAME_PLAYER_LEAVE("settings.messages.chat.gameplayerleave", "%player% has left %game%."),
/* 113 */   CHAT_GAME_UNO_LAST_CARD("settings.messages.chat.unolastcard", "%player%: Uno!"),
/* 114 */   CHAT_GAME_UNO_COLOR_RED("settings.messages.chat.unored", "RED"),
/* 115 */   CHAT_GAME_UNO_COLOR_BLUE("settings.messages.chat.unoblue", "BLUE"),
/* 116 */   CHAT_GAME_UNO_COLOR_YELLOW("settings.messages.chat.unoyellow", "YELLOW"),
/* 117 */   CHAT_GAME_UNO_COLOR_GREEN("settings.messages.chat.unogreen", "GREEN"),
/* 118 */   CHAT_GAME_UNO_COLOR("settings.messages.chat.unocolor", "%player%: %color%!"),
/*     */ 
/*     */   
/* 121 */   CHAT_NO_DB("settings.messages.chat.nodb", "&cDatabase must be enabled to view stats."),
/* 122 */   CHAT_NO_GAME("settings.messages.chat.nogame", "&cNo game found with that name."),
/* 123 */   CHAT_NO_PLAYER("settings.messages.chat.noplayer", "&cNo player found with that name."),
/* 124 */   CHAT_DB_ERROR("settings.messages.chat.dberror", "&cError calling to database."),
/* 125 */   CHAT_RELOAD("settings.messages.chat.reload", "&aReloaded board games config."),
/* 126 */   CHAT_GAME_NAMES("settings.messages.chat.gamenames", "&r&lGame Names: &r"),
/* 127 */   CHAT_STATS_HEADER("settings.messages.chat.statsheader", "&r&l%game% &r&7%player%&r's stats"),
/* 128 */   CHAT_STATS_FORMAT("settings.messages.chat.statsformat", "&7%statName% - &r%statVal%"),
/* 129 */   CHAT_LEADERBOARD_HEADER("settings.messages.chat.leaderboardheader", "&r&l%game% &rLeaderboard &7(%sort%)&r"),
/* 130 */   CHAT_LEADERBOARD_FORMAT("settings.messages.chat.leaderboardformat", "&7#%num%.&r %player% - %statVal%"),
/* 131 */   CHAT_AVAIL_COMMANDS("settings.messages.chat.availcommands", "&f&lBoard&9&lGames &rAvailable commands\n&r/bg games &7- lists games\n&r/bg board [game name] &7- gives you the game's item\n/&rbg stats [game name] [player name]\n/bg leaderboard [game name] [order by]\n/bg reload &7- reloads config"),
/* 132 */   CHAT_PLAYER_INGAME("settings.messages.chat.playeringame", "&cYou must finish your game before joining another."),
/* 133 */   CHAT_PLACED_BOARD("settings.messages.chat.placedboard", "&aPlaced board."),
/* 134 */   CHAT_NO_BOARD_ROOM("settings.messages.chat.noboardroom", "&cNo room to place board."),
/* 135 */   CHAT_WELCOME_GAME("settings.messages.chat.welcomegame", "&aWelcome to %game%!"),
/* 136 */   CHAT_RETURN_TO_GAME("settings.messages.chat.returntogame", "&cReturn to your %game% game!"); private final String path;
/*     */   private final String defaultValue;
/*     */   
/*     */   static {
/* 140 */     instance = BoardGames.getInstance();
/* 141 */     teamNameMap = new HashMap<>();
/*     */ 
/*     */     
/* 144 */     teamNameMap.put("RED", GUI_TEAM_RED_TEXT);
/* 145 */     teamNameMap.put("BLACK", GUI_TEAM_BLACK_TEXT);
/* 146 */     teamNameMap.put("WHITE", GUI_TEAM_WHITE_TEXT);
/* 147 */     teamNameMap.put("BLUE", GUI_TEAM_BLUE_TEXT);
/*     */   }
/*     */   private static final BoardGames instance; private static final HashMap<String, ConfigUtil> teamNameMap;
/*     */   ConfigUtil(String paramString1, String paramString2) {
/* 151 */     this.path = paramString1;
/* 152 */     this.defaultValue = paramString2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 157 */     String str = instance.getConfig().getString(this.path);
/*     */     
/* 159 */     if (str == null) return "";
/*     */     
/* 161 */     return ChatColor.translateAlternateColorCodes('&', str);
/*     */   }
/*     */   
/*     */   public String toRawString() {
/* 165 */     return ChatColor.stripColor(toString());
/*     */   }
/*     */   
/*     */   public boolean toBoolean() {
/* 169 */     return toString().equals("true");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPath() {
/* 174 */     return this.path;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultValue() {
/* 179 */     return this.defaultValue;
/*     */   }
/*     */   
/*     */   public static boolean getBoolean(String paramString) {
/* 183 */     String str = instance.getConfig().getString(paramString);
/*     */     
/* 185 */     if (str == null) return false;
/*     */     
/* 187 */     return str.equals("true");
/*     */   }
/*     */   
/*     */   public int toInteger() {
/* 191 */     if (MathUtils.isNumeric(toString())) {
/* 192 */       return Integer.parseInt(toString());
/*     */     }
/* 194 */     return 0;
/*     */   }
/*     */   
/*     */   public static String translateTeamName(String paramString) {
/* 198 */     if (teamNameMap.containsKey(paramString)) {
/* 199 */       return ((ConfigUtil)teamNameMap.get(paramString)).toString();
/*     */     }
/*     */     
/* 202 */     return paramString;
/*     */   }
/*     */   
/*     */   public String buildString(String paramString) {
/* 206 */     String str = toString();
/*     */     
/* 208 */     str = str.replace("%player%", paramString).replace("%game%", paramString);
/*     */     
/* 210 */     return str;
/*     */   }
/*     */   
/*     */   public String buildStringPlayerGame(String paramString1, String paramString2) {
/* 214 */     String str = toString();
/*     */     
/* 216 */     str = str.replace("%player%", paramString1).replace("%game%", paramString2);
/*     */     
/* 218 */     return str;
/*     */   }
/*     */   
/*     */   public String buildString(String paramString1, String paramString2) {
/* 222 */     String str = toString();
/*     */     
/* 224 */     str = str.replace("%game%", paramString1).replace("%sort%", paramString2);
/*     */     
/* 226 */     return str;
/*     */   }
/*     */   
/*     */   public String buildString(int paramInt) {
/* 230 */     String str = toString();
/*     */     
/* 232 */     str = str.replace("%num%", paramInt + "");
/*     */     
/* 234 */     return str;
/*     */   }
/*     */   
/*     */   public String buildStatsFormat(String paramString1, String paramString2) {
/* 238 */     String str = toString();
/*     */     
/* 240 */     str = str.replace("%statName%", paramString1).replace("%statVal%", paramString2);
/*     */     
/* 242 */     return str;
/*     */   }
/*     */   
/*     */   public String buildLeaderBoardFormat(int paramInt, String paramString1, String paramString2) {
/* 246 */     String str = toString();
/*     */     
/* 248 */     str = str.replace("%num%", paramInt + "").replace("%player%", paramString1).replace("%statVal%", paramString2);
/*     */     
/* 250 */     return str;
/*     */   }
/*     */   
/*     */   public void setValue(String paramString) {
/* 254 */     instance.getConfig().set(this.path, paramString);
/* 255 */     instance.saveConfig();
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\config\ConfigUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package water.of.cup.boardgames;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Level;
/*     */ import net.milkbowl.vault.economy.Economy;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.TabCompleter;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.ItemFrame;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.RegisteredServiceProvider;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import water.of.cup.boardgames.commands.bgCommands;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.config.GameConfigLoader;
/*     */ import water.of.cup.boardgames.extension.ExtensionManager;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.GameManager;
/*     */ import water.of.cup.boardgames.game.games.chess.Chess;
/*     */ import water.of.cup.boardgames.game.games.chess.ChessBoardsUtil;
/*     */ import water.of.cup.boardgames.game.games.connectfour.ConnectFour;
/*     */ import water.of.cup.boardgames.game.games.conways_game_of_life.ConwaysGameOfLife;
/*     */ import water.of.cup.boardgames.game.games.minesweaper.MineSweeper;
/*     */ import water.of.cup.boardgames.game.games.sudoku.Sudoku;
/*     */ import water.of.cup.boardgames.game.games.uno.Uno;
/*     */ import water.of.cup.boardgames.game.maps.GameMap;
/*     */ import water.of.cup.boardgames.game.maps.MapManager;
/*     */ import water.of.cup.boardgames.game.npcs.GameNPC;
/*     */ import water.of.cup.boardgames.game.storage.StorageManager;
/*     */ import water.of.cup.boardgames.image_handling.ImageManager;
/*     */ import water.of.cup.boardgames.listeners.BlockBreak;
/*     */ import water.of.cup.boardgames.listeners.BlockPlace;
/*     */ import water.of.cup.boardgames.listeners.ChunkLoad;
/*     */ import water.of.cup.boardgames.listeners.HangingBreakByEntity;
/*     */ import water.of.cup.boardgames.listeners.PlayerItemCraft;
/*     */ import water.of.cup.boardgames.listeners.PlayerMove;
/*     */ import water.of.cup.boardgames.listeners.PlayerQuit;
/*     */ import water.of.cup.boardgames.listeners.ProjectileHit;
/*     */ import water.of.cup.boardgames.metrics.Metrics;
/*     */ 
/*     */ public class BoardGames extends JavaPlugin {
/*  55 */   private static GameManager gameManager = new GameManager();
/*     */   private static BoardGames instance;
/*     */   private File configFile;
/*     */   private FileConfiguration config;
/*  59 */   private static Economy economy = null;
/*     */   
/*     */   private StorageManager storageManager;
/*     */   
/*     */   private ExtensionManager extensionManager;
/*     */   
/*     */   private static ImageManager IMAGE_MANAGER;
/*     */   
/*     */   private static boolean hasCitizens;
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  71 */     loadConfig0(); instance = this;
/*     */ 
/*     */     
/*  74 */     IMAGE_MANAGER = new ImageManager();
/*     */ 
/*     */     
/*  77 */     loadConfig();
/*     */ 
/*     */     
/*  80 */     if (ConfigUtil.DB_ENABLED.toBoolean()) {
/*  81 */       loadStorage();
/*     */     }
/*  83 */     Game.setGameIdKey(new NamespacedKey((Plugin)this, "game_id_key"));
/*  84 */     Game.setGameNameKey(new NamespacedKey((Plugin)this, "game_name_key"));
/*  85 */     MapManager.setMapValsKey(new NamespacedKey((Plugin)this, "map_vals_key"));
/*  86 */     MapManager.setRotationKey(new NamespacedKey((Plugin)this, "rotation_key"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     setupCitizens();
/*     */     
/* 100 */     gameManager.registerGames(new Class[] { Sudoku.class, Chess.class, ConwaysGameOfLife.class, TicTacToe.class, ConnectFour.class, Checkers.class, MineSweeper.class, Uno.class });
/*     */ 
/*     */     
/* 103 */     loadExtensionManager();
/*     */     
/* 105 */     getCommand("bg").setExecutor((CommandExecutor)new bgCommands());
/* 106 */     getCommand("bg").setTabCompleter((TabCompleter)new bgCommandsTabCompleter());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     registerListeners(new Listener[] { (Listener)new PlayerQuit(), (Listener)new ChunkLoad(), (Listener)new BlockPlace(), (Listener)new BoardInteract(), (Listener)new BlockBreak(), (Listener)new PlayerJoin(), (Listener)new PlayerItemCraft(), (Listener)new PlayerMove(), (Listener)new ProjectileHit(), (Listener)new HangingBreakByEntity() });
/*     */ 
/*     */     
/* 116 */     GameConfigLoader.loadGameConfig();
/*     */     
/* 118 */     if (ConfigUtil.WAGERS_ENABLED.toBoolean()) {
/* 119 */       boolean bool = setupEconomy();
/* 120 */       if (!bool) {
/* 121 */         Bukkit.getLogger().info("[BoardGames] Server must have Vault in order to place wagers on games.");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 126 */     setupPlaceholders();
/*     */ 
/*     */     
/* 129 */     ChessBoardsUtil.loadGames();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     Metrics metrics = new Metrics((Plugin)this, 11839);
/* 135 */     Bukkit.getLogger().info("[BoardGames] bStats: " + metrics.isEnabled() + " plugin ver: " + getDescription().getVersion());
/*     */     
/* 137 */     metrics.addCustomChart((Metrics.CustomChart)new Metrics.SimplePie("plugin_version", () -> getDescription().getVersion()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 145 */     boolean bool = instance.getConfig().getBoolean("settings.database.enabled");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     IMAGE_MANAGER.clearImages();
/*     */ 
/*     */     
/* 156 */     GameConfigLoader.unloadConfig();
/*     */ 
/*     */     
/* 159 */     if (this.storageManager != null) {
/* 160 */       this.storageManager.closeConnection();
/*     */     }
/* 162 */     if (hasCitizens() && GameNPC.REGISTRY != null) {
/* 163 */       GameNPC.REGISTRY.deregisterAll();
/*     */     }
/*     */     
/* 166 */     Bukkit.getScheduler().cancelTasks((Plugin)this);
/*     */   }
/*     */   
/*     */   private void updateLoadedChunkBoards() {
/* 170 */     for (World world : Bukkit.getWorlds()) {
/* 171 */       for (Chunk chunk : world.getLoadedChunks()) {
/* 172 */         for (Entity entity : chunk.getEntities()) {
/* 173 */           if (!entity.isDead())
/*     */           {
/* 175 */             if (entity instanceof ItemFrame) {
/*     */               
/* 177 */               ItemFrame itemFrame = (ItemFrame)entity;
/* 178 */               ItemStack itemStack = itemFrame.getItem();
/* 179 */               if (GameMap.isGameMap(itemStack)) {
/*     */                 
/* 181 */                 GameMap gameMap = new GameMap(itemStack);
/*     */                 
/* 183 */                 if (gameManager.getGameByGameId(gameMap.getGameId()) == null) {
/* 184 */                   Game game = gameManager.newGame(gameMap.getGameName(), gameMap.getRotation());
/* 185 */                   if (game != null) {
/*     */ 
/*     */ 
/*     */                     
/* 189 */                     Location location = itemFrame.getLocation().getBlock().getLocation();
/* 190 */                     game.replace(location, game.getRotation(), gameMap.getMapVal());
/* 191 */                     gameManager.addGame(game);
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } private void registerListeners(Listener... paramVarArgs) {
/* 200 */     Arrays.<Listener>stream(paramVarArgs).forEach(paramListener -> getServer().getPluginManager().registerEvents(paramListener, (Plugin)this));
/*     */   }
/*     */   
/*     */   public static BoardGames getInstance() {
/* 204 */     return instance;
/*     */   }
/*     */   
/*     */   private boolean setupEconomy() {
/* 208 */     if (getServer().getPluginManager().getPlugin("Vault") == null) {
/* 209 */       return false;
/*     */     }
/* 211 */     RegisteredServiceProvider registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);
/* 212 */     if (registeredServiceProvider == null) {
/* 213 */       return false;
/*     */     }
/* 215 */     economy = (Economy)registeredServiceProvider.getProvider();
/* 216 */     return (economy != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupCitizens() {
/* 221 */     if (getServer().getPluginManager().getPlugin("Citizens") == null || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
/* 222 */       getLogger().log(Level.SEVERE, "[BoardGames] Citizens 2.0 not found or not enabled, NPCS disabled.");
/* 223 */       hasCitizens = false;
/*     */       
/*     */       return;
/*     */     } 
/* 227 */     hasCitizens = true;
/*     */   }
/*     */   
/*     */   private void setupPlaceholders() {
/* 231 */     if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
/* 232 */       (new BoardGamesPlaceholder(this)).register();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGameRecipes() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadConfig() {
/* 253 */     if (!getDataFolder().exists()) {
/* 254 */       getDataFolder().mkdir();
/*     */     }
/*     */     
/* 257 */     this.configFile = new File(getDataFolder(), "config.yml");
/* 258 */     if (!this.configFile.exists()) {
/*     */       try {
/* 260 */         this.configFile.createNewFile();
/* 261 */       } catch (IOException iOException) {
/* 262 */         iOException.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 266 */     this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.configFile);
/*     */     
/* 268 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 284 */     for (ConfigUtil configUtil : ConfigUtil.values()) {
/* 285 */       hashMap.put(configUtil.getPath(), configUtil.getDefaultValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 292 */     for (String str : hashMap.keySet()) {
/* 293 */       if (!this.config.contains(str)) {
/* 294 */         this.config.set(str, hashMap.get(str));
/*     */       }
/*     */     } 
/*     */     
/* 298 */     File file = new File(getDataFolder(), "custom_images");
/* 299 */     if (!file.exists()) {
/* 300 */       file.mkdir();
/*     */     }
/*     */     
/* 303 */     saveConfig();
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveConfig() {
/*     */     try {
/* 309 */       this.config.save(this.configFile);
/* 310 */     } catch (IOException iOException) {
/* 311 */       iOException.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addToConfig(HashMap<String, Object> paramHashMap) {
/* 316 */     for (String str : paramHashMap.keySet()) {
/* 317 */       if (!this.config.contains(str)) {
/* 318 */         this.config.set(str, paramHashMap.get(str));
/*     */       }
/*     */     } 
/*     */     
/* 322 */     saveConfig();
/*     */   }
/*     */ 
/*     */   
/*     */   public FileConfiguration getConfig() {
/* 327 */     return this.config;
/*     */   }
/*     */   
/*     */   public GameManager getGameManager() {
/* 331 */     return gameManager;
/*     */   }
/*     */   
/*     */   public Economy getEconomy() {
/* 335 */     return economy;
/*     */   }
/*     */   
/*     */   public void loadStorage() {
/* 339 */     this.storageManager = new StorageManager();
/*     */   }
/*     */   
/*     */   public StorageManager getStorageManager() {
/* 343 */     return this.storageManager;
/*     */   }
/*     */   
/*     */   public boolean hasStorage() {
/* 347 */     return (this.storageManager != null && ConfigUtil.DB_ENABLED.toBoolean());
/*     */   }
/*     */   
/*     */   private void loadExtensionManager() {
/* 351 */     this.extensionManager = new ExtensionManager();
/* 352 */     this.extensionManager.loadExtensions();
/*     */   }
/*     */   
/*     */   public static boolean hasCitizens() {
/* 356 */     return hasCitizens;
/*     */   }
/*     */   
/*     */   public static ImageManager getImageManager() {
/* 360 */     return IMAGE_MANAGER;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\BoardGames.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
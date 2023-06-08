/*     */ package water.of.cup.boardgames.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.Recipe;
/*     */ import org.bukkit.inventory.ShapedRecipe;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ 
/*     */ public class GameConfigLoader
/*     */ {
/*  18 */   private static final BoardGames instance = BoardGames.getInstance();
/*  19 */   private static final HashMap<String, Game> GAMES = new HashMap<>();
/*     */   
/*     */   public static void loadGameConfig() {
/*  22 */     for (String str : instance.getGameManager().getGameNames()) {
/*  23 */       Game game = instance.getGameManager().newGame(str, 0);
/*     */       
/*  25 */       if (game != null) {
/*  26 */         Bukkit.getLogger().info("[BoardGames] Loading game " + game.getName());
/*  27 */         GAMES.put(game.getName(), game);
/*     */       } 
/*     */     } 
/*     */     
/*  31 */     loadRecipes();
/*  32 */     loadGameSounds();
/*  33 */     loadCustomConfigValues();
/*  34 */     loadGameWinAmounts();
/*  35 */     loadDefaults();
/*     */   }
/*     */   
/*     */   public static void unloadConfig() {
/*  39 */     GAMES.clear();
/*     */   }
/*     */   
/*     */   private static void loadRecipes() {
/*  43 */     for (String str : GAMES.keySet()) {
/*  44 */       Game game = GAMES.get(str);
/*     */       
/*  46 */       GameRecipe gameRecipe = game.getGameRecipe();
/*  47 */       if (gameRecipe != null) {
/*  48 */         gameRecipe.addToConfig();
/*     */       }
/*     */     } 
/*  51 */     if (ConfigUtil.RECIPE_ENABLED.toBoolean())
/*  52 */       addBukkitRecipes(); 
/*     */   }
/*     */   
/*     */   private static void loadGameSounds() {
/*  56 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */     
/*  58 */     for (String str : GAMES.keySet()) {
/*  59 */       Game game = GAMES.get(str);
/*     */       
/*  61 */       if (game != null) {
/*  62 */         ArrayList arrayList = game.getGameSounds();
/*  63 */         if (arrayList != null) {
/*  64 */           String str1 = "settings.games." + game.getName() + ".sounds";
/*  65 */           for (GameSound gameSound : arrayList) {
/*  66 */             hashMap.put(str1 + "." + gameSound.getName(), gameSound.getSound().toString());
/*     */           }
/*     */           
/*  69 */           hashMap.put(str1 + ".enabled", "true");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  74 */     instance.addToConfig(hashMap);
/*     */   }
/*     */   
/*     */   private static void loadGameWinAmounts() {
/*  78 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */     
/*  80 */     for (String str : GAMES.keySet()) {
/*  81 */       Game game = GAMES.get(str);
/*     */       
/*  83 */       if (game != null) {
/*  84 */         int i = game.getGameWinAmount();
/*  85 */         String str1 = "settings.games." + game.getName() + ".winAmount";
/*  86 */         hashMap.put(str1, Integer.valueOf(i));
/*     */       } 
/*     */     } 
/*     */     
/*  90 */     instance.addToConfig(hashMap);
/*     */   }
/*     */   
/*     */   private static void loadCustomConfigValues() {
/*  94 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */     
/*  96 */     for (String str : GAMES.keySet()) {
/*  97 */       Game game = GAMES.get(str);
/*     */       
/*  99 */       if (game != null) {
/* 100 */         HashMap hashMap1 = game.getCustomValues();
/* 101 */         if (hashMap1 != null) {
/* 102 */           String str1 = "settings.games." + game.getName() + ".misc";
/* 103 */           for (String str2 : hashMap1.keySet()) {
/* 104 */             hashMap.put(str1 + "." + str2, hashMap1.get(str2));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 110 */     instance.addToConfig(hashMap);
/*     */   }
/*     */   
/*     */   private static void loadDefaults() {
/* 114 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*     */     
/* 116 */     for (String str : GAMES.keySet()) {
/* 117 */       Game game = GAMES.get(str);
/*     */       
/* 119 */       if (game != null) {
/* 120 */         String str1 = "settings.games." + game.getName();
/* 121 */         hashMap.put(str1 + ".enabled", "true");
/* 122 */         hashMap.put(str1 + ".altName", game.getName());
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     instance.addToConfig(hashMap);
/*     */   }
/*     */   
/*     */   private static void addBukkitRecipes() {
/* 130 */     FileConfiguration fileConfiguration = instance.getConfig();
/*     */     
/* 132 */     for (String str1 : fileConfiguration.getConfigurationSection("settings.games").getKeys(false)) {
/* 133 */       if (!instance.getGameManager().isValidGame(str1))
/*     */         continue; 
/* 135 */       Game game = instance.getGameManager().newGame(str1, 0);
/*     */       
/* 137 */       if (game.getGameRecipe() == null)
/*     */         continue; 
/* 139 */       ItemStack itemStack = game.getBoardItem();
/* 140 */       NamespacedKey namespacedKey = new NamespacedKey((Plugin)instance, str1);
/* 141 */       ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, itemStack);
/*     */       
/* 143 */       String str2 = "settings.games." + str1 + ".recipe";
/*     */       
/* 145 */       ArrayList arrayList = (ArrayList)fileConfiguration.get(str2 + ".shape");
/* 146 */       shapedRecipe.shape((String[])arrayList.toArray((Object[])new String[arrayList.size()]));
/*     */       
/* 148 */       String str3 = str2 + ".ingredients";
/*     */       
/* 150 */       for (String str : fileConfiguration.getConfigurationSection(str3).getKeys(false)) {
/* 151 */         shapedRecipe.setIngredient(str.charAt(0), 
/* 152 */             Material.valueOf((String)fileConfiguration.get(str3 + "." + str)));
/*     */       }
/*     */       
/* 155 */       Bukkit.addRecipe((Recipe)shapedRecipe);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\config\GameConfigLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
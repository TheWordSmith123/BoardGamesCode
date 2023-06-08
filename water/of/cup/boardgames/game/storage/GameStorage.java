/*     */ package water.of.cup.boardgames.game.storage;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ 
/*     */ 
/*     */ public abstract class GameStorage
/*     */ {
/*  14 */   private final BoardGames instance = BoardGames.getInstance();
/*     */   private final Game game;
/*     */   private final StorageType[] storageTypes;
/*     */   
/*     */   protected abstract String getTableName();
/*     */   
/*     */   protected abstract StorageType[] getGameStores();
/*     */   
/*     */   public GameStorage(Game paramGame) {
/*  23 */     this.game = paramGame;
/*  24 */     this.storageTypes = getGameStores();
/*     */     
/*  26 */     StorageManager storageManager = this.instance.getStorageManager();
/*  27 */     if (storageManager != null) {
/*  28 */       storageManager.addGameStorage(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateData(Player paramPlayer, StorageType paramStorageType, Object paramObject) {
/*  34 */     if (!canExecute(paramStorageType))
/*     */       return; 
/*  36 */     this.instance.getStorageManager().updateColumn(paramPlayer, getTableName(), paramStorageType, paramObject, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setData(Player paramPlayer, StorageType paramStorageType, Object paramObject) {
/*  41 */     if (!canExecute(paramStorageType))
/*     */       return; 
/*  43 */     this.instance.getStorageManager().updateColumn(paramPlayer, getTableName(), paramStorageType, paramObject, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canExecute(StorageType paramStorageType) {
/*  48 */     if (!this.game.hasGameStorage()) return false;
/*     */ 
/*     */     
/*  51 */     if (!hasStorageType(paramStorageType)) return false;
/*     */ 
/*     */     
/*  54 */     String str1 = "settings.games." + this.game.getName() + ".database";
/*     */     
/*  56 */     String str2 = str1 + ".enabled";
/*  57 */     if (!ConfigUtil.getBoolean(str2)) return false;
/*     */     
/*  59 */     String str3 = str1 + "." + paramStorageType.getKey();
/*  60 */     return ConfigUtil.getBoolean(str3);
/*     */   }
/*     */   
/*     */   private boolean hasStorageType(StorageType paramStorageType) {
/*  64 */     for (StorageType storageType : getGameStores()) {
/*  65 */       if (storageType == paramStorageType) {
/*  66 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  70 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasStorageType(String paramString) {
/*  74 */     for (StorageType storageType : getGameStores()) {
/*  75 */       if (storageType.getKey().equals(paramString)) {
/*  76 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  80 */     return false;
/*     */   }
/*     */   
/*     */   protected void initializeConfig() {
/*  84 */     FileConfiguration fileConfiguration = this.instance.getConfig();
/*     */     
/*  86 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*  87 */     String str = "settings.games." + this.game.getName() + ".database";
/*     */     
/*  89 */     for (StorageType storageType : getGameStores()) {
/*  90 */       hashMap.put(str + "." + storageType.getKey(), "true");
/*     */     }
/*     */     
/*  93 */     hashMap.put(str + ".enabled", "true");
/*     */     
/*  95 */     for (String str1 : hashMap.keySet()) {
/*  96 */       if (!fileConfiguration.contains(str1)) {
/*  97 */         fileConfiguration.set(str1, hashMap.get(str1));
/*     */       }
/*     */     } 
/*     */     
/* 101 */     this.instance.saveConfig();
/*     */   }
/*     */   
/*     */   public ArrayList<StorageType> getStorageTypes() {
/* 105 */     ArrayList<StorageType> arrayList = new ArrayList();
/* 106 */     for (StorageType storageType : getGameStores()) {
/* 107 */       if (canExecute(storageType)) arrayList.add(storageType);
/*     */     
/*     */     } 
/* 110 */     return arrayList;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\storage\GameStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package water.of.cup.boardgames.game.maps;
/*    */ 
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.meta.ItemMeta;
/*    */ import org.bukkit.inventory.meta.MapMeta;
/*    */ import org.bukkit.persistence.PersistentDataContainer;
/*    */ import org.bukkit.persistence.PersistentDataType;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ 
/*    */ public class GameMap extends ItemStack {
/*    */   private int gameId;
/*    */   private int mapVal;
/*    */   private Game game;
/*    */   private int rotation;
/*    */   private String gameName;
/*    */   
/*    */   public GameMap(ItemStack paramItemStack) {
/* 20 */     super(paramItemStack);
/* 21 */     MapMeta mapMeta = (MapMeta)paramItemStack.getItemMeta();
/* 22 */     PersistentDataContainer persistentDataContainer = mapMeta.getPersistentDataContainer();
/* 23 */     this.gameId = ((Integer)persistentDataContainer.get(Game.getGameIdKey(), PersistentDataType.INTEGER)).intValue();
/* 24 */     this.mapVal = ((Integer)persistentDataContainer.get(MapManager.getMapValsKey(), PersistentDataType.INTEGER)).intValue();
/* 25 */     this.game = BoardGames.getInstance().getGameManager().getGameByGameId(this.gameId);
/* 26 */     this.rotation = ((Integer)persistentDataContainer.get(MapManager.getRotationKey(), PersistentDataType.INTEGER)).intValue();
/* 27 */     this.gameName = (String)persistentDataContainer.get(Game.getGameNameKey(), PersistentDataType.STRING);
/*    */   }
/*    */   
/*    */   public GameMap(Game paramGame, int paramInt, ItemStack paramItemStack) {
/* 31 */     super(paramItemStack);
/* 32 */     MapMeta mapMeta = (MapMeta)paramItemStack.getItemMeta();
/* 33 */     PersistentDataContainer persistentDataContainer = mapMeta.getPersistentDataContainer();
/* 34 */     this.game = paramGame;
/* 35 */     this.mapVal = paramInt;
/* 36 */     this.gameId = paramGame.getGameId();
/* 37 */     this.rotation = paramGame.getRotation();
/* 38 */     this.gameName = paramGame.getGameName();
/*    */     
/* 40 */     persistentDataContainer.set(Game.getGameIdKey(), PersistentDataType.INTEGER, Integer.valueOf(this.gameId));
/* 41 */     persistentDataContainer.set(MapManager.getMapValsKey(), PersistentDataType.INTEGER, Integer.valueOf(paramInt));
/* 42 */     persistentDataContainer.set(MapManager.getRotationKey(), PersistentDataType.INTEGER, Integer.valueOf(this.rotation));
/* 43 */     persistentDataContainer.set(Game.getGameNameKey(), PersistentDataType.STRING, this.gameName);
/* 44 */     setItemMeta((ItemMeta)mapMeta);
/*    */   }
/*    */   
/*    */   public static boolean isGameMap(ItemStack paramItemStack) {
/* 48 */     if (paramItemStack.getType() != Material.FILLED_MAP)
/* 49 */       return false; 
/* 50 */     MapMeta mapMeta = (MapMeta)paramItemStack.getItemMeta();
/* 51 */     PersistentDataContainer persistentDataContainer = mapMeta.getPersistentDataContainer();
/*    */ 
/*    */     
/* 54 */     if (!persistentDataContainer.has(Game.getGameIdKey(), PersistentDataType.INTEGER))
/* 55 */       return false; 
/* 56 */     if (!persistentDataContainer.has(MapManager.getMapValsKey(), PersistentDataType.INTEGER))
/* 57 */       return false; 
/* 58 */     if (!persistentDataContainer.has(MapManager.getRotationKey(), PersistentDataType.INTEGER))
/* 59 */       return false; 
/* 60 */     if (!persistentDataContainer.has(Game.getGameNameKey(), PersistentDataType.STRING)) {
/* 61 */       return false;
/*    */     }
/* 63 */     return true;
/*    */   }
/*    */   
/*    */   public MapMeta getMapMeta() {
/* 67 */     return (MapMeta)getItemMeta();
/*    */   }
/*    */   
/*    */   public int getGameId() {
/* 71 */     return this.gameId;
/*    */   }
/*    */   
/*    */   public int getMapVal() {
/* 75 */     return this.mapVal;
/*    */   }
/*    */   
/*    */   public Game getGame() {
/* 79 */     return this.game;
/*    */   }
/*    */   
/*    */   public int getRotation() {
/* 83 */     return this.rotation;
/*    */   }
/*    */   
/*    */   public String getGameName() {
/* 87 */     return this.gameName;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\maps\GameMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
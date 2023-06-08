/*     */ package water.of.cup.boardgames.game.maps;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.inventory.meta.MapMeta;
/*     */ import org.bukkit.map.MapRenderer;
/*     */ import org.bukkit.map.MapView;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.MathUtils;
/*     */ 
/*     */ 
/*     */ public class MapManager
/*     */ {
/*     */   private static NamespacedKey mapValsKey;
/*     */   private static NamespacedKey rotationKey;
/*     */   private int[][] mapStructure;
/*     */   private int[][] rotatedMapStructure;
/*     */   private int[] dimensions;
/*     */   private int rotation;
/*     */   private Game game;
/*     */   
/*     */   public MapManager(int[][] paramArrayOfint, int paramInt, Game paramGame) {
/*  30 */     this.mapStructure = MathUtils.cloneIntMatrix(paramArrayOfint);
/*  31 */     this.rotation = paramInt;
/*  32 */     this.game = paramGame;
/*  33 */     this.dimensions = new int[] { (paramArrayOfint[0]).length, paramArrayOfint.length };
/*     */ 
/*     */     
/*  36 */     this.rotatedMapStructure = MathUtils.cloneIntMatrix(paramArrayOfint);
/*  37 */     byte b = 0;
/*  38 */     while (b < paramInt) {
/*  39 */       this.rotatedMapStructure = MathUtils.rotateMatrix(this.rotatedMapStructure);
/*  40 */       b++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int[] getClickLocation(double[] paramArrayOfdouble, ItemStack paramItemStack) {
/*  45 */     if (!GameMap.isGameMap(paramItemStack)) {
/*  46 */       return new int[] { 0, 0 };
/*     */     }
/*  48 */     GameMap gameMap = new GameMap(paramItemStack);
/*  49 */     if (!gameMap.getGame().equals(this.game)) {
/*  50 */       return new int[] { 0, 0 };
/*     */     }
/*     */     
/*  53 */     int i = (int)((paramArrayOfdouble[0] - Math.floor(paramArrayOfdouble[0])) * 128.0D);
/*  54 */     int j = (int)((paramArrayOfdouble[1] - Math.floor(paramArrayOfdouble[1])) * 128.0D);
/*     */     
/*  56 */     int[] arrayOfInt1 = { i, j };
/*     */ 
/*     */     
/*  59 */     Screen screen = getClickedScreen(paramItemStack);
/*  60 */     if (getClickedScreen(paramItemStack) != null) {
/*  61 */       return screen.getClickLocation(arrayOfInt1, gameMap.getMapVal());
/*     */     }
/*     */     
/*  64 */     int k = this.rotation;
/*  65 */     while (k < 4) {
/*  66 */       arrayOfInt1 = MathUtils.rotatePointAroundPoint90Degrees(new double[] { 63.5D, 63.5D }, arrayOfInt1);
/*  67 */       k++;
/*     */     } 
/*     */ 
/*     */     
/*  71 */     int[] arrayOfInt2 = getMapValsLocationOnBoard(gameMap.getMapVal());
/*  72 */     arrayOfInt1 = new int[] { arrayOfInt1[0] + arrayOfInt2[0] * 128, arrayOfInt1[1] + arrayOfInt2[1] * 128 };
/*     */     
/*  74 */     return arrayOfInt1;
/*     */   }
/*     */   
/*     */   public static NamespacedKey getMapValsKey() {
/*  78 */     return mapValsKey;
/*     */   }
/*     */   
/*     */   public static void setMapValsKey(NamespacedKey paramNamespacedKey) {
/*  82 */     mapValsKey = paramNamespacedKey;
/*     */   }
/*     */   
/*     */   public int[] getMapValsLocationOnBoard(int paramInt) {
/*  86 */     byte b1 = 0;
/*  87 */     byte b2 = 0;
/*     */     
/*  89 */     label13: while (b2 < this.mapStructure.length) {
/*  90 */       while (b1 < (this.mapStructure[b2]).length) {
/*  91 */         if (this.mapStructure[b2][b1] == paramInt)
/*     */           break label13; 
/*  93 */         b1++;
/*     */       } 
/*  95 */       b1 = 0;
/*  96 */       b2++;
/*     */     } 
/*     */     
/*  99 */     return new int[] { b1, b2 };
/*     */   }
/*     */   
/*     */   public int[] getMapValsLocationOnRotatedBoard(int paramInt) {
/* 103 */     int[] arrayOfInt = getMapValsLocationOnBoard(paramInt);
/* 104 */     int i = this.rotation;
/*     */     
/* 106 */     for (Screen screen : this.game.getScreens()) {
/* 107 */       if (screen.containsMapVal(paramInt)) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 112 */         arrayOfInt = new int[] { screen.getPosition()[0], screen.getPosition()[1] };
/*     */         
/* 114 */         arrayOfInt[screen.getInitialDirection() % 2] = arrayOfInt[screen.getInitialDirection() % 2] + screen.getMapValsLocationOnScreen(paramInt)[0];
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 119 */     byte b = 0;
/* 120 */     while (b < i) {
/* 121 */       b++;
/* 122 */       arrayOfInt = MathUtils.rotatePointAroundPoint90Degrees(new double[] { 0.0D, 0.0D }, arrayOfInt);
/*     */     } 
/*     */     
/* 125 */     return arrayOfInt;
/*     */   }
/*     */   
/*     */   public int getMapValAtLocationOnRotatedBoard(int paramInt1, int paramInt2) {
/* 129 */     int i = (4 - this.rotation) % 4;
/* 130 */     int[] arrayOfInt = { paramInt1, paramInt2 };
/* 131 */     byte b = 0;
/* 132 */     while (b < i) {
/* 133 */       b++;
/* 134 */       arrayOfInt = MathUtils.rotatePointAroundPoint90Degrees(new double[] { 0.0D, 0.0D }, arrayOfInt);
/*     */     } 
/* 136 */     return this.mapStructure[arrayOfInt[1]][arrayOfInt[0]];
/*     */   }
/*     */   
/*     */   public ArrayList<MapData> getMapDataAtLocationOnRotatedBoard(int paramInt1, int paramInt2, int paramInt3) {
/* 140 */     ArrayList<MapData> arrayList = new ArrayList();
/* 141 */     int i = (4 - this.rotation) % 4;
/* 142 */     int[] arrayOfInt = { paramInt1, paramInt2 };
/* 143 */     byte b = 0;
/* 144 */     while (b < i) {
/* 145 */       b++;
/* 146 */       arrayOfInt = MathUtils.rotatePointAroundPoint90Degrees(new double[] { 0.0D, 0.0D }, arrayOfInt);
/*     */     } 
/* 148 */     arrayList.add(new MapData(this.mapStructure[arrayOfInt[1]][arrayOfInt[0]], BlockFace.UP));
/*     */     
/* 150 */     for (Screen screen : this.game.getScreens()) {
/* 151 */       int j = screen.getMapValAtLocation(arrayOfInt[0], arrayOfInt[1], paramInt3);
/* 152 */       if (j > 0) {
/* 153 */         arrayList.add(new MapData(j, screen.getBlockFace()));
/*     */       }
/*     */     } 
/* 156 */     return arrayList;
/*     */   }
/*     */   
/*     */   public int[] getDimensions() {
/* 160 */     return (int[])this.dimensions.clone();
/*     */   }
/*     */   
/*     */   public int[] getMapVals() {
/* 164 */     ArrayList<Integer> arrayList = new ArrayList();
/* 165 */     for (int[] arrayOfInt1 : this.mapStructure) {
/* 166 */       for (int i : arrayOfInt1) {
/* 167 */         if (i > 0)
/* 168 */           arrayList.add(Integer.valueOf(i)); 
/*     */       } 
/* 170 */     }  int[] arrayOfInt = new int[arrayList.size()];
/* 171 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/* 172 */       arrayOfInt[b] = ((Integer)arrayList.get(b)).intValue();
/*     */     }
/*     */     
/* 175 */     return arrayOfInt;
/*     */   }
/*     */   
/*     */   public int[] getRotatedDimensions() {
/* 179 */     int[] arrayOfInt = (int[])this.dimensions.clone();
/* 180 */     arrayOfInt = new int[] { arrayOfInt[0] - 1, arrayOfInt[1] - 1 };
/* 181 */     byte b = 0;
/* 182 */     while (b < this.rotation) {
/* 183 */       arrayOfInt = MathUtils.rotatePointAroundPoint90Degrees(new double[] { 0.0D, 0.0D }, arrayOfInt);
/* 184 */       b++;
/*     */     } 
/* 186 */     return arrayOfInt;
/*     */   }
/*     */   
/*     */   public void renderBoard() {
/* 190 */     for (int i : getMapVals()) {
/* 191 */       GameMap gameMap = this.game.getGameMapByMapVal(i);
/* 192 */       MapMeta mapMeta = gameMap.getMapMeta();
/* 193 */       final MapView view = mapMeta.getMapView();
/*     */       
/* 195 */       for (MapRenderer mapRenderer : mapView.getRenderers()) {
/* 196 */         if (mapRenderer instanceof GameRenderer)
/* 197 */           ((GameRenderer)mapRenderer).rerender(); 
/* 198 */       }  Bukkit.getServer().getScheduler().runTaskAsynchronously((Plugin)BoardGames.getInstance(), new Runnable()
/*     */           {
/*     */             public void run() {
/* 201 */               view.getWorld().getPlayers().forEach(param1Player -> param1Player.sendMap(param1MapView));
/*     */             }
/*     */           });
/*     */     } 
/* 205 */     for (Screen screen : this.game.getScreens()) {
/* 206 */       for (int i : screen.getMapVals()) {
/* 207 */         GameMap gameMap = this.game.getGameMapByMapVal(i);
/* 208 */         MapMeta mapMeta = gameMap.getMapMeta();
/* 209 */         final MapView view = mapMeta.getMapView();
/*     */         
/* 211 */         for (MapRenderer mapRenderer : mapView.getRenderers()) {
/* 212 */           if (mapRenderer instanceof GameRenderer)
/* 213 */             ((GameRenderer)mapRenderer).rerender(); 
/* 214 */         }  Bukkit.getServer().getScheduler().runTaskAsynchronously((Plugin)BoardGames.getInstance(), new Runnable()
/*     */             {
/*     */               public void run() {
/* 217 */                 view.getWorld().getPlayers().forEach(param1Player -> param1Player.sendMap(param1MapView));
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void renderBoard(final Player player) {
/* 224 */     for (int i : getMapVals()) {
/* 225 */       GameMap gameMap = this.game.getGameMapByMapVal(i);
/* 226 */       if (gameMap != null) {
/*     */         
/* 228 */         MapMeta mapMeta = gameMap.getMapMeta();
/* 229 */         final MapView view = mapMeta.getMapView();
/*     */         
/* 231 */         for (MapRenderer mapRenderer : mapView.getRenderers()) {
/* 232 */           if (mapRenderer instanceof GameRenderer)
/* 233 */             ((GameRenderer)mapRenderer).rerender(player); 
/* 234 */         }  Bukkit.getServer().getScheduler().runTaskAsynchronously((Plugin)BoardGames.getInstance(), new Runnable()
/*     */             {
/*     */               public void run() {
/* 237 */                 player.sendMap(view); }
/*     */             });
/*     */       } 
/*     */     } 
/* 241 */     for (Screen screen : this.game.getScreens()) {
/* 242 */       for (int i : screen.getMapVals()) {
/* 243 */         GameMap gameMap = this.game.getGameMapByMapVal(i);
/* 244 */         if (gameMap != null) {
/*     */           
/* 246 */           MapMeta mapMeta = gameMap.getMapMeta();
/* 247 */           final MapView view = mapMeta.getMapView();
/*     */           
/* 249 */           for (MapRenderer mapRenderer : mapView.getRenderers()) {
/* 250 */             if (mapRenderer instanceof GameRenderer)
/* 251 */               ((GameRenderer)mapRenderer).rerender(player); 
/* 252 */           }  Bukkit.getServer().getScheduler().runTaskAsynchronously((Plugin)BoardGames.getInstance(), new Runnable()
/*     */               {
/*     */                 public void run() {
/* 255 */                   player.sendMap(view);
/*     */                 }
/*     */               });
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void resetRenderers() {
/* 263 */     for (int i : getMapVals()) {
/* 264 */       GameMap gameMap = this.game.getGameMapByMapVal(i);
/* 265 */       MapMeta mapMeta = gameMap.getMapMeta();
/* 266 */       final MapView view = mapMeta.getMapView();
/*     */       
/* 268 */       for (MapRenderer mapRenderer : mapView.getRenderers()) {
/* 269 */         mapView.removeRenderer(mapRenderer);
/*     */       }
/* 271 */       GameRenderer gameRenderer = new GameRenderer(this.game, getMapValsLocationOnBoard(i));
/* 272 */       mapView.addRenderer(gameRenderer);
/* 273 */       mapMeta.setMapView(mapView);
/* 274 */       gameMap.setItemMeta((ItemMeta)mapMeta);
/*     */       
/* 276 */       Bukkit.getServer().getScheduler().runTaskAsynchronously((Plugin)BoardGames.getInstance(), new Runnable()
/*     */           {
/*     */             public void run() {
/* 279 */               view.getWorld().getPlayers().forEach(param1Player -> param1Player.sendMap(param1MapView));
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 284 */     for (Screen screen : this.game.getScreens()) {
/* 285 */       screen.renderScreen();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRotation() {
/* 291 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public Screen getClickedScreen(ItemStack paramItemStack) {
/* 295 */     GameMap gameMap = new GameMap(paramItemStack);
/* 296 */     int i = gameMap.getMapVal();
/* 297 */     for (Screen screen : this.game.getScreens()) {
/* 298 */       if (screen.containsMapVal(i))
/* 299 */         return screen; 
/*     */     } 
/* 301 */     return null;
/*     */   }
/*     */   
/*     */   public static NamespacedKey getRotationKey() {
/* 305 */     return rotationKey;
/*     */   }
/*     */   
/*     */   public static void setRotationKey(NamespacedKey paramNamespacedKey) {
/* 309 */     rotationKey = paramNamespacedKey;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\maps\MapManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
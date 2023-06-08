/*     */ package water.of.cup.boardgames.game.maps;
/*     */ import java.util.ArrayList;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.inventory.meta.MapMeta;
/*     */ import org.bukkit.map.MapRenderer;
/*     */ import org.bukkit.map.MapView;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.game.Button;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.GameImage;
/*     */ import water.of.cup.boardgames.game.GamePlayer;
/*     */ 
/*     */ public class Screen {
/*     */   private int direction;
/*     */   private int initialDirection;
/*     */   private int[] dimensions;
/*     */   private int[] position;
/*     */   private int[][] mapStructure;
/*     */   private GameImage gameImage;
/*     */   private Game game;
/*     */   
/*     */   public Screen(Game paramGame, String paramString, int paramInt1, int[] paramArrayOfint, int[][] paramArrayOfint1, int paramInt2) {
/*  27 */     this.game = paramGame;
/*  28 */     this.initialDirection = paramInt1 % 4;
/*  29 */     this.direction = (paramInt1 + paramInt2) % 4;
/*  30 */     this.dimensions = new int[] { (paramArrayOfint1[0]).length, paramArrayOfint1.length };
/*  31 */     this.mapStructure = paramArrayOfint1;
/*  32 */     this.position = paramArrayOfint;
/*  33 */     this.gameImage = new GameImage(paramString);
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  37 */     return this.dimensions[1] - 1;
/*     */   }
/*     */   
/*     */   public int getMapValAtLocation(int paramInt1, int paramInt2, int paramInt3) {
/*     */     int i;
/*  42 */     if (this.initialDirection % 2 == 0) {
/*  43 */       i = paramInt1;
/*     */       
/*  45 */       if (this.position[1] != paramInt2)
/*  46 */         return 0; 
/*     */     } else {
/*  48 */       i = paramInt2;
/*     */       
/*  50 */       if (this.position[0] != paramInt1)
/*  51 */         return 0; 
/*     */     } 
/*  53 */     if (i >= this.dimensions[0] || paramInt3 >= this.dimensions[1])
/*  54 */       return 0; 
/*  55 */     return this.mapStructure[paramInt3][i];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getClickLocation(int[] paramArrayOfint, int paramInt) {
/*  62 */     if (this.direction <= 1) {
/*  63 */       paramArrayOfint[0] = 127 - paramArrayOfint[0];
/*     */     }
/*     */     
/*  66 */     paramArrayOfint[1] = 127 - paramArrayOfint[1];
/*     */     
/*  68 */     int[] arrayOfInt = getMapValsLocationOnScreen(paramInt);
/*  69 */     paramArrayOfint = new int[] { paramArrayOfint[0] + arrayOfInt[0] * 128, paramArrayOfint[1] + arrayOfInt[1] * 128 };
/*     */     
/*  71 */     return paramArrayOfint;
/*     */   }
/*     */   
/*     */   public int[] getMapValsLocationOnScreen(int paramInt) {
/*  75 */     byte b1 = 0;
/*  76 */     byte b2 = 0;
/*     */     
/*  78 */     label13: while (b2 < this.mapStructure.length) {
/*  79 */       while (b1 < (this.mapStructure[b2]).length) {
/*  80 */         if (this.mapStructure[b2][b1] == paramInt)
/*     */           break label13; 
/*  82 */         b1++;
/*     */       } 
/*  84 */       b1 = 0;
/*  85 */       b2++;
/*     */     } 
/*     */     
/*  88 */     return new int[] { b1, b2 };
/*     */   }
/*     */   
/*     */   public int[] getPosition() {
/*  92 */     return this.position;
/*     */   }
/*     */   
/*     */   public boolean containsMapVal(int paramInt) {
/*  96 */     for (int[] arrayOfInt : this.mapStructure) {
/*  97 */       for (int i : arrayOfInt)
/*  98 */       { if (paramInt == i)
/*  99 */           return true;  } 
/* 100 */     }  return false;
/*     */   }
/*     */   
/*     */   public Button getClickedButton(GamePlayer paramGamePlayer, int[] paramArrayOfint) {
/* 104 */     for (Button button : this.game.getButtons()) {
/* 105 */       if (button.getScreen() == this && button.clicked(paramGamePlayer, paramArrayOfint))
/* 106 */         return button; 
/*     */     } 
/* 108 */     return null;
/*     */   }
/*     */   
/*     */   public BlockFace getBlockFace() {
/* 112 */     switch (this.direction) {
/*     */       case 0:
/* 114 */         return BlockFace.NORTH;
/*     */       case 1:
/* 116 */         return BlockFace.EAST;
/*     */       case 2:
/* 118 */         return BlockFace.SOUTH;
/*     */       case 3:
/* 120 */         return BlockFace.WEST;
/*     */     } 
/*     */     
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   public void renderScreen() {
/* 127 */     for (int i : getMapVals()) {
/* 128 */       GameMap gameMap = this.game.getGameMapByMapVal(i);
/* 129 */       if (gameMap != null) {
/*     */         
/* 131 */         MapMeta mapMeta = gameMap.getMapMeta();
/* 132 */         final MapView view = mapMeta.getMapView();
/* 133 */         for (MapRenderer mapRenderer : mapView.getRenderers())
/* 134 */           mapView.removeRenderer(mapRenderer); 
/* 135 */         mapView.setLocked(false);
/* 136 */         mapView.addRenderer(new GameRenderer(this.game, getMapValsLocationOnScreen(i), this));
/* 137 */         mapMeta.setMapView(mapView);
/* 138 */         gameMap.setItemMeta((ItemMeta)mapMeta);
/*     */         
/* 140 */         Bukkit.getServer().getScheduler().runTaskAsynchronously((Plugin)BoardGames.getInstance(), new Runnable()
/*     */             {
/*     */               public void run() {
/* 143 */                 view.getWorld().getPlayers().forEach(param1Player -> param1Player.sendMap(param1MapView));
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public int[] getMapVals() {
/* 150 */     ArrayList<Integer> arrayList = new ArrayList();
/* 151 */     for (int[] arrayOfInt1 : this.mapStructure) {
/* 152 */       for (int i : arrayOfInt1) {
/* 153 */         if (i != 0)
/* 154 */           arrayList.add(Integer.valueOf(i)); 
/*     */       } 
/* 156 */     }  int[] arrayOfInt = new int[arrayList.size()];
/* 157 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/* 158 */       arrayOfInt[b] = ((Integer)arrayList.get(b)).intValue();
/*     */     }
/*     */     
/* 161 */     return arrayOfInt;
/*     */   }
/*     */   
/*     */   public int getDirection() {
/* 165 */     return this.direction;
/*     */   }
/*     */   
/*     */   public int getInitialDirection() {
/* 169 */     return this.initialDirection;
/*     */   }
/*     */   
/*     */   public GameImage getGameImage() {
/* 173 */     return this.gameImage;
/*     */   }
/*     */   
/*     */   public void setGameImage(GameImage paramGameImage) {
/* 177 */     this.gameImage = paramGameImage;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\maps\Screen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
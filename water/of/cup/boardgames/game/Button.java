/*     */ package water.of.cup.boardgames.game;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.ArrayList;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.game.maps.Screen;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Button
/*     */ {
/*     */   private ArrayList<GamePlayer> visiblePlayers;
/*     */   private boolean visibleForAll;
/*     */   private Game game;
/*     */   private String name;
/*     */   private boolean clickAble;
/*     */   private int[] location;
/*     */   private int rotation;
/*     */   private GameImage image;
/*     */   private boolean turnBased;
/*     */   private boolean renderTurnBased;
/*     */   private boolean canClickInvisible;
/*     */   private boolean visible;
/*     */   private Screen screen;
/*     */   
/*     */   public Button(Game paramGame, String paramString1, int[] paramArrayOfint, int paramInt, String paramString2) {
/*  31 */     this.image = new GameImage(BoardGames.getImageManager().getImage(paramString1), paramInt);
/*  32 */     this.visiblePlayers = new ArrayList<>();
/*  33 */     this.visibleForAll = true;
/*  34 */     this.game = paramGame;
/*  35 */     this.name = paramString2;
/*  36 */     this.clickAble = false;
/*  37 */     this.location = paramArrayOfint;
/*  38 */     this.rotation = paramInt;
/*  39 */     this.image = new GameImage(BoardGames.getImageManager().getImage(paramString1), paramInt);
/*  40 */     this.turnBased = false;
/*  41 */     this.renderTurnBased = false;
/*  42 */     this.visible = true;
/*     */   }
/*     */   
/*     */   public Button(Game paramGame, GameImage paramGameImage, int[] paramArrayOfint, int paramInt, String paramString) {
/*  46 */     this.visiblePlayers = new ArrayList<>();
/*  47 */     this.visibleForAll = true;
/*  48 */     this.game = paramGame;
/*  49 */     this.name = paramString;
/*  50 */     this.clickAble = false;
/*  51 */     this.location = paramArrayOfint;
/*  52 */     this.rotation = paramInt;
/*  53 */     this.image = paramGameImage.clone();
/*  54 */     this.image.setRotation(paramInt);
/*  55 */     this.turnBased = false;
/*  56 */     this.renderTurnBased = false;
/*  57 */     this.visible = true;
/*  58 */     this.canClickInvisible = false;
/*     */   }
/*     */   
/*     */   public void changeLocationByRotation() {
/*  62 */     int[] arrayOfInt = this.image.getDimensions();
/*  63 */     if (this.rotation >= 2) {
/*  64 */       this.location[1] = this.location[1] - arrayOfInt[1] - 1;
/*     */     }
/*  66 */     if (this.rotation == 1 || this.rotation == 2) {
/*  67 */       this.location[0] = this.location[0] - arrayOfInt[0] - 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean clicked(GamePlayer paramGamePlayer, int[] paramArrayOfint) {
/*  73 */     if (!this.clickAble) {
/*  74 */       return false;
/*     */     }
/*  76 */     if (!this.canClickInvisible && !visibleForPlayer(paramGamePlayer)) {
/*  77 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  82 */     if (this.image.equals(null)) {
/*  83 */       return false;
/*     */     }
/*  85 */     int[] arrayOfInt1 = (int[])this.location.clone();
/*  86 */     int[] arrayOfInt2 = { this.location[0] + this.image.getDimensions()[0], this.location[1] + this.image.getDimensions()[1] };
/*     */ 
/*     */     
/*  89 */     if (((paramArrayOfint[0] < arrayOfInt1[0]) ? true : false) != ((paramArrayOfint[0] < arrayOfInt2[0]) ? true : false)) if (((paramArrayOfint[1] < arrayOfInt1[1]) ? true : false) != ((paramArrayOfint[1] < arrayOfInt2[1]) ? true : false))
/*     */       {
/*     */         
/*  92 */         return true; }  
/*     */     return false;
/*     */   }
/*     */   public boolean visibleForPlayer(GamePlayer paramGamePlayer) {
/*  96 */     if (!this.visible) {
/*  97 */       return false;
/*     */     }
/*  99 */     if (this.visibleForAll) {
/* 100 */       return true;
/*     */     }
/* 102 */     if (!this.visiblePlayers.contains(paramGamePlayer)) {
/* 103 */       return false;
/*     */     }
/* 105 */     if (!this.game.getTurn().equals(paramGamePlayer) && this.renderTurnBased) {
/* 106 */       return false;
/*     */     }
/* 108 */     return true;
/*     */   }
/*     */   
/*     */   public Boolean getRenderTurnBased() {
/* 112 */     return Boolean.valueOf(this.renderTurnBased);
/*     */   }
/*     */   
/*     */   public void setRenderTurnBased(Boolean paramBoolean) {
/* 116 */     this.renderTurnBased = paramBoolean.booleanValue();
/*     */   }
/*     */   
/*     */   public GameImage getImage() {
/* 120 */     return this.image;
/*     */   }
/*     */   
/*     */   public void setClickable(boolean paramBoolean) {
/* 124 */     this.clickAble = paramBoolean;
/*     */   }
/*     */   
/*     */   public void setImage(GameImage paramGameImage) {
/* 128 */     this.image = paramGameImage;
/*     */   }
/*     */   
/*     */   public void setImage(String paramString) {
/* 132 */     this.image = new GameImage(BoardGames.getImageManager().getImage(paramString), this.rotation);
/*     */   }
/*     */   
/*     */   public void setVisible(boolean paramBoolean) {
/* 136 */     this.visible = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean isVisibleForAll() {
/* 140 */     return (this.visibleForAll && this.visible);
/*     */   }
/*     */   
/*     */   public void setVisibleForAll(boolean paramBoolean) {
/* 144 */     this.visibleForAll = paramBoolean;
/*     */   }
/*     */   
/*     */   public int[] getLocation() {
/* 148 */     return this.location;
/*     */   }
/*     */   
/*     */   public void addVisiblePlayer(GamePlayer paramGamePlayer) {
/* 152 */     this.visiblePlayers.add(paramGamePlayer);
/*     */   }
/*     */   
/*     */   public ArrayList<GamePlayer> getVisablePlayers() {
/* 156 */     return this.visiblePlayers;
/*     */   }
/*     */   
/*     */   public void clearVisablePlayers() {
/* 160 */     this.visiblePlayers.clear();
/*     */   }
/*     */   
/*     */   public String getName() {
/* 164 */     return "" + this.name;
/*     */   }
/*     */   
/*     */   public void setName(String paramString) {
/* 168 */     this.name = paramString;
/*     */   }
/*     */   
/*     */   public void setScreen(Screen paramScreen) {
/* 172 */     this.screen = paramScreen;
/*     */   }
/*     */   
/*     */   public Screen getScreen() {
/* 176 */     return this.screen;
/*     */   }
/*     */   
/*     */   public void setImage(BufferedImage paramBufferedImage) {
/* 180 */     this.image = new GameImage(paramBufferedImage, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRotation() {
/* 185 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public void setRotation(int paramInt) {
/* 189 */     this.rotation = paramInt;
/* 190 */     this.image.setRotation(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean getCanClickInvisible() {
/* 195 */     return Boolean.valueOf(this.canClickInvisible);
/*     */   }
/*     */   
/*     */   public void setCanClickInvisible(Boolean paramBoolean) {
/* 199 */     this.canClickInvisible = paramBoolean.booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\Button.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package water.of.cup.boardgames.game.maps;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.map.MapCanvas;
/*    */ import org.bukkit.map.MapRenderer;
/*    */ import org.bukkit.map.MapView;
/*    */ import water.of.cup.boardgames.game.Button;
/*    */ import water.of.cup.boardgames.game.Game;
/*    */ import water.of.cup.boardgames.game.GameImage;
/*    */ import water.of.cup.boardgames.game.GamePlayer;
/*    */ 
/*    */ public class GameRenderer
/*    */   extends MapRenderer
/*    */ {
/*    */   private Game game;
/*    */   private Screen screen;
/*    */   private int[] loc;
/*    */   private HashSet<UUID> rendered;
/*    */   
/*    */   public GameRenderer(Game paramGame, int[] paramArrayOfint) {
/* 24 */     super(true);
/* 25 */     this.game = paramGame;
/* 26 */     this.loc = paramArrayOfint;
/* 27 */     this.rendered = new HashSet<>();
/*    */   }
/*    */   
/*    */   public GameRenderer(Game paramGame, int[] paramArrayOfint, Screen paramScreen) {
/* 31 */     super(true);
/* 32 */     this.game = paramGame;
/* 33 */     this.loc = paramArrayOfint;
/* 34 */     this.screen = paramScreen;
/* 35 */     this.rendered = new HashSet<>();
/*    */   }
/*    */   
/*    */   public void render(MapView paramMapView, MapCanvas paramMapCanvas, Player paramPlayer) {
/*    */     GameImage gameImage;
/* 40 */     if (this.rendered == null)
/* 41 */       this.rendered = new HashSet<>(); 
/* 42 */     if (this.rendered.contains(paramPlayer.getUniqueId())) {
/*    */       return;
/*    */     }
/*    */     
/* 46 */     this.rendered.add(paramPlayer.getUniqueId());
/*    */     
/* 48 */     GamePlayer gamePlayer = this.game.getGamePlayer(paramPlayer);
/* 49 */     boolean bool = (gamePlayer != null) ? true : false;
/*    */ 
/*    */     
/* 52 */     if (this.screen != null) {
/* 53 */       gameImage = this.screen.getGameImage().clone();
/*    */     } else {
/* 55 */       gameImage = this.game.getGameImage().clone();
/*    */     } 
/*    */     
/* 58 */     ArrayList arrayList = new ArrayList(this.game.getButtons());
/* 59 */     for (Button button : arrayList) {
/*    */ 
/*    */ 
/*    */       
/* 63 */       if (button.getScreen() == this.screen && (button
/* 64 */         .isVisibleForAll() || (bool && button.visibleForPlayer(gamePlayer)))) {
/* 65 */         gameImage.addGameImage(button.getImage(), button.getLocation());
/*    */       }
/*    */     } 
/*    */     
/* 69 */     gameImage.cropMap(this.loc);
/* 70 */     paramMapCanvas.drawImage(0, 0, gameImage.getImage((this.screen == null) ? this.game.getRotation() : 0));
/*    */   }
/*    */   
/*    */   public void rerender() {
/* 74 */     this.rendered = new HashSet<>();
/*    */   }
/*    */   
/*    */   public void rerender(Player paramPlayer) {
/* 78 */     if (this.rendered == null)
/* 79 */       this.rendered = new HashSet<>(); 
/* 80 */     this.rendered.remove(paramPlayer.getUniqueId());
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\maps\GameRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
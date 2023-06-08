/*     */ package water.of.cup.boardgames.listeners;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.ItemFrame;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.inventory.EquipmentSlot;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.util.BoundingBox;
/*     */ import org.bukkit.util.RayTraceResult;
/*     */ import org.bukkit.util.Vector;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.GameManager;
/*     */ import water.of.cup.boardgames.game.games.chess.ChessBoardsUtil;
/*     */ import water.of.cup.boardgames.game.maps.GameMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BoardInteract
/*     */   implements Listener
/*     */ {
/*  35 */   private BoardGames instance = BoardGames.getInstance();
/*  36 */   private GameManager gameManager = this.instance.getGameManager();
/*  37 */   private final HashMap<Player, Long> clickDelays = new HashMap<>();
/*     */   
/*     */   @EventHandler
/*     */   public void clickBoard(PlayerInteractEvent paramPlayerInteractEvent) {
/*  41 */     Player player = paramPlayerInteractEvent.getPlayer();
/*     */ 
/*     */     
/*  44 */     Block block = player.getTargetBlock(null, 5);
/*  45 */     if (!block.getType().equals(Material.BARRIER)) {
/*     */       return;
/*     */     }
/*  48 */     if (ConfigUtil.PERMISSIONS_ENABLED.toBoolean() && 
/*  49 */       !player.hasPermission("boardgames.interact")) {
/*     */       return;
/*     */     }
/*     */     
/*  53 */     if ((paramPlayerInteractEvent.getAction().equals(Action.RIGHT_CLICK_AIR) || paramPlayerInteractEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && 
/*  54 */       paramPlayerInteractEvent.getHand().equals(EquipmentSlot.HAND)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  60 */     if (this.clickDelays.containsKey(player)) {
/*  61 */       long l = System.currentTimeMillis();
/*  62 */       if (l - ((Long)this.clickDelays.get(player)).longValue() >= ConfigUtil.BOARD_CLICK_DELAY.toInteger()) {
/*  63 */         this.clickDelays.remove(player);
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  71 */     Vector vector = player.getEyeLocation().getDirection();
/*     */ 
/*     */     
/*  74 */     Collection collection = player.getWorld().getNearbyEntities(player.getLocation(), 4.0D, 4.0D, 4.0D);
/*     */     
/*  76 */     RayTraceResult rayTraceResult = null;
/*  77 */     ItemFrame itemFrame = null;
/*  78 */     Game game = null;
/*  79 */     GameMap gameMap = null;
/*  80 */     boolean bool = false;
/*     */     
/*  82 */     double d = 100.0D;
/*  83 */     for (Entity entity : collection) {
/*     */       
/*  85 */       if (!(entity instanceof ItemFrame))
/*     */         continue; 
/*  87 */       ItemFrame itemFrame1 = (ItemFrame)entity;
/*  88 */       ItemStack itemStack = itemFrame1.getItem();
/*  89 */       boolean bool1 = GameMap.isGameMap(itemStack);
/*  90 */       if (bool1 || ChessBoardsUtil.isChessBoardsMap(itemStack)) {
/*     */ 
/*     */         
/*  93 */         GameMap gameMap1 = null;
/*     */         
/*  95 */         if (bool1) {
/*  96 */           gameMap1 = new GameMap(itemStack);
/*     */         }
/*     */ 
/*     */         
/* 100 */         Vector vector1 = itemFrame1.getLocation().toVector();
/* 101 */         double d1 = vector1.getX();
/* 102 */         double d2 = vector1.getY();
/* 103 */         double d3 = vector1.getZ();
/*     */ 
/*     */         
/* 106 */         double[] arrayOfDouble = { 0.5D, 0.0313D, 0.5D };
/* 107 */         if (itemFrame1.getAttachedFace() == BlockFace.NORTH || itemFrame1.getAttachedFace() == BlockFace.SOUTH)
/* 108 */           arrayOfDouble = new double[] { 0.5D, 0.5D, 0.0313D }; 
/* 109 */         if (itemFrame1.getAttachedFace() == BlockFace.EAST || itemFrame1.getAttachedFace() == BlockFace.WEST) {
/* 110 */           arrayOfDouble = new double[] { 0.0313D, 0.5D, 0.5D };
/*     */         }
/*     */         
/* 113 */         BoundingBox boundingBox = new BoundingBox(d1 - arrayOfDouble[0], d2 - arrayOfDouble[1], d3 - arrayOfDouble[2], d1 + arrayOfDouble[0], d2 + arrayOfDouble[1], d3 + arrayOfDouble[2]);
/*     */         
/* 115 */         RayTraceResult rayTraceResult1 = boundingBox.rayTrace(player.getEyeLocation().toVector(), vector, 5.0D);
/* 116 */         if (rayTraceResult1 != null) {
/* 117 */           double d4 = rayTraceResult1.getHitPosition().distance(player.getEyeLocation().toVector());
/* 118 */           if (d4 > d)
/*     */             continue; 
/* 120 */           d = d4;
/* 121 */           rayTraceResult = rayTraceResult1;
/* 122 */           itemFrame = itemFrame1;
/*     */           
/* 124 */           if (!bool1) {
/* 125 */             bool = true; continue;
/*     */           } 
/* 127 */           gameMap = gameMap1;
/* 128 */           game = gameMap1.getGame();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 135 */     if (bool) {
/* 136 */       Game game1 = this.gameManager.newGame("Chess", 0);
/*     */       
/* 138 */       if (game1 == null) {
/*     */         return;
/*     */       }
/* 141 */       ChessBoardsUtil.removeChessBoard(itemFrame);
/*     */       
/* 143 */       Location location = itemFrame.getLocation().getBlock().getLocation();
/* 144 */       game1.replace(location, game1.getRotation(), 1);
/* 145 */       this.gameManager.addGame(game1);
/*     */       
/*     */       return;
/*     */     } 
/* 149 */     if (game == null && itemFrame != null) {
/* 150 */       Game game1 = this.gameManager.newGame(gameMap.getGameName(), gameMap.getRotation());
/*     */       
/* 152 */       if (game1 == null) {
/*     */         return;
/*     */       }
/* 155 */       Location location = itemFrame.getLocation().getBlock().getLocation();
/* 156 */       game1.replace(location, game1.getRotation(), gameMap.getMapVal());
/* 157 */       this.gameManager.addGame(game1);
/*     */       
/*     */       return;
/*     */     } 
/* 161 */     if (game == null) {
/*     */       return;
/*     */     }
/* 164 */     if (itemFrame.getAttachedFace().getOppositeFace() != rayTraceResult.getHitBlockFace()) {
/*     */       return;
/*     */     }
/*     */     
/* 168 */     if (paramPlayerInteractEvent.getAction().equals(Action.LEFT_CLICK_BLOCK) || paramPlayerInteractEvent.getAction().equals(Action.LEFT_CLICK_AIR)) {
/* 169 */       paramPlayerInteractEvent.setCancelled(true);
/*     */ 
/*     */       
/* 172 */       if (game.isIngame() && game.hasPlayer(player)) {
/* 173 */         game.displayGameInventory(player);
/*     */         
/*     */         return;
/*     */       } 
/* 177 */       if (ConfigUtil.PERMISSIONS_ENABLED.toBoolean() && 
/* 178 */         !player.hasPermission("boardgames.destroy")) {
/*     */         return;
/*     */       }
/* 181 */       if (paramPlayerInteractEvent.getClickedBlock() != null && !game.isIngame() && game.destroy(itemFrame)) {
/*     */ 
/*     */         
/* 184 */         ItemStack itemStack = game.getBoardItem();
/* 185 */         if (itemStack != null) {
/* 186 */           player.getWorld().dropItem(paramPlayerInteractEvent.getClickedBlock().getLocation(), itemStack);
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 192 */     if (paramPlayerInteractEvent.getAction().equals(Action.RIGHT_CLICK_AIR) || paramPlayerInteractEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
/*     */ 
/*     */       
/* 195 */       if (this.gameManager.getGameByPlayer(player) != null && this.gameManager.getGameByPlayer(player) != game) {
/* 196 */         player.sendMessage(ConfigUtil.CHAT_PLAYER_INGAME.toString());
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
/*     */         return;
/*     */       } 
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
/* 223 */       if (game.hasPlayer(player) || !game.hasGameInventory() || (game.allowOutsideClicks() && game.isIngame())) {
/* 224 */         Vector vector1 = rayTraceResult.getHitPosition();
/* 225 */         double[] arrayOfDouble = { vector1.getX(), vector1.getZ() };
/* 226 */         if (itemFrame.getAttachedFace() == BlockFace.WEST || itemFrame.getAttachedFace() == BlockFace.EAST) {
/* 227 */           arrayOfDouble = new double[] { vector1.getZ(), vector1.getY() };
/*     */         }
/* 229 */         if (itemFrame.getAttachedFace() == BlockFace.NORTH || itemFrame.getAttachedFace() == BlockFace.SOUTH) {
/* 230 */           arrayOfDouble = new double[] { vector1.getX(), vector1.getY() };
/*     */         }
/*     */         
/* 233 */         this.clickDelays.put(player, Long.valueOf(System.currentTimeMillis()));
/* 234 */         game.click(player, arrayOfDouble, (ItemStack)gameMap);
/*     */       } else {
/* 236 */         game.displayGameInventory(player);
/*     */       } 
/*     */ 
/*     */       
/* 240 */       paramPlayerInteractEvent.setCancelled(true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\listeners\BoardInteract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
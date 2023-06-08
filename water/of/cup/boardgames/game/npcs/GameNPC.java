/*    */ package water.of.cup.boardgames.game.npcs;
/*    */ 
/*    */ import net.citizensnpcs.api.CitizensAPI;
/*    */ import net.citizensnpcs.api.npc.NPC;
/*    */ import net.citizensnpcs.api.npc.NPCRegistry;
/*    */ import net.citizensnpcs.trait.Gravity;
/*    */ import net.citizensnpcs.trait.SkinTrait;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.EntityType;
/*    */ import org.bukkit.entity.Player;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ import water.of.cup.boardgames.game.MathUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GameNPC
/*    */ {
/*    */   private NPC gameNPC;
/*    */   private Location npcLocation;
/*    */   private double[] loc;
/* 25 */   public static NPCRegistry REGISTRY = null;
/*    */   protected abstract String getName();
/*    */   public GameNPC(double[] paramArrayOfdouble) {
/* 28 */     if (REGISTRY == null && BoardGames.hasCitizens()) {
/* 29 */       REGISTRY = CitizensAPI.createAnonymousNPCRegistry(new GameNPCRegistry());
/*    */     }
/* 31 */     this.loc = paramArrayOfdouble;
/*    */   }
/*    */   protected abstract NPCSkin getSkin();
/*    */   public void spawnNPC() {
/* 35 */     if (!BoardGames.hasCitizens() || this.npcLocation == null)
/*    */       return; 
/* 37 */     if (this.gameNPC != null) {
/*    */       return;
/*    */     }
/* 40 */     this.gameNPC = REGISTRY.createNPC(EntityType.PLAYER, getName());
/*    */     
/* 42 */     ((Gravity)this.gameNPC.getOrAddTrait(Gravity.class)).gravitate(true);
/*    */     
/* 44 */     if (getSkin() != null) {
/* 45 */       ((SkinTrait)this.gameNPC.getOrAddTrait(SkinTrait.class)).setSkinPersistent(getName(), getSkin().getSkinSig(), getSkin().getSkinData());
/*    */     }
/* 47 */     if (!this.gameNPC.isSpawned())
/* 48 */       this.gameNPC.spawn(this.npcLocation); 
/*    */   }
/*    */   
/*    */   public void setMapValLoc(Location paramLocation, int paramInt) {
/* 52 */     double[] arrayOfDouble1 = { this.loc[0], this.loc[2] };
/* 53 */     double[] arrayOfDouble2 = { 0.5D, 0.5D };
/* 54 */     for (int i = paramInt; i > 0; i--) {
/* 55 */       arrayOfDouble1 = MathUtils.rotatePointAroundPoint90Degrees(arrayOfDouble2, arrayOfDouble1);
/*    */     }
/* 57 */     this.npcLocation = paramLocation.clone().add(arrayOfDouble1[0], this.loc[1], arrayOfDouble1[1]);
/*    */   }
/*    */   
/*    */   public void lookAt(Player paramPlayer) {
/* 61 */     if (hasSpawned())
/* 62 */       this.gameNPC.faceLocation(paramPlayer.getLocation()); 
/*    */   }
/*    */   
/*    */   public void removeNPC() {
/* 66 */     if (this.gameNPC == null) {
/*    */       return;
/*    */     }
/* 69 */     if (this.gameNPC.isSpawned()) {
/* 70 */       this.gameNPC.despawn();
/*    */     }
/* 72 */     this.gameNPC.destroy();
/* 73 */     this.gameNPC = null;
/*    */   }
/*    */   
/*    */   private boolean hasSpawned() {
/* 77 */     return (this.gameNPC != null && this.gameNPC.isSpawned());
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\npcs\GameNPC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
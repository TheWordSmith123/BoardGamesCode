/*    */ package water.of.cup.boardgames.game.maps;
/*    */ 
/*    */ import org.bukkit.block.BlockFace;
/*    */ 
/*    */ public class MapData {
/*    */   int mapVal;
/*    */   BlockFace blockFace;
/*    */   
/*    */   public MapData(int paramInt, BlockFace paramBlockFace) {
/* 10 */     this.mapVal = paramInt;
/* 11 */     this.blockFace = paramBlockFace;
/*    */   }
/*    */   
/*    */   public int getMapVal() {
/* 15 */     return this.mapVal;
/*    */   }
/*    */   
/*    */   public BlockFace getBlockFace() {
/* 19 */     return this.blockFace;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\maps\MapData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
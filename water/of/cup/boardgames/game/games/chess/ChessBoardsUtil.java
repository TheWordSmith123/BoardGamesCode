/*    */ package water.of.cup.boardgames.game.games.chess;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.ArrayList;
/*    */ import java.util.regex.Pattern;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.NamespacedKey;
/*    */ import org.bukkit.entity.ItemFrame;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.meta.MapMeta;
/*    */ import org.bukkit.persistence.PersistentDataType;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChessBoardsUtil
/*    */ {
/* 23 */   public static final ArrayList<Integer> OLD_MAP_IDS = new ArrayList<>();
/*    */   
/*    */   public static void loadGames() {
/* 26 */     File file = new File(BoardGames.getInstance().getDataFolder() + "/saved_games");
/*    */     
/* 28 */     if (!file.exists() && BoardGames.getInstance().getDataFolder().getParentFile() != null) {
/* 29 */       file = new File(BoardGames.getInstance().getDataFolder().getParentFile() + "/ChessBoards/saved_games");
/*    */     }
/*    */     
/* 32 */     File[] arrayOfFile = file.listFiles();
/*    */     
/* 34 */     if (arrayOfFile == null)
/*    */       return; 
/* 36 */     for (File file1 : arrayOfFile) {
/* 37 */       if (file1.isFile()) {
/* 38 */         int i = Integer.parseInt(file1.getName().split("_")[1].split(Pattern.quote("."))[0]);
/* 39 */         OLD_MAP_IDS.add(Integer.valueOf(i));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static boolean isChessBoardsMap(ItemStack paramItemStack) {
/* 45 */     if (paramItemStack.getItemMeta() == null || !(paramItemStack.getItemMeta() instanceof MapMeta)) return false; 
/* 46 */     MapMeta mapMeta = (MapMeta)paramItemStack.getItemMeta();
/*    */     
/* 48 */     if (mapMeta.getMapView() == null) return false;
/*    */     
/* 50 */     int i = mapMeta.getMapView().getId();
/* 51 */     return OLD_MAP_IDS.contains(Integer.valueOf(i));
/*    */   }
/*    */   
/*    */   public static boolean isChessBoardsItem(ItemStack paramItemStack) {
/* 55 */     if (paramItemStack.getItemMeta() == null) return false; 
/* 56 */     NamespacedKey namespacedKey = new NamespacedKey((Plugin)BoardGames.getInstance(), "chess_board");
/* 57 */     return paramItemStack.getItemMeta().getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE);
/*    */   }
/*    */   
/*    */   public static void removeChessBoard(ItemFrame paramItemFrame) {
/* 61 */     Location location = paramItemFrame.getLocation();
/* 62 */     paramItemFrame.remove();
/* 63 */     location.getBlock().setType(Material.AIR);
/*    */     
/* 65 */     if (!isChessBoardsMap(paramItemFrame.getItem()))
/*    */       return; 
/* 67 */     ItemStack itemStack = paramItemFrame.getItem();
/* 68 */     MapMeta mapMeta = (MapMeta)itemStack.getItemMeta();
/*    */     
/* 70 */     if (mapMeta == null || mapMeta.getMapView() == null)
/*    */       return; 
/* 72 */     int i = mapMeta.getMapView().getId();
/*    */     
/* 74 */     OLD_MAP_IDS.remove(Integer.valueOf(i));
/*    */     
/* 76 */     deleteChessBoardsGame(i);
/*    */   }
/*    */   
/*    */   private static void deleteChessBoardsGame(int paramInt) {
/* 80 */     File file = new File(BoardGames.getInstance().getDataFolder(), "saved_games/game_" + paramInt + ".txt");
/* 81 */     if (!file.exists()) {
/*    */       return;
/*    */     }
/* 84 */     file.delete();
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\chess\ChessBoardsUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
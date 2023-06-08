/*    */ package water.of.cup.boardgames.config;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import water.of.cup.boardgames.BoardGames;
/*    */ 
/*    */ 
/*    */ public class GameRecipe
/*    */ {
/* 11 */   private final BoardGames instance = BoardGames.getInstance();
/*    */   private final HashMap<String, String> recipe;
/*    */   private final ArrayList<String> shape;
/*    */   private final String name;
/*    */   
/*    */   public GameRecipe(String paramString, HashMap<String, String> paramHashMap, ArrayList<String> paramArrayList) {
/* 17 */     this.name = paramString;
/* 18 */     this.recipe = paramHashMap;
/* 19 */     this.shape = paramArrayList;
/*    */   }
/*    */   
/*    */   public void addToConfig() {
/* 23 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 24 */     FileConfiguration fileConfiguration = this.instance.getConfig();
/*    */     
/* 26 */     String str = "settings.games." + this.name + ".recipe";
/*    */     
/* 28 */     hashMap.put(str + ".shape", this.shape);
/*    */     
/* 30 */     if (!fileConfiguration.contains(str + ".ingredients")) {
/* 31 */       for (String str1 : this.recipe.keySet()) {
/* 32 */         hashMap.put(str + ".ingredients." + str1, this.recipe.get(str1));
/*    */       }
/*    */     }
/*    */     
/* 36 */     for (String str1 : hashMap.keySet()) {
/* 37 */       if (!fileConfiguration.contains(str1)) {
/* 38 */         fileConfiguration.set(str1, hashMap.get(str1));
/*    */       }
/*    */     } 
/*    */     
/* 42 */     this.instance.saveConfig();
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\config\GameRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
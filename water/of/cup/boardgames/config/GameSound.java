/*    */ package water.of.cup.boardgames.config;
/*    */ 
/*    */ import org.bukkit.Sound;
/*    */ 
/*    */ public class GameSound
/*    */ {
/*    */   private final String name;
/*    */   private final Sound sound;
/*    */   
/*    */   public GameSound(String paramString, Sound paramSound) {
/* 11 */     this.name = paramString;
/* 12 */     this.sound = paramSound;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 16 */     return this.name;
/*    */   }
/*    */   
/*    */   public Sound getSound() {
/* 20 */     return this.sound;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\config\GameSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
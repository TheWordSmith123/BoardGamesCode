/*    */ package water.of.cup.boardgames.game.inventories;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.properties.Property;
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.enchantments.Enchantment;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemFlag;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.meta.ItemMeta;
/*    */ import org.bukkit.inventory.meta.SkullMeta;
/*    */ import water.of.cup.boardgames.config.ConfigUtil;
/*    */ 
/*    */ 
/*    */ public class InventoryUtils
/*    */ {
/*    */   public static final String UP_ARROW = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNThmZTI1MWE0MGU0MTY3ZDM1ZDA4MWMyNzg2OWFjMTUxYWY5NmI2YmQxNmRkMjgzNGQ1ZGM3MjM1ZjQ3NzkxZCJ9fX0=";
/*    */   public static final String DOWN_ARROW = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI3Y2U2ODNkMDg2OGFhNDM3OGFlYjYwY2FhNWVhODA1OTZiY2ZmZGFiNmI1YWYyZDEyNTk1ODM3YTg0ODUzIn19fQ==";
/*    */   public static final String RIGHT_ARROW = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJmM2EyZGZjZTBjM2RhYjdlZTEwZGIzODVlNTIyOWYxYTM5NTM0YThiYTI2NDYxNzhlMzdjNGZhOTNiIn19fQ==";
/*    */   public static final String LEFT_ARROW = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmIwZjZlOGFmNDZhYzZmYWY4ODkxNDE5MWFiNjZmMjYxZDY3MjZhNzk5OWM2MzdjZjJlNDE1OWZlMWZjNDc3In19fQ==";
/*    */   
/*    */   public static ItemStack getPlayerHead(Player paramPlayer, boolean paramBoolean) {
/* 26 */     ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
/*    */     
/* 28 */     if (!ConfigUtil.LOAD_SKULLS.toBoolean()) {
/* 29 */       return itemStack;
/*    */     }
/* 31 */     ItemMeta itemMeta = itemStack.getItemMeta();
/*    */     
/* 33 */     if (paramBoolean) {
/* 34 */       itemStack.addUnsafeEnchantment(Enchantment.LURE, 1);
/* 35 */       assert itemMeta != null;
/* 36 */       itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
/* 37 */       itemStack.setItemMeta(itemMeta);
/*    */     } 
/*    */     
/* 40 */     SkullMeta skullMeta = (SkullMeta)itemMeta;
/* 41 */     assert skullMeta != null;
/* 42 */     skullMeta.setOwningPlayer((OfflinePlayer)paramPlayer);
/* 43 */     itemStack.setItemMeta((ItemMeta)skullMeta);
/* 44 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static ItemStack getPlayerHead(Player paramPlayer) {
/* 48 */     return getPlayerHead(paramPlayer, false);
/*    */   }
/*    */   
/*    */   public static ItemStack getCustomTextureHead(String paramString) {
/* 52 */     ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
/*    */     
/* 54 */     if (!ConfigUtil.LOAD_SKULLS.toBoolean()) {
/* 55 */       return itemStack;
/*    */     }
/* 57 */     SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
/* 58 */     GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
/* 59 */     gameProfile.getProperties().put("textures", new Property("textures", paramString));
/* 60 */     Field field = null;
/*    */     try {
/* 62 */       field = skullMeta.getClass().getDeclaredField("profile");
/* 63 */       field.setAccessible(true);
/* 64 */       field.set(skullMeta, gameProfile);
/* 65 */     } catch (IllegalArgumentException|IllegalAccessException|NoSuchFieldException|SecurityException illegalArgumentException) {
/* 66 */       illegalArgumentException.printStackTrace();
/*    */     } 
/*    */     
/* 69 */     skullMeta.setDisplayName(" ");
/* 70 */     itemStack.setItemMeta((ItemMeta)skullMeta);
/* 71 */     return itemStack;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\InventoryUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package water.of.cup.boardgames.game.inventories;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import org.bukkit.Material;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameOption
/*     */ {
/*     */   private final String key;
/*     */   private final Material material;
/*     */   private final GameOptionType optionType;
/*     */   private final String defaultValue;
/*     */   private final List<String> customValues;
/*     */   private final String label;
/*     */   private final boolean requiresEconomy;
/*     */   private final int minIntValue;
/*     */   private final int maxIntValue;
/*     */   
/*     */   public GameOption(String paramString1, Material paramMaterial, GameOptionType paramGameOptionType, String paramString2, String paramString3, boolean paramBoolean, int paramInt1, int paramInt2) {
/*  24 */     this.key = paramString1;
/*  25 */     this.material = paramMaterial;
/*  26 */     this.optionType = paramGameOptionType;
/*  27 */     this.label = paramString2;
/*  28 */     this.defaultValue = paramString3;
/*  29 */     this.customValues = null;
/*  30 */     this.requiresEconomy = paramBoolean;
/*  31 */     this.minIntValue = paramInt1;
/*  32 */     this.maxIntValue = paramInt2;
/*     */   }
/*     */   
/*     */   public GameOption(String paramString1, Material paramMaterial, GameOptionType paramGameOptionType, String paramString2, String paramString3, boolean paramBoolean) {
/*  36 */     this.key = paramString1;
/*  37 */     this.material = paramMaterial;
/*  38 */     this.optionType = paramGameOptionType;
/*  39 */     this.label = paramString2;
/*  40 */     this.defaultValue = paramString3;
/*  41 */     this.customValues = null;
/*  42 */     this.requiresEconomy = paramBoolean;
/*  43 */     this.minIntValue = 0;
/*  44 */     this.maxIntValue = Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   public GameOption(String paramString1, Material paramMaterial, GameOptionType paramGameOptionType, String paramString2, String paramString3, List<String> paramList) {
/*  48 */     this.key = paramString1;
/*  49 */     this.material = paramMaterial;
/*  50 */     this.optionType = paramGameOptionType;
/*  51 */     this.label = paramString2;
/*  52 */     this.defaultValue = paramString3;
/*  53 */     this.customValues = paramList;
/*  54 */     this.requiresEconomy = false;
/*  55 */     this.minIntValue = 0;
/*  56 */     this.maxIntValue = 0;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  60 */     return this.key;
/*     */   }
/*     */   
/*     */   public GameOptionType getOptionType() {
/*  64 */     return this.optionType;
/*     */   }
/*     */   
/*     */   public String getDefaultValue() {
/*  68 */     return this.defaultValue;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public List<String> getCustomValues() {
/*  73 */     return this.customValues;
/*     */   }
/*     */   
/*     */   public Material getMaterial() {
/*  77 */     return this.material;
/*     */   }
/*     */   
/*     */   public String getLabel() {
/*  81 */     return this.label;
/*     */   }
/*     */   
/*     */   public boolean requiresEconomy() {
/*  85 */     return this.requiresEconomy;
/*     */   }
/*     */   
/*     */   public int getMinIntValue() {
/*  89 */     return this.minIntValue;
/*     */   }
/*     */   
/*     */   public int getMaxIntValue() {
/*  93 */     return this.maxIntValue;
/*     */   }
/*     */   
/*     */   public static GameOption getWagerGameOption() {
/*  97 */     return new GameOption("wager", Material.GOLD_INGOT, GameOptionType.COUNT, ConfigUtil.GUI_WAGER_LABEL.toString(), "0", true);
/*     */   }
/*     */   
/*     */   public static GameOption getTeamSelectGameOption(ArrayList<String> paramArrayList) {
/* 101 */     return new GameOption("team", Material.PAPER, GameOptionType.COUNT, ConfigUtil.GUI_TEAM_LABEL.toString(), paramArrayList.get(0), paramArrayList);
/*     */   }
/*     */   
/*     */   public static GameOption getTradeItemsOption() {
/* 105 */     ArrayList<String> arrayList = new ArrayList();
/* 106 */     arrayList.add(ConfigUtil.GUI_WAGERITEMS_ENABLED_LABEL.toString());
/* 107 */     arrayList.add(ConfigUtil.GUI_WAGERITEMS_DISABLED_LABEL.toString());
/* 108 */     return new GameOption("trade", Material.CHEST, GameOptionType.TOGGLE, ConfigUtil.GUI_WAGERITEMS_LABEL.toString(), arrayList.get(1), arrayList);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\GameOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
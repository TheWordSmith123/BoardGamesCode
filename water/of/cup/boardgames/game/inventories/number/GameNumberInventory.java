/*     */ package water.of.cup.boardgames.game.inventories.number;
/*     */ import de.themoep.inventorygui.DynamicGuiElement;
/*     */ import de.themoep.inventorygui.GuiElement;
/*     */ import de.themoep.inventorygui.InventoryGui;
/*     */ import de.themoep.inventorygui.StaticGuiElement;
/*     */ import java.util.Arrays;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.MathUtils;
/*     */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*     */ import water.of.cup.boardgames.game.inventories.GameOption;
/*     */ import water.of.cup.boardgames.game.inventories.InventoryScreen;
/*     */ import water.of.cup.boardgames.game.inventories.InventoryUtils;
/*     */ 
/*     */ public class GameNumberInventory extends InventoryScreen {
/*  24 */   private final BoardGames instance = BoardGames.getInstance();
/*     */   private final GameInventory gameInventory;
/*     */   private final Game game;
/*     */   
/*     */   public GameNumberInventory(GameInventory paramGameInventory) {
/*  29 */     super(paramGameInventory);
/*  30 */     this.gameInventory = paramGameInventory;
/*  31 */     this.game = paramGameInventory.getGame();
/*     */   }
/*     */   
/*     */   public void build(Player paramPlayer, GameNumberInventoryCallback paramGameNumberInventoryCallback, GameOption paramGameOption, int paramInt) {
/*  35 */     char[][] arrayOfChar = getGuiSetup();
/*  36 */     String[] arrayOfString = formatGuiSetup(arrayOfChar);
/*     */     
/*  38 */     String str = (paramGameOption.getLabel() != null) ? paramGameOption.getLabel() : this.game.getAltName();
/*  39 */     InventoryGui inventoryGui = new InventoryGui((JavaPlugin)BoardGames.getInstance(), (InventoryHolder)paramPlayer, str, arrayOfString, new GuiElement[0]);
/*     */     
/*  41 */     inventoryGui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
/*     */     
/*  43 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('w', new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*     */     
/*  45 */     ItemStack itemStack1 = InventoryUtils.getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI3Y2U2ODNkMDg2OGFhNDM3OGFlYjYwY2FhNWVhODA1OTZiY2ZmZGFiNmI1YWYyZDEyNTk1ODM3YTg0ODUzIn19fQ==");
/*  46 */     ItemStack itemStack2 = InventoryUtils.getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNThmZTI1MWE0MGU0MTY3ZDM1ZDA4MWMyNzg2OWFjMTUxYWY5NmI2YmQxNmRkMjgzNGQ1ZGM3MjM1ZjQ3NzkxZCJ9fX0=");
/*     */     
/*  48 */     int[] arrayOfInt = new int[7];
/*  49 */     setNumAmounts(arrayOfInt, paramInt);
/*     */     
/*  51 */     for (byte b = 1; b <= 7; b++) {
/*  52 */       char c1 = arrayOfChar[2][b];
/*  53 */       char c2 = arrayOfChar[1][b];
/*  54 */       char c3 = arrayOfChar[3][b];
/*  55 */       int i = b - 1;
/*     */       
/*  57 */       inventoryGui.addElement((GuiElement)new DynamicGuiElement(c1, () -> new StaticGuiElement(paramChar, getItemStack(paramArrayOfint[paramInt]), paramArrayOfint[paramInt], (), new String[] { ChatColor.GREEN + getNumString(paramArrayOfint) })));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  62 */       inventoryGui.addElement((GuiElement)new StaticGuiElement(c2, itemStack2, paramClick -> { incrementNumAmounts(paramArrayOfint, paramInt, 1, paramGameOption); paramClick.getGui().draw(); return true; }new String[] { ConfigUtil.GUI_UP_ARROW
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  68 */               .toString() }));
/*     */       
/*  70 */       inventoryGui.addElement((GuiElement)new StaticGuiElement(c3, itemStack1, paramClick -> { incrementNumAmounts(paramArrayOfint, paramInt, -1, paramGameOption); paramClick.getGui().draw(); return true; }new String[] { ConfigUtil.GUI_DOWN_ARROW
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  76 */               .toString() }));
/*     */     } 
/*     */     
/*  79 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('z', new ItemStack(Material.RED_STAINED_GLASS_PANE), paramClick -> { setNumAmounts(paramArrayOfint, paramGameOption.getMinIntValue()); paramClick.getGui().draw(); return true; }new String[] { ConfigUtil.GUI_RESET_NUMBERS
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  84 */             .toString() }));
/*     */ 
/*     */ 
/*     */     
/*  88 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('y', new ItemStack(Material.LIME_STAINED_GLASS_PANE), paramClick -> { paramInventoryGui.close(false); paramGameNumberInventoryCallback.onSubmit(paramGameOption.getKey(), getFinalNum(paramArrayOfint)); return true; }new String[] { ConfigUtil.GUI_DONE_TEXT
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  93 */             .toString() }));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('a', new ItemStack(Material.BLUE_STAINED_GLASS_PANE), paramClick -> { int i = getFinalNum(paramArrayOfint); setNumAmounts(paramArrayOfint, i / 2); normalizeNumAmounts(paramArrayOfint, paramGameOption); paramClick.getGui().draw(); return true; }new String[] { ConfigUtil.GUI_NUMBERS_HALF
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 105 */             .toString() }));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('b', new ItemStack(Material.BLUE_STAINED_GLASS_PANE), paramClick -> { int i = getFinalNum(paramArrayOfint); setNumAmounts(paramArrayOfint, i * 2); normalizeNumAmounts(paramArrayOfint, paramGameOption); paramClick.getGui().draw(); return true; }new String[] { ConfigUtil.GUI_NUMBERS_DOUBLE
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 117 */             .toString() }));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('c', new ItemStack(Material.BLUE_STAINED_GLASS_PANE), paramClick -> { int i = paramGameOption.requiresEconomy() ? (int)this.instance.getEconomy().getBalance((OfflinePlayer)paramPlayer) : paramGameOption.getMaxIntValue(); setNumAmounts(paramArrayOfint, i); normalizeNumAmounts(paramArrayOfint, paramGameOption); paramClick.getGui().draw(); return true; }new String[] { ConfigUtil.GUI_NUMBERS_MAX
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 129 */             .toString() }));
/*     */ 
/*     */ 
/*     */     
/* 133 */     inventoryGui.setCloseAction(paramClose -> true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     inventoryGui.show((HumanEntity)paramPlayer);
/*     */   }
/*     */   
/*     */   private void incrementNumAmounts(int[] paramArrayOfint, int paramInt1, int paramInt2, GameOption paramGameOption) {
/* 142 */     paramArrayOfint[paramInt1] = paramArrayOfint[paramInt1] + paramInt2;
/* 143 */     if (paramArrayOfint[paramInt1] < 0) {
/* 144 */       paramArrayOfint[paramInt1] = 0;
/* 145 */     } else if (paramArrayOfint[paramInt1] > 9) {
/* 146 */       paramArrayOfint[paramInt1] = 9;
/*     */     } 
/*     */     
/* 149 */     normalizeNumAmounts(paramArrayOfint, paramGameOption);
/*     */   }
/*     */   
/*     */   private void setNumAmounts(int[] paramArrayOfint, int paramInt) {
/* 153 */     Arrays.fill(paramArrayOfint, 0);
/*     */     
/* 155 */     if (paramInt > 9999999) {
/* 156 */       paramInt = 9999999;
/*     */     }
/* 158 */     int i = paramInt;
/* 159 */     for (int j = paramArrayOfint.length - 1; j >= 0; j--) {
/* 160 */       paramArrayOfint[j] = i % 10;
/* 161 */       i /= 10;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void normalizeNumAmounts(int[] paramArrayOfint, GameOption paramGameOption) {
/* 166 */     int i = getFinalNum(paramArrayOfint);
/* 167 */     if (i > paramGameOption.getMaxIntValue()) {
/* 168 */       setNumAmounts(paramArrayOfint, paramGameOption.getMaxIntValue());
/* 169 */     } else if (i < paramGameOption.getMinIntValue()) {
/* 170 */       setNumAmounts(paramArrayOfint, paramGameOption.getMinIntValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   private char[][] getGuiSetup() {
/* 175 */     char[][] arrayOfChar = new char[6][9];
/*     */ 
/*     */     
/* 178 */     for (char[] arrayOfChar1 : arrayOfChar) {
/* 179 */       Arrays.fill(arrayOfChar1, ' ');
/*     */     }
/*     */ 
/*     */     
/* 183 */     for (byte b1 = 1; b1 <= 3; b1++) {
/* 184 */       for (byte b = 1; b <= 7; b++) {
/* 185 */         arrayOfChar[b1][b] = 'w';
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 190 */     arrayOfChar[5][4] = 'y';
/*     */ 
/*     */     
/* 193 */     arrayOfChar[2][0] = 'z';
/*     */ 
/*     */     
/* 196 */     arrayOfChar[4][3] = 'a';
/*     */     
/* 198 */     arrayOfChar[4][4] = 'b';
/*     */     
/* 200 */     arrayOfChar[4][5] = 'c';
/*     */     
/* 202 */     int[] arrayOfInt = { 1, 2 };
/*     */     
/* 204 */     char c = 'd';
/*     */     
/* 206 */     for (byte b2 = 0; b2 < 7; b2++) {
/* 207 */       arrayOfChar[arrayOfInt[1]][arrayOfInt[0]] = c;
/* 208 */       arrayOfChar[arrayOfInt[1] + 1][arrayOfInt[0]] = (char)(c + 1);
/* 209 */       arrayOfChar[arrayOfInt[1] - 1][arrayOfInt[0]] = (char)(c + 2);
/*     */       
/* 211 */       c = (char)(c + 3);
/*     */       
/* 213 */       arrayOfInt[0] = arrayOfInt[0] + 1;
/*     */     } 
/*     */     
/* 216 */     return arrayOfChar;
/*     */   }
/*     */   
/*     */   private String getNumString(int[] paramArrayOfint) {
/* 220 */     StringBuilder stringBuilder = new StringBuilder();
/* 221 */     for (int i : paramArrayOfint) {
/* 222 */       stringBuilder.append(i);
/*     */     }
/* 224 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   private int getFinalNum(int[] paramArrayOfint) {
/* 228 */     String str = getNumString(paramArrayOfint);
/* 229 */     if (!MathUtils.isNumeric(str)) return 0; 
/* 230 */     return Integer.parseInt(str);
/*     */   }
/*     */   
/*     */   private ItemStack getItemStack(int paramInt) {
/* 234 */     if (paramInt == 0) return new ItemStack(Material.GOLD_NUGGET); 
/* 235 */     return new ItemStack(Material.GOLD_INGOT);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\number\GameNumberInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
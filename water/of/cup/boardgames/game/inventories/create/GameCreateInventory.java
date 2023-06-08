/*     */ package water.of.cup.boardgames.game.inventories.create;
/*     */ import de.themoep.inventorygui.DynamicGuiElement;
/*     */ import de.themoep.inventorygui.GuiElement;
/*     */ import de.themoep.inventorygui.GuiStateElement;
/*     */ import de.themoep.inventorygui.InventoryGui;
/*     */ import de.themoep.inventorygui.StaticGuiElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.MathUtils;
/*     */ import water.of.cup.boardgames.game.inventories.GameInventory;
/*     */ import water.of.cup.boardgames.game.inventories.GameOption;
/*     */ import water.of.cup.boardgames.game.inventories.GameOptionType;
/*     */ import water.of.cup.boardgames.game.inventories.InventoryUtils;
/*     */ 
/*     */ public class GameCreateInventory extends InventoryScreen {
/*     */   private final ArrayList<GameOption> gameOptions;
/*     */   private final Game game;
/*     */   private final GameInventory gameInventory;
/*     */   
/*     */   public GameCreateInventory(GameInventory paramGameInventory) {
/*  31 */     super(paramGameInventory);
/*  32 */     this.gameOptions = paramGameInventory.getGameOptions();
/*  33 */     this.game = paramGameInventory.getGame();
/*  34 */     this.gameInventory = paramGameInventory;
/*     */   }
/*     */   
/*     */   public void build(Player paramPlayer, int paramInt, HashMap<String, Object> paramHashMap, CreateInventoryCallback paramCreateInventoryCallback) {
/*  38 */     HashMap<Object, Object> hashMap1 = new HashMap<>();
/*  39 */     ArrayList<GameOption> arrayList = new ArrayList<>(this.gameOptions);
/*     */ 
/*     */     
/*  42 */     if (arrayList.size() > 4) {
/*  43 */       arrayList.clear();
/*     */ 
/*     */       
/*  46 */       int i = paramInt * 3;
/*  47 */       for (byte b1 = 0; b1 < 3 && 
/*  48 */         i < this.gameOptions.size(); b1++) {
/*     */         
/*  50 */         arrayList.add(this.gameOptions.get(i));
/*  51 */         i++;
/*     */       } 
/*     */     } 
/*     */     
/*  55 */     HashMap<Object, Object> hashMap2 = new HashMap<>();
/*     */     
/*  57 */     if (paramHashMap == null) {
/*     */       
/*  59 */       for (GameOption gameOption : this.gameOptions) {
/*  60 */         if (MathUtils.isNumeric(gameOption.getDefaultValue())) {
/*  61 */           hashMap2.put(gameOption.getKey(), Integer.valueOf(Integer.parseInt(gameOption.getDefaultValue()))); continue;
/*     */         } 
/*  63 */         hashMap2.put(gameOption.getKey(), gameOption.getDefaultValue());
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  68 */       for (String str : paramHashMap.keySet()) {
/*  69 */         hashMap2.put(str, paramHashMap.get(str));
/*     */       }
/*     */     } 
/*     */     
/*  73 */     char[][] arrayOfChar = getGuiSetup(arrayList, (HashMap)hashMap1);
/*  74 */     String[] arrayOfString = formatGuiSetup(arrayOfChar);
/*     */ 
/*     */     
/*  77 */     InventoryGui inventoryGui = new InventoryGui((JavaPlugin)BoardGames.getInstance(), (InventoryHolder)paramPlayer, ConfigUtil.GUI_GAME_CREATE_TITLE.buildString(this.game.getAltName()), arrayOfString, new GuiElement[0]);
/*     */     
/*  79 */     inventoryGui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
/*     */     
/*  81 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('w', new ItemStack(Material.WHITE_STAINED_GLASS_PANE), new String[] { " " }));
/*     */     
/*  83 */     ItemStack itemStack = InventoryUtils.getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJmM2EyZGZjZTBjM2RhYjdlZTEwZGIzODVlNTIyOWYxYTM5NTM0YThiYTI2NDYxNzhlMzdjNGZhOTNiIn19fQ==");
/*  84 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('x', itemStack, paramClick -> { int i = (this.gameOptions.size() - 1) / 3; int j = paramInt; if (paramInt + 1 > i) { j = 0; } else { j++; }  build(paramPlayer, j, paramHashMap, paramCreateInventoryCallback); return true; }new String[] { ConfigUtil.GUI_NEXT_PAGE
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
/*  97 */             .toString() }));
/*     */ 
/*     */ 
/*     */     
/* 101 */     inventoryGui.addElement((GuiElement)new StaticGuiElement('y', new ItemStack(Material.LIME_STAINED_GLASS_PANE), paramClick -> { paramInventoryGui.close(true); paramCreateInventoryCallback.onCreateGame(paramHashMap); return true; }new String[] { this.gameInventory
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 106 */             .getCreateGameText() }));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     for (byte b = 1; b <= 7; b++) {
/* 112 */       char c = arrayOfChar[2][b];
/* 113 */       if (hashMap1.containsKey(Character.valueOf(c))) {
/* 114 */         GameOption gameOption = (GameOption)hashMap1.get(Character.valueOf(c));
/* 115 */         if (gameOption.getOptionType() != GameOptionType.CUSTOM) {
/*     */           char c1; GuiStateElement guiStateElement; char c2; ItemStack itemStack1, itemStack2;
/* 117 */           String str = (gameOption.getLabel() == null) ? "" : gameOption.getLabel();
/*     */           
/* 119 */           if (gameOption.getOptionType() == GameOptionType.COUNT && gameOption.getCustomValues() == null) {
/* 120 */             inventoryGui.addElement((GuiElement)new DynamicGuiElement(c, () -> new StaticGuiElement(paramChar, new ItemStack(paramGameOption.getMaterial()), (), new String[] { paramString + ConfigUtil.GUI_CREATE_GAME_DATA_COLOR.toString() + ConfigUtil.translateTeamName(paramHashMap.get(paramGameOption.getKey()).toString()) })));
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 131 */             inventoryGui.addElement((GuiElement)new DynamicGuiElement(c, () -> new StaticGuiElement(paramChar, new ItemStack(paramGameOption.getMaterial()), (), new String[] { paramString + ConfigUtil.GUI_CREATE_GAME_DATA_COLOR.toString() + ConfigUtil.translateTeamName(paramHashMap.get(paramGameOption.getKey()).toString()) })));
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 138 */           switch (gameOption.getOptionType()) {
/*     */             case TOGGLE:
/* 140 */               c1 = arrayOfChar[3][b];
/*     */               
/* 142 */               assert hashMap1.get(Character.valueOf(c1)) != null;
/* 143 */               assert gameOption.getCustomValues() != null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 161 */               guiStateElement = new GuiStateElement(c1, new GuiStateElement.State[] { new GuiStateElement.State(paramClick -> { paramHashMap.put(paramGameOption.getKey(), paramGameOption.getCustomValues().get(0)); paramClick.getGui().draw(); }gameOption.getCustomValues().get(0), new ItemStack(Material.GREEN_STAINED_GLASS_PANE), new String[] { ChatColor.GREEN + (String)gameOption.getCustomValues().get(0) }), new GuiStateElement.State(paramClick -> { paramHashMap.put(paramGameOption.getKey(), paramGameOption.getCustomValues().get(1)); paramClick.getGui().draw(); }gameOption.getCustomValues().get(1), new ItemStack(Material.RED_STAINED_GLASS_PANE), new String[] { ChatColor.RED + (String)gameOption.getCustomValues().get(1) }) });
/*     */ 
/*     */ 
/*     */               
/* 165 */               guiStateElement.setState((String)hashMap2.get(gameOption.getKey()));
/*     */               
/* 167 */               inventoryGui.addElement((GuiElement)guiStateElement);
/*     */               break;
/*     */             
/*     */             case COUNT:
/* 171 */               c1 = arrayOfChar[1][b];
/* 172 */               c2 = arrayOfChar[3][b];
/*     */               
/* 174 */               assert hashMap1.get(Character.valueOf(c1)) != null;
/* 175 */               assert hashMap1.get(Character.valueOf(c2)) != null;
/*     */ 
/*     */ 
/*     */               
/* 179 */               itemStack1 = InventoryUtils.getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNThmZTI1MWE0MGU0MTY3ZDM1ZDA4MWMyNzg2OWFjMTUxYWY5NmI2YmQxNmRkMjgzNGQ1ZGM3MjM1ZjQ3NzkxZCJ9fX0=");
/* 180 */               inventoryGui.addElement((GuiElement)new StaticGuiElement(c1, itemStack1, paramClick -> { if (paramGameOption.getCustomValues() == null) { byte b = 1; if (paramClick.getType().isShiftClick()) b = 10;  int i = ((Integer)paramHashMap.get(paramGameOption.getKey())).intValue() + b; if (i <= paramGameOption.getMaxIntValue()) paramHashMap.put(paramGameOption.getKey(), Integer.valueOf(i));  } else { String str1 = (new StringBuilder()).append(paramHashMap.get(paramGameOption.getKey())).append("").toString(); int i = paramGameOption.getCustomValues().indexOf(str1); if (i == paramGameOption.getCustomValues().size() - 1) return true;  String str2 = paramGameOption.getCustomValues().get(i + 1); if (MathUtils.isNumeric(str2)) { paramHashMap.put(paramGameOption.getKey(), Integer.valueOf(Integer.parseInt(str2))); } else { paramHashMap.put(paramGameOption.getKey(), str2); }  }  paramClick.getGui().draw(); return true; }new String[] { ConfigUtil.GUI_UP_ARROW
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
/*     */                       
/* 206 */                       .toString() }));
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 211 */               itemStack2 = InventoryUtils.getCustomTextureHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI3Y2U2ODNkMDg2OGFhNDM3OGFlYjYwY2FhNWVhODA1OTZiY2ZmZGFiNmI1YWYyZDEyNTk1ODM3YTg0ODUzIn19fQ==");
/* 212 */               inventoryGui.addElement((GuiElement)new StaticGuiElement(c2, itemStack2, paramClick -> { if (paramGameOption.getCustomValues() == null) { byte b = 1; if (paramClick.getType().isShiftClick()) b = 10;  int i = ((Integer)paramHashMap.get(paramGameOption.getKey())).intValue() - b; if (i < paramGameOption.getMinIntValue()) i = paramGameOption.getMinIntValue();  paramHashMap.put(paramGameOption.getKey(), Integer.valueOf(i)); } else { String str1 = (new StringBuilder()).append(paramHashMap.get(paramGameOption.getKey())).append("").toString(); int i = paramGameOption.getCustomValues().indexOf(str1); if (i == 0) return true;  String str2 = paramGameOption.getCustomValues().get(i - 1); if (MathUtils.isNumeric(str2)) { paramHashMap.put(paramGameOption.getKey(), Integer.valueOf(Integer.parseInt(str2))); } else { paramHashMap.put(paramGameOption.getKey(), str2); }  }  paramClick.getGui().draw(); return true; }new String[] { ConfigUtil.GUI_DOWN_ARROW
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
/*     */ 
/*     */ 
/*     */                       
/* 240 */                       .toString() }));
/*     */               break;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/* 251 */     inventoryGui.setCloseAction(paramClose -> {
/*     */           paramCreateInventoryCallback.onCreateGame(null);
/*     */           
/*     */           return false;
/*     */         });
/*     */     
/* 257 */     inventoryGui.show((HumanEntity)paramPlayer);
/*     */   }
/*     */   
/*     */   public void build(Player paramPlayer, CreateInventoryCallback paramCreateInventoryCallback) {
/* 261 */     build(paramPlayer, 0, (HashMap<String, Object>)null, paramCreateInventoryCallback);
/*     */   }
/*     */   
/*     */   private char[][] getGuiSetup(ArrayList<GameOption> paramArrayList, HashMap<Character, GameOption> paramHashMap) {
/* 265 */     char[][] arrayOfChar = new char[6][9];
/*     */ 
/*     */     
/* 268 */     for (char[] arrayOfChar1 : arrayOfChar) {
/* 269 */       Arrays.fill(arrayOfChar1, ' ');
/*     */     }
/*     */ 
/*     */     
/* 273 */     for (byte b = 1; b <= 3; b++) {
/* 274 */       for (byte b1 = 1; b1 <= 7; b1++) {
/* 275 */         arrayOfChar[b][b1] = 'w';
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 280 */     arrayOfChar[5][4] = 'y';
/*     */ 
/*     */     
/* 283 */     if (this.gameOptions.size() > 4) {
/* 284 */       arrayOfChar[2][7] = 'x';
/*     */     }
/* 286 */     int[] arrayOfInt = { 1, 2 };
/*     */     
/* 288 */     char c = 'a';
/*     */ 
/*     */     
/* 291 */     for (GameOption gameOption : paramArrayList) {
/* 292 */       switch (gameOption.getOptionType()) {
/*     */         case TOGGLE:
/* 294 */           arrayOfChar[arrayOfInt[1]][arrayOfInt[0]] = c;
/* 295 */           arrayOfChar[arrayOfInt[1] + 1][arrayOfInt[0]] = (char)(c + 1);
/*     */           
/* 297 */           paramHashMap.put(Character.valueOf(c), gameOption);
/* 298 */           paramHashMap.put(Character.valueOf((char)(c + 1)), gameOption);
/*     */           
/* 300 */           c = (char)(c + 2);
/*     */           break;
/*     */         
/*     */         case COUNT:
/* 304 */           arrayOfChar[arrayOfInt[1] - 1][arrayOfInt[0]] = c;
/* 305 */           arrayOfChar[arrayOfInt[1]][arrayOfInt[0]] = (char)(c + 1);
/* 306 */           arrayOfChar[arrayOfInt[1] + 1][arrayOfInt[0]] = (char)(c + 2);
/*     */           
/* 308 */           paramHashMap.put(Character.valueOf(c), gameOption);
/* 309 */           paramHashMap.put(Character.valueOf((char)(c + 1)), gameOption);
/* 310 */           paramHashMap.put(Character.valueOf((char)(c + 2)), gameOption);
/*     */           
/* 312 */           c = (char)(c + 3);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 318 */       arrayOfInt[0] = arrayOfInt[0] + 2;
/*     */     } 
/*     */     
/* 321 */     return arrayOfChar;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\create\GameCreateInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*      */ package de.themoep.inventorygui;
/*      */ 
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Deque;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.UUID;
/*      */ import java.util.logging.Level;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.stream.Collectors;
/*      */ import org.bukkit.ChatColor;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.Nameable;
/*      */ import org.bukkit.Sound;
/*      */ import org.bukkit.block.BlockState;
/*      */ import org.bukkit.entity.Entity;
/*      */ import org.bukkit.entity.HumanEntity;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.event.EventHandler;
/*      */ import org.bukkit.event.EventPriority;
/*      */ import org.bukkit.event.Listener;
/*      */ import org.bukkit.event.block.BlockBreakEvent;
/*      */ import org.bukkit.event.block.BlockDispenseEvent;
/*      */ import org.bukkit.event.entity.EntityDeathEvent;
/*      */ import org.bukkit.event.inventory.InventoryAction;
/*      */ import org.bukkit.event.inventory.InventoryClickEvent;
/*      */ import org.bukkit.event.inventory.InventoryCloseEvent;
/*      */ import org.bukkit.event.inventory.InventoryDragEvent;
/*      */ import org.bukkit.event.inventory.InventoryMoveItemEvent;
/*      */ import org.bukkit.event.inventory.InventoryType;
/*      */ import org.bukkit.event.player.PlayerSwapHandItemsEvent;
/*      */ import org.bukkit.event.server.PluginDisableEvent;
/*      */ import org.bukkit.inventory.Inventory;
/*      */ import org.bukkit.inventory.InventoryHolder;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ import org.bukkit.inventory.meta.ItemMeta;
/*      */ import org.bukkit.material.MaterialData;
/*      */ import org.bukkit.plugin.Plugin;
/*      */ import org.bukkit.plugin.java.JavaPlugin;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class InventoryGui
/*      */   implements Listener
/*      */ {
/*   75 */   private static final int[] ROW_WIDTHS = new int[] { 3, 5, 9 };
/*   76 */   private static final InventoryType[] INVENTORY_TYPES = new InventoryType[] { InventoryType.DISPENSER, InventoryType.HOPPER, InventoryType.CHEST };
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Sound CLICK_SOUND;
/*      */ 
/*      */   
/*   83 */   private static final Map<String, InventoryGui> GUI_MAP = new HashMap<>();
/*   84 */   private static final Map<UUID, ArrayDeque<InventoryGui>> GUI_HISTORY = new HashMap<>();
/*      */   
/*   86 */   private static final Map<String, Pattern> PATTERN_CACHE = new HashMap<>();
/*      */   
/*      */   private final JavaPlugin plugin;
/*   89 */   private final List<UnregisterableListener> listeners = new ArrayList<>();
/*   90 */   private final UnregisterableListener[] optionalListeners = new UnregisterableListener[] { new ItemSwapGuiListener(this) };
/*      */   
/*      */   private String title;
/*      */   
/*      */   private final char[] slots;
/*   95 */   private final Map<Character, GuiElement> elements = new HashMap<>();
/*      */   private InventoryType inventoryType;
/*   97 */   private Map<UUID, Inventory> inventories = new LinkedHashMap<>();
/*   98 */   private InventoryHolder owner = null;
/*      */   private boolean listenersRegistered = false;
/*  100 */   private Map<UUID, Integer> pageNumbers = new LinkedHashMap<>();
/*  101 */   private Map<UUID, Integer> pageAmounts = new LinkedHashMap<>();
/*      */   
/*      */   private GuiElement.Action outsideAction = paramClick -> false;
/*      */   private CloseAction closeAction = paramClose -> true;
/*      */   private boolean silent = false;
/*      */   
/*      */   static {
/*  108 */     Sound sound = null;
/*  109 */     String[] arrayOfString = { "UI_BUTTON_CLICK", "CLICK" };
/*  110 */     for (String str : arrayOfString) {
/*      */       try {
/*  112 */         sound = Sound.valueOf(str.toUpperCase());
/*      */         break;
/*  114 */       } catch (IllegalArgumentException illegalArgumentException) {}
/*      */     } 
/*  116 */     if (sound == null) {
/*  117 */       for (Sound sound1 : Sound.values()) {
/*  118 */         if (sound1.name().contains("CLICK")) {
/*  119 */           sound = sound1;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/*  124 */     CLICK_SOUND = sound;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InventoryGui(JavaPlugin paramJavaPlugin, InventoryHolder paramInventoryHolder, String paramString, String[] paramArrayOfString, GuiElement... paramVarArgs) {
/*  139 */     this.plugin = paramJavaPlugin;
/*  140 */     this.owner = paramInventoryHolder;
/*  141 */     this.title = paramString;
/*  142 */     this.listeners.add(new GuiListener(this));
/*  143 */     for (UnregisterableListener unregisterableListener : this.optionalListeners) {
/*  144 */       if (unregisterableListener.isCompatible()) {
/*  145 */         this.listeners.add(unregisterableListener);
/*      */       }
/*      */     } 
/*      */     
/*  149 */     int i = ROW_WIDTHS[0];
/*  150 */     for (String str : paramArrayOfString) {
/*  151 */       if (str.length() > i) {
/*  152 */         i = str.length();
/*      */       }
/*      */     } 
/*  155 */     for (byte b = 0; b < ROW_WIDTHS.length && b < INVENTORY_TYPES.length; b++) {
/*  156 */       if (i < ROW_WIDTHS[b]) {
/*  157 */         i = ROW_WIDTHS[b];
/*      */       }
/*  159 */       if (i == ROW_WIDTHS[b]) {
/*  160 */         this.inventoryType = INVENTORY_TYPES[b];
/*      */         break;
/*      */       } 
/*      */     } 
/*  164 */     if (this.inventoryType == null) {
/*  165 */       throw new IllegalArgumentException("Could not match row setup to an inventory type!");
/*      */     }
/*      */     
/*  168 */     StringBuilder stringBuilder = new StringBuilder();
/*  169 */     for (String str : paramArrayOfString) {
/*  170 */       if (str.length() < i) {
/*  171 */         double d = ((i - str.length()) / 2); byte b1;
/*  172 */         for (b1 = 0; b1 < Math.floor(d); b1++) {
/*  173 */           stringBuilder.append(" ");
/*      */         }
/*  175 */         stringBuilder.append(str);
/*  176 */         for (b1 = 0; b1 < Math.ceil(d); b1++) {
/*  177 */           stringBuilder.append(" ");
/*      */         }
/*  179 */       } else if (str.length() == i) {
/*  180 */         stringBuilder.append(str);
/*      */       } else {
/*  182 */         stringBuilder.append(str.substring(0, i));
/*      */       } 
/*      */     } 
/*  185 */     this.slots = stringBuilder.toString().toCharArray();
/*      */     
/*  187 */     addElements(paramVarArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InventoryGui(JavaPlugin paramJavaPlugin, String paramString, String[] paramArrayOfString, GuiElement... paramVarArgs) {
/*  200 */     this(paramJavaPlugin, (InventoryHolder)null, paramString, paramArrayOfString, paramVarArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InventoryGui(JavaPlugin paramJavaPlugin, InventoryHolder paramInventoryHolder, String paramString, String[] paramArrayOfString, Collection<GuiElement> paramCollection) {
/*  215 */     this(paramJavaPlugin, paramInventoryHolder, paramString, paramArrayOfString, new GuiElement[0]);
/*  216 */     addElements(paramCollection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addElement(GuiElement paramGuiElement) {
/*  224 */     this.elements.put(Character.valueOf(paramGuiElement.getSlotChar()), paramGuiElement);
/*  225 */     paramGuiElement.setGui(this);
/*  226 */     paramGuiElement.setSlots(getSlots(paramGuiElement.getSlotChar()));
/*      */   }
/*      */   
/*      */   private int[] getSlots(char paramChar) {
/*  230 */     ArrayList<Integer> arrayList = new ArrayList();
/*  231 */     for (byte b = 0; b < this.slots.length; b++) {
/*  232 */       if (this.slots[b] == paramChar) {
/*  233 */         arrayList.add(Integer.valueOf(b));
/*      */       }
/*      */     } 
/*  236 */     return arrayList.stream().mapToInt(Integer::intValue).toArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addElement(char paramChar, ItemStack paramItemStack, GuiElement.Action paramAction, String... paramVarArgs) {
/*  251 */     addElement(new StaticGuiElement(paramChar, paramItemStack, paramAction, paramVarArgs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addElement(char paramChar, ItemStack paramItemStack, String... paramVarArgs) {
/*  265 */     addElement(new StaticGuiElement(paramChar, paramItemStack, null, paramVarArgs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addElement(char paramChar, MaterialData paramMaterialData, GuiElement.Action paramAction, String... paramVarArgs) {
/*  280 */     addElement(paramChar, paramMaterialData.toItemStack(1), paramAction, paramVarArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addElement(char paramChar, Material paramMaterial, byte paramByte, GuiElement.Action paramAction, String... paramVarArgs) {
/*  296 */     addElement(paramChar, new MaterialData(paramMaterial, paramByte), paramAction, paramVarArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addElement(char paramChar, Material paramMaterial, GuiElement.Action paramAction, String... paramVarArgs) {
/*  311 */     addElement(paramChar, paramMaterial, (byte)0, paramAction, paramVarArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addElements(GuiElement... paramVarArgs) {
/*  319 */     for (GuiElement guiElement : paramVarArgs) {
/*  320 */       addElement(guiElement);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addElements(Collection<GuiElement> paramCollection) {
/*  329 */     for (GuiElement guiElement : paramCollection) {
/*  330 */       addElement(guiElement);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeElement(GuiElement paramGuiElement) {
/*  340 */     return this.elements.remove(Character.valueOf(paramGuiElement.getSlotChar()), paramGuiElement);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GuiElement removeElement(char paramChar) {
/*  349 */     return this.elements.remove(Character.valueOf(paramChar));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFiller(ItemStack paramItemStack) {
/*  357 */     addElement(new StaticGuiElement(' ', paramItemStack, new String[] { " " }));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GuiElement getFiller() {
/*  365 */     return this.elements.get(Character.valueOf(' '));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getPageNumber() {
/*  375 */     return getPageNumber(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPageNumber(HumanEntity paramHumanEntity) {
/*  384 */     return (paramHumanEntity != null) ? ((Integer)this.pageNumbers
/*  385 */       .getOrDefault(paramHumanEntity.getUniqueId(), Integer.valueOf(0))).intValue() : (
/*  386 */       this.pageNumbers.isEmpty() ? 0 : ((Integer)this.pageNumbers.values().iterator().next()).intValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPageNumber(int paramInt) {
/*  394 */     for (UUID uUID : this.inventories.keySet()) {
/*  395 */       Player player = this.plugin.getServer().getPlayer(uUID);
/*  396 */       if (player != null) {
/*  397 */         setPageNumber((HumanEntity)player, paramInt);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPageNumber(HumanEntity paramHumanEntity, int paramInt) {
/*  408 */     setPageNumberInternal(paramHumanEntity, paramInt);
/*  409 */     draw(paramHumanEntity, false);
/*      */   }
/*      */   
/*      */   private void setPageNumberInternal(HumanEntity paramHumanEntity, int paramInt) {
/*  413 */     this.pageNumbers.put(paramHumanEntity.getUniqueId(), Integer.valueOf((paramInt > 0) ? paramInt : 0));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getPageAmount() {
/*  423 */     return getPageAmount(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPageAmount(HumanEntity paramHumanEntity) {
/*  432 */     return (paramHumanEntity != null) ? ((Integer)this.pageAmounts
/*  433 */       .getOrDefault(paramHumanEntity.getUniqueId(), Integer.valueOf(1))).intValue() : (
/*  434 */       this.pageAmounts.isEmpty() ? 1 : ((Integer)this.pageAmounts.values().iterator().next()).intValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setPageAmount(HumanEntity paramHumanEntity, int paramInt) {
/*  443 */     this.pageAmounts.put(paramHumanEntity.getUniqueId(), Integer.valueOf(paramInt));
/*      */   }
/*      */   
/*      */   private void calculatePageAmount(HumanEntity paramHumanEntity) {
/*  447 */     int i = 0;
/*  448 */     for (GuiElement guiElement : this.elements.values()) {
/*  449 */       int j = calculateElementSize(paramHumanEntity, guiElement);
/*  450 */       if (j > 0 && (i - 1) * (guiElement.getSlots()).length < j) {
/*  451 */         i = (int)Math.ceil(j / (guiElement.getSlots()).length);
/*      */       }
/*      */     } 
/*  454 */     setPageAmount(paramHumanEntity, i);
/*  455 */     if (getPageNumber(paramHumanEntity) >= i) {
/*  456 */       setPageNumberInternal(paramHumanEntity, i - 1);
/*      */     }
/*      */   }
/*      */   
/*      */   private int calculateElementSize(HumanEntity paramHumanEntity, GuiElement paramGuiElement) {
/*  461 */     if (paramGuiElement instanceof GuiElementGroup)
/*  462 */       return ((GuiElementGroup)paramGuiElement).size(); 
/*  463 */     if (paramGuiElement instanceof GuiStorageElement)
/*  464 */       return ((GuiStorageElement)paramGuiElement).getStorage().getSize(); 
/*  465 */     if (paramGuiElement instanceof DynamicGuiElement) {
/*  466 */       return calculateElementSize(paramHumanEntity, ((DynamicGuiElement)paramGuiElement).getCachedElement(paramHumanEntity));
/*      */     }
/*  468 */     return 0;
/*      */   }
/*      */   
/*      */   private void registerListeners() {
/*  472 */     if (this.listenersRegistered) {
/*      */       return;
/*      */     }
/*  475 */     for (UnregisterableListener unregisterableListener : this.listeners) {
/*  476 */       this.plugin.getServer().getPluginManager().registerEvents(unregisterableListener, (Plugin)this.plugin);
/*      */     }
/*  478 */     this.listenersRegistered = true;
/*      */   }
/*      */   
/*      */   private void unregisterListeners() {
/*  482 */     for (UnregisterableListener unregisterableListener : this.listeners) {
/*  483 */       unregisterableListener.unregister();
/*      */     }
/*  485 */     this.listenersRegistered = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void show(HumanEntity paramHumanEntity) {
/*  493 */     show(paramHumanEntity, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void show(HumanEntity paramHumanEntity, boolean paramBoolean) {
/*  502 */     draw(paramHumanEntity);
/*  503 */     if (!paramBoolean || !equals(getOpen(paramHumanEntity))) {
/*  504 */       if (paramHumanEntity.getOpenInventory().getType() != InventoryType.CRAFTING) {
/*      */ 
/*      */ 
/*      */         
/*  508 */         this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, () -> {
/*      */               Inventory inventory = getInventory(paramHumanEntity);
/*      */               if (inventory != null) {
/*      */                 addHistory(paramHumanEntity, this);
/*      */                 paramHumanEntity.openInventory(inventory);
/*      */               } 
/*      */             });
/*      */       } else {
/*  516 */         Inventory inventory = getInventory(paramHumanEntity);
/*  517 */         if (inventory != null) {
/*  518 */           clearHistory(paramHumanEntity);
/*  519 */           addHistory(paramHumanEntity, this);
/*  520 */           paramHumanEntity.openInventory(inventory);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void build() {
/*  530 */     build(this.owner);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void build(InventoryHolder paramInventoryHolder) {
/*  538 */     setOwner(paramInventoryHolder);
/*  539 */     registerListeners();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw() {
/*  546 */     for (UUID uUID : this.inventories.keySet()) {
/*  547 */       Player player = this.plugin.getServer().getPlayer(uUID);
/*  548 */       if (player != null) {
/*  549 */         draw((HumanEntity)player);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(HumanEntity paramHumanEntity) {
/*  559 */     draw(paramHumanEntity, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(HumanEntity paramHumanEntity, boolean paramBoolean) {
/*  568 */     if (paramBoolean) {
/*  569 */       updateElements(paramHumanEntity, this.elements.values());
/*      */     }
/*  571 */     calculatePageAmount(paramHumanEntity);
/*  572 */     Inventory inventory = getInventory(paramHumanEntity);
/*  573 */     if (inventory == null) {
/*  574 */       build();
/*  575 */       if (this.slots.length != this.inventoryType.getDefaultSize()) {
/*  576 */         inventory = this.plugin.getServer().createInventory(new Holder(this), this.slots.length, replaceVars(paramHumanEntity, this.title, new String[0]));
/*      */       } else {
/*  578 */         inventory = this.plugin.getServer().createInventory(new Holder(this), this.inventoryType, replaceVars(paramHumanEntity, this.title, new String[0]));
/*      */       } 
/*  580 */       this.inventories.put((paramHumanEntity != null) ? paramHumanEntity.getUniqueId() : null, inventory);
/*      */     } else {
/*  582 */       inventory.clear();
/*      */     } 
/*  584 */     for (byte b = 0; b < inventory.getSize(); b++) {
/*  585 */       GuiElement guiElement = getElement(b);
/*  586 */       if (guiElement == null) {
/*  587 */         guiElement = getFiller();
/*      */       }
/*  589 */       if (guiElement != null) {
/*  590 */         inventory.setItem(b, guiElement.getItem(paramHumanEntity, b));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateElements(HumanEntity paramHumanEntity, Collection<GuiElement> paramCollection) {
/*  601 */     for (GuiElement guiElement : paramCollection) {
/*  602 */       if (guiElement instanceof DynamicGuiElement) {
/*  603 */         ((DynamicGuiElement)guiElement).update(paramHumanEntity); continue;
/*  604 */       }  if (guiElement instanceof GuiElementGroup) {
/*  605 */         updateElements(paramHumanEntity, ((GuiElementGroup)guiElement).getElements());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() {
/*  614 */     close(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close(boolean paramBoolean) {
/*  622 */     for (Inventory inventory : this.inventories.values()) {
/*  623 */       for (HumanEntity humanEntity : new ArrayList(inventory.getViewers())) {
/*  624 */         if (paramBoolean) {
/*  625 */           clearHistory(humanEntity);
/*      */         }
/*  627 */         humanEntity.closeInventory();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroy() {
/*  636 */     destroy(true);
/*      */   }
/*      */   
/*      */   private void destroy(boolean paramBoolean) {
/*  640 */     if (paramBoolean) {
/*  641 */       close();
/*      */     }
/*  643 */     for (Inventory inventory : this.inventories.values()) {
/*  644 */       inventory.clear();
/*      */     }
/*  646 */     this.inventories.clear();
/*  647 */     this.pageNumbers.clear();
/*  648 */     this.pageAmounts.clear();
/*  649 */     unregisterListeners();
/*  650 */     removeFromMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addHistory(HumanEntity paramHumanEntity, InventoryGui paramInventoryGui) {
/*  659 */     GUI_HISTORY.putIfAbsent(paramHumanEntity.getUniqueId(), new ArrayDeque<>());
/*  660 */     Deque<InventoryGui> deque = getHistory(paramHumanEntity);
/*  661 */     if (deque.peekLast() != paramInventoryGui) {
/*  662 */       deque.add(paramInventoryGui);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Deque<InventoryGui> getHistory(HumanEntity paramHumanEntity) {
/*  673 */     return GUI_HISTORY.getOrDefault(paramHumanEntity.getUniqueId(), new ArrayDeque<>());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean goBack(HumanEntity paramHumanEntity) {
/*  682 */     Deque<InventoryGui> deque = getHistory(paramHumanEntity);
/*  683 */     deque.pollLast();
/*  684 */     if (deque.isEmpty()) {
/*  685 */       return false;
/*      */     }
/*  687 */     InventoryGui inventoryGui = deque.peekLast();
/*  688 */     if (inventoryGui != null) {
/*  689 */       inventoryGui.show(paramHumanEntity, false);
/*      */     }
/*  691 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Deque<InventoryGui> clearHistory(HumanEntity paramHumanEntity) {
/*  700 */     if (GUI_HISTORY.containsKey(paramHumanEntity.getUniqueId())) {
/*  701 */       return GUI_HISTORY.remove(paramHumanEntity.getUniqueId());
/*      */     }
/*  703 */     return new ArrayDeque<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JavaPlugin getPlugin() {
/*  711 */     return this.plugin;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GuiElement getElement(int paramInt) {
/*  720 */     return (paramInt < 0 || paramInt >= this.slots.length) ? null : this.elements.get(Character.valueOf(this.slots[paramInt]));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GuiElement getElement(char paramChar) {
/*  729 */     return this.elements.get(Character.valueOf(paramChar));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<GuiElement> getElements() {
/*  738 */     return Collections.unmodifiableCollection(this.elements.values());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOwner(InventoryHolder paramInventoryHolder) {
/*  746 */     removeFromMap();
/*  747 */     this.owner = paramInventoryHolder;
/*  748 */     if (paramInventoryHolder instanceof Entity) {
/*  749 */       GUI_MAP.put(((Entity)paramInventoryHolder).getUniqueId().toString(), this);
/*  750 */     } else if (paramInventoryHolder instanceof BlockState) {
/*  751 */       GUI_MAP.put(((BlockState)paramInventoryHolder).getLocation().toString(), this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InventoryHolder getOwner() {
/*  760 */     return this.owner;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasRealOwner() {
/*  768 */     return (this.owner != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GuiElement.Action getOutsideAction() {
/*  776 */     return this.outsideAction;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutsideAction(GuiElement.Action paramAction) {
/*  784 */     this.outsideAction = paramAction;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CloseAction getCloseAction() {
/*  792 */     return this.closeAction;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCloseAction(CloseAction paramCloseAction) {
/*  800 */     this.closeAction = paramCloseAction;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSilent() {
/*  808 */     return this.silent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSilent(boolean paramBoolean) {
/*  816 */     this.silent = paramBoolean;
/*      */   }
/*      */   
/*      */   private void removeFromMap() {
/*  820 */     if (this.owner instanceof Entity) {
/*  821 */       GUI_MAP.remove(((Entity)this.owner).getUniqueId().toString(), this);
/*  822 */     } else if (this.owner instanceof BlockState) {
/*  823 */       GUI_MAP.remove(((BlockState)this.owner).getLocation().toString(), this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InventoryGui get(InventoryHolder paramInventoryHolder) {
/*  833 */     if (paramInventoryHolder instanceof Entity)
/*  834 */       return GUI_MAP.get(((Entity)paramInventoryHolder).getUniqueId().toString()); 
/*  835 */     if (paramInventoryHolder instanceof BlockState) {
/*  836 */       return GUI_MAP.get(((BlockState)paramInventoryHolder).getLocation().toString());
/*      */     }
/*  838 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InventoryGui getOpen(HumanEntity paramHumanEntity) {
/*  847 */     return getHistory(paramHumanEntity).peekLast();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTitle() {
/*  855 */     return this.title;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTitle(String paramString) {
/*  863 */     this.title = paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void playClickSound() {
/*  870 */     if (isSilent())
/*  871 */       return;  for (Inventory inventory : this.inventories.values()) {
/*  872 */       for (HumanEntity humanEntity : inventory.getViewers()) {
/*  873 */         if (humanEntity instanceof Player) {
/*  874 */           ((Player)humanEntity).playSound(humanEntity.getEyeLocation(), CLICK_SOUND, 1.0F, 1.0F);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Inventory getInventory() {
/*  885 */     return getInventory(null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Inventory getInventory(HumanEntity paramHumanEntity) {
/*  894 */     return (paramHumanEntity != null) ? this.inventories.get(paramHumanEntity.getUniqueId()) : (this.inventories.isEmpty() ? null : this.inventories.values().iterator().next());
/*      */   }
/*      */   
/*      */   private static interface UnregisterableListener extends Listener {
/*      */     void unregister();
/*      */     
/*      */     default boolean isCompatible() {
/*      */       try {
/*  902 */         getClass().getMethods();
/*  903 */         getClass().getDeclaredMethods();
/*  904 */         return true;
/*  905 */       } catch (NoClassDefFoundError e) {
/*  906 */         return false;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public class GuiListener
/*      */     implements UnregisterableListener
/*      */   {
/*      */     private final InventoryGui gui;
/*      */     
/*      */     private GuiListener(InventoryGui param1InventoryGui1) {
/*  918 */       this.gui = param1InventoryGui1;
/*      */     }
/*      */     
/*      */     @EventHandler
/*      */     private void onInventoryClick(InventoryClickEvent param1InventoryClickEvent) {
/*  923 */       if (param1InventoryClickEvent.getInventory().equals(InventoryGui.this.getInventory(param1InventoryClickEvent.getWhoClicked()))) {
/*      */         
/*  925 */         int i = -1;
/*  926 */         if (param1InventoryClickEvent.getRawSlot() < param1InventoryClickEvent.getView().getTopInventory().getSize()) {
/*  927 */           i = param1InventoryClickEvent.getRawSlot();
/*  928 */         } else if (param1InventoryClickEvent.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
/*  929 */           i = param1InventoryClickEvent.getInventory().firstEmpty();
/*      */         } 
/*      */         
/*  932 */         GuiElement.Action action = null;
/*  933 */         GuiElement guiElement = null;
/*  934 */         if (i >= 0) {
/*  935 */           guiElement = InventoryGui.this.getElement(i);
/*  936 */           if (guiElement != null) {
/*  937 */             action = guiElement.getAction(param1InventoryClickEvent.getWhoClicked());
/*      */           }
/*  939 */         } else if (i == -999) {
/*  940 */           action = InventoryGui.this.outsideAction;
/*      */         }
/*      */         else {
/*      */           
/*  944 */           if (param1InventoryClickEvent.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
/*  945 */             InventoryGui.this.simulateCollectToCursor(new GuiElement.Click(this.gui, i, null, param1InventoryClickEvent));
/*      */           }
/*      */           return;
/*      */         } 
/*      */         try {
/*  950 */           if (action == null || action.onClick(new GuiElement.Click(this.gui, i, guiElement, param1InventoryClickEvent))) {
/*  951 */             param1InventoryClickEvent.setCancelled(true);
/*  952 */             if (param1InventoryClickEvent.getWhoClicked() instanceof Player) {
/*  953 */               ((Player)param1InventoryClickEvent.getWhoClicked()).updateInventory();
/*      */             }
/*      */           } 
/*  956 */           if (action != null)
/*      */           {
/*  958 */             for (UUID uUID : InventoryGui.this.inventories.keySet()) {
/*  959 */               if (!param1InventoryClickEvent.getWhoClicked().getUniqueId().equals(uUID)) {
/*  960 */                 Player player = InventoryGui.this.plugin.getServer().getPlayer(uUID);
/*  961 */                 if (player != null) {
/*  962 */                   InventoryGui.this.draw((HumanEntity)player, false);
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           }
/*  967 */         } catch (Throwable throwable) {
/*  968 */           param1InventoryClickEvent.setCancelled(true);
/*  969 */           if (param1InventoryClickEvent.getWhoClicked() instanceof Player) {
/*  970 */             ((Player)param1InventoryClickEvent.getWhoClicked()).updateInventory();
/*      */           }
/*  972 */           InventoryGui.this.plugin.getLogger().log(Level.SEVERE, "Exception while trying to run action for click on " + ((guiElement != null) ? guiElement
/*  973 */               .getClass().getSimpleName() : "empty element") + " in slot " + param1InventoryClickEvent
/*  974 */               .getRawSlot() + " of " + this.gui.getTitle() + " GUI!");
/*  975 */           throwable.printStackTrace();
/*      */         } 
/*  977 */       } else if (InventoryGui.this.hasRealOwner() && InventoryGui.this.owner.equals(param1InventoryClickEvent.getInventory().getHolder())) {
/*      */ 
/*      */         
/*  980 */         InventoryGui.this.plugin.getServer().getScheduler().runTask((Plugin)InventoryGui.this.plugin, this.gui::draw);
/*      */       } 
/*      */     }
/*      */     
/*      */     @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
/*      */     public void onInventoryDrag(InventoryDragEvent param1InventoryDragEvent) {
/*  986 */       Inventory inventory = InventoryGui.this.getInventory(param1InventoryDragEvent.getWhoClicked());
/*  987 */       if (param1InventoryDragEvent.getInventory().equals(inventory)) {
/*  988 */         int i = 0;
/*  989 */         HashMap<Object, Object> hashMap = new HashMap<>();
/*  990 */         for (Map.Entry entry : param1InventoryDragEvent.getNewItems().entrySet()) {
/*  991 */           if (((Integer)entry.getKey()).intValue() < inventory.getSize()) {
/*  992 */             GuiElement guiElement = InventoryGui.this.getElement(((Integer)entry.getKey()).intValue());
/*  993 */             if (!(guiElement instanceof GuiStorageElement) || 
/*  994 */               !((GuiStorageElement)guiElement).setStorageItem(param1InventoryDragEvent.getWhoClicked(), ((Integer)entry.getKey()).intValue(), (ItemStack)entry.getValue())) {
/*  995 */               ItemStack itemStack = param1InventoryDragEvent.getInventory().getItem(((Integer)entry.getKey()).intValue());
/*  996 */               if (!((ItemStack)entry.getValue()).isSimilar(itemStack)) {
/*  997 */                 i += ((ItemStack)entry.getValue()).getAmount();
/*  998 */               } else if (itemStack != null) {
/*  999 */                 i += ((ItemStack)entry.getValue()).getAmount() - itemStack.getAmount();
/*      */               } 
/*      */               
/* 1002 */               hashMap.put(entry.getKey(), param1InventoryDragEvent.getInventory().getItem(((Integer)entry.getKey()).intValue()));
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/* 1007 */         InventoryGui.this.plugin.getServer().getScheduler().runTask((Plugin)InventoryGui.this.plugin, () -> {
/*      */               for (Map.Entry entry : param1Map.entrySet()) {
/*      */                 param1InventoryDragEvent.getView().getTopInventory().setItem(((Integer)entry.getKey()).intValue(), (ItemStack)entry.getValue());
/*      */               }
/*      */             });
/*      */         
/* 1013 */         if (i > 0) {
/* 1014 */           byte b = (param1InventoryDragEvent.getCursor() != null) ? param1InventoryDragEvent.getCursor().getAmount() : 0;
/* 1015 */           if (!param1InventoryDragEvent.getOldCursor().isSimilar(param1InventoryDragEvent.getCursor())) {
/* 1016 */             param1InventoryDragEvent.setCursor(param1InventoryDragEvent.getOldCursor());
/* 1017 */             b = 0;
/*      */           } 
/* 1019 */           int j = b + i;
/* 1020 */           if (j <= param1InventoryDragEvent.getCursor().getMaxStackSize()) {
/* 1021 */             param1InventoryDragEvent.getCursor().setAmount(j);
/*      */           } else {
/* 1023 */             param1InventoryDragEvent.getCursor().setAmount(param1InventoryDragEvent.getCursor().getMaxStackSize());
/* 1024 */             ItemStack itemStack = param1InventoryDragEvent.getCursor().clone();
/* 1025 */             int k = j - param1InventoryDragEvent.getCursor().getMaxStackSize();
/* 1026 */             if (k > 0) {
/* 1027 */               itemStack.setAmount(k);
/* 1028 */               for (ItemStack itemStack1 : param1InventoryDragEvent.getWhoClicked().getInventory().addItem(new ItemStack[] { itemStack }).values()) {
/* 1029 */                 param1InventoryDragEvent.getWhoClicked().getLocation().getWorld().dropItem(param1InventoryDragEvent.getWhoClicked().getLocation(), itemStack1);
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     @EventHandler(priority = EventPriority.MONITOR)
/*      */     public void onInventoryClose(InventoryCloseEvent param1InventoryCloseEvent) {
/* 1039 */       Inventory inventory = InventoryGui.this.getInventory(param1InventoryCloseEvent.getPlayer());
/* 1040 */       if (param1InventoryCloseEvent.getInventory().equals(inventory)) {
/*      */         
/* 1042 */         if (this.gui.equals(InventoryGui.getOpen(param1InventoryCloseEvent.getPlayer()))) {
/* 1043 */           if (InventoryGui.this.closeAction == null || InventoryGui.this.closeAction.onClose(new InventoryGui.Close(param1InventoryCloseEvent.getPlayer(), this.gui, param1InventoryCloseEvent))) {
/* 1044 */             InventoryGui.goBack(param1InventoryCloseEvent.getPlayer());
/*      */           } else {
/* 1046 */             InventoryGui.clearHistory(param1InventoryCloseEvent.getPlayer());
/*      */           } 
/*      */         }
/* 1049 */         if (InventoryGui.this.inventories.size() <= 1) {
/* 1050 */           InventoryGui.this.destroy(false);
/*      */         } else {
/* 1052 */           inventory.clear();
/* 1053 */           for (HumanEntity humanEntity : inventory.getViewers()) {
/* 1054 */             if (humanEntity != param1InventoryCloseEvent.getPlayer()) {
/* 1055 */               humanEntity.closeInventory();
/*      */             }
/*      */           } 
/* 1058 */           InventoryGui.this.inventories.remove(param1InventoryCloseEvent.getPlayer().getUniqueId());
/* 1059 */           InventoryGui.this.pageAmounts.remove(param1InventoryCloseEvent.getPlayer().getUniqueId());
/* 1060 */           InventoryGui.this.pageNumbers.remove(param1InventoryCloseEvent.getPlayer().getUniqueId());
/* 1061 */           for (GuiElement guiElement : InventoryGui.this.getElements()) {
/* 1062 */             if (guiElement instanceof DynamicGuiElement) {
/* 1063 */               ((DynamicGuiElement)guiElement).removeCachedElement(param1InventoryCloseEvent.getPlayer());
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     @EventHandler(priority = EventPriority.MONITOR)
/*      */     public void onInventoryMoveItem(InventoryMoveItemEvent param1InventoryMoveItemEvent) {
/* 1072 */       if (InventoryGui.this.hasRealOwner() && (InventoryGui.this.owner.equals(param1InventoryMoveItemEvent.getDestination().getHolder()) || InventoryGui.this.owner.equals(param1InventoryMoveItemEvent.getSource().getHolder()))) {
/* 1073 */         InventoryGui.this.plugin.getServer().getScheduler().runTask((Plugin)InventoryGui.this.plugin, this.gui::draw);
/*      */       }
/*      */     }
/*      */     
/*      */     @EventHandler(priority = EventPriority.MONITOR)
/*      */     public void onDispense(BlockDispenseEvent param1BlockDispenseEvent) {
/* 1079 */       if (InventoryGui.this.hasRealOwner() && InventoryGui.this.owner.equals(param1BlockDispenseEvent.getBlock().getState())) {
/* 1080 */         InventoryGui.this.plugin.getServer().getScheduler().runTask((Plugin)InventoryGui.this.plugin, this.gui::draw);
/*      */       }
/*      */     }
/*      */     
/*      */     @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
/*      */     public void onBlockBreak(BlockBreakEvent param1BlockBreakEvent) {
/* 1086 */       if (InventoryGui.this.hasRealOwner() && InventoryGui.this.owner.equals(param1BlockBreakEvent.getBlock().getState())) {
/* 1087 */         InventoryGui.this.destroy();
/*      */       }
/*      */     }
/*      */     
/*      */     @EventHandler(priority = EventPriority.MONITOR)
/*      */     public void onEntityDeath(EntityDeathEvent param1EntityDeathEvent) {
/* 1093 */       if (InventoryGui.this.hasRealOwner() && InventoryGui.this.owner.equals(param1EntityDeathEvent.getEntity())) {
/* 1094 */         InventoryGui.this.destroy();
/*      */       }
/*      */     }
/*      */     
/*      */     @EventHandler(priority = EventPriority.MONITOR)
/*      */     public void onPluginDisable(PluginDisableEvent param1PluginDisableEvent) {
/* 1100 */       if (param1PluginDisableEvent.getPlugin() == InventoryGui.this.plugin) {
/* 1101 */         InventoryGui.this.destroy();
/*      */       }
/*      */     }
/*      */     
/*      */     public void unregister() {
/* 1106 */       InventoryClickEvent.getHandlerList().unregister(this);
/* 1107 */       InventoryDragEvent.getHandlerList().unregister(this);
/* 1108 */       InventoryCloseEvent.getHandlerList().unregister(this);
/* 1109 */       InventoryMoveItemEvent.getHandlerList().unregister(this);
/* 1110 */       BlockDispenseEvent.getHandlerList().unregister(this);
/* 1111 */       BlockBreakEvent.getHandlerList().unregister(this);
/* 1112 */       EntityDeathEvent.getHandlerList().unregister(this);
/* 1113 */       PluginDisableEvent.getHandlerList().unregister(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public class ItemSwapGuiListener
/*      */     implements UnregisterableListener
/*      */   {
/*      */     private final InventoryGui gui;
/*      */ 
/*      */     
/*      */     private ItemSwapGuiListener(InventoryGui param1InventoryGui1) {
/* 1125 */       this.gui = param1InventoryGui1;
/*      */     }
/*      */     
/*      */     @EventHandler(priority = EventPriority.HIGHEST)
/*      */     public void onInventoryMoveItem(PlayerSwapHandItemsEvent param1PlayerSwapHandItemsEvent) {
/* 1130 */       Inventory inventory = InventoryGui.this.getInventory((HumanEntity)param1PlayerSwapHandItemsEvent.getPlayer());
/* 1131 */       if (param1PlayerSwapHandItemsEvent.getPlayer().getOpenInventory().getTopInventory().equals(inventory)) {
/* 1132 */         param1PlayerSwapHandItemsEvent.setCancelled(true);
/*      */       }
/*      */     }
/*      */     
/*      */     public void unregister() {
/* 1137 */       PlayerSwapHandItemsEvent.getHandlerList().unregister(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class Holder
/*      */     implements InventoryHolder
/*      */   {
/*      */     private InventoryGui gui;
/*      */     
/*      */     public Holder(InventoryGui param1InventoryGui) {
/* 1148 */       this.gui = param1InventoryGui;
/*      */     }
/*      */ 
/*      */     
/*      */     public Inventory getInventory() {
/* 1153 */       return this.gui.getInventory();
/*      */     }
/*      */     
/*      */     public InventoryGui getGui() {
/* 1157 */       return this.gui;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static interface CloseAction
/*      */   {
/*      */     boolean onClose(InventoryGui.Close param1Close);
/*      */   }
/*      */ 
/*      */   
/*      */   public static class Close
/*      */   {
/*      */     private final HumanEntity player;
/*      */     
/*      */     private final InventoryGui gui;
/*      */     
/*      */     private final InventoryCloseEvent event;
/*      */ 
/*      */     
/*      */     public Close(HumanEntity param1HumanEntity, InventoryGui param1InventoryGui, InventoryCloseEvent param1InventoryCloseEvent) {
/* 1178 */       this.player = param1HumanEntity;
/* 1179 */       this.gui = param1InventoryGui;
/* 1180 */       this.event = param1InventoryCloseEvent;
/*      */     }
/*      */     
/*      */     public HumanEntity getPlayer() {
/* 1184 */       return this.player;
/*      */     }
/*      */     
/*      */     public InventoryGui getGui() {
/* 1188 */       return this.gui;
/*      */     }
/*      */     
/*      */     public InventoryCloseEvent getEvent() {
/* 1192 */       return this.event;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setItemText(ItemStack paramItemStack, String... paramVarArgs) {
/* 1206 */     setItemText(null, paramItemStack, paramVarArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setItemText(HumanEntity paramHumanEntity, ItemStack paramItemStack, String... paramVarArgs) {
/* 1218 */     if (paramItemStack != null && paramVarArgs != null && paramVarArgs.length > 0) {
/* 1219 */       ItemMeta itemMeta = paramItemStack.getItemMeta();
/* 1220 */       if (itemMeta != null) {
/* 1221 */         String str = replaceVars(paramHumanEntity, Arrays.<String>stream(paramVarArgs)
/* 1222 */             .map(paramString -> (paramString == null) ? " " : paramString)
/* 1223 */             .filter(paramString -> !paramString.isEmpty())
/* 1224 */             .collect(Collectors.joining("\n")), new String[0]);
/* 1225 */         String[] arrayOfString = str.split("\n");
/* 1226 */         if (paramVarArgs[0] != null) {
/* 1227 */           itemMeta.setDisplayName(arrayOfString[0]);
/*      */         }
/* 1229 */         if (arrayOfString.length > 1) {
/* 1230 */           itemMeta.setLore(Arrays.asList(Arrays.copyOfRange((Object[])arrayOfString, 1, arrayOfString.length)));
/*      */         } else {
/* 1232 */           itemMeta.setLore(null);
/*      */         } 
/* 1234 */         paramItemStack.setItemMeta(itemMeta);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String replaceVars(String paramString, String... paramVarArgs) {
/* 1256 */     return replaceVars(null, paramString, paramVarArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replaceVars(HumanEntity paramHumanEntity, String paramString, String... paramVarArgs) {
/* 1275 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/* 1276 */     for (byte b = 0; b + 1 < paramVarArgs.length; b += 2) {
/* 1277 */       linkedHashMap.putIfAbsent(paramVarArgs[b], paramVarArgs[b + 1]);
/*      */     }
/*      */     
/* 1280 */     linkedHashMap.putIfAbsent("plugin", this.plugin.getName());
/*      */     try {
/* 1282 */       linkedHashMap.putIfAbsent("owner", (this.owner instanceof Nameable) ? ((Nameable)this.owner).getCustomName() : "");
/* 1283 */     } catch (NoSuchMethodError noSuchMethodError) {
/* 1284 */       linkedHashMap.putIfAbsent("owner", (this.owner instanceof Entity) ? ((Entity)this.owner).getCustomName() : "");
/*      */     } 
/* 1286 */     linkedHashMap.putIfAbsent("title", this.title);
/* 1287 */     linkedHashMap.putIfAbsent("page", String.valueOf(getPageNumber(paramHumanEntity) + 1));
/* 1288 */     linkedHashMap.putIfAbsent("nextpage", (getPageNumber(paramHumanEntity) + 1 < getPageAmount(paramHumanEntity)) ? String.valueOf(getPageNumber(paramHumanEntity) + 2) : "none");
/* 1289 */     linkedHashMap.putIfAbsent("prevpage", (getPageNumber(paramHumanEntity) > 0) ? String.valueOf(getPageNumber(paramHumanEntity)) : "none");
/* 1290 */     linkedHashMap.putIfAbsent("pages", String.valueOf(getPageAmount(paramHumanEntity)));
/*      */     
/* 1292 */     return ChatColor.translateAlternateColorCodes('&', replace(paramString, (Map)linkedHashMap));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String replace(String paramString, Map<String, String> paramMap) {
/* 1302 */     for (Map.Entry<String, String> entry : paramMap.entrySet()) {
/* 1303 */       if (entry.getKey() == null) {
/*      */         continue;
/*      */       }
/* 1306 */       String str = "%" + (String)entry.getKey() + "%";
/* 1307 */       Pattern pattern = PATTERN_CACHE.get(str);
/* 1308 */       if (pattern == null) {
/* 1309 */         PATTERN_CACHE.put(str, pattern = Pattern.compile(str, 16));
/*      */       }
/* 1311 */       paramString = pattern.matcher(paramString).replaceAll(Matcher.quoteReplacement((entry.getValue() != null) ? (String)entry.getValue() : "null"));
/*      */     } 
/* 1313 */     return paramString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void simulateCollectToCursor(GuiElement.Click paramClick) {
/* 1321 */     ItemStack itemStack = paramClick.getEvent().getCursor().clone();
/*      */     
/* 1323 */     boolean bool = false;
/* 1324 */     for (byte b = 0; b < paramClick.getEvent().getView().getTopInventory().getSize(); b++) {
/* 1325 */       if (b != paramClick.getEvent().getRawSlot()) {
/* 1326 */         ItemStack itemStack1 = paramClick.getEvent().getView().getTopInventory().getItem(b);
/* 1327 */         if (itemStack.isSimilar(itemStack1)) {
/* 1328 */           bool = true;
/*      */         }
/* 1330 */         GuiElement guiElement = getElement(b);
/* 1331 */         if (guiElement instanceof GuiStorageElement) {
/* 1332 */           GuiStorageElement guiStorageElement = (GuiStorageElement)guiElement;
/* 1333 */           ItemStack itemStack2 = guiStorageElement.getStorageItem(paramClick.getWhoClicked(), b);
/* 1334 */           if (addToStack(itemStack, itemStack2)) {
/* 1335 */             if (itemStack2.getAmount() == 0) {
/* 1336 */               itemStack2 = null;
/*      */             }
/* 1338 */             guiStorageElement.setStorageItem(b, itemStack2);
/* 1339 */             if (itemStack.getAmount() == itemStack.getMaxStackSize()) {
/*      */               break;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1347 */     if (bool) {
/* 1348 */       paramClick.getEvent().setCurrentItem(null);
/* 1349 */       paramClick.getEvent().setCancelled(true);
/* 1350 */       if (paramClick.getEvent().getWhoClicked() instanceof Player) {
/* 1351 */         ((Player)paramClick.getEvent().getWhoClicked()).updateInventory();
/*      */       }
/*      */       
/* 1354 */       if (paramClick.getElement() instanceof GuiStorageElement) {
/* 1355 */         ((GuiStorageElement)paramClick.getElement()).setStorageItem(paramClick.getWhoClicked(), paramClick.getSlot(), null);
/*      */       }
/*      */       
/* 1358 */       if (itemStack.getAmount() < itemStack.getMaxStackSize()) {
/* 1359 */         Inventory inventory = paramClick.getEvent().getView().getBottomInventory();
/* 1360 */         for (ListIterator<ItemStack> listIterator = inventory.iterator(); listIterator.hasNext(); ) { ItemStack itemStack1 = listIterator.next();
/* 1361 */           if (addToStack(itemStack, itemStack1) && 
/* 1362 */             itemStack.getAmount() == itemStack.getMaxStackSize()) {
/*      */             break;
/*      */           } }
/*      */       
/*      */       } 
/*      */       
/* 1368 */       paramClick.getEvent().setCursor(itemStack);
/* 1369 */       draw();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean addToStack(ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 1380 */     if (paramItemStack1.isSimilar(paramItemStack2)) {
/* 1381 */       int i = paramItemStack1.getAmount() + paramItemStack2.getAmount();
/* 1382 */       if (i >= paramItemStack1.getMaxStackSize()) {
/* 1383 */         paramItemStack1.setAmount(paramItemStack1.getMaxStackSize());
/* 1384 */         paramItemStack2.setAmount(i - paramItemStack1.getAmount());
/*      */       } else {
/* 1386 */         paramItemStack1.setAmount(i);
/* 1387 */         paramItemStack2.setAmount(0);
/*      */       } 
/* 1389 */       return true;
/*      */     } 
/* 1391 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\de\themoep\inventorygui\InventoryGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
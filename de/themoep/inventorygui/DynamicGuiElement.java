/*     */ package de.themoep.inventorygui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
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
/*     */ public class DynamicGuiElement
/*     */   extends GuiElement
/*     */ {
/*     */   private Function<HumanEntity, GuiElement> query;
/*  42 */   private Map<UUID, CacheEntry> cachedElements = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DynamicGuiElement(char paramChar, Supplier<GuiElement> paramSupplier) {
/*  50 */     this(paramChar, paramHumanEntity -> (GuiElement)paramSupplier.get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DynamicGuiElement(char paramChar, Function<HumanEntity, GuiElement> paramFunction) {
/*  59 */     super(paramChar);
/*  60 */     this.query = paramFunction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update() {
/*  67 */     for (UUID uUID : new ArrayList(this.cachedElements.keySet())) {
/*  68 */       Player player = this.gui.getPlugin().getServer().getPlayer(uUID);
/*  69 */       if (player != null && player.isOnline()) {
/*  70 */         update((HumanEntity)player); continue;
/*     */       } 
/*  72 */       this.cachedElements.remove(uUID);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheEntry update(HumanEntity paramHumanEntity) {
/*  82 */     CacheEntry cacheEntry = new CacheEntry(queryElement(paramHumanEntity));
/*  83 */     this.cachedElements.put(paramHumanEntity.getUniqueId(), cacheEntry);
/*  84 */     return cacheEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGui(InventoryGui paramInventoryGui) {
/*  89 */     super.setGui(paramInventoryGui);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(HumanEntity paramHumanEntity, int paramInt) {
/*  94 */     GuiElement guiElement = getCachedElement(paramHumanEntity);
/*  95 */     return (guiElement != null) ? guiElement.getItem(paramHumanEntity, paramInt) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuiElement.Action getAction(HumanEntity paramHumanEntity) {
/* 100 */     GuiElement guiElement = getCachedElement(paramHumanEntity);
/* 101 */     return (guiElement != null) ? guiElement.getAction(paramHumanEntity) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Function<HumanEntity, GuiElement> getQuery() {
/* 109 */     return this.query;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQuery(Function<HumanEntity, GuiElement> paramFunction) {
/* 117 */     this.query = paramFunction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiElement queryElement(HumanEntity paramHumanEntity) {
/* 126 */     GuiElement guiElement = getQuery().apply(paramHumanEntity);
/* 127 */     if (guiElement != null) {
/* 128 */       guiElement.setGui(this.gui);
/* 129 */       guiElement.setSlots(this.slots);
/*     */     } 
/* 131 */     return guiElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiElement getCachedElement(HumanEntity paramHumanEntity) {
/* 141 */     CacheEntry cacheEntry = this.cachedElements.get(paramHumanEntity.getUniqueId());
/* 142 */     if (cacheEntry == null) {
/* 143 */       cacheEntry = update(paramHumanEntity);
/*     */     }
/* 145 */     return cacheEntry.getElement();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiElement removeCachedElement(HumanEntity paramHumanEntity) {
/* 154 */     CacheEntry cacheEntry = this.cachedElements.remove(paramHumanEntity.getUniqueId());
/* 155 */     return (cacheEntry != null) ? cacheEntry.getElement() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastCached(HumanEntity paramHumanEntity) {
/* 164 */     CacheEntry cacheEntry = this.cachedElements.get(paramHumanEntity.getUniqueId());
/* 165 */     return (cacheEntry != null) ? cacheEntry.getCreated() : -1L;
/*     */   }
/*     */   
/*     */   public class CacheEntry {
/*     */     private final GuiElement element;
/* 170 */     private final long created = System.currentTimeMillis();
/*     */     
/*     */     CacheEntry(GuiElement param1GuiElement) {
/* 173 */       this.element = param1GuiElement;
/*     */     }
/*     */     
/*     */     public GuiElement getElement() {
/* 177 */       return this.element;
/*     */     }
/*     */     
/*     */     public long getCreated() {
/* 181 */       return this.created;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\de\themoep\inventorygui\DynamicGuiElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
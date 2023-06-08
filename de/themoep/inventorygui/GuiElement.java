/*     */ package de.themoep.inventorygui;
/*     */ 
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.event.inventory.ClickType;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
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
/*     */ public abstract class GuiElement
/*     */ {
/*     */   private final char slotChar;
/*     */   private Action action;
/*  36 */   protected int[] slots = new int[0];
/*     */ 
/*     */ 
/*     */   
/*     */   protected InventoryGui gui;
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiElement(char paramChar, Action paramAction) {
/*  45 */     this.slotChar = paramChar;
/*  46 */     setAction(paramAction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiElement(char paramChar) {
/*  54 */     this(paramChar, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getSlotChar() {
/*  62 */     return this.slotChar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract ItemStack getItem(HumanEntity paramHumanEntity, int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Action getAction(HumanEntity paramHumanEntity) {
/*  79 */     return this.action;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAction(Action paramAction) {
/*  88 */     this.action = paramAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getSlots() {
/*  96 */     return this.slots;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSlots(int[] paramArrayOfint) {
/* 104 */     this.slots = paramArrayOfint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSlotIndex(int paramInt) {
/* 113 */     return getSlotIndex(paramInt, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSlotIndex(int paramInt1, int paramInt2) {
/* 123 */     for (byte b = 0; b < this.slots.length; b++) {
/* 124 */       if (this.slots[b] == paramInt1) {
/* 125 */         return b + this.slots.length * paramInt2;
/*     */       }
/*     */     } 
/* 128 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGui(InventoryGui paramInventoryGui) {
/* 136 */     this.gui = paramInventoryGui;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Click
/*     */   {
/*     */     private final InventoryGui gui;
/*     */ 
/*     */     
/*     */     private final int slot;
/*     */ 
/*     */     
/*     */     private final GuiElement element;
/*     */ 
/*     */     
/*     */     private final InventoryClickEvent event;
/*     */ 
/*     */ 
/*     */     
/*     */     public Click(InventoryGui param1InventoryGui, int param1Int, GuiElement param1GuiElement, InventoryClickEvent param1InventoryClickEvent) {
/* 157 */       this.gui = param1InventoryGui;
/* 158 */       this.slot = param1Int;
/* 159 */       this.element = param1GuiElement;
/* 160 */       this.event = param1InventoryClickEvent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getSlot() {
/* 168 */       return this.slot;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public GuiElement getElement() {
/* 176 */       return this.element;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ClickType getType() {
/* 184 */       return this.event.getClick();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public HumanEntity getWhoClicked() {
/* 192 */       return this.event.getWhoClicked();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InventoryClickEvent getEvent() {
/* 200 */       return this.event;
/*     */     }
/*     */     
/*     */     public InventoryGui getGui() {
/* 204 */       return this.gui;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Action {
/*     */     boolean onClick(GuiElement.Click param1Click);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\de\themoep\inventorygui\GuiElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
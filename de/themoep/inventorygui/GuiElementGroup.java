/*     */ package de.themoep.inventorygui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.bukkit.entity.HumanEntity;
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
/*     */ public class GuiElementGroup
/*     */   extends GuiElement
/*     */ {
/*  37 */   private List<GuiElement> elements = new ArrayList<>();
/*  38 */   private GuiElement filler = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiElementGroup(char paramChar, GuiElement... paramVarArgs) {
/*  46 */     super(paramChar, null);
/*  47 */     setAction(paramClick -> {
/*     */           GuiElement guiElement = getElement(paramClick.getSlot(), paramClick.getGui().getPageNumber(paramClick.getWhoClicked()));
/*  49 */           return (guiElement != null && guiElement.getAction(paramClick.getEvent().getWhoClicked()) != null) ? guiElement.getAction(paramClick.getEvent().getWhoClicked()).onClick(paramClick) : true;
/*     */         });
/*     */ 
/*     */ 
/*     */     
/*  54 */     Collections.addAll(this.elements, paramVarArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(HumanEntity paramHumanEntity, int paramInt) {
/*  59 */     GuiElement guiElement = getElement(paramInt, this.gui.getPageNumber(paramHumanEntity));
/*  60 */     if (guiElement != null) {
/*  61 */       return guiElement.getItem(paramHumanEntity, paramInt);
/*     */     }
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGui(InventoryGui paramInventoryGui) {
/*  68 */     super.setGui(paramInventoryGui);
/*  69 */     for (GuiElement guiElement : this.elements) {
/*  70 */       guiElement.setGui(paramInventoryGui);
/*     */     }
/*  72 */     if (this.filler != null) {
/*  73 */       this.filler.setGui(paramInventoryGui);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSlots(int[] paramArrayOfint) {
/*  79 */     super.setSlots(paramArrayOfint);
/*  80 */     for (GuiElement guiElement : this.elements) {
/*  81 */       guiElement.setSlots(paramArrayOfint);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addElement(GuiElement paramGuiElement) {
/*  90 */     this.elements.add(paramGuiElement);
/*  91 */     paramGuiElement.setGui(this.gui);
/*  92 */     paramGuiElement.setSlots(this.slots);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addElements(GuiElement... paramVarArgs) {
/* 100 */     for (GuiElement guiElement : paramVarArgs) {
/* 101 */       addElement(guiElement);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addElements(Collection<GuiElement> paramCollection) {
/* 110 */     for (GuiElement guiElement : paramCollection) {
/* 111 */       addElement(guiElement);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiElement getElement(int paramInt) {
/* 121 */     return getElement(paramInt, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiElement getElement(int paramInt1, int paramInt2) {
/* 131 */     if (this.elements.isEmpty()) {
/* 132 */       return null;
/*     */     }
/* 134 */     int i = getSlotIndex(paramInt1, (this.slots.length < this.elements.size()) ? paramInt2 : 0);
/* 135 */     if (i > -1 && i < this.elements.size()) {
/* 136 */       return this.elements.get(i);
/*     */     }
/* 138 */     return this.filler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<GuiElement> getElements() {
/* 147 */     return Collections.unmodifiableList(this.elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearElements() {
/* 154 */     this.elements.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFiller(ItemStack paramItemStack) {
/* 162 */     this.filler = new StaticGuiElement(' ', paramItemStack, new String[] { " " });
/* 163 */     this.filler.setGui(this.gui);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFiller(GuiElement paramGuiElement) {
/* 171 */     this.filler = paramGuiElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiElement getFiller() {
/* 179 */     return this.filler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 187 */     return this.elements.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\de\themoep\inventorygui\GuiElementGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
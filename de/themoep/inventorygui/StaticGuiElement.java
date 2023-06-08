/*     */ package de.themoep.inventorygui;
/*     */ 
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
/*     */ public class StaticGuiElement
/*     */   extends GuiElement
/*     */ {
/*     */   private ItemStack item;
/*     */   private int number;
/*     */   private String[] text;
/*     */   
/*     */   public StaticGuiElement(char paramChar, ItemStack paramItemStack, int paramInt, GuiElement.Action paramAction, String... paramVarArgs) {
/*  51 */     super(paramChar, paramAction);
/*  52 */     this.item = paramItemStack;
/*  53 */     this.text = paramVarArgs;
/*  54 */     setNumber(paramInt);
/*     */   }
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
/*     */   public StaticGuiElement(char paramChar, ItemStack paramItemStack, GuiElement.Action paramAction, String... paramVarArgs) {
/*  69 */     this(paramChar, paramItemStack, (paramItemStack != null) ? paramItemStack.getAmount() : 1, paramAction, paramVarArgs);
/*     */   }
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
/*     */   public StaticGuiElement(char paramChar, ItemStack paramItemStack, String... paramVarArgs) {
/*  83 */     this(paramChar, paramItemStack, (paramItemStack != null) ? paramItemStack.getAmount() : 1, null, paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItem(ItemStack paramItemStack) {
/*  92 */     this.item = paramItemStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getRawItem() {
/* 101 */     return this.item;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(HumanEntity paramHumanEntity, int paramInt) {
/* 106 */     if (this.item == null) {
/* 107 */       return null;
/*     */     }
/* 109 */     ItemStack itemStack = this.item.clone();
/* 110 */     this.gui.setItemText(paramHumanEntity, itemStack, getText());
/* 111 */     if (this.number > 0 && this.number <= 64) {
/* 112 */       itemStack.setAmount(this.number);
/*     */     }
/* 114 */     return itemStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(String... paramVarArgs) {
/* 126 */     this.text = paramVarArgs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getText() {
/* 134 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setNumber(int paramInt) {
/* 143 */     if (paramInt < 1 || paramInt > 64) {
/* 144 */       this.number = 1;
/* 145 */       return false;
/*     */     } 
/* 147 */     this.number = paramInt;
/* 148 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumber() {
/* 156 */     return this.number;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\de\themoep\inventorygui\StaticGuiElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
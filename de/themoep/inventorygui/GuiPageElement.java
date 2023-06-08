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
/*     */ public class GuiPageElement
/*     */   extends StaticGuiElement
/*     */ {
/*     */   private PageAction pageAction;
/*     */   private boolean silent = false;
/*     */   
/*     */   public GuiPageElement(char paramChar, ItemStack paramItemStack, PageAction paramPageAction, String... paramVarArgs) {
/*  48 */     super(paramChar, paramItemStack, paramVarArgs);
/*  49 */     setAction(paramClick -> {
/*     */           switch (paramPageAction) {
/*     */             case NEXT:
/*     */               if (paramClick.getGui().getPageNumber(paramClick.getWhoClicked()) + 1 < paramClick.getGui().getPageAmount(paramClick.getWhoClicked())) {
/*     */                 if (!isSilent()) {
/*     */                   paramClick.getGui().playClickSound();
/*     */                 }
/*     */                 paramClick.getGui().setPageNumber(paramClick.getWhoClicked(), paramClick.getGui().getPageNumber(paramClick.getWhoClicked()) + 1);
/*     */               } 
/*     */               break;
/*     */             case PREVIOUS:
/*     */               if (paramClick.getGui().getPageNumber(paramClick.getWhoClicked()) > 0) {
/*     */                 if (!isSilent()) {
/*     */                   paramClick.getGui().playClickSound();
/*     */                 }
/*     */                 paramClick.getGui().setPageNumber(paramClick.getWhoClicked(), paramClick.getGui().getPageNumber(paramClick.getWhoClicked()) - 1);
/*     */               } 
/*     */               break;
/*     */             case FIRST:
/*     */               if (!isSilent()) {
/*     */                 paramClick.getGui().playClickSound();
/*     */               }
/*     */               paramClick.getGui().setPageNumber(paramClick.getWhoClicked(), 0);
/*     */               break;
/*     */             case LAST:
/*     */               if (!isSilent()) {
/*     */                 paramClick.getGui().playClickSound();
/*     */               }
/*     */               paramClick.getGui().setPageNumber(paramClick.getWhoClicked(), paramClick.getGui().getPageAmount(paramClick.getWhoClicked()) - 1);
/*     */               break;
/*     */           } 
/*     */           return true;
/*     */         });
/*  82 */     this.pageAction = paramPageAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSilent() {
/*  90 */     return this.silent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSilent(boolean paramBoolean) {
/*  98 */     this.silent = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(HumanEntity paramHumanEntity, int paramInt) {
/* 103 */     if (((this.pageAction == PageAction.FIRST || this.pageAction == PageAction.LAST) && this.gui.getPageAmount(paramHumanEntity) < 3) || (this.pageAction == PageAction.NEXT && this.gui
/* 104 */       .getPageNumber(paramHumanEntity) + 1 >= this.gui.getPageAmount(paramHumanEntity)) || (this.pageAction == PageAction.PREVIOUS && this.gui
/* 105 */       .getPageNumber(paramHumanEntity) == 0)) {
/* 106 */       return (this.gui.getFiller() != null) ? this.gui.getFiller().getItem(paramHumanEntity, paramInt) : null;
/*     */     }
/* 108 */     if (this.pageAction == PageAction.PREVIOUS) {
/* 109 */       setNumber(this.gui.getPageNumber(paramHumanEntity));
/* 110 */     } else if (this.pageAction == PageAction.NEXT) {
/* 111 */       setNumber(this.gui.getPageNumber(paramHumanEntity) + 2);
/* 112 */     } else if (this.pageAction == PageAction.LAST) {
/* 113 */       setNumber(this.gui.getPageAmount(paramHumanEntity));
/*     */     } 
/* 115 */     return super.getItem(paramHumanEntity, paramInt).clone();
/*     */   }
/*     */   
/*     */   public enum PageAction {
/* 119 */     NEXT,
/* 120 */     PREVIOUS,
/* 121 */     FIRST,
/* 122 */     LAST;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\de\themoep\inventorygui\GuiPageElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
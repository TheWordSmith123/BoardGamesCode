/*     */ package de.themoep.inventorygui;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.event.inventory.InventoryAction;
/*     */ import org.bukkit.inventory.Inventory;
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
/*     */ public class GuiStorageElement
/*     */   extends GuiElement
/*     */ {
/*     */   private final Inventory storage;
/*     */   private final int invSlot;
/*     */   private Runnable applyStorage;
/*     */   private Function<ValidatorInfo, Boolean> itemValidator;
/*     */   
/*     */   public GuiStorageElement(char paramChar, Inventory paramInventory) {
/*  52 */     this(paramChar, paramInventory, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiStorageElement(char paramChar, Inventory paramInventory, int paramInt) {
/*  62 */     this(paramChar, paramInventory, paramInt, (Runnable)null, (Function<ValidatorInfo, Boolean>)null);
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
/*     */   public GuiStorageElement(char paramChar, Inventory paramInventory, int paramInt, Runnable paramRunnable, Function<ValidatorInfo, Boolean> paramFunction) {
/*  75 */     super(paramChar, null);
/*  76 */     this.invSlot = paramInt;
/*  77 */     this.applyStorage = paramRunnable;
/*  78 */     this.itemValidator = paramFunction;
/*  79 */     setAction(paramClick -> {
/*     */           int i;
/*     */           ItemStack itemStack4;
/*     */           if (getStorageSlot(paramClick.getWhoClicked(), paramClick.getSlot()) < 0) {
/*     */             return true;
/*     */           }
/*     */           ItemStack itemStack1 = getStorageItem(paramClick.getWhoClicked(), paramClick.getSlot());
/*     */           ItemStack itemStack2 = paramClick.getEvent().getView().getTopInventory().getItem(paramClick.getSlot());
/*     */           if ((itemStack2 == null && itemStack1 != null && itemStack1.getType() != Material.AIR) || (itemStack1 == null && itemStack2 != null && itemStack2.getType() != Material.AIR) || (itemStack1 != null && !itemStack1.equals(itemStack2))) {
/*     */             this.gui.draw(paramClick.getEvent().getWhoClicked(), false);
/*     */             return true;
/*     */           } 
/*     */           ItemStack itemStack3 = null;
/*     */           switch (paramClick.getEvent().getAction()) {
/*     */             case NOTHING:
/*     */             case CLONE_STACK:
/*     */               return false;
/*     */             
/*     */             case MOVE_TO_OTHER_INVENTORY:
/*     */               if (paramClick.getEvent().getRawSlot() < paramClick.getEvent().getView().getTopInventory().getSize()) {
/*     */                 itemStack3 = null;
/*     */                 break;
/*     */               } 
/*     */               itemStack3 = paramClick.getEvent().getCurrentItem();
/*     */               break;
/*     */             case HOTBAR_MOVE_AND_READD:
/*     */             case HOTBAR_SWAP:
/*     */               i = paramClick.getEvent().getHotbarButton();
/*     */               if (i < 0) {
/*     */                 return true;
/*     */               }
/*     */               itemStack4 = paramClick.getEvent().getView().getBottomInventory().getItem(i);
/*     */               if (itemStack4 != null) {
/*     */                 itemStack3 = itemStack4.clone();
/*     */               }
/*     */               break;
/*     */             case PICKUP_ONE:
/*     */             case DROP_ONE_SLOT:
/*     */               itemStack3 = paramClick.getEvent().getCurrentItem().clone();
/*     */               itemStack3.setAmount(itemStack3.getAmount() - 1);
/*     */               break;
/*     */             case DROP_ALL_SLOT:
/*     */               itemStack3 = null;
/*     */               break;
/*     */             case PICKUP_HALF:
/*     */               itemStack3 = paramClick.getEvent().getCurrentItem().clone();
/*     */               itemStack3.setAmount(itemStack3.getAmount() / 2);
/*     */               break;
/*     */             case PLACE_SOME:
/*     */               if (paramClick.getEvent().getCurrentItem() == null) {
/*     */                 itemStack3 = paramClick.getEvent().getCursor();
/*     */                 break;
/*     */               } 
/*     */               itemStack3 = paramClick.getEvent().getCurrentItem().clone();
/*     */               if (itemStack3.getAmount() + paramClick.getEvent().getCursor().getAmount() < itemStack3.getMaxStackSize()) {
/*     */                 itemStack3.setAmount(itemStack3.getAmount() + paramClick.getEvent().getCursor().getAmount());
/*     */                 break;
/*     */               } 
/*     */               itemStack3.setAmount(itemStack3.getMaxStackSize());
/*     */               break;
/*     */             case PLACE_ONE:
/*     */               if (paramClick.getEvent().getCurrentItem() == null) {
/*     */                 itemStack3 = paramClick.getEvent().getCursor().clone();
/*     */                 itemStack3.setAmount(1);
/*     */                 break;
/*     */               } 
/*     */               itemStack3 = paramClick.getEvent().getCursor().clone();
/*     */               itemStack3.setAmount(paramClick.getEvent().getCurrentItem().getAmount() + 1);
/*     */               break;
/*     */             case PLACE_ALL:
/*     */               itemStack3 = paramClick.getEvent().getCursor().clone();
/*     */               if (paramClick.getEvent().getCurrentItem() != null && paramClick.getEvent().getCurrentItem().getAmount() > 0) {
/*     */                 itemStack3.setAmount(paramClick.getEvent().getCurrentItem().getAmount() + itemStack3.getAmount());
/*     */               }
/*     */               break;
/*     */             case PICKUP_ALL:
/*     */             case SWAP_WITH_CURSOR:
/*     */               itemStack3 = paramClick.getEvent().getCursor();
/*     */               break;
/*     */             case COLLECT_TO_CURSOR:
/*     */               if (paramClick.getEvent().getCursor() == null || (paramClick.getEvent().getCurrentItem() != null && paramClick.getEvent().getCurrentItem().getType() != Material.AIR)) {
/*     */                 return true;
/*     */               }
/*     */               this.gui.simulateCollectToCursor(paramClick);
/*     */               return false;
/*     */             default:
/*     */               paramClick.getEvent().getWhoClicked().sendMessage(ChatColor.RED + "The action " + paramClick.getEvent().getAction() + " is not supported! Sorry about that :(");
/*     */               return true;
/*     */           } 
/*     */           return !setStorageItem(paramClick.getWhoClicked(), paramClick.getSlot(), itemStack3);
/*     */         });
/* 170 */     this.storage = paramInventory;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(HumanEntity paramHumanEntity, int paramInt) {
/* 175 */     int i = getSlotIndex(paramInt);
/* 176 */     if (i > -1 && i < this.storage.getSize()) {
/* 177 */       return this.storage.getItem(i);
/*     */     }
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Inventory getStorage() {
/* 187 */     return this.storage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getStorageSlot(HumanEntity paramHumanEntity, int paramInt) {
/* 197 */     int i = (this.invSlot != -1) ? this.invSlot : getSlotIndex(paramInt, this.gui.getPageNumber(paramHumanEntity));
/* 198 */     if (i < 0 || i >= this.storage.getSize()) {
/* 199 */       return -1;
/*     */     }
/* 201 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ItemStack getStorageItem(int paramInt) {
/* 212 */     return getStorageItem((HumanEntity)null, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getStorageItem(HumanEntity paramHumanEntity, int paramInt) {
/* 222 */     int i = getStorageSlot(paramHumanEntity, paramInt);
/* 223 */     if (i == -1) {
/* 224 */       return null;
/*     */     }
/* 226 */     return this.storage.getItem(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean setStorageItem(int paramInt, ItemStack paramItemStack) {
/* 238 */     return setStorageItem((HumanEntity)null, paramInt, paramItemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setStorageItem(HumanEntity paramHumanEntity, int paramInt, ItemStack paramItemStack) {
/* 249 */     int i = getStorageSlot(paramHumanEntity, paramInt);
/* 250 */     if (i == -1) {
/* 251 */       return false;
/*     */     }
/* 253 */     if (!validateItem(paramInt, paramItemStack)) {
/* 254 */       return false;
/*     */     }
/* 256 */     this.storage.setItem(i, paramItemStack);
/* 257 */     if (this.applyStorage != null) {
/* 258 */       this.applyStorage.run();
/*     */     }
/* 260 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Runnable getApplyStorage() {
/* 268 */     return this.applyStorage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplyStorage(Runnable paramRunnable) {
/* 277 */     this.applyStorage = paramRunnable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Function<ValidatorInfo, Boolean> getItemValidator() {
/* 285 */     return this.itemValidator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItemValidator(Function<ValidatorInfo, Boolean> paramFunction) {
/* 294 */     this.itemValidator = paramFunction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean validateItem(int paramInt, ItemStack paramItemStack) {
/* 304 */     return (this.itemValidator == null || ((Boolean)this.itemValidator.apply(new ValidatorInfo(this, paramInt, paramItemStack))).booleanValue());
/*     */   }
/*     */   
/*     */   public static class ValidatorInfo {
/*     */     private final GuiElement element;
/*     */     private final int slot;
/*     */     private final ItemStack item;
/*     */     
/*     */     public ValidatorInfo(GuiElement param1GuiElement, int param1Int, ItemStack param1ItemStack) {
/* 313 */       this.item = param1ItemStack;
/* 314 */       this.slot = param1Int;
/* 315 */       this.element = param1GuiElement;
/*     */     }
/*     */     
/*     */     public GuiElement getElement() {
/* 319 */       return this.element;
/*     */     }
/*     */     
/*     */     public int getSlot() {
/* 323 */       return this.slot;
/*     */     }
/*     */     
/*     */     public ItemStack getItem() {
/* 327 */       return this.item;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\de\themoep\inventorygui\GuiStorageElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
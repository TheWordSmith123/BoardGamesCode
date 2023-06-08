/*     */ package water.of.cup.boardgames.game.inventories.trade;
/*     */ 
/*     */ import de.themoep.inventorygui.GuiElement;
/*     */ import de.themoep.inventorygui.GuiStorageElement;
/*     */ import java.util.ListIterator;
/*     */ import java.util.function.Function;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.inventory.InventoryAction;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ public class GuiStorageActionElement extends GuiElement {
/*     */   private final Inventory storage;
/*     */   private final int invSlot;
/*     */   private Runnable applyStorage;
/*     */   private Function<ValidatorInfo, Boolean> itemValidator;
/*     */   
/*     */   public GuiStorageActionElement(char paramChar, Inventory paramInventory, GameTradePlayer paramGameTradePlayer, GuiElement.Action paramAction) {
/*  22 */     super(paramChar, null);
/*  23 */     this.invSlot = -1;
/*  24 */     this.applyStorage = null;
/*  25 */     this.itemValidator = null;
/*  26 */     setAction(paramClick -> {
/*     */           ItemStack itemStack4;
/*     */           if (paramGameTradePlayer.isReady()) {
/*     */             return true;
/*     */           }
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
/*     */             
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
/*     */             
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
/*     */               simulateCollectToCursor(paramClick);
/*     */               return false;
/*     */             default:
/*     */               paramClick.getEvent().getWhoClicked().sendMessage(ChatColor.RED + "The action " + paramClick.getEvent().getAction() + " is not supported! Sorry about that :(");
/*     */               return true;
/*     */           } 
/*     */           int i = !setStorageItem(paramClick.getWhoClicked(), paramClick.getSlot(), itemStack3) ? 1 : 0;
/*     */           paramAction.onClick(null);
/*     */           return i;
/*     */         });
/* 123 */     this.storage = paramInventory;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(HumanEntity paramHumanEntity, int paramInt) {
/* 128 */     int i = getSlotIndex(paramInt);
/* 129 */     if (i > -1 && i < this.storage.getSize()) {
/* 130 */       return this.storage.getItem(i);
/*     */     }
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Inventory getStorage() {
/* 140 */     return this.storage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getStorageSlot(HumanEntity paramHumanEntity, int paramInt) {
/* 150 */     int i = (this.invSlot != -1) ? this.invSlot : getSlotIndex(paramInt, this.gui.getPageNumber(paramHumanEntity));
/* 151 */     if (i < 0 || i >= this.storage.getSize()) {
/* 152 */       return -1;
/*     */     }
/* 154 */     return i;
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
/* 165 */     return getStorageItem((HumanEntity)null, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getStorageItem(HumanEntity paramHumanEntity, int paramInt) {
/* 175 */     int i = getStorageSlot(paramHumanEntity, paramInt);
/* 176 */     if (i == -1) {
/* 177 */       return null;
/*     */     }
/* 179 */     return this.storage.getItem(i);
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
/* 191 */     return setStorageItem((HumanEntity)null, paramInt, paramItemStack);
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
/* 202 */     int i = getStorageSlot(paramHumanEntity, paramInt);
/* 203 */     if (i == -1) {
/* 204 */       return false;
/*     */     }
/* 206 */     if (!validateItem(paramInt, paramItemStack)) {
/* 207 */       return false;
/*     */     }
/* 209 */     this.storage.setItem(i, paramItemStack);
/* 210 */     if (this.applyStorage != null) {
/* 211 */       this.applyStorage.run();
/*     */     }
/* 213 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Runnable getApplyStorage() {
/* 221 */     return this.applyStorage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplyStorage(Runnable paramRunnable) {
/* 230 */     this.applyStorage = paramRunnable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Function<ValidatorInfo, Boolean> getItemValidator() {
/* 238 */     return this.itemValidator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItemValidator(Function<ValidatorInfo, Boolean> paramFunction) {
/* 247 */     this.itemValidator = paramFunction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean validateItem(int paramInt, ItemStack paramItemStack) {
/* 257 */     return (this.itemValidator == null || ((Boolean)this.itemValidator.apply(new ValidatorInfo(this, paramInt, paramItemStack))).booleanValue());
/*     */   }
/*     */   
/*     */   public static class ValidatorInfo {
/*     */     private final GuiElement element;
/*     */     private final int slot;
/*     */     private final ItemStack item;
/*     */     
/*     */     public ValidatorInfo(GuiElement param1GuiElement, int param1Int, ItemStack param1ItemStack) {
/* 266 */       this.item = param1ItemStack;
/* 267 */       this.slot = param1Int;
/* 268 */       this.element = param1GuiElement;
/*     */     }
/*     */     
/*     */     public GuiElement getElement() {
/* 272 */       return this.element;
/*     */     }
/*     */     
/*     */     public int getSlot() {
/* 276 */       return this.slot;
/*     */     }
/*     */     
/*     */     public ItemStack getItem() {
/* 280 */       return this.item;
/*     */     }
/*     */   }
/*     */   
/*     */   private void simulateCollectToCursor(GuiElement.Click paramClick) {
/* 285 */     ItemStack itemStack = paramClick.getEvent().getCursor().clone();
/*     */     
/* 287 */     boolean bool = false;
/* 288 */     for (byte b = 0; b < paramClick.getEvent().getView().getTopInventory().getSize(); b++) {
/* 289 */       if (b != paramClick.getEvent().getRawSlot()) {
/* 290 */         ItemStack itemStack1 = paramClick.getEvent().getView().getTopInventory().getItem(b);
/* 291 */         if (itemStack.isSimilar(itemStack1)) {
/* 292 */           bool = true;
/*     */         }
/* 294 */         GuiElement guiElement = this.gui.getElement(b);
/* 295 */         if (guiElement instanceof GuiStorageElement) {
/* 296 */           GuiStorageElement guiStorageElement = (GuiStorageElement)guiElement;
/* 297 */           ItemStack itemStack2 = guiStorageElement.getStorageItem(paramClick.getWhoClicked(), b);
/* 298 */           if (addToStack(itemStack, itemStack2)) {
/* 299 */             if (itemStack2.getAmount() == 0) {
/* 300 */               itemStack2 = null;
/*     */             }
/* 302 */             guiStorageElement.setStorageItem(b, itemStack2);
/* 303 */             if (itemStack.getAmount() == itemStack.getMaxStackSize()) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 311 */     if (bool) {
/* 312 */       paramClick.getEvent().setCurrentItem(null);
/* 313 */       paramClick.getEvent().setCancelled(true);
/* 314 */       if (paramClick.getEvent().getWhoClicked() instanceof Player) {
/* 315 */         ((Player)paramClick.getEvent().getWhoClicked()).updateInventory();
/*     */       }
/*     */       
/* 318 */       if (paramClick.getElement() instanceof GuiStorageElement) {
/* 319 */         ((GuiStorageElement)paramClick.getElement()).setStorageItem(paramClick.getWhoClicked(), paramClick.getSlot(), null);
/*     */       }
/*     */       
/* 322 */       if (itemStack.getAmount() < itemStack.getMaxStackSize()) {
/* 323 */         Inventory inventory = paramClick.getEvent().getView().getBottomInventory();
/* 324 */         for (ListIterator<ItemStack> listIterator = inventory.iterator(); listIterator.hasNext(); ) { ItemStack itemStack1 = listIterator.next();
/* 325 */           if (addToStack(itemStack, itemStack1) && 
/* 326 */             itemStack.getAmount() == itemStack.getMaxStackSize()) {
/*     */             break;
/*     */           } }
/*     */       
/*     */       } 
/*     */       
/* 332 */       paramClick.getEvent().setCursor(itemStack);
/* 333 */       this.gui.draw();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean addToStack(ItemStack paramItemStack1, ItemStack paramItemStack2) {
/* 338 */     if (paramItemStack1.isSimilar(paramItemStack2)) {
/* 339 */       int i = paramItemStack1.getAmount() + paramItemStack2.getAmount();
/* 340 */       if (i >= paramItemStack1.getMaxStackSize()) {
/* 341 */         paramItemStack1.setAmount(paramItemStack1.getMaxStackSize());
/* 342 */         paramItemStack2.setAmount(i - paramItemStack1.getAmount());
/*     */       } else {
/* 344 */         paramItemStack1.setAmount(i);
/* 345 */         paramItemStack2.setAmount(0);
/*     */       } 
/* 347 */       return true;
/*     */     } 
/* 349 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\trade\GuiStorageActionElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
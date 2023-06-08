/*     */ package de.themoep.inventorygui;
/*     */ 
/*     */ import java.util.function.Supplier;
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
/*     */ public class GuiStateElement
/*     */   extends GuiElement
/*     */ {
/*  35 */   private Supplier<Integer> queryState = null;
/*     */ 
/*     */   
/*     */   private boolean silent = false;
/*     */ 
/*     */   
/*     */   private int currentState;
/*     */   
/*     */   private final State[] states;
/*     */ 
/*     */   
/*     */   public GuiStateElement(char paramChar, int paramInt, State... paramVarArgs) {
/*  47 */     super(paramChar, null);
/*  48 */     if (paramVarArgs.length == 0) {
/*  49 */       throw new IllegalArgumentException("You need to add at least one State!");
/*     */     }
/*  51 */     this.currentState = paramInt;
/*  52 */     this.states = paramVarArgs;
/*     */     
/*  54 */     setAction(paramClick -> {
/*     */           State state = nextState();
/*     */           paramClick.getEvent().setCurrentItem(state.getItem(paramClick.getWhoClicked()));
/*     */           state.change.onChange(paramClick);
/*     */           if (!isSilent()) {
/*     */             paramClick.getGui().playClickSound();
/*     */           }
/*     */           return true;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiStateElement(char paramChar, String paramString, State... paramVarArgs) {
/*  72 */     this(paramChar, getStateIndex(paramString, paramVarArgs), paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiStateElement(char paramChar, Supplier<String> paramSupplier, State... paramVarArgs) {
/*  82 */     this(paramChar, paramSupplier.get(), paramVarArgs);
/*  83 */     this.queryState = (() -> Integer.valueOf(getStateIndex(paramSupplier.get(), paramArrayOfState)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuiStateElement(char paramChar, State... paramVarArgs) {
/*  92 */     this(paramChar, 0, paramVarArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public State nextState() {
/* 100 */     queryCurrentState();
/* 101 */     this.currentState = (this.states.length > this.currentState + 1) ? (this.currentState + 1) : 0;
/* 102 */     return this.states[this.currentState];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public State previousState() {
/* 110 */     queryCurrentState();
/* 111 */     this.currentState = (this.currentState > 0) ? (this.currentState - 1) : (this.states.length - 1);
/* 112 */     return this.states[this.currentState];
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(HumanEntity paramHumanEntity, int paramInt) {
/* 117 */     return getState().getItem(paramHumanEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGui(InventoryGui paramInventoryGui) {
/* 122 */     super.setGui(paramInventoryGui);
/* 123 */     for (State state : this.states) {
/* 124 */       state.setGui(paramInventoryGui);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public State getState() {
/* 133 */     queryCurrentState();
/* 134 */     return this.states[this.currentState];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSilent() {
/* 142 */     return this.silent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSilent(boolean paramBoolean) {
/* 150 */     this.silent = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void queryCurrentState() {
/* 157 */     if (this.queryState != null) {
/* 158 */       this.currentState = ((Integer)this.queryState.get()).intValue();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setState(String paramString) {
/* 168 */     this.currentState = getStateIndex(paramString, this.states);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getStateIndex(String paramString, State[] paramArrayOfState) {
/* 179 */     for (byte b = 0; b < paramArrayOfState.length; b++) {
/* 180 */       if (paramArrayOfState[b].getKey().equals(paramString)) {
/* 181 */         return b;
/*     */       }
/*     */     } 
/* 184 */     throw new IllegalArgumentException("This element does not have the state " + paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class State
/*     */   {
/*     */     private final Change change;
/*     */ 
/*     */     
/*     */     private final String key;
/*     */ 
/*     */     
/*     */     private final ItemStack item;
/*     */ 
/*     */     
/*     */     private String[] text;
/*     */ 
/*     */     
/*     */     private InventoryGui gui;
/*     */ 
/*     */ 
/*     */     
/*     */     public State(Change param1Change, String param1String, ItemStack param1ItemStack, String... param1VarArgs) {
/* 209 */       this.change = param1Change;
/* 210 */       this.key = param1String;
/* 211 */       this.item = param1ItemStack;
/* 212 */       this.text = param1VarArgs;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setText(String... param1VarArgs) {
/* 224 */       this.text = param1VarArgs;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public ItemStack getItem() {
/* 234 */       return getItem(null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ItemStack getItem(HumanEntity param1HumanEntity) {
/* 243 */       ItemStack itemStack = this.item.clone();
/* 244 */       this.gui.setItemText(param1HumanEntity, itemStack, getText());
/* 245 */       return itemStack;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getKey() {
/* 253 */       return this.key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getText() {
/* 261 */       return this.text;
/*     */     }
/*     */     
/*     */     private void setGui(InventoryGui param1InventoryGui) {
/* 265 */       this.gui = param1InventoryGui;
/*     */     }
/*     */     
/*     */     public static interface Change {
/*     */       void onChange(GuiElement.Click param2Click);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Change {
/*     */     void onChange(GuiElement.Click param1Click);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\de\themoep\inventorygui\GuiStateElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
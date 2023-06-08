/*    */ package water.of.cup.boardgames.game.games.uno;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnoHand
/*    */ {
/*  9 */   ArrayList<UnoCard> cards = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public ArrayList<UnoCard> draw(UnoDeck paramUnoDeck, int paramInt) {
/* 13 */     ArrayList<UnoCard> arrayList = paramUnoDeck.drawCards(paramInt);
/* 14 */     this.cards.addAll(arrayList);
/* 15 */     return arrayList;
/*    */   }
/*    */   
/*    */   public ArrayList<UnoCard> getCards() {
/* 19 */     return new ArrayList<>(this.cards);
/*    */   }
/*    */   
/*    */   public ArrayList<UnoCard> getCards(UnoCard paramUnoCard) {
/* 23 */     if (paramUnoCard == null) {
/* 24 */       return getCards();
/*    */     }
/* 26 */     ArrayList<UnoCard> arrayList = new ArrayList();
/* 27 */     for (UnoCard unoCard : this.cards) {
/* 28 */       if (unoCard.matches(paramUnoCard)) {
/* 29 */         arrayList.add(0, unoCard); continue;
/*    */       } 
/* 31 */       arrayList.add(unoCard);
/*    */     } 
/*    */     
/* 34 */     return arrayList;
/*    */   }
/*    */   
/*    */   public void removeCard(UnoCard paramUnoCard) {
/* 38 */     this.cards.remove(paramUnoCard);
/*    */   }
/*    */   
/*    */   public int cardsLeft() {
/* 42 */     return this.cards.size();
/*    */   }
/*    */   
/*    */   public boolean canPlay(UnoCard paramUnoCard) {
/* 46 */     for (UnoCard unoCard : this.cards) {
/* 47 */       if (unoCard.matches(paramUnoCard))
/* 48 */         return true; 
/*    */     } 
/* 50 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\game\\uno\UnoHand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
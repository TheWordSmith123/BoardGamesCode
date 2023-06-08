/*    */ package water.of.cup.boardgames.game.games.uno;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ 
/*    */ public class UnoDeck {
/*    */   ArrayList<UnoCard> cards;
/*    */   
/*    */   public UnoDeck() {
/* 10 */     this.cards = new ArrayList<>();
/* 11 */     fillDeck();
/*    */   }
/*    */   
/*    */   private void fillDeck() {
/*    */     byte b;
/* 16 */     for (b = 0; b < 10; b++) {
/* 17 */       for (String str : new String[] { "RED", "YELLOW", "BLUE", "GREEN" }) {
/* 18 */         this.cards.add(new UnoCard("" + b, str));
/* 19 */         if (b > 0) {
/* 20 */           this.cards.add(new UnoCard("" + b, str));
/*    */         }
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 26 */     for (String str : new String[] { "DRAW2", "REVERSE", "SKIP" }) {
/* 27 */       for (String str1 : new String[] { "RED", "YELLOW", "BLUE", "GREEN" }) {
/* 28 */         this.cards.add(new UnoCard(str, str1));
/* 29 */         this.cards.add(new UnoCard(str, str1));
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 34 */     for (b = 0; b < 4; b++) {
/* 35 */       this.cards.add(new UnoCard("WILD", "ALL"));
/* 36 */       this.cards.add(new UnoCard("WILDDRAW4", "ALL"));
/*    */     } 
/*    */     
/* 39 */     Collections.shuffle(this.cards);
/*    */   }
/*    */   
/*    */   public ArrayList<UnoCard> drawCards(int paramInt) {
/* 43 */     ArrayList<UnoCard> arrayList = new ArrayList();
/* 44 */     for (byte b = 0; b < paramInt; b++) {
/* 45 */       if (this.cards.size() == 0) {
/* 46 */         fillDeck();
/*    */       }
/* 48 */       UnoCard unoCard = this.cards.get(0);
/* 49 */       this.cards.remove(0);
/* 50 */       arrayList.add(unoCard);
/*    */     } 
/*    */     
/* 53 */     return arrayList;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\game\\uno\UnoDeck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package water.of.cup.boardgames.game.games.chess;
/*    */ 
/*    */ import java.sql.ResultSet;
/*    */ 
/*    */ 
/*    */ public class OldChessPlayer
/*    */ {
/*    */   private final int id;
/*    */   private final String uuid;
/*    */   private final int wins;
/*    */   private final int losses;
/*    */   private final int ties;
/*    */   private final double rating;
/*    */   private final double ratingDeviation;
/*    */   private final double volatility;
/*    */   private final int numberOfResults;
/*    */   
/*    */   public OldChessPlayer(ResultSet paramResultSet) {
/* 19 */     this.id = paramResultSet.getInt(1);
/* 20 */     this.uuid = paramResultSet.getString(2);
/* 21 */     this.wins = paramResultSet.getInt(3);
/* 22 */     this.losses = paramResultSet.getInt(4);
/* 23 */     this.ties = paramResultSet.getInt(5);
/* 24 */     this.rating = paramResultSet.getDouble(6);
/* 25 */     this.ratingDeviation = paramResultSet.getDouble(7);
/* 26 */     this.volatility = paramResultSet.getDouble(8);
/* 27 */     this.numberOfResults = paramResultSet.getInt(9);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 31 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getUuid() {
/* 35 */     return this.uuid;
/*    */   }
/*    */   
/*    */   public int getWins() {
/* 39 */     return this.wins;
/*    */   }
/*    */   
/*    */   public int getLosses() {
/* 43 */     return this.losses;
/*    */   }
/*    */   
/*    */   public int getTies() {
/* 47 */     return this.ties;
/*    */   }
/*    */   
/*    */   public double getRating() {
/* 51 */     return this.rating;
/*    */   }
/*    */   
/*    */   public double getRatingDeviation() {
/* 55 */     return this.ratingDeviation;
/*    */   }
/*    */   
/*    */   public double getVolatility() {
/* 59 */     return this.volatility;
/*    */   }
/*    */   
/*    */   public int getNumberOfResults() {
/* 63 */     return this.numberOfResults;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\chess\OldChessPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
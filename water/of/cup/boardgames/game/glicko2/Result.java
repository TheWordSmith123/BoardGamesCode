/*     */ package water.of.cup.boardgames.game.glicko2;
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
/*     */ public class Result
/*     */ {
/*     */   private static final double POINTS_FOR_WIN = 1.0D;
/*     */   private static final double POINTS_FOR_LOSS = 0.0D;
/*     */   private static final double POINTS_FOR_DRAW = 0.5D;
/*     */   private boolean isDraw = false;
/*     */   private Rating winner;
/*     */   private Rating loser;
/*     */   
/*     */   public Result(Rating paramRating1, Rating paramRating2) {
/*  31 */     if (!validPlayers(paramRating1, paramRating2)) {
/*  32 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*  35 */     this.winner = paramRating1;
/*  36 */     this.loser = paramRating2;
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
/*     */   public Result(Rating paramRating1, Rating paramRating2, boolean paramBoolean) {
/*  48 */     if (!paramBoolean || !validPlayers(paramRating1, paramRating2)) {
/*  49 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/*  52 */     this.winner = paramRating1;
/*  53 */     this.loser = paramRating2;
/*  54 */     this.isDraw = true;
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
/*     */   private boolean validPlayers(Rating paramRating1, Rating paramRating2) {
/*  66 */     if (paramRating1.equals(paramRating2)) {
/*  67 */       return false;
/*     */     }
/*  69 */     return true;
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
/*     */   public boolean participated(Rating paramRating) {
/*  81 */     if (this.winner.equals(paramRating) || this.loser.equals(paramRating)) {
/*  82 */       return true;
/*     */     }
/*  84 */     return false;
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
/*     */   public double getScore(Rating paramRating) {
/*     */     double d;
/*  99 */     if (this.winner.equals(paramRating)) {
/* 100 */       d = 1.0D;
/* 101 */     } else if (this.loser.equals(paramRating)) {
/* 102 */       d = 0.0D;
/*     */     } else {
/* 104 */       throw new IllegalArgumentException("Player " + paramRating.getUid() + " did not participate in match");
/*     */     } 
/*     */     
/* 107 */     if (this.isDraw) {
/* 108 */       d = 0.5D;
/*     */     }
/*     */     
/* 111 */     return d;
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
/*     */   public Rating getOpponent(Rating paramRating) {
/*     */     Rating rating;
/* 124 */     if (this.winner.equals(paramRating)) {
/* 125 */       rating = this.loser;
/* 126 */     } else if (this.loser.equals(paramRating)) {
/* 127 */       rating = this.winner;
/*     */     } else {
/* 129 */       throw new IllegalArgumentException("Player " + paramRating.getUid() + " did not participate in match");
/*     */     } 
/*     */     
/* 132 */     return rating;
/*     */   }
/*     */ 
/*     */   
/*     */   public Rating getWinner() {
/* 137 */     return this.winner;
/*     */   }
/*     */ 
/*     */   
/*     */   public Rating getLoser() {
/* 142 */     return this.loser;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\glicko2\Result.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
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
/*     */ public class Rating
/*     */ {
/*     */   private String uid;
/*     */   private double rating;
/*     */   private double ratingDeviation;
/*     */   private double volatility;
/*  25 */   private int numberOfResults = 0;
/*     */ 
/*     */   
/*     */   private double workingRating;
/*     */ 
/*     */   
/*     */   private double workingRatingDeviation;
/*     */ 
/*     */   
/*     */   private double workingVolatility;
/*     */ 
/*     */   
/*     */   public Rating(String paramString, RatingCalculator paramRatingCalculator) {
/*  38 */     this.uid = paramString;
/*  39 */     this.rating = paramRatingCalculator.getDefaultRating();
/*  40 */     this.ratingDeviation = paramRatingCalculator.getDefaultRatingDeviation();
/*  41 */     this.volatility = paramRatingCalculator.getDefaultVolatility();
/*     */   }
/*     */   
/*     */   public Rating(String paramString, RatingCalculator paramRatingCalculator, double paramDouble1, double paramDouble2, double paramDouble3) {
/*  45 */     this.uid = paramString;
/*  46 */     this.rating = paramDouble1;
/*  47 */     this.ratingDeviation = paramDouble2;
/*  48 */     this.volatility = paramDouble3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRating() {
/*  57 */     return this.rating;
/*     */   }
/*     */   
/*     */   public void setRating(double paramDouble) {
/*  61 */     this.rating = paramDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getGlicko2Rating() {
/*  71 */     return RatingCalculator.convertRatingToGlicko2Scale(this.rating);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGlicko2Rating(double paramDouble) {
/*  80 */     this.rating = RatingCalculator.convertRatingToOriginalGlickoScale(paramDouble);
/*     */   }
/*     */   
/*     */   public double getVolatility() {
/*  84 */     return this.volatility;
/*     */   }
/*     */   
/*     */   public void setVolatility(double paramDouble) {
/*  88 */     this.volatility = paramDouble;
/*     */   }
/*     */   
/*     */   public double getRatingDeviation() {
/*  92 */     return this.ratingDeviation;
/*     */   }
/*     */   
/*     */   public void setRatingDeviation(double paramDouble) {
/*  96 */     this.ratingDeviation = paramDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getGlicko2RatingDeviation() {
/* 106 */     return RatingCalculator.convertRatingDeviationToGlicko2Scale(this.ratingDeviation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGlicko2RatingDeviation(double paramDouble) {
/* 115 */     this.ratingDeviation = RatingCalculator.convertRatingDeviationToOriginalGlickoScale(paramDouble);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finaliseRating() {
/* 123 */     setGlicko2Rating(this.workingRating);
/* 124 */     setGlicko2RatingDeviation(this.workingRatingDeviation);
/* 125 */     setVolatility(this.workingVolatility);
/*     */     
/* 127 */     setWorkingRatingDeviation(0.0D);
/* 128 */     setWorkingRating(0.0D);
/* 129 */     setWorkingVolatility(0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 139 */     return this.uid + " / " + this.rating + " / " + this.ratingDeviation + " / " + this.volatility + " / " + this.numberOfResults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfResults() {
/* 147 */     return this.numberOfResults;
/*     */   }
/*     */   
/*     */   public void incrementNumberOfResults(int paramInt) {
/* 151 */     this.numberOfResults += paramInt;
/*     */   }
/*     */   
/*     */   public String getUid() {
/* 155 */     return this.uid;
/*     */   }
/*     */   
/*     */   public void setWorkingVolatility(double paramDouble) {
/* 159 */     this.workingVolatility = paramDouble;
/*     */   }
/*     */   
/*     */   public void setWorkingRating(double paramDouble) {
/* 163 */     this.workingRating = paramDouble;
/*     */   }
/*     */   
/*     */   public void setWorkingRatingDeviation(double paramDouble) {
/* 167 */     this.workingRatingDeviation = paramDouble;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\glicko2\Rating.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
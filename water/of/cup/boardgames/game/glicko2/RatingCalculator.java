/*     */ package water.of.cup.boardgames.game.glicko2;
/*     */ 
/*     */ import java.util.List;
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
/*     */ public class RatingCalculator
/*     */ {
/*     */   private static final double DEFAULT_RATING = 1500.0D;
/*     */   private static final double DEFAULT_DEVIATION = 350.0D;
/*     */   private static final double DEFAULT_VOLATILITY = 0.06D;
/*     */   private static final double DEFAULT_TAU = 0.75D;
/*     */   private static final double MULTIPLIER = 173.7178D;
/*     */   private static final double CONVERGENCE_TOLERANCE = 1.0E-6D;
/*     */   private double tau;
/*     */   private double defaultVolatility;
/*     */   
/*     */   public RatingCalculator() {
/*  35 */     this.tau = 0.75D;
/*  36 */     this.defaultVolatility = 0.06D;
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
/*     */   public RatingCalculator(double paramDouble1, double paramDouble2) {
/*  49 */     this.defaultVolatility = paramDouble1;
/*  50 */     this.tau = paramDouble2;
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
/*     */   public void updateRatings(RatingPeriodResults paramRatingPeriodResults) {
/*  63 */     for (Rating rating : paramRatingPeriodResults.getParticipants()) {
/*  64 */       if (paramRatingPeriodResults.getResults(rating).size() > 0) {
/*  65 */         calculateNewRating(rating, paramRatingPeriodResults.getResults(rating));
/*     */         
/*     */         continue;
/*     */       } 
/*  69 */       rating.setWorkingRating(rating.getGlicko2Rating());
/*  70 */       rating.setWorkingRatingDeviation(calculateNewRD(rating.getGlicko2RatingDeviation(), rating.getVolatility()));
/*  71 */       rating.setWorkingVolatility(rating.getVolatility());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  76 */     for (Rating rating : paramRatingPeriodResults.getParticipants()) {
/*  77 */       rating.finaliseRating();
/*     */     }
/*     */ 
/*     */     
/*  81 */     paramRatingPeriodResults.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void calculateNewRating(Rating paramRating, List<Result> paramList) {
/*  92 */     double d1 = paramRating.getGlicko2RatingDeviation();
/*  93 */     double d2 = paramRating.getVolatility();
/*  94 */     double d3 = Math.log(Math.pow(d2, 2.0D));
/*  95 */     double d4 = delta(paramRating, paramList);
/*  96 */     double d5 = v(paramRating, paramList);
/*     */ 
/*     */     
/*  99 */     double d6 = d3;
/* 100 */     double d7 = 0.0D;
/* 101 */     if (Math.pow(d4, 2.0D) > Math.pow(d1, 2.0D) + d5) {
/* 102 */       d7 = Math.log(Math.pow(d4, 2.0D) - Math.pow(d1, 2.0D) - d5);
/*     */     } else {
/* 104 */       double d = 1.0D;
/* 105 */       d7 = d3 - d * Math.abs(this.tau);
/*     */       
/* 107 */       while (f(d7, d4, d1, d5, d3, this.tau) < 0.0D) {
/* 108 */         d++;
/* 109 */         d7 = d3 - d * Math.abs(this.tau);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 114 */     double d8 = f(d6, d4, d1, d5, d3, this.tau);
/* 115 */     double d9 = f(d7, d4, d1, d5, d3, this.tau);
/*     */ 
/*     */     
/* 118 */     while (Math.abs(d7 - d6) > 1.0E-6D) {
/* 119 */       double d13 = d6 + (d6 - d7) * d8 / (d9 - d8);
/* 120 */       double d14 = f(d13, d4, d1, d5, d3, this.tau);
/*     */       
/* 122 */       if (d14 * d9 < 0.0D) {
/* 123 */         d6 = d7;
/* 124 */         d8 = d9;
/*     */       } else {
/* 126 */         d8 /= 2.0D;
/*     */       } 
/*     */       
/* 129 */       d7 = d13;
/* 130 */       d9 = d14;
/*     */     } 
/*     */     
/* 133 */     double d10 = Math.exp(d6 / 2.0D);
/*     */     
/* 135 */     paramRating.setWorkingVolatility(d10);
/*     */ 
/*     */     
/* 138 */     double d11 = calculateNewRD(d1, d10);
/*     */ 
/*     */     
/* 141 */     double d12 = 1.0D / Math.sqrt(1.0D / Math.pow(d11, 2.0D) + 1.0D / d5);
/*     */ 
/*     */ 
/*     */     
/* 145 */     paramRating.setWorkingRating(paramRating
/* 146 */         .getGlicko2Rating() + 
/* 147 */         Math.pow(d12, 2.0D) * outcomeBasedRating(paramRating, paramList));
/* 148 */     paramRating.setWorkingRatingDeviation(d12);
/* 149 */     paramRating.incrementNumberOfResults(paramList.size());
/*     */   }
/*     */   
/*     */   private double f(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
/* 153 */     return Math.exp(paramDouble1) * (Math.pow(paramDouble2, 2.0D) - Math.pow(paramDouble3, 2.0D) - paramDouble4 - Math.exp(paramDouble1)) / 2.0D * 
/* 154 */       Math.pow(Math.pow(paramDouble3, 2.0D) + paramDouble4 + Math.exp(paramDouble1), 2.0D) - (paramDouble1 - paramDouble5) / 
/* 155 */       Math.pow(paramDouble6, 2.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double g(double paramDouble) {
/* 166 */     return 1.0D / Math.sqrt(1.0D + 3.0D * Math.pow(paramDouble, 2.0D) / Math.pow(Math.PI, 2.0D));
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
/*     */   private double E(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 179 */     return 1.0D / (1.0D + Math.exp(-1.0D * g(paramDouble3) * (paramDouble1 - paramDouble2)));
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
/*     */   private double v(Rating paramRating, List<Result> paramList) {
/* 191 */     double d = 0.0D;
/*     */     
/* 193 */     for (Result result : paramList) {
/* 194 */       d += 
/* 195 */         Math.pow(g(result.getOpponent(paramRating).getGlicko2RatingDeviation()), 2.0D) * 
/* 196 */         E(paramRating.getGlicko2Rating(), result
/* 197 */           .getOpponent(paramRating).getGlicko2Rating(), result
/* 198 */           .getOpponent(paramRating).getGlicko2RatingDeviation()) * (1.0D - 
/* 199 */         E(paramRating.getGlicko2Rating(), result
/* 200 */           .getOpponent(paramRating).getGlicko2Rating(), result
/* 201 */           .getOpponent(paramRating).getGlicko2RatingDeviation()));
/*     */     }
/*     */ 
/*     */     
/* 205 */     return Math.pow(d, -1.0D);
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
/*     */   private double delta(Rating paramRating, List<Result> paramList) {
/* 217 */     return v(paramRating, paramList) * outcomeBasedRating(paramRating, paramList);
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
/*     */   private double outcomeBasedRating(Rating paramRating, List<Result> paramList) {
/* 229 */     double d = 0.0D;
/*     */     
/* 231 */     for (Result result : paramList) {
/* 232 */       d += 
/* 233 */         g(result.getOpponent(paramRating).getGlicko2RatingDeviation()) * (result
/* 234 */         .getScore(paramRating) - E(paramRating
/* 235 */           .getGlicko2Rating(), result
/* 236 */           .getOpponent(paramRating).getGlicko2Rating(), result
/* 237 */           .getOpponent(paramRating).getGlicko2RatingDeviation()));
/*     */     }
/*     */ 
/*     */     
/* 241 */     return d;
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
/*     */   private double calculateNewRD(double paramDouble1, double paramDouble2) {
/* 254 */     return Math.sqrt(Math.pow(paramDouble1, 2.0D) + Math.pow(paramDouble2, 2.0D));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double convertRatingToOriginalGlickoScale(double paramDouble) {
/* 265 */     return paramDouble * 173.7178D + 1500.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double convertRatingToGlicko2Scale(double paramDouble) {
/* 276 */     return (paramDouble - 1500.0D) / 173.7178D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double convertRatingDeviationToOriginalGlickoScale(double paramDouble) {
/* 287 */     return paramDouble * 173.7178D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double convertRatingDeviationToGlicko2Scale(double paramDouble) {
/* 298 */     return paramDouble / 173.7178D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDefaultRating() {
/* 303 */     return 1500.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDefaultVolatility() {
/* 308 */     return this.defaultVolatility;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDefaultRatingDeviation() {
/* 313 */     return 350.0D;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\glicko2\RatingCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package water.of.cup.boardgames.game.glicko2;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class RatingPeriodResults
/*     */ {
/*  20 */   private List<Result> results = new ArrayList<>();
/*  21 */   private Set<Rating> participants = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RatingPeriodResults() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RatingPeriodResults(Set<Rating> paramSet) {
/*  36 */     this.participants = paramSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addResult(Rating paramRating1, Rating paramRating2) {
/*  47 */     Result result = new Result(paramRating1, paramRating2);
/*     */     
/*  49 */     this.results.add(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDraw(Rating paramRating1, Rating paramRating2) {
/*  60 */     Result result = new Result(paramRating1, paramRating2, true);
/*     */     
/*  62 */     this.results.add(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Result> getResults(Rating paramRating) {
/*  73 */     ArrayList<Result> arrayList = new ArrayList();
/*     */     
/*  75 */     for (Result result : this.results) {
/*  76 */       if (result.participated(paramRating)) {
/*  77 */         arrayList.add(result);
/*     */       }
/*     */     } 
/*     */     
/*  81 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Rating> getParticipants() {
/*  92 */     for (Result result : this.results) {
/*  93 */       this.participants.add(result.getWinner());
/*  94 */       this.participants.add(result.getLoser());
/*     */     } 
/*     */     
/*  97 */     return this.participants;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParticipants(Rating paramRating) {
/* 108 */     this.participants.add(paramRating);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 116 */     this.results.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\glicko2\RatingPeriodResults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
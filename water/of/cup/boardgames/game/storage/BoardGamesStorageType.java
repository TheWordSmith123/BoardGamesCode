/*    */ package water.of.cup.boardgames.game.storage;
/*    */ 
/*    */ import java.sql.JDBCType;
/*    */ 
/*    */ public enum BoardGamesStorageType
/*    */   implements StorageType
/*    */ {
/*  8 */   WINS("wins", "int default 0", JDBCType.INTEGER, true),
/*  9 */   LOSSES("losses", "int default 0", JDBCType.INTEGER, true),
/* 10 */   TIES("ties", "int default 0", JDBCType.INTEGER, true),
/*    */ 
/*    */   
/* 13 */   CROSS_WINS("cross_wins", "int default 0", JDBCType.INTEGER, true),
/* 14 */   BEST_TIME("best_time", "double default 0", JDBCType.DOUBLE, false),
/* 15 */   POINTS("points", "double default 0", JDBCType.DOUBLE, true),
/*    */ 
/*    */   
/* 18 */   Rating("rating", "double default 0", JDBCType.DOUBLE, true),
/* 19 */   RatingDeviation("rating_deviation", "double default 0", JDBCType.DOUBLE, true),
/* 20 */   RatingVolatility("rating_volatility", "double default 0", JDBCType.DOUBLE, true);
/*    */   
/*    */   private final String key;
/*    */   private final JDBCType dataType;
/*    */   private final String query;
/*    */   private final boolean orderByDescending;
/*    */   
/*    */   BoardGamesStorageType(String paramString1, String paramString2, JDBCType paramJDBCType, boolean paramBoolean) {
/* 28 */     this.key = paramString1;
/* 29 */     this.dataType = paramJDBCType;
/* 30 */     this.query = paramString2;
/* 31 */     this.orderByDescending = paramBoolean;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 35 */     return this.key;
/*    */   }
/*    */   
/*    */   public JDBCType getDataType() {
/* 39 */     return this.dataType;
/*    */   }
/*    */   
/*    */   public boolean isOrderByDescending() {
/* 43 */     return this.orderByDescending;
/*    */   }
/*    */   
/*    */   public String getQuery() {
/* 47 */     return this.query;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\storage\BoardGamesStorageType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
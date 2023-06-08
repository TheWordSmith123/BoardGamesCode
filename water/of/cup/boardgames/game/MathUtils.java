/*    */ package water.of.cup.boardgames.game;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ 
/*    */ public class MathUtils
/*    */ {
/*    */   public static int[] rotatePointAroundPoint90Degrees(double[] paramArrayOfdouble, int[] paramArrayOfint) {
/*  8 */     return new int[] { (int)(-paramArrayOfint[1] + paramArrayOfdouble[0] + paramArrayOfdouble[1]), (int)(paramArrayOfint[0] - paramArrayOfdouble[0] + paramArrayOfdouble[1]) };
/*    */   }
/*    */ 
/*    */   
/*    */   public static double[] rotatePointAroundPoint90Degrees(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/* 13 */     return new double[] { -paramArrayOfdouble2[1] + paramArrayOfdouble1[0] + paramArrayOfdouble1[1], paramArrayOfdouble2[0] - paramArrayOfdouble1[0] + paramArrayOfdouble1[1] };
/*    */   }
/*    */ 
/*    */   
/*    */   public static int[][] rotateMatrix(int[][] paramArrayOfint) {
/* 18 */     int i = (paramArrayOfint[0]).length;
/* 19 */     int j = paramArrayOfint.length;
/*    */     
/* 21 */     int[][] arrayOfInt = new int[i][j];
/*    */     
/* 23 */     for (byte b = 0; b < i; b++) {
/* 24 */       for (byte b1 = 0; b1 < j; b1++) {
/* 25 */         arrayOfInt[i - b - 1][b1] = paramArrayOfint[b1][b];
/*    */       }
/*    */     } 
/* 28 */     return arrayOfInt;
/*    */   }
/*    */   
/*    */   public static int[][] cloneIntMatrix(int[][] paramArrayOfint) {
/* 32 */     return (int[][])Arrays.<int[]>stream(paramArrayOfint).map(paramObject -> (int[])((int[])paramObject).clone()).toArray(paramInt -> new int[paramInt][]);
/*    */   }
/*    */   
/*    */   public static boolean isNumeric(String paramString) {
/*    */     try {
/* 37 */       Integer.parseInt(paramString);
/* 38 */       return true;
/* 39 */     } catch (NumberFormatException numberFormatException) {
/* 40 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\MathUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
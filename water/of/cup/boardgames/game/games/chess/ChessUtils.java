/*     */ package water.of.cup.boardgames.game.games.chess;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ public class ChessUtils
/*     */ {
/*  17 */   private static String[] xPositionLetters = new String[] { "a", "b", "c", "d", "e", "f", "g", "h" };
/*     */ 
/*     */   
/*     */   public static boolean locationThreatened(int[] paramArrayOfint, ChessPiece[][] paramArrayOfChessPiece) {
/*  21 */     ChessPiece[][] arrayOfChessPiece = cloneBoard(paramArrayOfChessPiece);
/*     */ 
/*     */     
/*  24 */     for (byte b = 0; b < arrayOfChessPiece.length; b++) {
/*  25 */       for (byte b1 = 0; b1 < (arrayOfChessPiece[0]).length; b1++) {
/*  26 */         ChessPiece chessPiece = arrayOfChessPiece[b][b1];
/*  27 */         if (chessPiece != null && 
/*  28 */           !chessPiece.getColor().equals(arrayOfChessPiece[paramArrayOfint[1]][paramArrayOfint[0]].getColor()) && chessPiece
/*  29 */           .getMoves(arrayOfChessPiece, new int[] { b1, b }, new boolean[8][8], new ArrayList(), true)[paramArrayOfint[1]][paramArrayOfint[0]])
/*     */         {
/*  31 */           return true; } 
/*     */       } 
/*     */     } 
/*  34 */     return false;
/*     */   }
/*     */   
/*     */   public static int[] locateKing(ChessPiece[][] paramArrayOfChessPiece, String paramString) {
/*  38 */     for (byte b = 0; b < paramArrayOfChessPiece.length; b++) {
/*  39 */       for (byte b1 = 0; b1 < (paramArrayOfChessPiece[0]).length; b1++) {
/*  40 */         if (paramArrayOfChessPiece[b][b1] != null && paramArrayOfChessPiece[b][b1].equals(ChessPiece.valueOf(paramString + "_KING")))
/*  41 */           return new int[] { b1, b }; 
/*     */       } 
/*     */     } 
/*  44 */     return new int[] { -1, -1 };
/*     */   }
/*     */   
/*     */   public static int[] locatePromotedPawn(ChessPiece[][] paramArrayOfChessPiece, String paramString) {
/*  48 */     byte b1 = 7;
/*  49 */     ChessPiece chessPiece = ChessPiece.BLACK_PAWN;
/*  50 */     if (paramString.equals("WHITE")) {
/*  51 */       b1 = 0;
/*  52 */       chessPiece = ChessPiece.WHITE_PAWN;
/*     */     } 
/*     */     
/*  55 */     for (byte b2 = 0; b2 < 8; b2++) {
/*  56 */       if (paramArrayOfChessPiece[b1][b2] != null && paramArrayOfChessPiece[b1][b2].equals(chessPiece)) {
/*  57 */         return new int[] { b2, b1 };
/*     */       }
/*     */     } 
/*  60 */     return new int[] { -1, -1 };
/*     */   }
/*     */   
/*     */   public static ChessPiece[][] cloneBoard(ChessPiece[][] paramArrayOfChessPiece) {
/*  64 */     ChessPiece[][] arrayOfChessPiece = new ChessPiece[paramArrayOfChessPiece.length][(paramArrayOfChessPiece[0]).length];
/*     */ 
/*     */     
/*  67 */     byte b = 0;
/*  68 */     for (ChessPiece[] arrayOfChessPiece1 : paramArrayOfChessPiece) {
/*  69 */       arrayOfChessPiece[b] = (ChessPiece[])arrayOfChessPiece1.clone();
/*  70 */       b++;
/*     */     } 
/*  72 */     return arrayOfChessPiece;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean[][] allMovesForColor(ChessPiece[][] paramArrayOfChessPiece, String paramString, boolean[][] paramArrayOfboolean, ArrayList<String> paramArrayList) {
/*  77 */     boolean[][] arrayOfBoolean = new boolean[paramArrayOfChessPiece.length][(paramArrayOfChessPiece[0]).length];
/*     */     
/*  79 */     for (byte b = 0; b < paramArrayOfChessPiece.length; b++) {
/*  80 */       for (byte b1 = 0; b1 < (paramArrayOfChessPiece[0]).length; b1++) {
/*  81 */         ChessPiece chessPiece = paramArrayOfChessPiece[b][b1];
/*  82 */         if (chessPiece != null && chessPiece.getColor().equals(paramString)) {
/*  83 */           arrayOfBoolean = combineMoves(arrayOfBoolean, chessPiece
/*  84 */               .getMoves(paramArrayOfChessPiece, new int[] { b1, b }, paramArrayOfboolean, paramArrayList));
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  90 */     return arrayOfBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean[][] combineMoves(boolean[][] paramArrayOfboolean1, boolean[][] paramArrayOfboolean2) {
/*  95 */     boolean[][] arrayOfBoolean = new boolean[paramArrayOfboolean1.length][(paramArrayOfboolean1[0]).length];
/*  96 */     for (byte b = 0; b < paramArrayOfboolean1.length; b++) {
/*  97 */       for (byte b1 = 0; b1 < (paramArrayOfboolean1[0]).length; b1++) {
/*  98 */         if (paramArrayOfboolean1[b][b1] || paramArrayOfboolean2[b][b1]) {
/*  99 */           arrayOfBoolean[b][b1] = true;
/*     */         }
/*     */       } 
/*     */     } 
/* 103 */     return arrayOfBoolean;
/*     */   }
/*     */   
/*     */   public static boolean colorHasMoves(ChessPiece[][] paramArrayOfChessPiece, String paramString, ArrayList<String> paramArrayList) {
/* 107 */     boolean[][] arrayOfBoolean = allMovesForColor(paramArrayOfChessPiece, paramString, new boolean[8][8], paramArrayList);
/* 108 */     for (boolean[] arrayOfBoolean1 : arrayOfBoolean) {
/* 109 */       for (boolean bool : arrayOfBoolean1) {
/* 110 */         if (bool)
/* 111 */           return true; 
/*     */       } 
/*     */     } 
/* 114 */     return false;
/*     */   }
/*     */   
/*     */   public static String getNotationPosition(int paramInt1, int paramInt2) {
/* 118 */     return xPositionLetters[paramInt1] + (8 - paramInt2);
/*     */   }
/*     */   
/*     */   public static String boardToString(ChessPiece[][] paramArrayOfChessPiece) {
/* 122 */     String str = "";
/* 123 */     for (ChessPiece[] arrayOfChessPiece : paramArrayOfChessPiece) {
/* 124 */       for (ChessPiece chessPiece : arrayOfChessPiece) {
/* 125 */         if (chessPiece == null) {
/* 126 */           str = str + " ";
/*     */         }
/*     */         else {
/*     */           
/* 130 */           String str1 = chessPiece.getNotationCharacter();
/*     */           
/* 132 */           if (chessPiece.toString().contains("PAWN")) {
/* 133 */             str1 = "P";
/*     */           }
/* 135 */           if (chessPiece.getColor().equals("BLACK")) {
/* 136 */             str1 = str1.toLowerCase();
/*     */           }
/* 138 */           str = str + str1;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 143 */     return str;
/*     */   }
/*     */   
/*     */   public static ChessPiece[][] boardFromString(String paramString) {
/* 147 */     ChessPiece[][] arrayOfChessPiece = new ChessPiece[8][8];
/*     */     
/* 149 */     for (byte b = 0; b < 63; b++) {
/* 150 */       arrayOfChessPiece[b / 8][b % 8] = ChessPiece.getPieceByNotationCharacter(paramString.charAt(b));
/*     */     }
/*     */     
/* 153 */     return arrayOfChessPiece;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\chess\ChessUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
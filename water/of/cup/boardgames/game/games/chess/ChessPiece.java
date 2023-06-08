/*     */ package water.of.cup.boardgames.game.games.chess;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.ArrayList;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.image_handling.ImageUtils;
/*     */ 
/*     */ 
/*     */ public enum ChessPiece
/*     */ {
/*  11 */   BLACK_PAWN, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK, BLACK_QUEEN, BLACK_KING, WHITE_PAWN, WHITE_BISHOP, WHITE_KNIGHT,
/*  12 */   WHITE_ROOK, WHITE_QUEEN, WHITE_KING;
/*     */ 
/*     */   
/*     */   public boolean[][] getMoves(ChessPiece[][] paramArrayOfChessPiece, int[] paramArrayOfint, boolean[][] paramArrayOfboolean, ArrayList<String> paramArrayList) {
/*  16 */     return getMoves(paramArrayOfChessPiece, paramArrayOfint, paramArrayOfboolean, paramArrayList, false); } public boolean[][] getMoves(ChessPiece[][] paramArrayOfChessPiece, int[] paramArrayOfint, boolean[][] paramArrayOfboolean, ArrayList<String> paramArrayList, boolean paramBoolean) { int[][] arrayOfInt;
/*     */     byte b;
/*     */     byte b2;
/*     */     ChessPiece chessPiece;
/*     */     int[] arrayOfInt1;
/*  21 */     boolean[][] arrayOfBoolean = new boolean[8][8];
/*     */     
/*  23 */     int i = paramArrayOfint[0];
/*  24 */     int j = paramArrayOfint[1];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  29 */     byte b1 = 0;
/*  30 */     switch (this) {
/*     */       case BLACK_PAWN:
/*     */       case WHITE_PAWN:
/*  33 */         b = 0;
/*  34 */         if (getColor().equals("BLACK")) {
/*  35 */           b = 1;
/*  36 */           b1 = 1;
/*     */         } else {
/*  38 */           b = -1;
/*  39 */           b1 = 6;
/*     */         } 
/*     */         
/*  42 */         if (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, i, j + b, true, false)) {
/*  43 */           if (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, i, j + b, paramBoolean, false)) {
/*  44 */             arrayOfBoolean[j + b][i] = true;
/*     */           }
/*     */           
/*  47 */           if (j == b1 && checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, i, j + b * 2, paramBoolean, false)) {
/*  48 */             arrayOfBoolean[j + b * 2][i] = true;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/*  53 */         if (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, i + 1, j + b, paramBoolean, true, true)) {
/*  54 */           arrayOfBoolean[j + b][i + 1] = true;
/*     */         }
/*  56 */         if (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, i - 1, j + b, paramBoolean, true, true)) {
/*  57 */           arrayOfBoolean[j + b][i - 1] = true;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  62 */         b2 = 0;
/*  63 */         chessPiece = null;
/*  64 */         if (getColor().equals("WHITE")) {
/*  65 */           chessPiece = BLACK_PAWN;
/*  66 */           b2 = 1;
/*  67 */           b1 = 3;
/*  68 */         } else if (getColor().equals("BLACK")) {
/*  69 */           chessPiece = WHITE_PAWN;
/*  70 */           b1 = 4;
/*  71 */           b2 = 6;
/*     */         } 
/*  73 */         arrayOfInt1 = new int[] { i + 1, i - 1 };
/*     */ 
/*     */         
/*  76 */         if (j == b1) {
/*  77 */           for (int k : arrayOfInt1) {
/*  78 */             if (k >= 0 && k < 8 && chessPiece.equals(paramArrayOfChessPiece[b1][k]))
/*     */             {
/*  80 */               if (paramArrayList.size() > 0) if (((String)paramArrayList.get(paramArrayList.size() - 1)).replaceAll("\\+", "")
/*  81 */                   .equals(ChessUtils.getNotationPosition(k, b2) + 
/*  82 */                     ChessUtils.getNotationPosition(k, b1))) {
/*     */                   
/*  84 */                   ChessPiece[][] arrayOfChessPiece = ChessUtils.cloneBoard(paramArrayOfChessPiece);
/*  85 */                   arrayOfChessPiece[b1][k] = null;
/*  86 */                   if (checkMovePossible(paramArrayOfint, arrayOfChessPiece, k, j + b, paramBoolean, false)) {
/*  87 */                     arrayOfBoolean[j + b][k] = true;
/*     */                   }
/*     */                 } 
/*     */             
/*     */             }
/*     */           } 
/*     */         }
/*     */         break;
/*     */       case BLACK_BISHOP:
/*     */       case WHITE_BISHOP:
/*  97 */         arrayOfInt = new int[][] { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };
/*     */ 
/*     */         
/* 100 */         for (int[] arrayOfInt2 : arrayOfInt) {
/* 101 */           int k = i;
/* 102 */           int m = j;
/*     */           
/* 104 */           while (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, k + arrayOfInt2[0], m + arrayOfInt2[1], true)) {
/*     */             
/* 106 */             k += arrayOfInt2[0];
/* 107 */             m += arrayOfInt2[1];
/* 108 */             if (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, k, m, paramBoolean))
/* 109 */               arrayOfBoolean[m][k] = true; 
/* 110 */             if (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, k, m, true, true, true)) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } 
/*     */         break;
/*     */       
/*     */       case BLACK_KING:
/*     */       case WHITE_KING:
/* 119 */         if (!paramBoolean && !((String)paramArrayList.get(paramArrayList.size() - 1)).contains("+")) {
/*     */ 
/*     */           
/* 122 */           if (getColor().equals("WHITE")) {
/* 123 */             b1 = 7;
/* 124 */           } else if (getColor().equals("BLACK")) {
/* 125 */             b1 = 0;
/*     */           } 
/*     */           
/* 128 */           if (!paramArrayOfboolean[b1][4]) {
/*     */             
/* 130 */             if (!paramArrayOfboolean[b1][0]) {
/* 131 */               boolean bool = true;
/* 132 */               for (byte b3 = 2; b3 < 4; b3++) {
/*     */                 
/* 134 */                 if (!checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, b3, b1, false, false, false)) {
/* 135 */                   bool = false;
/*     */                   
/*     */                   break;
/*     */                 } 
/*     */               } 
/* 140 */               if (paramArrayOfChessPiece[b1][1] != null)
/* 141 */                 bool = false; 
/* 142 */               if (bool) {
/* 143 */                 arrayOfBoolean[b1][0] = true;
/*     */               }
/*     */             } 
/*     */             
/* 147 */             if (!paramArrayOfboolean[b1][7]) {
/* 148 */               boolean bool = true;
/* 149 */               for (byte b3 = 5; b3 < 7; b3++) {
/*     */                 
/* 151 */                 if (!checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, b3, b1, false, false, false)) {
/* 152 */                   bool = false;
/*     */                   break;
/*     */                 } 
/*     */               } 
/* 156 */               if (bool)
/* 157 */                 arrayOfBoolean[b1][7] = true; 
/*     */             } 
/*     */           } 
/*     */         } 
/* 161 */         arrayOfInt = new int[][] { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 }, { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
/*     */ 
/*     */         
/* 164 */         for (int[] arrayOfInt2 : arrayOfInt) {
/* 165 */           if (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, i + arrayOfInt2[0], j + arrayOfInt2[1], paramBoolean)) {
/* 166 */             arrayOfBoolean[j + arrayOfInt2[1]][i + arrayOfInt2[0]] = true;
/*     */           }
/*     */         } 
/*     */         break;
/*     */       
/*     */       case BLACK_KNIGHT:
/*     */       case WHITE_KNIGHT:
/* 173 */         arrayOfInt = new int[][] { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { -1, 2 }, { 1, -2 }, { -1, -2 } };
/*     */ 
/*     */         
/* 176 */         for (int[] arrayOfInt2 : arrayOfInt) {
/* 177 */           if (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, i + arrayOfInt2[0], j + arrayOfInt2[1], paramBoolean)) {
/* 178 */             arrayOfBoolean[j + arrayOfInt2[1]][i + arrayOfInt2[0]] = true;
/*     */           }
/*     */         } 
/*     */         break;
/*     */       case BLACK_QUEEN:
/*     */       case WHITE_QUEEN:
/* 184 */         arrayOfInt = new int[][] { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 }, { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
/*     */ 
/*     */ 
/*     */         
/* 188 */         for (int[] arrayOfInt2 : arrayOfInt) {
/* 189 */           int k = i;
/* 190 */           int m = j;
/*     */           
/* 192 */           while (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, k + arrayOfInt2[0], m + arrayOfInt2[1], true)) {
/* 193 */             k += arrayOfInt2[0];
/* 194 */             m += arrayOfInt2[1];
/*     */             
/* 196 */             if (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, k, m, paramBoolean))
/* 197 */               arrayOfBoolean[m][k] = true; 
/* 198 */             if (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, k, m, true, true, true)) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } 
/*     */         break;
/*     */       case BLACK_ROOK:
/*     */       case WHITE_ROOK:
/* 206 */         arrayOfInt = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
/*     */ 
/*     */         
/* 209 */         for (int[] arrayOfInt2 : arrayOfInt) {
/* 210 */           int k = i;
/* 211 */           int m = j;
/*     */           
/* 213 */           while (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, k + arrayOfInt2[0], m + arrayOfInt2[1], true)) {
/* 214 */             k += arrayOfInt2[0];
/* 215 */             m += arrayOfInt2[1];
/* 216 */             if (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, k, m, paramBoolean))
/* 217 */               arrayOfBoolean[m][k] = true; 
/* 218 */             if (checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, k, m, true, true, true)) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } 
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 228 */     return arrayOfBoolean; }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColor() {
/* 233 */     if (toString().contains("BLACK"))
/* 234 */       return "BLACK"; 
/* 235 */     if (toString().contains("WHITE"))
/* 236 */       return "WHITE"; 
/* 237 */     return "EMPTY";
/*     */   }
/*     */   
/*     */   public boolean checkMovePossible(int[] paramArrayOfint, ChessPiece[][] paramArrayOfChessPiece, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 241 */     return checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, paramInt1, paramInt2, paramBoolean, true, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkMovePossible(int[] paramArrayOfint, ChessPiece[][] paramArrayOfChessPiece, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2) {
/* 246 */     return checkMovePossible(paramArrayOfint, paramArrayOfChessPiece, paramInt1, paramInt2, paramBoolean1, paramBoolean2, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkMovePossible(int[] paramArrayOfint, ChessPiece[][] paramArrayOfChessPiece, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/* 253 */     if (paramInt1 >= 8 || paramInt2 < 0 || paramInt2 >= 8 || paramInt1 < 0) {
/* 254 */       return false;
/*     */     }
/*     */     
/* 257 */     if (paramArrayOfChessPiece[paramInt2][paramInt1] == null) {
/*     */       
/* 259 */       if (paramBoolean3) {
/* 260 */         return false;
/*     */       }
/*     */     } else {
/* 263 */       if (getColor().equals(paramArrayOfChessPiece[paramInt2][paramInt1].getColor())) {
/* 264 */         return false;
/*     */       }
/*     */       
/* 267 */       if (!paramBoolean2) {
/* 268 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 272 */     ChessPiece[][] arrayOfChessPiece = ChessUtils.cloneBoard(paramArrayOfChessPiece);
/* 273 */     arrayOfChessPiece[paramInt2][paramInt1] = this;
/* 274 */     arrayOfChessPiece[paramArrayOfint[1]][paramArrayOfint[0]] = null;
/* 275 */     if (!paramBoolean1 && !getColor().equals("EMPTY") && 
/* 276 */       ChessUtils.locationThreatened(ChessUtils.locateKing(arrayOfChessPiece, getColor()), arrayOfChessPiece)) {
/* 277 */       return false;
/*     */     }
/*     */     
/* 280 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ChessPiece getPieceByNotationCharacter(char paramChar) {
/* 285 */     switch (paramChar) {
/*     */       case 'P':
/* 287 */         return WHITE_PAWN;
/*     */       case 'p':
/* 289 */         return BLACK_PAWN;
/*     */       case 'B':
/* 291 */         return WHITE_BISHOP;
/*     */       case 'b':
/* 293 */         return BLACK_BISHOP;
/*     */       case 'R':
/* 295 */         return WHITE_ROOK;
/*     */       case 'r':
/* 297 */         return BLACK_ROOK;
/*     */       case 'N':
/* 299 */         return WHITE_KNIGHT;
/*     */       case 'n':
/* 301 */         return BLACK_KNIGHT;
/*     */       case 'K':
/* 303 */         return WHITE_KING;
/*     */       case 'k':
/* 305 */         return BLACK_KING;
/*     */       case 'Q':
/* 307 */         return WHITE_QUEEN;
/*     */       case 'q':
/* 309 */         return BLACK_QUEEN;
/*     */     } 
/* 311 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNotationCharacter() {
/* 316 */     switch (this) {
/*     */       case BLACK_BISHOP:
/*     */       case WHITE_BISHOP:
/* 319 */         return "B";
/*     */       case BLACK_KING:
/*     */       case WHITE_KING:
/* 322 */         return "K";
/*     */       case BLACK_KNIGHT:
/*     */       case WHITE_KNIGHT:
/* 325 */         return "N";
/*     */       case BLACK_PAWN:
/*     */       case WHITE_PAWN:
/* 328 */         return "";
/*     */       case BLACK_QUEEN:
/*     */       case WHITE_QUEEN:
/* 331 */         return "Q";
/*     */       case BLACK_ROOK:
/*     */       case WHITE_ROOK:
/* 334 */         return "R";
/*     */     } 
/* 336 */     return "";
/*     */   }
/*     */   
/*     */   public BufferedImage getImage() {
/* 340 */     BufferedImage bufferedImage = BoardGames.getImageManager().getImage(toString());
/* 341 */     if (getColor() == "BLACK")
/* 342 */       bufferedImage = ImageUtils.rotateImage(bufferedImage, 2); 
/* 343 */     return bufferedImage;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\chess\ChessPiece.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
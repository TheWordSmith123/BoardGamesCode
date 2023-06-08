/*     */ package water.of.cup.boardgames.game.games.chess;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class ChessBoard {
/*     */   private ChessPiece[][] structure;
/*     */   private ArrayList<String> record;
/*     */   private boolean[][] movedPieces;
/*     */   private int fiftyMoveDrawCount;
/*     */   private ArrayList<String> boardStates;
/*     */   
/*     */   public ChessBoard() {
/*  13 */     this.fiftyMoveDrawCount = 0;
/*  14 */     this.structure = new ChessPiece[][] { { ChessPiece.BLACK_ROOK, ChessPiece.BLACK_KNIGHT, ChessPiece.BLACK_BISHOP, ChessPiece.BLACK_QUEEN, ChessPiece.BLACK_KING, ChessPiece.BLACK_BISHOP, ChessPiece.BLACK_KNIGHT, ChessPiece.BLACK_ROOK }, { ChessPiece.BLACK_PAWN, ChessPiece.BLACK_PAWN, ChessPiece.BLACK_PAWN, ChessPiece.BLACK_PAWN, ChessPiece.BLACK_PAWN, ChessPiece.BLACK_PAWN, ChessPiece.BLACK_PAWN, ChessPiece.BLACK_PAWN }, { null, null, null, null, null, null, null, null }, { null, null, null, null, null, null, null, null }, { null, null, null, null, null, null, null, null }, { null, null, null, null, null, null, null, null }, { ChessPiece.WHITE_PAWN, ChessPiece.WHITE_PAWN, ChessPiece.WHITE_PAWN, ChessPiece.WHITE_PAWN, ChessPiece.WHITE_PAWN, ChessPiece.WHITE_PAWN, ChessPiece.WHITE_PAWN, ChessPiece.WHITE_PAWN }, { ChessPiece.WHITE_ROOK, ChessPiece.WHITE_KNIGHT, ChessPiece.WHITE_BISHOP, ChessPiece.WHITE_QUEEN, ChessPiece.WHITE_KING, ChessPiece.WHITE_BISHOP, ChessPiece.WHITE_KNIGHT, ChessPiece.WHITE_ROOK } };
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
/*  28 */     this.movedPieces = new boolean[8][8];
/*  29 */     this.record = new ArrayList<>();
/*  30 */     this.record.add("start");
/*  31 */     this.boardStates = new ArrayList<>();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ChessPiece[][] getStructure() {
/*  36 */     return this.structure;
/*     */   }
/*     */   
/*     */   protected boolean move(int[] paramArrayOfint1, int[] paramArrayOfint2, String paramString) {
/*  40 */     ChessPiece chessPiece1 = this.structure[paramArrayOfint1[1]][paramArrayOfint1[0]];
/*  41 */     if (chessPiece1 == null) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     boolean[][] arrayOfBoolean = chessPiece1.getMoves(this.structure, paramArrayOfint1, this.movedPieces, this.record);
/*  46 */     if (!arrayOfBoolean[paramArrayOfint2[1]][paramArrayOfint2[0]]) {
/*  47 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  51 */     if (!chessPiece1.getColor().equals(paramString)) {
/*  52 */       return false;
/*     */     }
/*  54 */     String str1 = "WHITE".equals(paramString) ? "BLACK" : "WHITE";
/*     */ 
/*     */     
/*  57 */     String str2 = "";
/*     */ 
/*     */     
/*  60 */     if (chessPiece1.toString().contains("PAWN")) {
/*  61 */       this.fiftyMoveDrawCount = 0;
/*     */     }
/*     */ 
/*     */     
/*  65 */     ChessPiece chessPiece2 = this.structure[paramArrayOfint2[1]][paramArrayOfint2[0]];
/*  66 */     if (chessPiece2 != null && chessPiece1.toString().contains("KING") && chessPiece2.toString().contains("ROOK") && chessPiece1
/*  67 */       .getColor().equals(chessPiece2.getColor())) {
/*     */       
/*  69 */       if (paramArrayOfint2[0] == 0) {
/*     */         
/*  71 */         this.structure[paramArrayOfint1[1]][2] = chessPiece1;
/*  72 */         this.structure[paramArrayOfint1[1]][paramArrayOfint1[0]] = null;
/*  73 */         this.structure[paramArrayOfint2[1]][3] = chessPiece2;
/*  74 */         this.structure[paramArrayOfint2[1]][paramArrayOfint2[0]] = null;
/*     */         
/*  76 */         str2 = "0-0-0";
/*     */       }
/*     */       else {
/*     */         
/*  80 */         this.structure[paramArrayOfint1[1]][6] = chessPiece1;
/*  81 */         this.structure[paramArrayOfint1[1]][paramArrayOfint1[0]] = null;
/*  82 */         this.structure[paramArrayOfint2[1]][5] = chessPiece2;
/*  83 */         this.structure[paramArrayOfint2[1]][paramArrayOfint2[0]] = null;
/*  84 */         str2 = "0-0";
/*     */       } 
/*     */       
/*  87 */       this.movedPieces[paramArrayOfint2[1]][paramArrayOfint2[0]] = true;
/*  88 */       this.movedPieces[paramArrayOfint1[1]][paramArrayOfint1[0]] = true;
/*     */     } else {
/*     */       
/*  91 */       if (chessPiece1.toString().contains("PAWN") && chessPiece2 == null && paramArrayOfint2[0] != paramArrayOfint1[0])
/*     */       {
/*  93 */         this.structure[paramArrayOfint1[1]][paramArrayOfint2[0]] = null;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  98 */       str2 = chessPiece1.getNotationCharacter() + ChessUtils.getNotationPosition(paramArrayOfint1[0], paramArrayOfint1[1]);
/*     */ 
/*     */       
/* 101 */       if (chessPiece2 != null) {
/* 102 */         str2 = str2 + "x";
/* 103 */         this.fiftyMoveDrawCount = 0;
/*     */       } 
/* 105 */       str2 = str2 + ChessUtils.getNotationPosition(paramArrayOfint2[0], paramArrayOfint2[1]);
/*     */       
/* 107 */       this.structure[paramArrayOfint2[1]][paramArrayOfint2[0]] = chessPiece1;
/* 108 */       this.structure[paramArrayOfint1[1]][paramArrayOfint1[0]] = null;
/*     */       
/* 110 */       this.movedPieces[paramArrayOfint2[1]][paramArrayOfint2[0]] = true;
/* 111 */       this.movedPieces[paramArrayOfint1[1]][paramArrayOfint1[0]] = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 116 */     if (getPawnPromotion().equals("NONE"))
/*     */     {
/*     */       
/* 119 */       if (ChessUtils.locationThreatened(ChessUtils.locateKing(this.structure, str1), this.structure)) {
/* 120 */         str2 = str2 + "+";
/*     */         
/* 122 */         if (!ChessUtils.colorHasMoves(this.structure, str1, this.record)) {
/* 123 */           str2 = str2 + "+";
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 130 */     this.record.add(str2);
/*     */ 
/*     */ 
/*     */     
/* 134 */     String str3 = ChessUtils.boardToString(this.structure);
/* 135 */     this.boardStates.add(str3);
/*     */     
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String checkGameOver() {
/* 142 */     if (this.fiftyMoveDrawCount >= 50) {
/* 143 */       return "DRAW";
/*     */     }
/*     */ 
/*     */     
/* 147 */     if (getBoardRepeats((String)this.boardStates.get(this.boardStates.size() - 1)) >= 3)
/*     */     {
/* 149 */       return "DRAW";
/*     */     }
/*     */ 
/*     */     
/* 153 */     for (String str : new String[] { "WHITE", "BLACK" }) {
/*     */       
/* 155 */       if (!ChessUtils.colorHasMoves(this.structure, str, this.record)) {
/*     */         
/* 157 */         if (ChessUtils.locationThreatened(ChessUtils.locateKing(this.structure, str), this.structure)) {
/*     */           
/* 159 */           if (str.equals("WHITE")) {
/* 160 */             return "BLACK";
/*     */           }
/* 162 */           return "WHITE";
/*     */         } 
/*     */ 
/*     */         
/* 166 */         return "DRAW";
/*     */       } 
/*     */     } 
/*     */     
/* 170 */     return "";
/*     */   }
/*     */   
/*     */   private int getBoardRepeats(String paramString) {
/* 174 */     byte b = 0;
/*     */     
/* 176 */     for (String str : this.boardStates) {
/* 177 */       if (str.equals(paramString))
/* 178 */         b++; 
/*     */     } 
/* 180 */     return b;
/*     */   }
/*     */   
/*     */   protected String getPawnPromotion() {
/* 184 */     for (byte b = 0; b < 8; b++) {
/* 185 */       if (this.structure[7][b] == ChessPiece.BLACK_PAWN)
/* 186 */         return "BLACK"; 
/* 187 */       if (this.structure[0][b] == ChessPiece.WHITE_PAWN) {
/* 188 */         return "WHITE";
/*     */       }
/*     */     } 
/* 191 */     return "NONE";
/*     */   }
/*     */   
/*     */   protected void setPiece(int[] paramArrayOfint, ChessPiece paramChessPiece) {
/* 195 */     this.structure[paramArrayOfint[1]][paramArrayOfint[0]] = paramChessPiece;
/*     */   }
/*     */   
/*     */   protected boolean[][] getMoves(int[] paramArrayOfint) {
/* 199 */     return this.structure[paramArrayOfint[1]][paramArrayOfint[0]].getMoves(this.structure, paramArrayOfint, this.movedPieces, this.record);
/*     */   }
/*     */   
/*     */   public void promotePawn(String paramString, ChessPiece paramChessPiece) {
/* 203 */     for (byte b = 0; b < 8; b++) {
/* 204 */       if (this.structure[7][b] == ChessPiece.BLACK_PAWN && paramString.equals("BLACK")) {
/* 205 */         this.structure[7][b] = paramChessPiece; return;
/*     */       } 
/* 207 */       if (this.structure[0][b] == ChessPiece.WHITE_PAWN && paramString.equals("WHITE")) {
/* 208 */         this.structure[0][b] = paramChessPiece;
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\chess\ChessBoard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
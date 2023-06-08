/*     */ package water.of.cup.boardgames.game.games.sudoku;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SudokuPuzzle
/*     */ {
/*  12 */   private int[][] structure = new int[9][9];
/*  13 */   private int[][] filledStructure = new int[9][9];
/*     */   public SudokuPuzzle() {
/*  15 */     byte b1 = 1;
/*  16 */     generate();
/*  17 */     random_gen(1);
/*  18 */     random_gen(0);
/*     */     
/*  20 */     Random random = new Random();
/*  21 */     int[] arrayOfInt = { 0, 3, 6 }; byte b3;
/*  22 */     for (b3 = 0; b3 < 2; b3++) {
/*  23 */       int i = arrayOfInt[random.nextInt(arrayOfInt.length)];
/*     */       while (true) {
/*  25 */         int j = arrayOfInt[random.nextInt(arrayOfInt.length)];
/*  26 */         if (i != j) {
/*  27 */           if (b1 == 1) {
/*  28 */             row_change(i, j);
/*     */           } else {
/*  30 */             col_change(i, j);
/*  31 */           }  b1++; break;
/*     */         } 
/*     */       } 
/*  34 */     }  for (b3 = 0; b3 < 9; b3++) {
/*  35 */       for (byte b = 0; b < 9; b++) {
/*  36 */         this.filledStructure[b3][b] = this.structure[b3][b];
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  41 */     for (byte b2 = 0; b2 < 9; b2++) {
/*  42 */       for (byte b = 0; b < 9; b++) {
/*  43 */         strike_out(b2, b);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean check(int[] paramArrayOfint, int paramInt) {
/*  56 */     if (paramArrayOfint[0] > 8 || paramArrayOfint[1] > 8) {
/*  57 */       return false;
/*     */     }
/*  59 */     if (paramInt == this.structure[paramArrayOfint[1]][paramArrayOfint[0]]) {
/*  60 */       this.filledStructure[paramArrayOfint[1]][paramArrayOfint[0]] = this.structure[paramArrayOfint[1]][paramArrayOfint[0]];
/*  61 */       return true;
/*     */     } 
/*  63 */     this.filledStructure[paramArrayOfint[1]][paramArrayOfint[0]] = this.structure[paramArrayOfint[1]][paramArrayOfint[0]];
/*  64 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[][] getKnownStructure() {
/*  69 */     return this.filledStructure;
/*     */   }
/*     */   
/*     */   public boolean checkGameWon() {
/*  73 */     for (int[] arrayOfInt : this.filledStructure) {
/*  74 */       for (int i : arrayOfInt)
/*  75 */       { if (i == 0)
/*  76 */           return false;  } 
/*  77 */     }  return true;
/*     */   }
/*     */   
/*     */   public ArrayList<Integer> getFinishedNumbers() {
/*  81 */     ArrayList<Integer> arrayList = new ArrayList();
/*  82 */     int[] arrayOfInt = new int[9];
/*  83 */     for (int[] arrayOfInt1 : this.filledStructure) {
/*  84 */       for (int i : arrayOfInt1)
/*  85 */       { if (i != 0)
/*  86 */           arrayOfInt[i - 1] = arrayOfInt[i - 1] + 1;  } 
/*  87 */     }  for (byte b = 0; b < 9; b++) {
/*  88 */       if (arrayOfInt[b] == 9)
/*  89 */         arrayList.add(Integer.valueOf(b + 1)); 
/*  90 */     }  return arrayList;
/*     */   }
/*     */   
/*     */   private void generate() {
/*  94 */     int i = 1, j = 1;
/*  95 */     for (byte b = 0; b < 9; b++) {
/*  96 */       i = j;
/*  97 */       for (byte b1 = 0; b1 < 9; b1++) {
/*  98 */         if (i <= 9) {
/*  99 */           this.structure[b][b1] = i;
/* 100 */           i++;
/*     */         } else {
/* 102 */           i = 1;
/* 103 */           this.structure[b][b1] = i;
/* 104 */           i++;
/*     */         } 
/*     */       } 
/* 107 */       j = i + 3;
/* 108 */       if (i == 10)
/* 109 */         j = 4; 
/* 110 */       if (j > 9)
/* 111 */         j = j % 9 + 1; 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void random_gen(int paramInt) {
/* 116 */     byte b1 = 2, b2 = 0;
/* 117 */     Random random = new Random();
/* 118 */     byte b3 = 0; while (true) { if (b3 < 3) {
/*     */         
/* 120 */         int i = random.nextInt(b1 - b2 + 1) + b2;
/*     */         
/*     */         for (;; b3++) {
/* 123 */           int j = random.nextInt(b1 - b2 + 1) + b2;
/* 124 */           if (i != j)
/* 125 */           { b1 += 3;
/* 126 */             b2 += 3;
/*     */ 
/*     */ 
/*     */             
/* 130 */             if (paramInt == 1) {
/*     */               
/* 132 */               permutation_row(i, j); break;
/* 133 */             }  if (paramInt == 0)
/* 134 */               permutation_col(i, j);  }
/*     */           else { continue; }
/*     */         
/*     */         } 
/*     */       } else {
/*     */         break;
/*     */       }  b3++; }
/* 141 */      } private void permutation_row(int paramInt1, int paramInt2) { for (byte b = 0; b < 9; b++) {
/* 142 */       int i = this.structure[paramInt1][b];
/* 143 */       this.structure[paramInt1][b] = this.structure[paramInt2][b];
/* 144 */       this.structure[paramInt2][b] = i;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   private void permutation_col(int paramInt1, int paramInt2) {
/* 150 */     for (byte b = 0; b < 9; b++) {
/* 151 */       int i = this.structure[b][paramInt1];
/* 152 */       this.structure[b][paramInt1] = this.structure[b][paramInt2];
/* 153 */       this.structure[b][paramInt2] = i;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void row_change(int paramInt1, int paramInt2) {
/* 159 */     for (byte b = 1; b <= 3; b++) {
/* 160 */       for (byte b1 = 0; b1 < 9; b1++) {
/* 161 */         int i = this.structure[paramInt1][b1];
/* 162 */         this.structure[paramInt1][b1] = this.structure[paramInt2][b1];
/* 163 */         this.structure[paramInt2][b1] = i;
/*     */       } 
/* 165 */       paramInt1++;
/* 166 */       paramInt2++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void col_change(int paramInt1, int paramInt2) {
/* 172 */     for (byte b = 1; b <= 3; b++) {
/* 173 */       for (byte b1 = 0; b1 < 9; b1++) {
/* 174 */         int i = this.structure[b1][paramInt1];
/* 175 */         this.structure[b1][paramInt1] = this.structure[b1][paramInt2];
/* 176 */         this.structure[b1][paramInt2] = i;
/*     */       } 
/* 178 */       paramInt1++;
/* 179 */       paramInt2++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void strike_out(int paramInt1, int paramInt2) {
/* 191 */     int i = this.filledStructure[paramInt1][paramInt2];
/* 192 */     byte b2 = 9;
/* 193 */     for (byte b1 = 1; b1 <= 9; b1++) {
/* 194 */       boolean bool = true;
/* 195 */       for (byte b = 0; b < 9; ) {
/* 196 */         if (b == paramInt2 || 
/* 197 */           b1 != this.filledStructure[paramInt1][b]) {
/*     */           b++; continue;
/*     */         } 
/* 200 */         bool = false;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 205 */       if (bool == true) {
/* 206 */         for (byte b3 = 0; b3 < 9; ) {
/* 207 */           if (b3 == paramInt1 || 
/* 208 */             b1 != this.filledStructure[b3][paramInt2]) {
/*     */             b3++; continue;
/*     */           } 
/* 211 */           bool = false;
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 217 */       if (bool == true) {
/* 218 */         int i2 = paramInt1 % 3;
/* 219 */         int i3 = paramInt2 % 3;
/* 220 */         int j = paramInt1 - i2;
/* 221 */         int k = paramInt1 + 2 - i2;
/* 222 */         int m = paramInt2 - i3;
/* 223 */         int n = paramInt2 + 2 - i3;
/* 224 */         for (int i1 = j; i1 <= k; i1++) {
/* 225 */           for (int i4 = m; i4 <= n; ) {
/* 226 */             if (i1 == paramInt1 || i4 == paramInt2 || 
/* 227 */               b1 != this.filledStructure[i1][i4]) {
/*     */               i4++; continue;
/*     */             } 
/* 230 */             bool = false;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 237 */       if (!bool)
/* 238 */         b2--; 
/*     */     } 
/* 240 */     if (b2 == 1)
/* 241 */       this.filledStructure[paramInt1][paramInt2] = 0; 
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\games\sudoku\SudokuPuzzle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
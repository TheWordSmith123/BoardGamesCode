/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class MessageFormatter
/*     */ {
/*     */   static final char DELIM_START = '{';
/*     */   static final char DELIM_STOP = '}';
/*     */   static final String DELIM_STR = "{}";
/*     */   private static final char ESCAPE_CHAR = '\\';
/*     */   
/*     */   public static final FormattingTuple format(String paramString, Object paramObject) {
/* 124 */     return arrayFormat(paramString, new Object[] { paramObject });
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
/*     */   public static final FormattingTuple format(String paramString, Object paramObject1, Object paramObject2) {
/* 151 */     return arrayFormat(paramString, new Object[] { paramObject1, paramObject2 });
/*     */   }
/*     */ 
/*     */   
/*     */   static final Throwable getThrowableCandidate(Object[] paramArrayOfObject) {
/* 156 */     if (paramArrayOfObject == null || paramArrayOfObject.length == 0) {
/* 157 */       return null;
/*     */     }
/*     */     
/* 160 */     Object object = paramArrayOfObject[paramArrayOfObject.length - 1];
/* 161 */     if (object instanceof Throwable) {
/* 162 */       return (Throwable)object;
/*     */     }
/* 164 */     return null;
/*     */   }
/*     */   
/*     */   public static final FormattingTuple arrayFormat(String paramString, Object[] paramArrayOfObject) {
/* 168 */     Throwable throwable = getThrowableCandidate(paramArrayOfObject);
/* 169 */     Object[] arrayOfObject = paramArrayOfObject;
/* 170 */     if (throwable != null) {
/* 171 */       arrayOfObject = trimmedCopy(paramArrayOfObject);
/*     */     }
/* 173 */     return arrayFormat(paramString, arrayOfObject, throwable);
/*     */   }
/*     */   
/*     */   private static Object[] trimmedCopy(Object[] paramArrayOfObject) {
/* 177 */     if (paramArrayOfObject == null || paramArrayOfObject.length == 0) {
/* 178 */       throw new IllegalStateException("non-sensical empty or null argument array");
/*     */     }
/* 180 */     int i = paramArrayOfObject.length - 1;
/* 181 */     Object[] arrayOfObject = new Object[i];
/* 182 */     System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, i);
/* 183 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final FormattingTuple arrayFormat(String paramString, Object[] paramArrayOfObject, Throwable paramThrowable) {
/* 188 */     if (paramString == null) {
/* 189 */       return new FormattingTuple(null, paramArrayOfObject, paramThrowable);
/*     */     }
/*     */     
/* 192 */     if (paramArrayOfObject == null) {
/* 193 */       return new FormattingTuple(paramString);
/*     */     }
/*     */     
/* 196 */     int i = 0;
/*     */ 
/*     */     
/* 199 */     StringBuilder stringBuilder = new StringBuilder(paramString.length() + 50);
/*     */ 
/*     */     
/* 202 */     for (byte b = 0; b < paramArrayOfObject.length; b++) {
/*     */       
/* 204 */       int j = paramString.indexOf("{}", i);
/*     */       
/* 206 */       if (j == -1) {
/*     */         
/* 208 */         if (!i) {
/* 209 */           return new FormattingTuple(paramString, paramArrayOfObject, paramThrowable);
/*     */         }
/*     */         
/* 212 */         stringBuilder.append(paramString, i, paramString.length());
/* 213 */         return new FormattingTuple(stringBuilder.toString(), paramArrayOfObject, paramThrowable);
/*     */       } 
/*     */       
/* 216 */       if (isEscapedDelimeter(paramString, j)) {
/* 217 */         if (!isDoubleEscaped(paramString, j)) {
/* 218 */           b--;
/* 219 */           stringBuilder.append(paramString, i, j - 1);
/* 220 */           stringBuilder.append('{');
/* 221 */           i = j + 1;
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 226 */           stringBuilder.append(paramString, i, j - 1);
/* 227 */           deeplyAppendParameter(stringBuilder, paramArrayOfObject[b], (Map)new HashMap<Object, Object>());
/* 228 */           i = j + 2;
/*     */         } 
/*     */       } else {
/*     */         
/* 232 */         stringBuilder.append(paramString, i, j);
/* 233 */         deeplyAppendParameter(stringBuilder, paramArrayOfObject[b], (Map)new HashMap<Object, Object>());
/* 234 */         i = j + 2;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 239 */     stringBuilder.append(paramString, i, paramString.length());
/* 240 */     return new FormattingTuple(stringBuilder.toString(), paramArrayOfObject, paramThrowable);
/*     */   }
/*     */ 
/*     */   
/*     */   static final boolean isEscapedDelimeter(String paramString, int paramInt) {
/* 245 */     if (paramInt == 0) {
/* 246 */       return false;
/*     */     }
/* 248 */     char c = paramString.charAt(paramInt - 1);
/* 249 */     if (c == '\\') {
/* 250 */       return true;
/*     */     }
/* 252 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static final boolean isDoubleEscaped(String paramString, int paramInt) {
/* 257 */     if (paramInt >= 2 && paramString.charAt(paramInt - 2) == '\\') {
/* 258 */       return true;
/*     */     }
/* 260 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void deeplyAppendParameter(StringBuilder paramStringBuilder, Object paramObject, Map<Object[], Object> paramMap) {
/* 266 */     if (paramObject == null) {
/* 267 */       paramStringBuilder.append("null");
/*     */       return;
/*     */     } 
/* 270 */     if (!paramObject.getClass().isArray()) {
/* 271 */       safeObjectAppend(paramStringBuilder, paramObject);
/*     */ 
/*     */     
/*     */     }
/* 275 */     else if (paramObject instanceof boolean[]) {
/* 276 */       booleanArrayAppend(paramStringBuilder, (boolean[])paramObject);
/* 277 */     } else if (paramObject instanceof byte[]) {
/* 278 */       byteArrayAppend(paramStringBuilder, (byte[])paramObject);
/* 279 */     } else if (paramObject instanceof char[]) {
/* 280 */       charArrayAppend(paramStringBuilder, (char[])paramObject);
/* 281 */     } else if (paramObject instanceof short[]) {
/* 282 */       shortArrayAppend(paramStringBuilder, (short[])paramObject);
/* 283 */     } else if (paramObject instanceof int[]) {
/* 284 */       intArrayAppend(paramStringBuilder, (int[])paramObject);
/* 285 */     } else if (paramObject instanceof long[]) {
/* 286 */       longArrayAppend(paramStringBuilder, (long[])paramObject);
/* 287 */     } else if (paramObject instanceof float[]) {
/* 288 */       floatArrayAppend(paramStringBuilder, (float[])paramObject);
/* 289 */     } else if (paramObject instanceof double[]) {
/* 290 */       doubleArrayAppend(paramStringBuilder, (double[])paramObject);
/*     */     } else {
/* 292 */       objectArrayAppend(paramStringBuilder, (Object[])paramObject, paramMap);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void safeObjectAppend(StringBuilder paramStringBuilder, Object paramObject) {
/*     */     try {
/* 299 */       String str = paramObject.toString();
/* 300 */       paramStringBuilder.append(str);
/* 301 */     } catch (Throwable throwable) {
/* 302 */       Util.report("SLF4J: Failed toString() invocation on an object of type [" + paramObject.getClass().getName() + "]", throwable);
/* 303 */       paramStringBuilder.append("[FAILED toString()]");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void objectArrayAppend(StringBuilder paramStringBuilder, Object[] paramArrayOfObject, Map<Object[], Object> paramMap) {
/* 309 */     paramStringBuilder.append('[');
/* 310 */     if (!paramMap.containsKey(paramArrayOfObject)) {
/* 311 */       paramMap.put(paramArrayOfObject, null);
/* 312 */       int i = paramArrayOfObject.length;
/* 313 */       for (byte b = 0; b < i; b++) {
/* 314 */         deeplyAppendParameter(paramStringBuilder, paramArrayOfObject[b], paramMap);
/* 315 */         if (b != i - 1) {
/* 316 */           paramStringBuilder.append(", ");
/*     */         }
/*     */       } 
/* 319 */       paramMap.remove(paramArrayOfObject);
/*     */     } else {
/* 321 */       paramStringBuilder.append("...");
/*     */     } 
/* 323 */     paramStringBuilder.append(']');
/*     */   }
/*     */   
/*     */   private static void booleanArrayAppend(StringBuilder paramStringBuilder, boolean[] paramArrayOfboolean) {
/* 327 */     paramStringBuilder.append('[');
/* 328 */     int i = paramArrayOfboolean.length;
/* 329 */     for (byte b = 0; b < i; b++) {
/* 330 */       paramStringBuilder.append(paramArrayOfboolean[b]);
/* 331 */       if (b != i - 1)
/* 332 */         paramStringBuilder.append(", "); 
/*     */     } 
/* 334 */     paramStringBuilder.append(']');
/*     */   }
/*     */   
/*     */   private static void byteArrayAppend(StringBuilder paramStringBuilder, byte[] paramArrayOfbyte) {
/* 338 */     paramStringBuilder.append('[');
/* 339 */     int i = paramArrayOfbyte.length;
/* 340 */     for (byte b = 0; b < i; b++) {
/* 341 */       paramStringBuilder.append(paramArrayOfbyte[b]);
/* 342 */       if (b != i - 1)
/* 343 */         paramStringBuilder.append(", "); 
/*     */     } 
/* 345 */     paramStringBuilder.append(']');
/*     */   }
/*     */   
/*     */   private static void charArrayAppend(StringBuilder paramStringBuilder, char[] paramArrayOfchar) {
/* 349 */     paramStringBuilder.append('[');
/* 350 */     int i = paramArrayOfchar.length;
/* 351 */     for (byte b = 0; b < i; b++) {
/* 352 */       paramStringBuilder.append(paramArrayOfchar[b]);
/* 353 */       if (b != i - 1)
/* 354 */         paramStringBuilder.append(", "); 
/*     */     } 
/* 356 */     paramStringBuilder.append(']');
/*     */   }
/*     */   
/*     */   private static void shortArrayAppend(StringBuilder paramStringBuilder, short[] paramArrayOfshort) {
/* 360 */     paramStringBuilder.append('[');
/* 361 */     int i = paramArrayOfshort.length;
/* 362 */     for (byte b = 0; b < i; b++) {
/* 363 */       paramStringBuilder.append(paramArrayOfshort[b]);
/* 364 */       if (b != i - 1)
/* 365 */         paramStringBuilder.append(", "); 
/*     */     } 
/* 367 */     paramStringBuilder.append(']');
/*     */   }
/*     */   
/*     */   private static void intArrayAppend(StringBuilder paramStringBuilder, int[] paramArrayOfint) {
/* 371 */     paramStringBuilder.append('[');
/* 372 */     int i = paramArrayOfint.length;
/* 373 */     for (byte b = 0; b < i; b++) {
/* 374 */       paramStringBuilder.append(paramArrayOfint[b]);
/* 375 */       if (b != i - 1)
/* 376 */         paramStringBuilder.append(", "); 
/*     */     } 
/* 378 */     paramStringBuilder.append(']');
/*     */   }
/*     */   
/*     */   private static void longArrayAppend(StringBuilder paramStringBuilder, long[] paramArrayOflong) {
/* 382 */     paramStringBuilder.append('[');
/* 383 */     int i = paramArrayOflong.length;
/* 384 */     for (byte b = 0; b < i; b++) {
/* 385 */       paramStringBuilder.append(paramArrayOflong[b]);
/* 386 */       if (b != i - 1)
/* 387 */         paramStringBuilder.append(", "); 
/*     */     } 
/* 389 */     paramStringBuilder.append(']');
/*     */   }
/*     */   
/*     */   private static void floatArrayAppend(StringBuilder paramStringBuilder, float[] paramArrayOffloat) {
/* 393 */     paramStringBuilder.append('[');
/* 394 */     int i = paramArrayOffloat.length;
/* 395 */     for (byte b = 0; b < i; b++) {
/* 396 */       paramStringBuilder.append(paramArrayOffloat[b]);
/* 397 */       if (b != i - 1)
/* 398 */         paramStringBuilder.append(", "); 
/*     */     } 
/* 400 */     paramStringBuilder.append(']');
/*     */   }
/*     */   
/*     */   private static void doubleArrayAppend(StringBuilder paramStringBuilder, double[] paramArrayOfdouble) {
/* 404 */     paramStringBuilder.append('[');
/* 405 */     int i = paramArrayOfdouble.length;
/* 406 */     for (byte b = 0; b < i; b++) {
/* 407 */       paramStringBuilder.append(paramArrayOfdouble[b]);
/* 408 */       if (b != i - 1)
/* 409 */         paramStringBuilder.append(", "); 
/*     */     } 
/* 411 */     paramStringBuilder.append(']');
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\org\slf4j\helpers\MessageFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
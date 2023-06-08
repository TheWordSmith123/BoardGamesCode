/*    */ package water.of.cup.boardgames.extension;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class MethodSignature {
/*    */   private final String name;
/*    */   private final Class<?>[] params;
/*    */   
/*    */   protected MethodSignature(String paramString, Class<?>[] paramArrayOfClass) {
/* 11 */     this.name = paramString;
/* 12 */     this.params = paramArrayOfClass;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 16 */     return this.name;
/*    */   }
/*    */   
/*    */   public Class<?>[] getParams() {
/* 20 */     return this.params;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 25 */     if (this == paramObject) return true; 
/* 26 */     if (paramObject == null || getClass() != paramObject.getClass()) return false; 
/* 27 */     MethodSignature methodSignature = (MethodSignature)paramObject;
/* 28 */     return (Objects.equals(this.name, methodSignature.name) && Arrays.equals((Object[])this.params, (Object[])methodSignature.params));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 33 */     int i = Objects.hash(new Object[] { this.name });
/* 34 */     i = 31 * i + Arrays.hashCode((Object[])this.params);
/* 35 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\extension\MethodSignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
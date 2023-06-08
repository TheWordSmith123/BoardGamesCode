/*     */ package com.zaxxer.hikari.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.UnaryOperator;
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
/*     */ public final class FastList<T>
/*     */   implements List<T>, RandomAccess, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4598088075242913858L;
/*     */   private final Class<?> clazz;
/*     */   private T[] elementData;
/*     */   private int size;
/*     */   
/*     */   public FastList(Class<?> paramClass) {
/*  53 */     this.elementData = (T[])Array.newInstance(paramClass, 32);
/*  54 */     this.clazz = paramClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastList(Class<?> paramClass, int paramInt) {
/*  65 */     this.elementData = (T[])Array.newInstance(paramClass, paramInt);
/*  66 */     this.clazz = paramClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(T paramT) {
/*  77 */     if (this.size < this.elementData.length) {
/*  78 */       this.elementData[this.size++] = paramT;
/*     */     }
/*     */     else {
/*     */       
/*  82 */       int i = this.elementData.length;
/*  83 */       int j = i << 1;
/*     */       
/*  85 */       Object[] arrayOfObject = (Object[])Array.newInstance(this.clazz, j);
/*  86 */       System.arraycopy(this.elementData, 0, arrayOfObject, 0, i);
/*  87 */       arrayOfObject[this.size++] = paramT;
/*  88 */       this.elementData = (T[])arrayOfObject;
/*     */     } 
/*     */     
/*  91 */     return true;
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
/*     */   public T get(int paramInt) {
/* 103 */     return this.elementData[paramInt];
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
/*     */   public T removeLast() {
/* 115 */     T t = this.elementData[--this.size];
/* 116 */     this.elementData[this.size] = null;
/* 117 */     return t;
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
/*     */   public boolean remove(Object paramObject) {
/* 130 */     for (int i = this.size - 1; i >= 0; i--) {
/* 131 */       if (paramObject == this.elementData[i]) {
/* 132 */         int j = this.size - i - 1;
/* 133 */         if (j > 0) {
/* 134 */           System.arraycopy(this.elementData, i + 1, this.elementData, i, j);
/*     */         }
/* 136 */         this.elementData[--this.size] = null;
/* 137 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 150 */     for (byte b = 0; b < this.size; b++) {
/* 151 */       this.elementData[b] = null;
/*     */     }
/*     */     
/* 154 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 165 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 172 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T set(int paramInt, T paramT) {
/* 179 */     T t = this.elementData[paramInt];
/* 180 */     this.elementData[paramInt] = paramT;
/* 181 */     return t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T remove(int paramInt) {
/* 188 */     if (this.size == 0) {
/* 189 */       return null;
/*     */     }
/*     */     
/* 192 */     T t = this.elementData[paramInt];
/*     */     
/* 194 */     int i = this.size - paramInt - 1;
/* 195 */     if (i > 0) {
/* 196 */       System.arraycopy(this.elementData, paramInt + 1, this.elementData, paramInt, i);
/*     */     }
/*     */     
/* 199 */     this.elementData[--this.size] = null;
/*     */     
/* 201 */     return t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object paramObject) {
/* 208 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/* 215 */     return new Iterator<T>()
/*     */       {
/*     */         private int index;
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 221 */           return (this.index < FastList.this.size);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public T next() {
/* 227 */           if (this.index < FastList.this.size) {
/* 228 */             return (T)FastList.this.elementData[this.index++];
/*     */           }
/*     */           
/* 231 */           throw new NoSuchElementException("No more elements in FastList");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 240 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E> E[] toArray(E[] paramArrayOfE) {
/* 247 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> paramCollection) {
/* 254 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends T> paramCollection) {
/* 261 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(int paramInt, Collection<? extends T> paramCollection) {
/* 268 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> paramCollection) {
/* 275 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> paramCollection) {
/* 282 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int paramInt, T paramT) {
/* 289 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(Object paramObject) {
/* 296 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object paramObject) {
/* 303 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListIterator<T> listIterator() {
/* 310 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListIterator<T> listIterator(int paramInt) {
/* 317 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<T> subList(int paramInt1, int paramInt2) {
/* 324 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 331 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super T> paramConsumer) {
/* 338 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Spliterator<T> spliterator() {
/* 345 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeIf(Predicate<? super T> paramPredicate) {
/* 352 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceAll(UnaryOperator<T> paramUnaryOperator) {
/* 359 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sort(Comparator<? super T> paramComparator) {
/* 366 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikar\\util\FastList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
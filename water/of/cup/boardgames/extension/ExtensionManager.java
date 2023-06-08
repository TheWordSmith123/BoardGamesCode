/*     */ package water.of.cup.boardgames.extension;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import org.bukkit.Bukkit;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtensionManager
/*     */ {
/*     */   private static final Set<MethodSignature> ABSTRACT_EXPANSION_METHODS;
/*     */   
/*     */   static {
/*  26 */     ABSTRACT_EXPANSION_METHODS = (Set<MethodSignature>)Arrays.<Method>stream(BoardGamesExtension.class.getDeclaredMethods()).filter(paramMethod -> Modifier.isAbstract(paramMethod.getModifiers())).map(paramMethod -> new MethodSignature(paramMethod.getName(), paramMethod.getParameterTypes())).collect(Collectors.toSet());
/*     */   }
/*  28 */   private final HashMap<String, BoardGamesExtension> extensions = new HashMap<>();
/*     */   
/*     */   private final File folder;
/*     */   
/*     */   public ExtensionManager() {
/*  33 */     this.folder = new File(BoardGames.getInstance().getDataFolder(), "extensions");
/*     */     
/*  35 */     if (!this.folder.exists() && !this.folder.mkdirs()) {
/*  36 */       Bukkit.getLogger().warning("[BoardGames] Error while trying to create extensions folder");
/*     */     }
/*     */   }
/*     */   
/*     */   public void loadExtensions() {
/*  41 */     boolean bool = registerExtensions();
/*     */     
/*  43 */     if (!bool) {
/*     */       return;
/*     */     }
/*  46 */     ExtensionUtil.loadExtensionImages();
/*     */     
/*  48 */     for (BoardGamesExtension boardGamesExtension : this.extensions.values()) {
/*  49 */       loadExtensionConfig(boardGamesExtension.getExtensionConfig());
/*     */       
/*  51 */       for (Class<? extends Game> clazz : boardGamesExtension.getGames()) {
/*  52 */         BoardGames.getInstance().getGameManager().registerGames(new Class[] { clazz });
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<Class<? extends BoardGamesExtension>> findExtensions() {
/*  58 */     return (List<Class<? extends BoardGamesExtension>>)Arrays.<File>stream(this.folder.listFiles((paramFile, paramString) -> paramString.endsWith(".jar")))
/*  59 */       .map(this::getExtension).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   private Class<? extends BoardGamesExtension> getExtension(File paramFile) {
/*     */     try {
/*  64 */       Class<? extends BoardGamesExtension> clazz = ExtensionUtil.findClass(paramFile, BoardGamesExtension.class);
/*     */       
/*  66 */       if (clazz == null) {
/*  67 */         Bukkit.getLogger().warning("[BoardGames] Error while loading the extension " + paramFile.getName() + " (Invalid jar)");
/*  68 */         return null;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  73 */       Set set = (Set)Arrays.<Method>stream(clazz.getDeclaredMethods()).map(paramMethod -> new MethodSignature(paramMethod.getName(), paramMethod.getParameterTypes())).collect(Collectors.toSet());
/*     */       
/*  75 */       if (!set.containsAll(ABSTRACT_EXPANSION_METHODS)) {
/*  76 */         Bukkit.getLogger().warning("[BoardGames] Error while loading the extension " + paramFile.getName() + " (Invalid methods)");
/*  77 */         return null;
/*     */       } 
/*     */       
/*  80 */       return clazz;
/*  81 */     } catch (Exception exception) {
/*  82 */       Bukkit.getLogger().warning("[BoardGames] Error while loading the extension " + paramFile.getName() + " (Exception)");
/*  83 */       exception.printStackTrace();
/*  84 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean registerExtensions() {
/*  89 */     List<Class<? extends BoardGamesExtension>> list = findExtensions();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     long l = list.stream().filter(Objects::nonNull).map(this::register).filter(Optional::isPresent).count();
/*     */     
/*  97 */     if (l == 0L) {
/*  98 */       Bukkit.getLogger().info("[BoardGames] No extensions were loaded.");
/*  99 */       return false;
/*     */     } 
/* 101 */     Bukkit.getLogger().info("[BoardGames] Loaded extensions.");
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private Optional<BoardGamesExtension> register(Class<? extends BoardGamesExtension> paramClass) {
/*     */     try {
/* 108 */       BoardGamesExtension boardGamesExtension = createExtensionInstance(paramClass);
/* 109 */       if (boardGamesExtension == null) {
/* 110 */         return Optional.empty();
/*     */       }
/* 112 */       this.extensions.put(boardGamesExtension.getExtensionName(), boardGamesExtension);
/* 113 */       return Optional.of(boardGamesExtension);
/* 114 */     } catch (LinkageError|NullPointerException linkageError) {
/* 115 */       Bukkit.getLogger().warning("[BoardGames] Error while loading extension");
/* 116 */       linkageError.printStackTrace();
/*     */ 
/*     */       
/* 119 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   private BoardGamesExtension createExtensionInstance(Class<? extends BoardGamesExtension> paramClass) {
/*     */     try {
/* 124 */       return paramClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 125 */     } catch (Exception exception) {
/* 126 */       if (exception.getCause() instanceof LinkageError) {
/* 127 */         throw (LinkageError)exception.getCause();
/*     */       }
/*     */       
/* 130 */       Bukkit.getLogger().warning("[BoardGames] Error while loading the extensions.");
/* 131 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadExtensionConfig(ArrayList<BoardGamesConfigOption> paramArrayList) {
/* 136 */     if (paramArrayList == null)
/*     */       return; 
/* 138 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 139 */     for (BoardGamesConfigOption boardGamesConfigOption : paramArrayList) {
/* 140 */       hashMap.put(boardGamesConfigOption.getPath(), boardGamesConfigOption.getDefaultValue());
/*     */     }
/* 142 */     BoardGames.getInstance().addToConfig(hashMap);
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\extension\ExtensionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package water.of.cup.boardgames.metrics;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Level;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.RegisteredServiceProvider;
/*     */ import org.bukkit.plugin.ServicePriority;
/*     */ 
/*     */ public class Metrics {
/*     */   static {
/*  38 */     if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
/*     */       
/*  40 */       String str1 = new String(new byte[] { 111, 114, 103, 46, 98, 115, 116, 97, 116, 115, 46, 98, 117, 107, 107, 105, 116 });
/*     */       
/*  42 */       String str2 = new String(new byte[] { 121, 111, 117, 114, 46, 112, 97, 99, 107, 97, 103, 101 });
/*     */       
/*  44 */       if (Metrics.class.getPackage().getName().equals(str1) || Metrics.class.getPackage().getName().equals(str2)) {
/*  45 */         throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  52 */   private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
/*     */ 
/*     */   
/*     */   public static final int B_STATS_VERSION = 1;
/*     */ 
/*     */   
/*     */   private static final String URL = "https://bStats.org/submitData/bukkit";
/*     */ 
/*     */   
/*     */   private boolean enabled;
/*     */ 
/*     */   
/*     */   private static boolean logFailedRequests;
/*     */ 
/*     */   
/*     */   private static boolean logSentData;
/*     */ 
/*     */   
/*     */   private static boolean logResponseStatusText;
/*     */ 
/*     */   
/*     */   private static String serverUUID;
/*     */ 
/*     */   
/*     */   private final Plugin plugin;
/*     */ 
/*     */   
/*     */   private final int pluginId;
/*     */ 
/*     */   
/*  82 */   private final List<CustomChart> charts = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Metrics(Plugin paramPlugin, int paramInt) {
/*  92 */     if (paramPlugin == null) {
/*  93 */       throw new IllegalArgumentException("Plugin cannot be null!");
/*     */     }
/*  95 */     this.plugin = paramPlugin;
/*  96 */     this.pluginId = paramInt;
/*     */ 
/*     */     
/*  99 */     File file1 = new File(paramPlugin.getDataFolder().getParentFile(), "bStats");
/* 100 */     File file2 = new File(file1, "config.yml");
/* 101 */     YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file2);
/*     */ 
/*     */     
/* 104 */     if (!yamlConfiguration.isSet("serverUuid")) {
/*     */ 
/*     */       
/* 107 */       yamlConfiguration.addDefault("enabled", Boolean.valueOf(true));
/*     */       
/* 109 */       yamlConfiguration.addDefault("serverUuid", UUID.randomUUID().toString());
/*     */       
/* 111 */       yamlConfiguration.addDefault("logFailedRequests", Boolean.valueOf(false));
/*     */       
/* 113 */       yamlConfiguration.addDefault("logSentData", Boolean.valueOf(false));
/*     */       
/* 115 */       yamlConfiguration.addDefault("logResponseStatusText", Boolean.valueOf(false));
/*     */ 
/*     */       
/* 118 */       yamlConfiguration.options().header("bStats collects some data for plugin authors like how many servers are using their plugins.\nTo honor their work, you should not disable it.\nThis has nearly no effect on the server performance!\nCheck out https://bStats.org/ to learn more :)")
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 123 */         .copyDefaults(true);
/*     */       try {
/* 125 */         yamlConfiguration.save(file2);
/* 126 */       } catch (IOException iOException) {}
/*     */     } 
/*     */ 
/*     */     
/* 130 */     this.enabled = yamlConfiguration.getBoolean("enabled", true);
/* 131 */     serverUUID = yamlConfiguration.getString("serverUuid");
/* 132 */     logFailedRequests = yamlConfiguration.getBoolean("logFailedRequests", false);
/* 133 */     logSentData = yamlConfiguration.getBoolean("logSentData", false);
/* 134 */     logResponseStatusText = yamlConfiguration.getBoolean("logResponseStatusText", false);
/*     */     
/* 136 */     if (this.enabled) {
/* 137 */       boolean bool = false;
/*     */       
/* 139 */       for (Class clazz : Bukkit.getServicesManager().getKnownServices()) {
/*     */         try {
/* 141 */           clazz.getField("B_STATS_VERSION");
/* 142 */           bool = true;
/*     */           break;
/* 144 */         } catch (NoSuchFieldException noSuchFieldException) {}
/*     */       } 
/*     */       
/* 147 */       Bukkit.getServicesManager().register(Metrics.class, this, paramPlugin, ServicePriority.Normal);
/* 148 */       if (!bool)
/*     */       {
/* 150 */         startSubmitting();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled() {
/* 161 */     return this.enabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCustomChart(CustomChart paramCustomChart) {
/* 170 */     if (paramCustomChart == null) {
/* 171 */       throw new IllegalArgumentException("Chart cannot be null!");
/*     */     }
/* 173 */     this.charts.add(paramCustomChart);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startSubmitting() {
/* 180 */     Runnable runnable = () -> {
/*     */         if (!this.plugin.isEnabled()) {
/*     */           this.scheduler.shutdown();
/*     */ 
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */ 
/*     */         
/*     */         Bukkit.getScheduler().runTask(this.plugin, this::submitData);
/*     */       };
/*     */ 
/*     */     
/* 194 */     long l1 = (long)(60000.0D * (3.0D + Math.random() * 3.0D));
/* 195 */     long l2 = (long)(60000.0D * Math.random() * 30.0D);
/* 196 */     this.scheduler.schedule(runnable, l1, TimeUnit.MILLISECONDS);
/* 197 */     this.scheduler.scheduleAtFixedRate(runnable, l1 + l2, 1800000L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonObject getPluginData() {
/* 207 */     JsonObject jsonObject = new JsonObject();
/*     */     
/* 209 */     String str1 = this.plugin.getDescription().getName();
/* 210 */     String str2 = this.plugin.getDescription().getVersion();
/*     */     
/* 212 */     jsonObject.addProperty("pluginName", str1);
/* 213 */     jsonObject.addProperty("id", Integer.valueOf(this.pluginId));
/* 214 */     jsonObject.addProperty("pluginVersion", str2);
/* 215 */     JsonArray jsonArray = new JsonArray();
/* 216 */     for (CustomChart customChart : this.charts) {
/*     */       
/* 218 */       JsonObject jsonObject1 = customChart.getRequestJsonObject();
/* 219 */       if (jsonObject1 == null) {
/*     */         continue;
/*     */       }
/* 222 */       jsonArray.add((JsonElement)jsonObject1);
/*     */     } 
/* 224 */     jsonObject.add("customCharts", (JsonElement)jsonArray);
/*     */     
/* 226 */     return jsonObject;
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
/*     */   private JsonObject getServerData() {
/*     */     int i;
/*     */     try {
/* 240 */       Method method = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers", new Class[0]);
/*     */ 
/*     */       
/* 243 */       i = method.getReturnType().equals(Collection.class) ? ((Collection)method.invoke(Bukkit.getServer(), new Object[0])).size() : ((Player[])method.invoke(Bukkit.getServer(), new Object[0])).length;
/* 244 */     } catch (Exception exception) {
/* 245 */       i = Bukkit.getOnlinePlayers().size();
/*     */     } 
/* 247 */     boolean bool = Bukkit.getOnlineMode() ? true : false;
/* 248 */     String str1 = Bukkit.getVersion();
/* 249 */     String str2 = Bukkit.getName();
/*     */ 
/*     */     
/* 252 */     String str3 = System.getProperty("java.version");
/* 253 */     String str4 = System.getProperty("os.name");
/* 254 */     String str5 = System.getProperty("os.arch");
/* 255 */     String str6 = System.getProperty("os.version");
/* 256 */     int j = Runtime.getRuntime().availableProcessors();
/*     */     
/* 258 */     JsonObject jsonObject = new JsonObject();
/*     */     
/* 260 */     jsonObject.addProperty("serverUUID", serverUUID);
/*     */     
/* 262 */     jsonObject.addProperty("playerAmount", Integer.valueOf(i));
/* 263 */     jsonObject.addProperty("onlineMode", Integer.valueOf(bool));
/* 264 */     jsonObject.addProperty("bukkitVersion", str1);
/* 265 */     jsonObject.addProperty("bukkitName", str2);
/*     */     
/* 267 */     jsonObject.addProperty("javaVersion", str3);
/* 268 */     jsonObject.addProperty("osName", str4);
/* 269 */     jsonObject.addProperty("osArch", str5);
/* 270 */     jsonObject.addProperty("osVersion", str6);
/* 271 */     jsonObject.addProperty("coreCount", Integer.valueOf(j));
/*     */     
/* 273 */     return jsonObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void submitData() {
/* 280 */     JsonObject jsonObject = getServerData();
/*     */     
/* 282 */     JsonArray jsonArray = new JsonArray();
/*     */     
/* 284 */     for (Class clazz : Bukkit.getServicesManager().getKnownServices()) {
/*     */       try {
/* 286 */         clazz.getField("B_STATS_VERSION");
/*     */         
/* 288 */         for (RegisteredServiceProvider registeredServiceProvider : Bukkit.getServicesManager().getRegistrations(clazz)) {
/*     */           try {
/* 290 */             Object object = registeredServiceProvider.getService().getMethod("getPluginData", new Class[0]).invoke(registeredServiceProvider.getProvider(), new Object[0]);
/* 291 */             if (object instanceof JsonObject) {
/* 292 */               jsonArray.add((JsonElement)object); continue;
/*     */             } 
/*     */             try {
/* 295 */               Class<?> clazz1 = Class.forName("org.json.simple.JSONObject");
/* 296 */               if (object.getClass().isAssignableFrom(clazz1)) {
/* 297 */                 Method method = clazz1.getDeclaredMethod("toJSONString", new Class[0]);
/* 298 */                 method.setAccessible(true);
/* 299 */                 String str = (String)method.invoke(object, new Object[0]);
/* 300 */                 JsonObject jsonObject1 = (new JsonParser()).parse(str).getAsJsonObject();
/* 301 */                 jsonArray.add((JsonElement)jsonObject1);
/*     */               } 
/* 303 */             } catch (ClassNotFoundException classNotFoundException) {
/*     */               
/* 305 */               if (logFailedRequests) {
/* 306 */                 this.plugin.getLogger().log(Level.SEVERE, "Encountered unexpected exception", classNotFoundException);
/*     */               }
/*     */             }
/*     */           
/* 310 */           } catch (NullPointerException|NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException nullPointerException) {}
/*     */         } 
/* 312 */       } catch (NoSuchFieldException noSuchFieldException) {}
/*     */     } 
/*     */     
/* 315 */     jsonObject.add("plugins", (JsonElement)jsonArray);
/*     */ 
/*     */     
/* 318 */     (new Thread(() -> {
/*     */           
/*     */           try {
/*     */             sendData(this.plugin, paramJsonObject);
/* 322 */           } catch (Exception exception) {
/*     */             
/*     */             if (logFailedRequests) {
/*     */               this.plugin.getLogger().log(Level.WARNING, "Could not submit plugin stats of " + this.plugin.getName(), exception);
/*     */             }
/*     */           } 
/* 328 */         })).start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void sendData(Plugin paramPlugin, JsonObject paramJsonObject) {
/* 339 */     if (paramJsonObject == null) {
/* 340 */       throw new IllegalArgumentException("Data cannot be null!");
/*     */     }
/* 342 */     if (Bukkit.isPrimaryThread()) {
/* 343 */       throw new IllegalAccessException("This method must not be called from the main thread!");
/*     */     }
/* 345 */     if (logSentData) {
/* 346 */       paramPlugin.getLogger().info("Sending data to bStats: " + paramJsonObject);
/*     */     }
/* 348 */     HttpsURLConnection httpsURLConnection = (HttpsURLConnection)(new URL("https://bStats.org/submitData/bukkit")).openConnection();
/*     */ 
/*     */     
/* 351 */     byte[] arrayOfByte = compress(paramJsonObject.toString());
/*     */ 
/*     */     
/* 354 */     httpsURLConnection.setRequestMethod("POST");
/* 355 */     httpsURLConnection.addRequestProperty("Accept", "application/json");
/* 356 */     httpsURLConnection.addRequestProperty("Connection", "close");
/* 357 */     httpsURLConnection.addRequestProperty("Content-Encoding", "gzip");
/* 358 */     httpsURLConnection.addRequestProperty("Content-Length", String.valueOf(arrayOfByte.length));
/* 359 */     httpsURLConnection.setRequestProperty("Content-Type", "application/json");
/* 360 */     httpsURLConnection.setRequestProperty("User-Agent", "MC-Server/1");
/*     */ 
/*     */     
/* 363 */     httpsURLConnection.setDoOutput(true);
/* 364 */     DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream()); 
/* 365 */     try { dataOutputStream.write(arrayOfByte);
/* 366 */       dataOutputStream.close(); } catch (Throwable throwable) { try { dataOutputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 368 */      StringBuilder stringBuilder = new StringBuilder();
/* 369 */     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream())); 
/*     */     try { String str;
/* 371 */       while ((str = bufferedReader.readLine()) != null) {
/* 372 */         stringBuilder.append(str);
/*     */       }
/* 374 */       bufferedReader.close(); } catch (Throwable throwable) { try { bufferedReader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 376 */      if (logResponseStatusText) {
/* 377 */       paramPlugin.getLogger().info("Sent data to bStats and received response: " + stringBuilder);
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
/*     */   private static byte[] compress(String paramString) {
/* 389 */     if (paramString == null) {
/* 390 */       return null;
/*     */     }
/* 392 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 393 */     GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream); 
/* 394 */     try { gZIPOutputStream.write(paramString.getBytes(StandardCharsets.UTF_8));
/* 395 */       gZIPOutputStream.close(); } catch (Throwable throwable) { try { gZIPOutputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 396 */      return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class CustomChart
/*     */   {
/*     */     final String chartId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CustomChart(String param1String) {
/* 413 */       if (param1String == null || param1String.isEmpty()) {
/* 414 */         throw new IllegalArgumentException("ChartId cannot be null or empty!");
/*     */       }
/* 416 */       this.chartId = param1String;
/*     */     }
/*     */     
/*     */     private JsonObject getRequestJsonObject() {
/* 420 */       JsonObject jsonObject = new JsonObject();
/* 421 */       jsonObject.addProperty("chartId", this.chartId);
/*     */       try {
/* 423 */         JsonObject jsonObject1 = getChartData();
/* 424 */         if (jsonObject1 == null)
/*     */         {
/* 426 */           return null;
/*     */         }
/* 428 */         jsonObject.add("data", (JsonElement)jsonObject1);
/* 429 */       } catch (Throwable throwable) {
/* 430 */         if (Metrics.logFailedRequests) {
/* 431 */           Bukkit.getLogger().log(Level.WARNING, "Failed to get data for custom chart with id " + this.chartId, throwable);
/*     */         }
/* 433 */         return null;
/*     */       } 
/* 435 */       return jsonObject;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract JsonObject getChartData();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SimplePie
/*     */     extends CustomChart
/*     */   {
/*     */     private final Callable<String> callable;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SimplePie(String param1String, Callable<String> param1Callable) {
/* 456 */       super(param1String);
/* 457 */       this.callable = param1Callable;
/*     */     }
/*     */ 
/*     */     
/*     */     protected JsonObject getChartData() {
/* 462 */       JsonObject jsonObject = new JsonObject();
/* 463 */       String str = this.callable.call();
/* 464 */       if (str == null || str.isEmpty())
/*     */       {
/* 466 */         return null;
/*     */       }
/* 468 */       jsonObject.addProperty("value", str);
/* 469 */       return jsonObject;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class AdvancedPie
/*     */     extends CustomChart
/*     */   {
/*     */     private final Callable<Map<String, Integer>> callable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AdvancedPie(String param1String, Callable<Map<String, Integer>> param1Callable) {
/* 487 */       super(param1String);
/* 488 */       this.callable = param1Callable;
/*     */     }
/*     */ 
/*     */     
/*     */     protected JsonObject getChartData() {
/* 493 */       JsonObject jsonObject1 = new JsonObject();
/* 494 */       JsonObject jsonObject2 = new JsonObject();
/* 495 */       Map map = this.callable.call();
/* 496 */       if (map == null || map.isEmpty())
/*     */       {
/* 498 */         return null;
/*     */       }
/* 500 */       boolean bool = true;
/* 501 */       for (Map.Entry entry : map.entrySet()) {
/* 502 */         if (((Integer)entry.getValue()).intValue() == 0) {
/*     */           continue;
/*     */         }
/* 505 */         bool = false;
/* 506 */         jsonObject2.addProperty((String)entry.getKey(), (Number)entry.getValue());
/*     */       } 
/* 508 */       if (bool)
/*     */       {
/* 510 */         return null;
/*     */       }
/* 512 */       jsonObject1.add("values", (JsonElement)jsonObject2);
/* 513 */       return jsonObject1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DrilldownPie
/*     */     extends CustomChart
/*     */   {
/*     */     private final Callable<Map<String, Map<String, Integer>>> callable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DrilldownPie(String param1String, Callable<Map<String, Map<String, Integer>>> param1Callable) {
/* 531 */       super(param1String);
/* 532 */       this.callable = param1Callable;
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonObject getChartData() {
/* 537 */       JsonObject jsonObject1 = new JsonObject();
/* 538 */       JsonObject jsonObject2 = new JsonObject();
/* 539 */       Map map = this.callable.call();
/* 540 */       if (map == null || map.isEmpty())
/*     */       {
/* 542 */         return null;
/*     */       }
/* 544 */       boolean bool = true;
/* 545 */       for (Map.Entry entry : map.entrySet()) {
/* 546 */         JsonObject jsonObject = new JsonObject();
/* 547 */         boolean bool1 = true;
/* 548 */         for (Map.Entry entry1 : ((Map)map.get(entry.getKey())).entrySet()) {
/* 549 */           jsonObject.addProperty((String)entry1.getKey(), (Number)entry1.getValue());
/* 550 */           bool1 = false;
/*     */         } 
/* 552 */         if (!bool1) {
/* 553 */           bool = false;
/* 554 */           jsonObject2.add((String)entry.getKey(), (JsonElement)jsonObject);
/*     */         } 
/*     */       } 
/* 557 */       if (bool)
/*     */       {
/* 559 */         return null;
/*     */       }
/* 561 */       jsonObject1.add("values", (JsonElement)jsonObject2);
/* 562 */       return jsonObject1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SingleLineChart
/*     */     extends CustomChart
/*     */   {
/*     */     private final Callable<Integer> callable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SingleLineChart(String param1String, Callable<Integer> param1Callable) {
/* 580 */       super(param1String);
/* 581 */       this.callable = param1Callable;
/*     */     }
/*     */ 
/*     */     
/*     */     protected JsonObject getChartData() {
/* 586 */       JsonObject jsonObject = new JsonObject();
/* 587 */       int i = ((Integer)this.callable.call()).intValue();
/* 588 */       if (i == 0)
/*     */       {
/* 590 */         return null;
/*     */       }
/* 592 */       jsonObject.addProperty("value", Integer.valueOf(i));
/* 593 */       return jsonObject;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MultiLineChart
/*     */     extends CustomChart
/*     */   {
/*     */     private final Callable<Map<String, Integer>> callable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultiLineChart(String param1String, Callable<Map<String, Integer>> param1Callable) {
/* 612 */       super(param1String);
/* 613 */       this.callable = param1Callable;
/*     */     }
/*     */ 
/*     */     
/*     */     protected JsonObject getChartData() {
/* 618 */       JsonObject jsonObject1 = new JsonObject();
/* 619 */       JsonObject jsonObject2 = new JsonObject();
/* 620 */       Map map = this.callable.call();
/* 621 */       if (map == null || map.isEmpty())
/*     */       {
/* 623 */         return null;
/*     */       }
/* 625 */       boolean bool = true;
/* 626 */       for (Map.Entry entry : map.entrySet()) {
/* 627 */         if (((Integer)entry.getValue()).intValue() == 0) {
/*     */           continue;
/*     */         }
/* 630 */         bool = false;
/* 631 */         jsonObject2.addProperty((String)entry.getKey(), (Number)entry.getValue());
/*     */       } 
/* 633 */       if (bool)
/*     */       {
/* 635 */         return null;
/*     */       }
/* 637 */       jsonObject1.add("values", (JsonElement)jsonObject2);
/* 638 */       return jsonObject1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SimpleBarChart
/*     */     extends CustomChart
/*     */   {
/*     */     private final Callable<Map<String, Integer>> callable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SimpleBarChart(String param1String, Callable<Map<String, Integer>> param1Callable) {
/* 657 */       super(param1String);
/* 658 */       this.callable = param1Callable;
/*     */     }
/*     */ 
/*     */     
/*     */     protected JsonObject getChartData() {
/* 663 */       JsonObject jsonObject1 = new JsonObject();
/* 664 */       JsonObject jsonObject2 = new JsonObject();
/* 665 */       Map map = this.callable.call();
/* 666 */       if (map == null || map.isEmpty())
/*     */       {
/* 668 */         return null;
/*     */       }
/* 670 */       for (Map.Entry entry : map.entrySet()) {
/* 671 */         JsonArray jsonArray = new JsonArray();
/* 672 */         jsonArray.add((JsonElement)new JsonPrimitive((Number)entry.getValue()));
/* 673 */         jsonObject2.add((String)entry.getKey(), (JsonElement)jsonArray);
/*     */       } 
/* 675 */       jsonObject1.add("values", (JsonElement)jsonObject2);
/* 676 */       return jsonObject1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class AdvancedBarChart
/*     */     extends CustomChart
/*     */   {
/*     */     private final Callable<Map<String, int[]>> callable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AdvancedBarChart(String param1String, Callable<Map<String, int[]>> param1Callable) {
/* 695 */       super(param1String);
/* 696 */       this.callable = param1Callable;
/*     */     }
/*     */ 
/*     */     
/*     */     protected JsonObject getChartData() {
/* 701 */       JsonObject jsonObject1 = new JsonObject();
/* 702 */       JsonObject jsonObject2 = new JsonObject();
/* 703 */       Map map = this.callable.call();
/* 704 */       if (map == null || map.isEmpty())
/*     */       {
/* 706 */         return null;
/*     */       }
/* 708 */       boolean bool = true;
/* 709 */       for (Map.Entry entry : map.entrySet()) {
/* 710 */         if (((int[])entry.getValue()).length == 0) {
/*     */           continue;
/*     */         }
/* 713 */         bool = false;
/* 714 */         JsonArray jsonArray = new JsonArray();
/* 715 */         for (int i : (int[])entry.getValue()) {
/* 716 */           jsonArray.add((JsonElement)new JsonPrimitive(Integer.valueOf(i)));
/*     */         }
/* 718 */         jsonObject2.add((String)entry.getKey(), (JsonElement)jsonArray);
/*     */       } 
/* 720 */       if (bool)
/*     */       {
/* 722 */         return null;
/*     */       }
/* 724 */       jsonObject1.add("values", (JsonElement)jsonObject2);
/* 725 */       return jsonObject1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\metrics\Metrics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
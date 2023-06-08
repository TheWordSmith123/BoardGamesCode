/*     */ package water.of.cup.boardgames.game.storage;
/*     */ import com.zaxxer.hikari.HikariConfig;
/*     */ import com.zaxxer.hikari.HikariDataSource;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.ForkJoinPool;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Player;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.config.ConfigUtil;
/*     */ import water.of.cup.boardgames.game.games.chess.OldChessPlayer;
/*     */ 
/*     */ public class StorageManager {
/*  24 */   private final BoardGames instance = BoardGames.getInstance();
/*  25 */   private static final HikariConfig config = new HikariConfig();
/*     */   private static HikariDataSource ds;
/*     */   private final ArrayList<GameStorage> gameStores;
/*  28 */   private final ExecutorService executorService = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/*     */   
/*     */   public StorageManager() {
/*  31 */     this.gameStores = new ArrayList<>();
/*     */     
/*  33 */     initialize();
/*     */   }
/*     */   
/*     */   public void addGameStorage(GameStorage paramGameStorage) {
/*  37 */     if (!hasGameStore(paramGameStorage)) {
/*  38 */       this.gameStores.add(paramGameStorage);
/*     */       
/*  40 */       paramGameStorage.initializeConfig();
/*     */ 
/*     */       
/*  43 */       initializeGameStorage(paramGameStorage);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initializeGameStorage(GameStorage paramGameStorage) {
/*  48 */     String str1 = paramGameStorage.getTableName();
/*  49 */     StorageType[] arrayOfStorageType = paramGameStorage.getGameStores();
/*     */     
/*  51 */     StringBuilder stringBuilder = new StringBuilder();
/*     */     
/*  53 */     stringBuilder.append("CREATE TABLE IF NOT EXISTS `").append(str1).append("` (");
/*  54 */     stringBuilder.append("`id` int PRIMARY KEY AUTO_INCREMENT,");
/*  55 */     stringBuilder.append("`uuid` varchar(255) UNIQUE,");
/*  56 */     stringBuilder.append("`username` varchar(255)");
/*     */     
/*  58 */     for (StorageType storageType : arrayOfStorageType) {
/*  59 */       stringBuilder.append(",").append("`").append(storageType.getKey()).append("` ")
/*  60 */         .append(storageType.getQuery());
/*     */     }
/*     */     
/*  63 */     stringBuilder.append(");");
/*     */     
/*  65 */     String str2 = stringBuilder.toString();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     try { Connection connection = getConnection(); try { Statement statement = connection.createStatement(); 
/*  71 */         try { statement.execute(str2);
/*  72 */           if (statement != null) statement.close();  } catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (connection != null) connection.close();  } catch (Throwable throwable) { if (connection != null) try { connection.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException sQLException)
/*  73 */     { sQLException.printStackTrace(); }
/*     */ 
/*     */     
/*  76 */     refreshGameStorageColumns(paramGameStorage);
/*     */     
/*  78 */     if (paramGameStorage.getTableName().equals("chess") && !ConfigUtil.DB_TRANSFERRED.toBoolean()) {
/*  79 */       transferChessBoardsData();
/*     */     }
/*     */   }
/*     */   
/*     */   private void refreshGameStorageColumns(GameStorage paramGameStorage) {
/*  84 */     StorageType[] arrayOfStorageType = paramGameStorage.getGameStores();
/*  85 */     String str = paramGameStorage.getTableName();
/*     */     
/*  87 */     if (!tableExists(str)) {
/*     */       return;
/*     */     }
/*  90 */     for (StorageType storageType : arrayOfStorageType) {
/*  91 */       if (!columnExists(str, storageType.getKey())) {
/*  92 */         addStorageType(str, storageType);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  97 */     ArrayList<String> arrayList = getColumns(str);
/*  98 */     for (String str1 : arrayList) {
/*  99 */       if (!paramGameStorage.hasStorageType(str1))
/* 100 */         removeStorageType(str, str1); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean tableExists(String paramString) {
/*     */     
/* 106 */     try { Connection connection = getConnection(); 
/* 107 */       try { DatabaseMetaData databaseMetaData = connection.getMetaData();
/* 108 */         ResultSet resultSet = databaseMetaData.getTables(null, null, paramString, null); 
/* 109 */         try { boolean bool = resultSet.next();
/* 110 */           if (resultSet != null) resultSet.close(); 
/* 111 */           if (connection != null) connection.close();  return bool; } catch (Throwable throwable) { if (resultSet != null) try { resultSet.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (connection != null) try { connection.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException sQLException)
/* 112 */     { sQLException.printStackTrace();
/* 113 */       return false; }
/*     */   
/*     */   }
/*     */   private boolean columnExists(String paramString1, String paramString2) {
/*     */     
/* 118 */     try { Connection connection = getConnection(); 
/* 119 */       try { DatabaseMetaData databaseMetaData = connection.getMetaData();
/* 120 */         ResultSet resultSet = databaseMetaData.getColumns(null, null, paramString1, paramString2); 
/* 121 */         try { boolean bool = resultSet.next();
/* 122 */           if (resultSet != null) resultSet.close(); 
/* 123 */           if (connection != null) connection.close();  return bool; } catch (Throwable throwable) { if (resultSet != null) try { resultSet.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (connection != null) try { connection.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException sQLException)
/* 124 */     { sQLException.printStackTrace();
/* 125 */       return false; }
/*     */   
/*     */   }
/*     */   
/*     */   private ArrayList<String> getColumns(String paramString) {
/* 130 */     String str = "SELECT * FROM " + paramString;
/*     */     
/* 132 */     try { Connection connection = getConnection(); try { Statement statement = connection.createStatement(); 
/* 133 */         try { ResultSet resultSet = statement.executeQuery(str); 
/* 134 */           try { int i = resultSet.getMetaData().getColumnCount();
/* 135 */             ArrayList<String> arrayList1 = new ArrayList();
/*     */ 
/*     */             
/* 138 */             for (byte b = 4; b <= i; b++) {
/* 139 */               arrayList1.add(resultSet.getMetaData().getColumnName(b));
/*     */             }
/*     */             
/* 142 */             ArrayList<String> arrayList2 = arrayList1;
/* 143 */             if (resultSet != null) resultSet.close(); 
/* 144 */             if (statement != null) statement.close();  if (connection != null) connection.close();  return arrayList2; } catch (Throwable throwable) { if (resultSet != null) try { resultSet.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (connection != null) try { connection.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SQLException sQLException)
/* 145 */     { sQLException.printStackTrace();
/* 146 */       return new ArrayList<>(); }
/*     */   
/*     */   }
/*     */   
/*     */   private void addStorageType(String paramString, StorageType paramStorageType) {
/* 151 */     String str = "ALTER TABLE " + paramString + " ADD COLUMN " + paramStorageType.getKey() + " " + paramStorageType.getQuery();
/*     */     
/* 153 */     this.executorService.submit(() -> { try { Connection connection = getConnection(); try { Statement statement = connection.createStatement(); try { statement.execute(paramString); if (statement != null)
/* 154 */                   statement.close();  } catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (connection != null) connection.close();  } catch (Throwable throwable) { if (connection != null) try { connection.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*     */              }
/* 156 */           catch (SQLException sQLException)
/*     */           { sQLException.printStackTrace(); }
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   private void removeStorageType(String paramString1, String paramString2) {
/* 163 */     String str = "ALTER TABLE " + paramString1 + " DROP COLUMN " + paramString2;
/*     */     
/* 165 */     this.executorService.submit(() -> { try { Connection connection = getConnection(); try { Statement statement = connection.createStatement(); try { statement.execute(paramString); if (statement != null)
/* 166 */                   statement.close();  } catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (connection != null) connection.close();  } catch (Throwable throwable) { if (connection != null) try { connection.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*     */              }
/* 168 */           catch (SQLException sQLException)
/*     */           { sQLException.printStackTrace(); }
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   private void initialize() {
/* 175 */     if (ds == null) {
/* 176 */       String str1 = ConfigUtil.DB_HOST.toRawString();
/* 177 */       String str2 = ConfigUtil.DB_PORT.toRawString();
/* 178 */       String str3 = ConfigUtil.DB_NAME.toRawString();
/* 179 */       String str4 = ConfigUtil.DB_USERNAME.toRawString();
/* 180 */       String str5 = ConfigUtil.DB_PASS.toRawString();
/*     */       
/* 182 */       String str6 = "jdbc:mysql://" + str1 + ":" + str2 + "/";
/*     */       
/* 184 */       createDatabaseIfNotExists(str6, str4, str5, str3);
/*     */       
/* 186 */       config.setJdbcUrl(str6 + str3);
/* 187 */       config.setUsername(str4);
/* 188 */       config.setPassword(str5);
/* 189 */       config.addDataSourceProperty("cachePrepStmts", "true");
/* 190 */       config.addDataSourceProperty("prepStmtCacheSize", "250");
/* 191 */       config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
/* 192 */       config.addDataSourceProperty("useSSL", "false");
/*     */       
/* 194 */       ds = new HikariDataSource(config);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createDatabaseIfNotExists(String paramString1, String paramString2, String paramString3, String paramString4) {
/*     */     try {
/* 202 */       HikariConfig hikariConfig = new HikariConfig();
/* 203 */       hikariConfig.setJdbcUrl(paramString1);
/* 204 */       hikariConfig.setUsername(paramString2);
/* 205 */       hikariConfig.setPassword(paramString3);
/* 206 */       hikariConfig.addDataSourceProperty("useSSL", "false");
/*     */       
/* 208 */       HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
/*     */       
/* 210 */       Connection connection = hikariDataSource.getConnection(); try { Statement statement = connection.createStatement(); 
/* 211 */         try { statement.execute("CREATE DATABASE IF NOT EXISTS " + paramString4);
/* 212 */           if (statement != null) statement.close();  } catch (Throwable throwable) { if (statement != null) try { statement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (connection != null) connection.close();  } catch (Throwable throwable) { if (connection != null)
/*     */           try { connection.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 214 */        hikariDataSource.close();
/* 215 */     } catch (SQLException sQLException) {
/* 216 */       sQLException.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateColumn(Player paramPlayer, String paramString, StorageType paramStorageType, Object paramObject, boolean paramBoolean) {
/* 222 */     this.executorService.submit(() -> {
/*     */           try {
/*     */             String str1 = paramPlayer.getName(); String str2 = paramPlayer.getUniqueId().toString(); String str3 = paramStorageType.getKey(); String str4 = "INSERT INTO `" + paramString + "` (uuid,username," + str3 + ") VALUES (?,?,?) ON DUPLICATE KEY UPDATE username = ?,"; str4 = str4 + (paramBoolean ? (str3 + " = ?;") : (str3 + " = " + str3 + " + ?;")); Connection connection = getConnection(); 
/*     */             try { PreparedStatement preparedStatement = connection.prepareStatement(str4); 
/*     */               try { preparedStatement.setString(1, str2);
/*     */                 preparedStatement.setString(2, str1);
/*     */                 preparedStatement.setObject(3, paramObject, paramStorageType.getDataType());
/*     */                 preparedStatement.setString(4, str1);
/*     */                 preparedStatement.setObject(5, paramObject, paramStorageType.getDataType());
/*     */                 preparedStatement.execute();
/*     */                 if (preparedStatement != null)
/*     */                   preparedStatement.close();  }
/* 234 */               catch (Throwable throwable) { if (preparedStatement != null) try { preparedStatement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (connection != null) connection.close();  } catch (Throwable throwable) { if (connection != null) try { connection.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               throw throwable; }
/*     */           
/* 244 */           } catch (SQLException sQLException) {
/*     */             sQLException.printStackTrace();
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkedHashMap<StorageType, Object> fetchPlayerStats(OfflinePlayer paramOfflinePlayer, GameStorage paramGameStorage, boolean paramBoolean) {
/* 252 */     CompletableFuture<LinkedHashMap<StorageType, Object>> completableFuture = new CompletableFuture();
/*     */     
/* 254 */     String str1 = paramGameStorage.getTableName();
/* 255 */     String str2 = paramOfflinePlayer.getUniqueId().toString();
/*     */     
/* 257 */     String str3 = "SELECT * FROM `" + str1 + "` WHERE uuid=?";
/*     */     
/* 259 */     this.executorService.submit(() -> { try { Connection connection = getConnection(); try { PreparedStatement preparedStatement = connection.prepareStatement(paramString1); try { preparedStatement.setString(1, paramString2); ResultSet resultSet = preparedStatement.executeQuery(); try { if (resultSet.next()) { LinkedHashMap<StorageType, Object> linkedHashMap; if (paramBoolean) { linkedHashMap = getStringStatsFromResult(paramGameStorage, resultSet); } else { linkedHashMap = getStatsFromResult(paramGameStorage, resultSet); }  paramCompletableFuture.complete(linkedHashMap); if (resultSet != null)
/*     */                       resultSet.close();  if (preparedStatement != null)
/*     */                       preparedStatement.close();  if (connection != null)
/*     */                       connection.close();  return; }  if (resultSet != null)
/*     */                     resultSet.close();  }
/* 264 */                 catch (Throwable throwable) { if (resultSet != null) try { resultSet.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*     */                  if (preparedStatement != null)
/*     */                   preparedStatement.close();  }
/*     */               catch (Throwable throwable) { if (preparedStatement != null)
/*     */                   try { preparedStatement.close(); }
/*     */                   catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */                     throw throwable; }
/*     */                if (connection != null)
/*     */                 connection.close();  }
/*     */             catch (Throwable throwable) { if (connection != null)
/*     */                 try { connection.close(); }
/*     */                 catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */                   throw throwable; }
/*     */              paramCompletableFuture.complete(null); }
/* 278 */           catch (SQLException sQLException)
/*     */           { sQLException.printStackTrace();
/*     */             
/*     */             paramCompletableFuture.complete(null); }
/*     */         
/*     */         });
/* 284 */     return completableFuture.join();
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkedHashMap<OfflinePlayer, LinkedHashMap<StorageType, Object>> fetchTopPlayers(GameStorage paramGameStorage, StorageType paramStorageType, int paramInt) {
/* 289 */     CompletableFuture<LinkedHashMap<OfflinePlayer, LinkedHashMap<StorageType, Object>>> completableFuture = new CompletableFuture();
/* 290 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*     */     
/* 292 */     String str1 = paramGameStorage.getTableName();
/* 293 */     String str2 = paramStorageType.isOrderByDescending() ? "DESC" : "ASC";
/* 294 */     String str3 = "SELECT * FROM `" + str1 + "` ORDER BY `" + paramStorageType.getKey() + "` " + str2;
/* 295 */     if (paramStorageType.isOrderByDescending()) str3 = str3 + " LIMIT " + (paramInt * 10) + ", 10";
/*     */     
/* 297 */     String str4 = str3;
/* 298 */     this.executorService.submit(() -> { try { Connection connection = getConnection(); try { PreparedStatement preparedStatement = connection.prepareStatement(paramString); try { ResultSet resultSet = preparedStatement.executeQuery(); try { LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>(); while (resultSet.next()) { String str = resultSet.getString("uuid"); if (str == null) continue;  OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(str)); if (!offlinePlayer.hasPlayedBefore()) continue;  LinkedHashMap<StorageType, Object> linkedHashMap1 = getStringStatsFromResult(paramGameStorage, resultSet); if (!paramStorageType.isOrderByDescending() && (new StringBuilder()).append(linkedHashMap1.get(paramStorageType)).append("").toString().equals("0:0")) { linkedHashMap.put(offlinePlayer, linkedHashMap1); continue; }  paramLinkedHashMap.put(offlinePlayer, linkedHashMap1); }  for (OfflinePlayer offlinePlayer : linkedHashMap.keySet()) paramLinkedHashMap.put(offlinePlayer, (LinkedHashMap<StorageType, Object>)linkedHashMap.get(offlinePlayer));  if (!paramStorageType.isOrderByDescending()) { LinkedHashMap<Object, Object> linkedHashMap1 = new LinkedHashMap<>(); for (int i = paramInt * 10; i < paramInt * 10 + 10; i++) { ArrayList arrayList = new ArrayList(paramLinkedHashMap.keySet()); if (i >= arrayList.size()) break;  OfflinePlayer offlinePlayer = (new ArrayList<>(paramLinkedHashMap.keySet())).get(i); linkedHashMap1.put(offlinePlayer, paramLinkedHashMap.get(offlinePlayer)); }  paramCompletableFuture.complete(linkedHashMap1); if (resultSet != null) resultSet.close();  if (preparedStatement != null) preparedStatement.close();  if (connection != null)
/*     */                       connection.close();  return; }  paramCompletableFuture.complete(paramLinkedHashMap); if (resultSet != null)
/* 300 */                     resultSet.close();  } catch (Throwable throwable) { if (resultSet != null) try { resultSet.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (preparedStatement != null) preparedStatement.close();  } catch (Throwable throwable) { if (preparedStatement != null) try { preparedStatement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (connection != null) connection.close();  } catch (Throwable throwable) { if (connection != null) try { connection.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
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
/*     */               throw throwable; }
/*     */              }
/* 346 */           catch (SQLException sQLException)
/*     */           { sQLException.printStackTrace();
/*     */             
/*     */             paramCompletableFuture.complete(null); }
/*     */         
/*     */         });
/* 352 */     return completableFuture.join();
/*     */   }
/*     */   
/*     */   public int getGamePlayerTotal(GameStorage paramGameStorage) {
/* 356 */     CompletableFuture<Integer> completableFuture = new CompletableFuture();
/*     */     
/* 358 */     String str = "SELECT * FROM " + paramGameStorage.getTableName();
/*     */     
/* 360 */     this.executorService.submit(() -> { try { byte b = 0; Connection connection = getConnection(); try { PreparedStatement preparedStatement = connection.prepareStatement(paramString); try { ResultSet resultSet = preparedStatement.executeQuery(); try { while (resultSet.next())
/*     */                     b++;  if (resultSet != null)
/*     */                     resultSet.close();  }
/* 363 */                 catch (Throwable throwable) { if (resultSet != null) try { resultSet.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (preparedStatement != null) preparedStatement.close();  } catch (Throwable throwable) { if (preparedStatement != null) try { preparedStatement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (connection != null) connection.close();  } catch (Throwable throwable) { if (connection != null) try { connection.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */               
/*     */ 
/*     */               
/*     */               throw throwable; }
/*     */ 
/*     */             
/*     */             paramCompletableFuture.complete(Integer.valueOf(b)); }
/* 372 */           catch (SQLException sQLException)
/*     */           { sQLException.printStackTrace();
/*     */             
/*     */             paramCompletableFuture.complete(Integer.valueOf(0)); }
/*     */         
/*     */         });
/* 378 */     return ((Integer)completableFuture.join()).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private LinkedHashMap<StorageType, Object> getStatsFromResult(GameStorage paramGameStorage, ResultSet paramResultSet) {
/* 383 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*     */     
/* 385 */     for (StorageType storageType : paramGameStorage.getGameStores()) {
/* 386 */       if (paramGameStorage.canExecute(storageType)) {
/*     */ 
/*     */         
/* 389 */         Object object = paramResultSet.getObject(storageType.getKey());
/*     */         
/* 391 */         if (object != null) {
/* 392 */           linkedHashMap.put(storageType, object);
/*     */         } else {
/* 394 */           linkedHashMap.put(storageType, Integer.valueOf(0));
/*     */         } 
/*     */       } 
/*     */     } 
/* 398 */     return (LinkedHashMap)linkedHashMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private LinkedHashMap<StorageType, Object> getStringStatsFromResult(GameStorage paramGameStorage, ResultSet paramResultSet) {
/* 403 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*     */     
/* 405 */     for (StorageType storageType : paramGameStorage.getGameStores()) {
/* 406 */       if (paramGameStorage.canExecute(storageType)) {
/*     */ 
/*     */         
/* 409 */         Object object = paramResultSet.getObject(storageType.getKey());
/*     */         
/* 411 */         if (object != null) {
/* 412 */           if (storageType.getKey().equals("best_time")) {
/* 413 */             object = (int)(((Double)object).doubleValue() / 60.0D) + ":" + (int)(((Double)object).doubleValue() % 60.0D);
/*     */           }
/* 415 */           linkedHashMap.put(storageType, object.toString());
/*     */         } else {
/* 417 */           linkedHashMap.put(storageType, Integer.valueOf(0));
/*     */         } 
/*     */       } 
/*     */     } 
/* 421 */     return (LinkedHashMap)linkedHashMap;
/*     */   }
/*     */   
/*     */   private boolean hasGameStore(GameStorage paramGameStorage) {
/* 425 */     for (GameStorage gameStorage : this.gameStores) {
/* 426 */       if (gameStorage.getTableName().equals(paramGameStorage.getTableName())) {
/* 427 */         return true;
/*     */       }
/*     */     } 
/* 430 */     return false;
/*     */   }
/*     */   
/*     */   public Connection getConnection() {
/* 434 */     return ds.getConnection();
/*     */   }
/*     */   
/*     */   public void closeConnection() {
/* 438 */     if (ds != null) {
/* 439 */       ds.close();
/*     */     }
/*     */   }
/*     */   
/*     */   private ArrayList<OldChessPlayer> getOldChessPlayers() {
/* 444 */     CompletableFuture<ArrayList<OldChessPlayer>> completableFuture = new CompletableFuture();
/* 445 */     ArrayList arrayList = new ArrayList();
/*     */     
/* 447 */     this.executorService.submit(() -> { try { Connection connection = getConnection(); try { PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chess_players;"); try { ResultSet resultSet = preparedStatement.executeQuery(); try { while (resultSet.next()) { OldChessPlayer oldChessPlayer = new OldChessPlayer(resultSet); paramArrayList.add(oldChessPlayer); }  if (resultSet != null)
/*     */                     resultSet.close();  }
/* 449 */                 catch (Throwable throwable) { if (resultSet != null) try { resultSet.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (preparedStatement != null) preparedStatement.close();  } catch (Throwable throwable) { if (preparedStatement != null) try { preparedStatement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (connection != null) connection.close();  } catch (Throwable throwable) { if (connection != null) try { connection.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */               
/*     */ 
/*     */ 
/*     */               
/*     */               throw throwable; }
/*     */ 
/*     */ 
/*     */             
/*     */             paramCompletableFuture.complete(paramArrayList); }
/* 460 */           catch (SQLException sQLException)
/*     */           { sQLException.printStackTrace();
/*     */             
/*     */             paramCompletableFuture.complete(null); }
/*     */         
/*     */         });
/* 466 */     return completableFuture.join();
/*     */   }
/*     */   
/*     */   private void insertOldChessPlayer(OldChessPlayer paramOldChessPlayer) {
/* 470 */     String str = "INSERT INTO `chess` (uuid, wins, losses, ties, rating, rating_deviation, rating_volatility) VALUES (?,?,?,?,?,?,?);";
/*     */ 
/*     */     
/* 473 */     this.executorService.submit(() -> { try { Connection connection = getConnection(); try { PreparedStatement preparedStatement = connection.prepareStatement(paramString); try { preparedStatement.setString(1, paramOldChessPlayer.getUuid()); preparedStatement.setInt(2, paramOldChessPlayer.getWins()); preparedStatement.setInt(3, paramOldChessPlayer.getLosses()); preparedStatement.setInt(4, paramOldChessPlayer.getTies()); preparedStatement.setDouble(5, paramOldChessPlayer.getRating()); preparedStatement.setDouble(6, paramOldChessPlayer.getRatingDeviation()); preparedStatement.setDouble(7, paramOldChessPlayer.getVolatility()); preparedStatement.execute(); if (preparedStatement != null)
/*     */                   preparedStatement.close();  }
/* 475 */               catch (Throwable throwable) { if (preparedStatement != null) try { preparedStatement.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (connection != null) connection.close();  } catch (Throwable throwable) { if (connection != null) try { connection.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               throw throwable; }
/*     */              }
/* 488 */           catch (SQLException sQLException)
/*     */           { sQLException.printStackTrace(); }
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   private void transferChessBoardsData() {
/* 495 */     if (!tableExists("chess_players"))
/* 496 */       return;  if (!tableExists("chess"))
/*     */       return; 
/* 498 */     ArrayList<OldChessPlayer> arrayList = getOldChessPlayers();
/* 499 */     if (arrayList == null)
/*     */       return; 
/* 501 */     for (OldChessPlayer oldChessPlayer : arrayList) {
/* 502 */       insertOldChessPlayer(oldChessPlayer);
/*     */     }
/*     */     
/* 505 */     ConfigUtil.DB_TRANSFERRED.setValue("true");
/*     */   }
/*     */   
/*     */   public StorageType getStorageTypeByKey(String paramString) {
/* 509 */     if (this.gameStores == null) return null;
/*     */     
/* 511 */     for (GameStorage gameStorage : this.gameStores) {
/* 512 */       for (StorageType storageType : gameStorage.getStorageTypes()) {
/* 513 */         if (storageType.getKey().equals(paramString)) {
/* 514 */           return storageType;
/*     */         }
/*     */       } 
/*     */     } 
/* 518 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\storage\StorageManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
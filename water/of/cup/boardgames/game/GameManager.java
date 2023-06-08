/*     */ package water.of.cup.boardgames.game;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.ItemFrame;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.util.Vector;
/*     */ import water.of.cup.boardgames.BoardGames;
/*     */ import water.of.cup.boardgames.game.maps.GameMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameManager
/*     */ {
/*     */   private HashMap<String, Class<? extends Game>> nameToGameTypes;
/*  26 */   private BoardGames instance = BoardGames.getInstance();
/*     */   
/*  28 */   private ArrayList<Game> games = new ArrayList<>();
/*     */   private int lastGameId;
/*     */   
/*     */   public GameManager() {
/*  32 */     this.lastGameId = (int)(Math.random() * 2.147483647E9D);
/*  33 */     this.nameToGameTypes = new HashMap<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerGames(Class<? extends Game>... paramVarArgs) {
/*  38 */     for (Class<? extends Game> clazz : paramVarArgs) {
/*     */       
/*     */       try {
/*  41 */         Constructor<? extends Game> constructor = clazz.getConstructor(new Class[] { int.class });
/*  42 */         Game game = constructor.newInstance(new Object[] { Integer.valueOf(0) });
/*  43 */         if (game.isEnabled())
/*  44 */           this.nameToGameTypes.put(game.getName(), clazz); 
/*  45 */       } catch (Exception exception) {
/*     */         
/*  47 */         exception.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Game newGame(String paramString, int paramInt) {
/*     */     try {
/*  54 */       for (String str : this.nameToGameTypes.keySet()) {
/*  55 */         if (!str.equals(paramString))
/*     */           continue; 
/*  57 */         Constructor<Game> constructor = ((Class)this.nameToGameTypes.get(str)).getConstructor(new Class[] { int.class });
/*  58 */         return constructor.newInstance(new Object[] { Integer.valueOf(paramInt) });
/*     */       } 
/*  60 */     } catch (Exception exception) {
/*     */       
/*  62 */       exception.printStackTrace();
/*     */     } 
/*  64 */     return null;
/*     */   }
/*     */   
/*     */   public Game newGame(BoardItem paramBoardItem, int paramInt) {
/*  68 */     return newGame(getGameNameByAlt(paramBoardItem.getName()), paramInt);
/*     */   }
/*     */   
/*     */   public String[] getGameNames() {
/*  72 */     return (String[])this.nameToGameTypes.keySet().toArray((Object[])new String[this.nameToGameTypes.keySet().size()]);
/*     */   }
/*     */   
/*     */   public ArrayList<String> getAltGameNames() {
/*  76 */     ArrayList<String> arrayList = new ArrayList();
/*  77 */     for (String str : getGameNames()) {
/*  78 */       Game game = newGame(str, 0);
/*     */       
/*  80 */       if (game != null) {
/*  81 */         arrayList.add(game.getAltName());
/*     */       }
/*     */     } 
/*  84 */     return arrayList;
/*     */   }
/*     */   
/*     */   public String getGameNameByAlt(String paramString) {
/*  88 */     if (this.nameToGameTypes.containsKey(paramString)) {
/*  89 */       return paramString;
/*     */     }
/*     */     
/*  92 */     for (String str : getGameNames()) {
/*  93 */       Game game = newGame(str, 0);
/*     */       
/*  95 */       if (game != null && game.getAltName().equals(paramString)) {
/*  96 */         return str;
/*     */       }
/*     */     } 
/*     */     
/* 100 */     return "";
/*     */   }
/*     */   
/*     */   public boolean isValidGame(String paramString) {
/* 104 */     for (String str : getGameNames()) {
/* 105 */       if (str.equals(paramString)) {
/* 106 */         return true;
/*     */       }
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */   
/*     */   public void addGame(Game paramGame) {
/* 113 */     if (!this.games.contains(paramGame)) {
/* 114 */       this.games.add(paramGame);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean removeGame(Game paramGame) {
/* 119 */     if (!this.games.contains(paramGame)) {
/* 120 */       return false;
/*     */     }
/*     */     
/* 123 */     this.games.remove(paramGame);
/* 124 */     return true;
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
/*     */   public Game getGameByPlayer(Player paramPlayer) {
/* 174 */     for (Game game : this.games) {
/* 175 */       if (game.hasPlayer(paramPlayer))
/* 176 */         return game; 
/*     */     } 
/* 178 */     return null;
/*     */   }
/*     */   
/*     */   public Game getGameByQueuePlayer(Player paramPlayer) {
/* 182 */     for (Game game : this.games) {
/* 183 */       if (game.getPlayerQueue().contains(paramPlayer)) {
/* 184 */         return game;
/*     */       }
/*     */     } 
/* 187 */     return null;
/*     */   }
/*     */   
/*     */   public Game getGameByDecisionQueuePlayer(Player paramPlayer) {
/* 191 */     for (Game game : this.games) {
/* 192 */       if (game.getPlayerDecideQueue().contains(paramPlayer)) {
/* 193 */         return game;
/*     */       }
/*     */     } 
/* 196 */     return null;
/*     */   }
/*     */   
/*     */   public Game getGameByGameId(int paramInt) {
/* 200 */     for (Game game : this.games) {
/* 201 */       if (game.getGameId() == paramInt)
/* 202 */         return game; 
/*     */     } 
/* 204 */     return null;
/*     */   }
/*     */   
/*     */   public ArrayList<Game> getGamesInRegion(World paramWorld, Vector paramVector1, Vector paramVector2) {
/* 208 */     ArrayList<Game> arrayList = new ArrayList();
/* 209 */     for (Entity entity : paramWorld.getEntities()) {
/* 210 */       if (!(entity instanceof ItemFrame))
/*     */         continue; 
/* 212 */       ItemFrame itemFrame = (ItemFrame)entity;
/* 213 */       ItemStack itemStack = itemFrame.getItem();
/*     */       
/* 215 */       if (!GameMap.isGameMap(itemStack))
/*     */         continue; 
/* 217 */       GameMap gameMap = new GameMap(itemStack);
/*     */       
/* 219 */       Game game = getGameByGameId(gameMap.getGameId());
/* 220 */       if (game != null && entity.getLocation().toVector().isInAABB(paramVector1, paramVector2)) {
/* 221 */         arrayList.add(game);
/*     */       }
/*     */     } 
/* 224 */     return arrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int nextGameId() {
/* 260 */     return ++this.lastGameId;
/*     */   }
/*     */   
/*     */   public void rerender(Player paramPlayer) {
/* 264 */     for (Game game : this.games)
/* 265 */       game.rerender(paramPlayer); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\GameManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
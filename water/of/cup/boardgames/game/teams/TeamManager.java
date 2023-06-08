/*     */ package water.of.cup.boardgames.game.teams;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.entity.Player;
/*     */ import water.of.cup.boardgames.game.Game;
/*     */ import water.of.cup.boardgames.game.GamePlayer;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TeamManager
/*     */ {
/*     */   private final Game game;
/*     */   private final LinkedHashMap<String, GamePlayer> teams;
/*     */   private final ArrayList<String> teamNames;
/*     */   private String currentTurn;
/*  18 */   private int turnDirection = 1;
/*     */   
/*     */   public TeamManager(Game paramGame) {
/*  21 */     this.game = paramGame;
/*  22 */     this.teamNames = paramGame.getTeamNames();
/*  23 */     this.teams = new LinkedHashMap<>();
/*  24 */     this.currentTurn = null;
/*     */   }
/*     */   
/*     */   public void addTeam(GamePlayer paramGamePlayer, String paramString) {
/*  28 */     if (paramString != null) {
/*  29 */       this.teams.put(paramString, paramGamePlayer);
/*  30 */     } else if (this.teamNames == null) {
/*     */       
/*  32 */       String str = paramGamePlayer.getPlayer().getName();
/*  33 */       this.teams.put(str, paramGamePlayer);
/*     */     } else {
/*     */       
/*  36 */       String str = this.teamNames.get(0);
/*  37 */       if (this.teams.containsKey(str)) {
/*  38 */         for (String str1 : this.teamNames) {
/*  39 */           if (!this.teams.containsKey(str1)) {
/*  40 */             str = str1;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*  46 */       this.teams.put(str, paramGamePlayer);
/*     */     } 
/*     */     
/*  49 */     if (this.currentTurn == null) {
/*  50 */       this.currentTurn = (String)this.teams.keySet().toArray()[0];
/*     */     }
/*     */   }
/*     */   
/*     */   public void addTeam(GamePlayer paramGamePlayer) {
/*  55 */     addTeam(paramGamePlayer, null);
/*     */   }
/*     */   
/*     */   public void removeTeamByPlayer(Player paramPlayer) {
/*  59 */     for (String str : this.teams.keySet()) {
/*  60 */       if (((GamePlayer)this.teams.get(str)).getPlayer().equals(paramPlayer)) {
/*  61 */         if (str == this.currentTurn)
/*  62 */           nextTurn(); 
/*  63 */         this.teams.remove(str);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetTeams() {
/*  70 */     this.teams.clear();
/*  71 */     this.currentTurn = null;
/*     */   }
/*     */   
/*     */   public GamePlayer getTurnPlayer() {
/*  75 */     return this.teams.get(this.currentTurn);
/*     */   }
/*     */   
/*     */   public String getTurnTeam() {
/*  79 */     return this.currentTurn;
/*     */   }
/*     */   
/*     */   public void setTurn(String paramString) {
/*  83 */     this.currentTurn = paramString;
/*     */   }
/*     */   
/*     */   public void setTurn(GamePlayer paramGamePlayer) {
/*  87 */     for (String str : this.teams.keySet()) {
/*  88 */       if (((GamePlayer)this.teams.get(str)).equals(paramGamePlayer)) {
/*  89 */         this.currentTurn = str;
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public GamePlayer nextTurn() {
/*  96 */     ArrayList<String> arrayList = new ArrayList(this.teams.keySet());
/*  97 */     if (arrayList.size() == 0) return null;
/*     */     
/*  99 */     int i = arrayList.indexOf(this.currentTurn) + this.turnDirection;
/* 100 */     if (i >= arrayList.size()) {
/* 101 */       this.currentTurn = arrayList.get(0);
/* 102 */     } else if (i <= -1) {
/* 103 */       this.currentTurn = arrayList.get(arrayList.size() - 1);
/*     */     } else {
/*     */       
/* 106 */       this.currentTurn = arrayList.get(i);
/*     */     } 
/* 108 */     return getTurnPlayer();
/*     */   }
/*     */   
/*     */   public void switchTurnDirection() {
/* 112 */     this.turnDirection *= -1;
/*     */   }
/*     */   
/*     */   public GamePlayer getGamePlayer(Player paramPlayer) {
/* 116 */     for (String str : this.teams.keySet()) {
/* 117 */       if (((GamePlayer)this.teams.get(str)).getPlayer().equals(paramPlayer)) {
/* 118 */         return this.teams.get(str);
/*     */       }
/*     */     } 
/*     */     
/* 122 */     return null;
/*     */   }
/*     */   
/*     */   public GamePlayer getGamePlayerByTeam(String paramString) {
/* 126 */     for (String str : this.teams.keySet()) {
/* 127 */       if (str.equals(paramString)) {
/* 128 */         return this.teams.get(str);
/*     */       }
/*     */     } 
/*     */     
/* 132 */     return null;
/*     */   }
/*     */   
/*     */   public ArrayList<GamePlayer> getGamePlayers() {
/* 136 */     return new ArrayList<>(this.teams.values());
/*     */   }
/*     */   
/*     */   public String getTeamByPlayer(GamePlayer paramGamePlayer) {
/* 140 */     for (Map.Entry<String, GamePlayer> entry : this.teams.entrySet()) {
/* 141 */       if (entry.getValue() == paramGamePlayer)
/* 142 */         return (String)entry.getKey(); 
/* 143 */     }  return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\teams\TeamManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
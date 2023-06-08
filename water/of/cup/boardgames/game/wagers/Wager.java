package water.of.cup.boardgames.game.wagers;

import water.of.cup.boardgames.game.GamePlayer;

public interface Wager {
  void complete(GamePlayer paramGamePlayer);
  
  void cancel();
}


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\wagers\Wager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
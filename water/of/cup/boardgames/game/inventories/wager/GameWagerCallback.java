package water.of.cup.boardgames.game.inventories.wager;

import org.bukkit.entity.Player;
import water.of.cup.boardgames.game.wagers.RequestWager;

public interface GameWagerCallback {
  void onCreate(RequestWager paramRequestWager);
  
  void onCancel(RequestWager paramRequestWager);
  
  void onAccept(Player paramPlayer, RequestWager paramRequestWager);
  
  void onLeave(Player paramPlayer);
}


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\wager\GameWagerCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
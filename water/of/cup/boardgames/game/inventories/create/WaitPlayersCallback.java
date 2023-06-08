package water.of.cup.boardgames.game.inventories.create;

import org.bukkit.entity.Player;

public interface WaitPlayersCallback {
  void onAccept(Player paramPlayer);
  
  void onDecline(Player paramPlayer);
  
  void onStart();
  
  void onLeave();
}


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\create\WaitPlayersCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
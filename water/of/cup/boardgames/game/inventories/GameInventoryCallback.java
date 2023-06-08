package water.of.cup.boardgames.game.inventories;

import java.util.HashMap;

public interface GameInventoryCallback {
  void onGameCreated(HashMap<String, Object> paramHashMap);
  
  void onCancel(HashMap<String, Object> paramHashMap);
}


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\inventories\GameInventoryCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
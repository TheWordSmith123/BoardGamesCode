package water.of.cup.boardgames.extension;

import java.util.ArrayList;
import water.of.cup.boardgames.game.Game;

public abstract class BoardGamesExtension {
  public abstract ArrayList<Class<? extends Game>> getGames();
  
  public abstract String getExtensionName();
  
  public abstract ArrayList<BoardGamesConfigOption> getExtensionConfig();
}


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\extension\BoardGamesExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
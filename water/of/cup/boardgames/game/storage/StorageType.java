package water.of.cup.boardgames.game.storage;

import java.sql.JDBCType;

public interface StorageType {
  String getKey();
  
  JDBCType getDataType();
  
  boolean isOrderByDescending();
  
  String getQuery();
}


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\water\of\cup\boardgames\game\storage\StorageType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
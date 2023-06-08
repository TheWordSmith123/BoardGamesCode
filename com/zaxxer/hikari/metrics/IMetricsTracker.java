package com.zaxxer.hikari.metrics;

public interface IMetricsTracker extends AutoCloseable {
  default void recordConnectionCreatedMillis(long connectionCreatedMillis) {}
  
  default void recordConnectionAcquiredNanos(long elapsedAcquiredNanos) {}
  
  default void recordConnectionUsageMillis(long elapsedBorrowedMillis) {}
  
  default void recordConnectionTimeout() {}
  
  default void close() {}
}


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\metrics\IMetricsTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
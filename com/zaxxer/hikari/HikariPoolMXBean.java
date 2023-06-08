package com.zaxxer.hikari;

public interface HikariPoolMXBean {
  int getIdleConnections();
  
  int getActiveConnections();
  
  int getTotalConnections();
  
  int getThreadsAwaitingConnection();
  
  void softEvictConnections();
  
  void suspendPool();
  
  void resumePool();
}


/* Location:              C:\Users\Andrew Horvath\Downloads\BoardGames.jar!\com\zaxxer\hikari\HikariPoolMXBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
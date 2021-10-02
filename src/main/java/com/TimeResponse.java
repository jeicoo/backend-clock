package com.example.clock;


public class TimeResponse {

  private String toTimestamp;

  public TimeResponse() {
  }

  public String getToTimestamp() {
    return this.toTimestamp;
  }

  public void setToTimestamp(String toTimestamp) {
    this.toTimestamp = toTimestamp;
  }

  @Override
  public String toString() {
    return toTimestamp;
  }
}
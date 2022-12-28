package me.jbduncan.adventofcode2022.day7;

public record DirectoryInfo(String name) implements PathInfo {
  public static DirectoryInfo empty() {
    return EMPTY;
  }

  private static final DirectoryInfo EMPTY = new DirectoryInfo("");
}

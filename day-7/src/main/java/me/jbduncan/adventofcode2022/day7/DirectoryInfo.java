package me.jbduncan.adventofcode2022.day7;

public record DirectoryInfo(String name) implements PathInfo {
  public static DirectoryInfo empty() {
    return EMPTY;
  }

  public DirectoryInfo append(String path) {
    return new DirectoryInfo(resolve(path));
  }

  private static final DirectoryInfo EMPTY = new DirectoryInfo("");
}

package me.jbduncan.adventofcode2022.day7;

public record FileInfo(String name, long size) implements PathInfo {
  public FileInfo(DirectoryInfo parent, String name, long size) {
    this(parent.resolve(name), size);
  }
}

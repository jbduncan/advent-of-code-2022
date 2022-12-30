package me.jbduncan.adventofcode2022.day7;

public sealed interface PathInfo permits DirectoryInfo, FileInfo {
  String name();

  default String resolve(String path) {
    return switch (name()) {
      case "" -> path;
      case "/" -> "/" + path;
      default -> name() + "/" + path;
    };
  }
}

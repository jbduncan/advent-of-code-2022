package me.jbduncan.adventofcode2022.day7;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public final class App {

  public static void main(String[] args) throws IOException {
    execute(Arrays.asList(args), new PrintWriter(System.out, true, UTF_8));
  }

  static void execute(List<String> args, PrintWriter out) throws IOException {
    var inputFile = Path.of(args.get(0));
    boolean findDirectoryToDelete =
        args.size() > 1 && args.get(1).equals("--find-directory-to-delete");
    var inMemoryFileTree = InMemoryFileTree.parse(Files.readString(inputFile, UTF_8));
    var directorySizes = DirectorySizes.from(inMemoryFileTree);
    if (findDirectoryToDelete) {
      out.println(directorySizes.smallestDirectoryToRemove());
      return;
    }
    out.println(directorySizes.lessThan(100_000L).sum());
  }
}

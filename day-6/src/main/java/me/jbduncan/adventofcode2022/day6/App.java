package me.jbduncan.adventofcode2022.day6;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.jooq.lambda.Collectable;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

public final class App {

  public static void main(String[] args) throws IOException {
    execute(Arrays.asList(args), new PrintWriter(System.out, true, UTF_8));
  }

  static void execute(List<String> args, PrintWriter out) throws IOException {
    var inputFile = Path.of(args.get(0));
    boolean startOfMessageMarker =
        args.size() > 1 && args.get(1).equals("--start-of-message-marker");
    int windowSize = startOfMessageMarker ? 14 : 4;

    try (Seq<Character> chars = Seq.seq(Files.newBufferedReader(inputFile, UTF_8))) {
      chars
          .sliding(windowSize)
          .zipWithIndex()
          .map(windowAndIndex -> windowAndIndex.map1(Collectable::toSet))
          .filter(uniqCharsAndIndex -> uniqCharsAndIndex.v1().size() == windowSize)
          .map(Tuple2::v2)
          .map(index -> index + windowSize)
          .findFirst()
          .ifPresent(out::println);
    } catch (UncheckedIOException e) {
      throw e.getCause();
    }
  }
}

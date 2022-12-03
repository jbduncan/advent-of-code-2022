package me.jbduncan.adventofcode2022.day1;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.base.Splitter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class App {
  public static void main(String[] args) throws IOException {
    execute(Arrays.asList(args), new PrintWriter(System.out, true, UTF_8));
  }

  static void execute(List<String> args, PrintWriter out) throws IOException {
    var inputFile = Path.of(args.get(0));
    var topN = args.size() > 2 ? Integer.parseInt(args.get(2)) : 1;
    out.println(
        Splitter.onPattern("\\R\\R")
            .splitToStream(Files.readString(inputFile))
            .map(String::strip)
            .map(caloriesList -> Splitter.onPattern("\\R").splitToList(caloriesList))
            .map(caloriesList -> caloriesList.stream().mapToInt(Integer::parseInt).sum())
            .sorted(Comparator.reverseOrder())
            .limit(topN)
            .mapToInt(i -> i)
            .sum());
  }
}

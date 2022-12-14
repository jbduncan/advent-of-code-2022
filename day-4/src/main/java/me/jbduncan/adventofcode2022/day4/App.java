package me.jbduncan.adventofcode2022.day5;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.base.Splitter;
import com.google.common.collect.Range;
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
    long numFullyContainingAssignments =
        Files.readAllLines(inputFile, UTF_8).stream()
            .map(RangePair::parse)
            .filter(
                rangePair -> {
                  if (args.size() > 1 && args.get(1).equals("--overlaps")) {
                    return rangePair.first().isConnected(rangePair.second());
                  }
                  return rangePair.first().encloses(rangePair.second())
                      || rangePair.second().encloses(rangePair.first());
                })
            .count();
    out.println(numFullyContainingAssignments);
  }

  private record RangePair(Range<Integer> first, Range<Integer> second) {
    static RangePair parse(String line) {
      List<String> parts = Splitter.on(',').limit(2).splitToList(line);
      List<String> firstRangeAsList = Splitter.on('-').limit(2).splitToList(parts.get(0));
      List<String> secondRangeAsList = Splitter.on('-').limit(2).splitToList(parts.get(1));
      var firstRange =
          Range.closed(
              Integer.parseInt(firstRangeAsList.get(0)), Integer.parseInt(firstRangeAsList.get(1)));
      var secondRange =
          Range.closed(
              Integer.parseInt(secondRangeAsList.get(0)),
              Integer.parseInt(secondRangeAsList.get(1)));
      return new RangePair(firstRange, secondRange);
    }
  }
}

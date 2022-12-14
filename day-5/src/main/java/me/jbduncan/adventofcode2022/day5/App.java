package me.jbduncan.adventofcode2022.day5;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class App {

  public static void main(String[] args) throws IOException {
    execute(Arrays.asList(args), new PrintWriter(System.out, true, UTF_8));
  }

  static void execute(List<String> args, PrintWriter out) throws IOException {
    var inputFile = Path.of(args.get(0));
    boolean crateMover9001 = args.size() > 1 && args.get(1).equals("--crate-mover-9001");
    var parts =
        Splitter.onPattern("\\R\\R").limit(2).splitToList(Files.readString(inputFile, UTF_8));
    List<Deque<String>> stacks = parseStacks(parts.get(0));
    var instructions = parseInstructions(parts.get(1), stacks, crateMover9001);
    instructions.run();

    stacks.stream().map(Deque::peekFirst).filter(Objects::nonNull).forEachOrdered(out::print);
    out.println();
  }

  private static List<Deque<String>> parseStacks(String stacks) {
    var lines = stacks.lines().limit(stacks.lines().count() - 1).toList();
    var numStacks =
        Streams.findLast(stacks.lines())
            .orElseThrow()
            .chars()
            .filter(c -> !Character.isWhitespace(c))
            .count();
    var result =
        Stream.generate(() -> (Deque<String>) new ArrayDeque<String>()).limit(numStacks).toList();

    for (var line : Lists.reverse(lines)) {
      int index = 0;
      for (String element : Splitter.fixedLength(4).trimResults().split(line)) {
        String elementUnwrapped =
            CharMatcher.is(']')
                .trimTrailingFrom(CharMatcher.is('[').trimLeadingFrom(element.strip()));
        if (!element.isEmpty()) {
          var stack = result.get(Ints.saturatedCast(index));
          stack.push(elementUnwrapped);
        }
        index++;
      }
    }

    return result;
  }

  private static Runnable parseInstructions(
      String instructions, List<Deque<String>> stacks, boolean crateMover9001) {
    record Instruction(int move, int from, int to, boolean crateMover9001) {}

    List<Instruction> instructionsList =
        instructions
            .lines()
            .map(
                line -> {
                  var instructionMatcher =
                      Pattern.compile("move (\\d+) from (\\d+) to (\\d+)").matcher(line);
                  if (instructionMatcher.find()) {
                    int move = Integer.parseInt(instructionMatcher.group(1));
                    int from = Integer.parseInt(instructionMatcher.group(2)) - 1;
                    int to = Integer.parseInt(instructionMatcher.group(3)) - 1;
                    return new Instruction(move, from, to, crateMover9001);
                  } else {
                    throw new AssertionError("unreachable");
                  }
                })
            .toList();

    return () ->
        instructionsList.forEach(
            instruction -> {
              if (!instruction.crateMover9001()) {
                for (int i = 0; i < instruction.move(); i++) {
                  stacks.get(instruction.to()).push(stacks.get(instruction.from()).pop());
                }
              } else {
                var buffer = new ArrayDeque<String>(instruction.move());
                var from = stacks.get(instruction.from());
                for (int i = 0; i < instruction.move(); i++) {
                  buffer.addLast(from.pop());
                }
                var to = stacks.get(instruction.to());
                for (int i = 0; i < instruction.move(); i++) {
                  to.push(buffer.removeLast());
                }
              }
            });
  }
}

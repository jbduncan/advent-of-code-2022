package me.jbduncan.adventofcode2022.day2;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.io.MoreFiles;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public final class App {

  public static void main(String[] args) throws IOException {
    execute(Arrays.asList(args), new PrintWriter(System.out, true, UTF_8));
  }

  static void execute(List<String> args, PrintWriter out) throws IOException {
    var inputFile = Path.of(args.get(0));
    boolean decryptCorrectly = args.size() > 1 && args.get(1).equals("--decrypt-correctly");
    var parseStrategy =
        decryptCorrectly
            ? ParseStrategy.SECOND_VALUE_IS_OUTCOME
            : ParseStrategy.SECOND_VALUE_IS_THEIR_MOVE;
    var lines = MoreFiles.asCharSource(inputFile, UTF_8).readLines();
    var gameStrategies = parse(lines, parseStrategy);
    var totalScore = gameStrategies.stream().mapToInt(GameStrategy::score).sum();
    out.println(totalScore);
  }

  private record GameStrategy(Move theirMove, Move yourMove) {
    int score() {
      return yourMove.selectionScore() + yourMove.outcomeVersus(theirMove).score();
    }
  }

  private enum Move {
    ROCK,
    PAPER,
    SCISSORS;

    int selectionScore() {
      return switch (this) {
        case ROCK -> 1;
        case PAPER -> 2;
        case SCISSORS -> 3;
      };
    }

    Outcome outcomeVersus(Move theirMove) {
      return switch (this) {
        case ROCK -> switch (theirMove) {
          case ROCK -> Outcome.DRAW;
          case PAPER -> Outcome.LOSS;
          case SCISSORS -> Outcome.WIN;
        };
        case PAPER -> switch (theirMove) {
          case ROCK -> Outcome.WIN;
          case PAPER -> Outcome.DRAW;
          case SCISSORS -> Outcome.LOSS;
        };
        case SCISSORS -> switch (theirMove) {
          case ROCK -> Outcome.LOSS;
          case PAPER -> Outcome.WIN;
          case SCISSORS -> Outcome.DRAW;
        };
      };
    }

    Move moveThatWinsAgainst() {
      return switch (this) {
        case ROCK -> SCISSORS;
        case PAPER -> ROCK;
        case SCISSORS -> PAPER;
      };
    }

    Move moveThatDrawsAgainst() {
      return this;
    }

    Move moveThatLosesAgainst() {
      return switch (this) {
        case ROCK -> PAPER;
        case PAPER -> SCISSORS;
        case SCISSORS -> ROCK;
      };
    }
  }

  private enum Outcome {
    WIN(6),
    DRAW(3),
    LOSS(0);

    private final int score;

    Outcome(int score) {
      this.score = score;
    }

    int score() {
      return score;
    }
  }

  private enum ParseStrategy {
    SECOND_VALUE_IS_THEIR_MOVE,
    SECOND_VALUE_IS_OUTCOME
  }

  private static ImmutableList<GameStrategy> parse(
      List<String> lines, ParseStrategy parseStrategy) {
    return lines.stream()
        .map(line -> Splitter.on(' ').limit(2).splitToList(line))
        .map(
            parts -> {
              var theirMove =
                  switch (parts.get(0)) {
                    case "A" -> Move.ROCK;
                    case "B" -> Move.PAPER;
                    case "C" -> Move.SCISSORS;
                    default -> throw unreachable();
                  };
              var yourMove =
                  switch (parseStrategy) {
                    case SECOND_VALUE_IS_THEIR_MOVE -> switch (parts.get(1)) {
                      case "X" -> Move.ROCK;
                      case "Y" -> Move.PAPER;
                      case "Z" -> Move.SCISSORS;
                      default -> throw unreachable();
                    };
                    case SECOND_VALUE_IS_OUTCOME -> switch (parts.get(1)) {
                      case "X" -> theirMove.moveThatWinsAgainst();
                      case "Y" -> theirMove.moveThatDrawsAgainst();
                      case "Z" -> theirMove.moveThatLosesAgainst();
                      default -> throw unreachable();
                    };
                  };
              return new GameStrategy(theirMove, yourMove);
            })
        .collect(toImmutableList());
  }

  private static AssertionError unreachable() {
    return new AssertionError("unreachable");
  }
}

package me.jbduncan.adventofcode2022.day6;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AppTests {

  private static Path tempFile(String contents) throws IOException {
    var tempFile = Files.createTempFile("", "");
    Files.writeString(tempFile, contents);
    return tempFile;
  }

  private static String executeAndReturnStdOut(Object... args) throws IOException {
    var out = new StringWriter();
    App.execute(Arrays.stream(args).map(Object::toString).toList(), new PrintWriter(out, true));
    return out.toString().strip();
  }

  @Nested
  class GivenStar11Input {

    @Nested
    class WhenRunningApp {

      private static Stream<Arguments> values() {
        return Stream.of(
            arguments("mjqjpqmgbljsphdztnvjfqwrcgsmlb", "7"),
            arguments("bvwbjplbgvbhsrlpgdmjqwftvncz", "5"),
            arguments("nppdvjthqldpwncqszvftbrmjlhg", "6"),
            arguments("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", "10"),
            arguments("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", "11"));
      }

      @MethodSource("values")
      @ParameterizedTest
      void thenItReturnsExpectedOutput(String input, String output) throws IOException {
        var tempFile = tempFile(input);
        var out = executeAndReturnStdOut(tempFile);
        assertThat(out).isEqualTo(output);
      }
    }
  }

  @Nested
  class GivenStar12Input {

    @Nested
    class WhenRunningApp {

      private static Stream<Arguments> values() {
        return Stream.of(
            arguments("mjqjpqmgbljsphdztnvjfqwrcgsmlb", "19"),
            arguments("bvwbjplbgvbhsrlpgdmjqwftvncz", "23"),
            arguments("nppdvjthqldpwncqszvftbrmjlhg", "23"),
            arguments("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", "29"),
            arguments("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", "26"));
      }

      @MethodSource("values")
      @ParameterizedTest
      void thenItReturnsExpectedOutput(String input, String output) throws IOException {
        var tempFile = tempFile(input);
        var out = executeAndReturnStdOut(tempFile, "--start-of-message-marker");
        assertThat(out).isEqualTo(output);
      }
    }
  }
}

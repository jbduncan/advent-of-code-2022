package me.jbduncan.adventofcode2022.day2;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AppTests {

  private static Path tempFile(String contents) throws IOException {
    var tempFile = Files.createTempFile("", "");
    Files.writeString(tempFile, contents);
    return tempFile;
  }

  private static String executeAndReturnOut(Object... args) throws IOException {
    var out = new StringWriter();
    App.execute(Arrays.stream(args).map(Object::toString).toList(), new PrintWriter(out, true));
    return out.toString().strip();
  }

  @Nested
  class GivenStar3Input {

    @Nested
    class WhenRunningApp {

      @Test
      void thenItReturns15() throws IOException {
        var tempFile =
            tempFile(
                """
                A Y
                B X
                C Z
                """);
        var out = executeAndReturnOut(tempFile);
        assertThat(out).isEqualTo("15");
      }
    }
  }

  @Nested
  class GivenStar4Input {

    @Nested
    class WhenRunningApp {

      @Test
      void thenItReturns12() throws IOException {
        var tempFile =
            tempFile(
                """
                A Y
                B X
                C Z
                """);
        var out = executeAndReturnOut(tempFile, "--decrypt-correctly");
        assertThat(out).isEqualTo("12");
      }
    }
  }
}

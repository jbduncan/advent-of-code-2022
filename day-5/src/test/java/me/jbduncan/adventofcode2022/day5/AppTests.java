package me.jbduncan.adventofcode2022.day5;

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
  class GivenStar9Input {

    @Nested
    class WhenRunningApp {

      @Test
      void thenItReturnsCmz() throws IOException {
        var tempFile =
            tempFile(
                """
                    [D]   \s
                [N] [C]   \s
                [Z] [M] [P]
                 1   2   3\s

                move 1 from 2 to 1
                move 3 from 1 to 3
                move 2 from 2 to 1
                move 1 from 1 to 2
                """);
        var out = executeAndReturnOut(tempFile);
        assertThat(out).isEqualTo("CMZ");
      }
    }
  }

  @Nested
  class GivenStar10Input {

    @Nested
    class WhenRunningApp {

      @Test
      void thenItReturnsMcd() throws IOException {
        var tempFile =
            tempFile(
                """
                    [D]   \s
                [N] [C]   \s
                [Z] [M] [P]
                 1   2   3\s

                move 1 from 2 to 1
                move 3 from 1 to 3
                move 2 from 2 to 1
                move 1 from 1 to 2
                """);
        var out = executeAndReturnOut(tempFile, "--crate-mover-9001");
        assertThat(out).isEqualTo("MCD");
      }
    }
  }
}

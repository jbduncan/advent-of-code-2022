package me.jbduncan.adventofcode2022.day1;

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

  private static String executeAndReturnStdOut(Object... args) throws IOException {
    var out = new StringWriter();
    App.execute(Arrays.stream(args).map(Object::toString).toList(), new PrintWriter(out, true));
    return out.toString().strip();
  }

  @Nested
  class GivenStar1Input {

    @Nested
    class WhenRunningApp {

      @Test
      void thenItReturns24_000() throws IOException {
        var tempFile =
            tempFile(
                """
                1000
                2000
                3000

                4000

                5000
                6000

                7000
                8000
                9000

                10000
                """);
        var out = executeAndReturnStdOut(tempFile);
        assertThat(out).isEqualTo("24000");
      }
    }
  }

  @Nested
  class GivenStar2Input {

    @Nested
    class WhenRunningApp {

      @Test
      void thenItReturns45_000() throws IOException {
        var tempFile =
            tempFile(
                """
                1000
                2000
                3000

                4000

                5000
                6000

                7000
                8000
                9000

                10000
                """);
        var out = executeAndReturnStdOut(tempFile, "--top", "3");
        assertThat(out).isEqualTo("45000");
      }
    }
  }
}

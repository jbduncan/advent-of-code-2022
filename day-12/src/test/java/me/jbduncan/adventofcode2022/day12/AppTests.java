package me.jbduncan.adventofcode2022.day12;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.Resources;
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
  class GivenStar23Input {

    @Nested
    class WhenRunningApp {

      @Test
      void thenItReturns31() throws IOException {
        var tempFile =
            tempFile(
                """
                Sabqponm
                abcryxxl
                accszExk
                acctuvwj
                abdefghi
                """);
        var out = executeAndReturnStdOut(tempFile);
        assertThat(out).isEqualTo("31");
      }
    }
  }

  @Nested
  class GivenPersonalStar23Input {

    @Nested
    class WhenRunningApp {

      @Test
      void thenItDoesNotReturn423() throws Exception {
        var inputFile = Path.of(Resources.getResource("input.txt").toURI());
        var out = executeAndReturnStdOut(inputFile);
        assertThat(out).isEqualTo("423");
      }
    }
  }
}

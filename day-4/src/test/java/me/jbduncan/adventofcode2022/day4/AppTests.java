package me.jbduncan.adventofcode2022.day4;

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
  class GivenStar7Input {

    @Nested
    class WhenRunningApp {

      @Test
      void thenItReturns2() throws IOException {
        var tempFile =
            tempFile(
                """
                2-4,6-8
                2-3,4-5
                5-7,7-9
                2-8,3-7
                6-6,4-6
                2-6,4-8
                """);
        var out = executeAndReturnOut(tempFile);
        assertThat(out).isEqualTo("2");
      }
    }
  }

  @Nested
  class GivenStar8Input {

    @Nested
    class WhenRunningApp {

      @Test
      void thenItReturns4() throws IOException {
        var tempFile =
            tempFile(
                """
                2-4,6-8
                2-3,4-5
                5-7,7-9
                2-8,3-7
                6-6,4-6
                2-6,4-8
                """);
        var out = executeAndReturnOut(tempFile, "--overlaps");
        assertThat(out).isEqualTo("4");
      }
    }
  }
}

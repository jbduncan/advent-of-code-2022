package me.jbduncan.adventofcode2022.day7;

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
  class GivenStar13Input {

    @Nested
    class WhenRunningApp {

      @Test
      void thenItReturns95_437() throws IOException {
        var tempFile =
            tempFile(
                """
                $ cd /
                $ ls
                dir a
                14848514 b.txt
                8504156 c.dat
                dir d
                $ cd a
                $ ls
                dir e
                29116 f
                2557 g
                62596 h.lst
                $ cd e
                $ ls
                584 i
                $ cd ..
                $ cd ..
                $ cd d
                $ ls
                4060174 j
                8033020 d.log
                5626152 d.ext
                7214296 k
                """);
        var out = executeAndReturnStdOut(tempFile);
        assertThat(out).isEqualTo("95437");
      }
    }
  }

  @Nested
  class GivenStar14Input {

    @Nested
    class WhenRunningApp {

      @Test
      void thenItReturns24_933_642() throws IOException {
        var tempFile =
            tempFile(
                """
                $ cd /
                $ ls
                dir a
                14848514 b.txt
                8504156 c.dat
                dir d
                $ cd a
                $ ls
                dir e
                29116 f
                2557 g
                62596 h.lst
                $ cd e
                $ ls
                584 i
                $ cd ..
                $ cd ..
                $ cd d
                $ ls
                4060174 j
                8033020 d.log
                5626152 d.ext
                7214296 k
                """);
        var out = executeAndReturnStdOut(tempFile, "--find-directory-to-delete");
        assertThat(out).isEqualTo("24933642");
      }
    }
  }
}

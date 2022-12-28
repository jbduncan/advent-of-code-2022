package me.jbduncan.adventofcode2022.day7;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.graph.ElementOrder;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InMemoryFileTreeTests {

  @Nested
  class GivenStar13Input {

    @Nested
    class WhenConstructingInMemoryFileTree {

      @Test
      void thenNodesEqualToAllFilesAndDirectories() {
        assertThat(Fixtures.inMemoryFileTree().nodes())
            .containsExactly(
                new DirectoryInfo("/"),
                new DirectoryInfo("/a"),
                new DirectoryInfo("/a/e"),
                new FileInfo("/a/f", 29_116),
                new FileInfo("/a/g", 2_557),
                new FileInfo("/a/h.lst", 62_596),
                new FileInfo("/a/e/i", 584),
                new FileInfo("/b.txt", 14_848_514),
                new FileInfo("/c.dat", 8_504_156),
                new DirectoryInfo("/d"),
                new FileInfo("/d/j", 4_060_174),
                new FileInfo("/d/d.log", 8_033_020),
                new FileInfo("/d/d.ext", 5_626_152),
                new FileInfo("/d/k", 7_214_296),
                new DirectoryInfo("/d/d"),
                new FileInfo("/d/d/l.txt", 4_000));
      }

      @Test
      void thenIsDirected() {
        assertThat(Fixtures.inMemoryFileTree().isDirected()).isTrue();
      }

      @Test
      void thenDisallowsSelfLoops() {
        assertThat(Fixtures.inMemoryFileTree().allowsSelfLoops()).isFalse();
      }

      @Test
      void thenHasUnorderedNodeOrder() {
        assertThat(Fixtures.inMemoryFileTree().nodeOrder()).isEqualTo(ElementOrder.insertion());
      }

      @MethodSource
      @ParameterizedTest
      void thenHasChildInfosAsSuccessors(PathInfo pathInfo, Set<PathInfo> childPathInfos) {
        assertThat(Fixtures.inMemoryFileTree().successors(pathInfo)).isEqualTo(childPathInfos);
      }

      private static Stream<Arguments> thenHasChildInfosAsSuccessors() {
        return Stream.of(
            arguments(
                new DirectoryInfo("/"),
                Set.of(
                    new DirectoryInfo("/a"),
                    new FileInfo("/b.txt", 14_848_514),
                    new FileInfo("/c.dat", 8_504_156),
                    new DirectoryInfo("/d"))),
            arguments(
                new DirectoryInfo("/a"),
                Set.of(
                    new DirectoryInfo("/a/e"),
                    new FileInfo("/a/f", 29_116),
                    new FileInfo("/a/g", 2_557),
                    new FileInfo("/a/h.lst", 62596))));
      }

      @Test
      void thenHasParentAsPredecessorOfNonRootNode() {
        assertThat(Fixtures.inMemoryFileTree().predecessors(new DirectoryInfo("/a")))
            .containsExactly(new DirectoryInfo("/"));
      }
    }
  }
}

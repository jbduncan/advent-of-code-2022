package me.jbduncan.adventofcode2022.day7;

import static com.google.common.truth.Truth8.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DirectorySizesTests {

  @Nested
  class GivenInMemoryFileTree {

    @Nested
    class WhenCalculatingSizeOfDirectory {

      @Test
      void thenItCanReturnSumOfSizesOfAllSuccessorFiles() {
        assertThat(DirectorySizes.from(Fixtures.inMemoryFileTree()).lessThan(Long.MAX_VALUE))
            .containsExactly(584, 4_000, 94_853, 24_937_642, 48_385_165);
      }

      @Test
      void thenItCanReturnSumOfSizesOfSuccessorFilesLessThanGivenSize() {
        assertThat(DirectorySizes.from(Fixtures.inMemoryFileTree()).lessThan(24_937_642))
            .containsExactly(584, 4_000, 94_853);
      }
    }
  }
}

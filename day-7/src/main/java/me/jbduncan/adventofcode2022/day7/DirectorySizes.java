package me.jbduncan.adventofcode2022.day7;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.MoreCollectors.toOptional;

import com.google.common.collect.Comparators;
import com.google.common.collect.ImmutableMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

public final class DirectorySizes {

  private final ImmutableMap<DirectoryInfo, Long> directorySizes;
  private final DirectoryInfo rootDirectory;

  public static DirectorySizes from(InMemoryFileTree inMemoryFileTree) {
    var memo = new Memo();
    // var directorySizes = new ArrayList<Long>();
    for (PathInfo pathInfo : inMemoryFileTree.nodes()) {
      if (pathInfo instanceof DirectoryInfo directoryInfo) {
        long size = memo.calculateSizeOf(directoryInfo, inMemoryFileTree);
        memo.put(directoryInfo, size);
        // directorySizes.add(size);
      }
    }
    return new DirectorySizes(memo.immutableCopy(), inMemoryFileTree.rootDirectory());
  }

  private DirectorySizes(
      ImmutableMap<DirectoryInfo, Long> directorySizes, DirectoryInfo rootDirectory) {
    this.directorySizes = directorySizes;
    this.rootDirectory = checkNotNull(rootDirectory);
  }

  private static class Memo {
    private final Map<DirectoryInfo, Long> memo = new HashMap<>();

    long calculateSizeOf(PathInfo pathInfo, InMemoryFileTree inMemoryFileTree) {
      long directorySize = 0L;
      for (PathInfo successorPathInfo : inMemoryFileTree.successors(pathInfo)) {
        if (successorPathInfo instanceof FileInfo successorFileInfo) {
          directorySize += successorFileInfo.size();
        } else if (successorPathInfo instanceof DirectoryInfo successorDirectoryInfo) {
          if (!memo.containsKey(successorDirectoryInfo)) {
            long successorDirectorySize = calculateSizeOf(successorDirectoryInfo, inMemoryFileTree);
            memo.put(successorDirectoryInfo, successorDirectorySize);
            directorySize += successorDirectorySize;
          } else {
            directorySize += memo.get(successorDirectoryInfo);
          }
        }
      }
      return directorySize;
    }

    void put(DirectoryInfo directoryInfo, long size) {
      memo.put(directoryInfo, size);
    }

    ImmutableMap<DirectoryInfo, Long> immutableCopy() {
      return ImmutableMap.copyOf(memo);
    }
  }

  public LongStream lessThan(long maximumSize) {
    return directorySizes.values().stream()
        .mapToLong(size -> size)
        .filter(size -> size < maximumSize);
  }

  private static final long TOTAL_DISK_SPACE = 70_000_000L;
  private static final long SPACE_NEEDED = 30_000_000L;

  public long smallestDirectoryToRemove() {
    long spaceTaken = directorySizes.get(rootDirectory);
    long spaceToFreeUp = spaceTaken - (TOTAL_DISK_SPACE - SPACE_NEEDED);
    return directorySizes.values().stream()
        .filter(size -> size >= spaceToFreeUp)
        .collect(Comparators.least(1, Comparator.naturalOrder()))
        .stream()
        .collect(toOptional())
        .orElseThrow();
  }
}

package me.jbduncan.adventofcode2022.day7;

import com.google.common.primitives.ImmutableLongArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

public final class DirectorySizes {

  private final ImmutableLongArray directorySizes;

  public static DirectorySizes from(InMemoryFileTree inMemoryFileTree) {
    var memo = new Memo();
    var directorySizes = new ArrayList<Long>();
    for (PathInfo pathInfo : inMemoryFileTree.nodes()) {
      if (pathInfo instanceof DirectoryInfo directoryInfo) {
        long size = memo.calculateSizeOf(directoryInfo, inMemoryFileTree);
        directorySizes.add(size);
      }
    }
    return new DirectorySizes(ImmutableLongArray.copyOf(directorySizes));
  }

  private DirectorySizes(ImmutableLongArray directorySizes) {
    this.directorySizes = directorySizes;
  }

  public LongStream lessThan(long maximumSize) {
    return directorySizes.stream().filter(size -> size < maximumSize);
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
  }
}

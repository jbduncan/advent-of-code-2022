package me.jbduncan.adventofcode2022.day7;

import com.google.common.graph.Graphs;
import com.google.common.primitives.ImmutableLongArray;
import java.util.LinkedHashMap;
import java.util.stream.LongStream;

public final class DirectorySizes {

  private final ImmutableLongArray directorySizes;

  public static DirectorySizes from(InMemoryFileTree inMemoryFileTree) {
    var directorySizes = new LinkedHashMap<DirectoryInfo, Long>();

    for (var pathInfo : TopologicalOrder.topologicalOrder(Graphs.transpose(inMemoryFileTree))) {
      if (pathInfo instanceof DirectoryInfo directoryInfo) {
        long directorySize = 0;
        for (PathInfo successorPathInfo : inMemoryFileTree.successors(directoryInfo)) {
          if (successorPathInfo instanceof FileInfo successorFileInfo) {
            directorySize += successorFileInfo.size();
          } else if (successorPathInfo instanceof DirectoryInfo successorDirectoryInfo) {
            directorySize += directorySizes.get(successorDirectoryInfo);
          } else {
            directorySize += 0L;
          }
        }
        directorySizes.put(directoryInfo, directorySize);
      }
    }

    return new DirectorySizes(ImmutableLongArray.copyOf(directorySizes.values()));
  }

  private DirectorySizes(ImmutableLongArray directorySizes) {
    this.directorySizes = directorySizes;
  }

  public LongStream lessThan(long maximumSize) {
    return directorySizes.stream().filter(size -> size < maximumSize);
  }
}

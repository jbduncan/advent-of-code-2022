package me.jbduncan.adventofcode2022.day7;

import static com.google.common.collect.Iterables.getOnlyElement;
import static java.lang.Integer.parseInt;

import com.google.common.graph.AbstractGraph;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import java.util.Set;
import java.util.regex.Pattern;

public final class InMemoryFileTree extends AbstractGraph<PathInfo> {
  private static final Pattern CD_BACK_LINE_PATTERN = Pattern.compile("\\$ cd \\.\\.");
  private static final Pattern CD_LINE_PATTERN = Pattern.compile("\\$ cd (.+)");
  private static final Pattern DIR_LINE_PATTERN = Pattern.compile("dir (.+)");
  private static final Pattern FILE_LINE_PATTERN = Pattern.compile("(\\d+) (.+)");

  private final ImmutableGraph<PathInfo> delegate;

  public static InMemoryFileTree parse(String terminalOutput) {
    MutableGraph<PathInfo> delegate = GraphBuilder.directed().build();
    DirectoryInfo currentDirectory = DirectoryInfo.empty();

    for (var line : (Iterable<String>) terminalOutput.lines()::iterator) {
      var cdBackLineMatcher = CD_BACK_LINE_PATTERN.matcher(line);
      if (cdBackLineMatcher.matches()) {
        currentDirectory = parent(currentDirectory, delegate);
        continue;
      }

      var cdLineMatcher = CD_LINE_PATTERN.matcher(line);
      if (cdLineMatcher.matches()) {
        currentDirectory = currentDirectory.append(cdLineMatcher.group(1));
        delegate.addNode(currentDirectory);
        continue;
      }

      var dirLineMatcher = DIR_LINE_PATTERN.matcher(line);
      if (dirLineMatcher.matches()) {
        var childDirectory = currentDirectory.append(dirLineMatcher.group(1));
        delegate.putEdge(currentDirectory, childDirectory);
      }

      var fileLineMatcher = FILE_LINE_PATTERN.matcher(line);
      if (fileLineMatcher.matches()) {
        var file =
            new FileInfo(
                currentDirectory, fileLineMatcher.group(2), parseInt(fileLineMatcher.group(1)));
        delegate.putEdge(currentDirectory, file);
      }
    }

    return new InMemoryFileTree(ImmutableGraph.copyOf(delegate));
  }

  private static DirectoryInfo parent(PathInfo pathInfo, Graph<PathInfo> delegate) {
    return (DirectoryInfo) getOnlyElement(delegate.predecessors(pathInfo));
  }

  private InMemoryFileTree(ImmutableGraph<PathInfo> delegate) {
    this.delegate = delegate;
  }

  @Override
  public Set<PathInfo> nodes() {
    return delegate.nodes();
  }

  @Override
  public boolean isDirected() {
    return delegate.isDirected();
  }

  @Override
  public boolean allowsSelfLoops() {
    return delegate.allowsSelfLoops();
  }

  @Override
  public ElementOrder<PathInfo> nodeOrder() {
    return delegate.nodeOrder();
  }

  @Override
  public Set<PathInfo> adjacentNodes(PathInfo node) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public Set<PathInfo> predecessors(PathInfo node) {
    return delegate.predecessors(node);
  }

  @Override
  public Set<PathInfo> successors(PathInfo node) {
    return delegate.successors(node);
  }

  public DirectoryInfo rootDirectory() {
    return nodes().stream()
        .filter(DirectoryInfo.class::isInstance)
        .map(DirectoryInfo.class::cast)
        .filter(node -> predecessors(node).isEmpty())
        .findAny()
        .orElseThrow();
  }
}

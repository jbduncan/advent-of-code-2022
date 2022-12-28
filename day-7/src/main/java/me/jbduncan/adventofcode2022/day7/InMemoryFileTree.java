package me.jbduncan.adventofcode2022.day7;

import static com.google.common.collect.MoreCollectors.*;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toUnmodifiableSet;

import com.google.common.collect.Iterables;
import com.google.common.graph.AbstractGraph;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public final class InMemoryFileTree extends AbstractGraph<PathInfo> {
  private static final Pattern CD_BACK_LINE_PATTERN = Pattern.compile("\\$ cd \\.\\.");
  private static final Pattern CD_LINE_PATTERN = Pattern.compile("\\$ cd (.+)");
  private static final Pattern DIR_LINE_PATTERN = Pattern.compile("dir (.+)");
  private static final Pattern FILE_LINE_PATTERN = Pattern.compile("(\\d+) (.+)");

  private final ImmutableGraph<PathInfo> delegate;

  public static InMemoryFileTree parse(String terminalOutput) {
    MutableGraph<PathInfo> delegateBuilder = GraphBuilder.directed().build();
    DirectoryInfo currentDirectory = DirectoryInfo.empty();

    Iterable<String> lines = terminalOutput.lines()::iterator;
    for (var line : lines) {
      var cdBackLineMatcher = CD_BACK_LINE_PATTERN.matcher(line);
      if (cdBackLineMatcher.matches()) {
        currentDirectory = parentDirectory(delegateBuilder, currentDirectory);
        continue;
      }

      var cdLineMatcher = CD_LINE_PATTERN.matcher(line);
      if (cdLineMatcher.matches()) {
        //        currentDirectory = new DirectoryInfo(currentDirectory.name() + "/" +
        // (cdLineMatcher.group(1).equals("/") ? "" : "/"));
        currentDirectory =
            new DirectoryInfo(Path.of(currentDirectory.name(), cdLineMatcher.group(1)).toString());
        delegateBuilder.addNode(currentDirectory);
        continue;
      }

      var dirLineMatcher = DIR_LINE_PATTERN.matcher(line);
      if (dirLineMatcher.matches()) {
        //        var childDirectory = new DirectoryInfo(currentDirectory.name() + "/" +
        // (dirLineMatcher.group(1).equals("/") ? "" : "/"));
        var childDirectory =
            new DirectoryInfo(Path.of(currentDirectory.name(), dirLineMatcher.group(1)).toString());
        delegateBuilder.putEdge(currentDirectory, childDirectory);
      }

      var fileLineMatcher = FILE_LINE_PATTERN.matcher(line);
      if (fileLineMatcher.matches()) {
        var file =
            new FileInfo(
                Path.of(currentDirectory.name(), fileLineMatcher.group(2)).toString(),
                parseInt(fileLineMatcher.group(1)));
        delegateBuilder.putEdge(currentDirectory, file);
      }
    }

    return new InMemoryFileTree(ImmutableGraph.copyOf(delegateBuilder));
  }

  private static DirectoryInfo parentDirectory(Graph<PathInfo> graph, PathInfo pathInfo) {
    return (DirectoryInfo) Iterables.getOnlyElement(graph.predecessors(pathInfo));
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

  public Set<PathInfo> leaves() {
    return nodes().stream().filter(node -> successors(node).isEmpty()).collect(toUnmodifiableSet());
  }

  public Optional<DirectoryInfo> parent(PathInfo pathInfo) {
    return delegate.predecessors(pathInfo).stream()
        .collect(toOptional())
        .map(DirectoryInfo.class::cast);
  }
}

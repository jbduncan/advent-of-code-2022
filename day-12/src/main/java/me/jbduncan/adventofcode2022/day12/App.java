package me.jbduncan.adventofcode2022.day12;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Comparator.comparing;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.joda.collect.grid.DenseGrid;
import org.joda.collect.grid.Grid;
import org.joda.collect.grid.ImmutableGrid;

public final class App {
  public static void main(String[] args) throws IOException {
    execute(Arrays.asList(args), new PrintWriter(System.out, true, UTF_8));
  }

  static void execute(List<String> args, PrintWriter out) throws IOException {
    var inputFile = Path.of(args.get(0));
    var inputGrid = toGrid(Files.readString(inputFile, UTF_8));
    var startCell = start(inputGrid);
    var endCell = end(inputGrid);
    ImmutableList<Grid.Cell<String>> shortestPath = shortestPath(inputGrid, startCell, endCell);
    int steps = shortestPath.size() - 1;
    out.println(steps);
  }

  private static Grid.Cell<String> start(Grid<String> grid) {
    return grid.cells().stream()
        .filter(cell -> cell.getValue().equals("S"))
        .findFirst()
        .orElseThrow();
  }

  private static Grid.Cell<String> end(Grid<String> grid) {
    return grid.cells().stream()
        .filter(cell -> cell.getValue().equals("E"))
        .findFirst()
        .orElseThrow();
  }

  private static ImmutableList<Grid.Cell<String>> shortestPath(
      ImmutableGrid<String> grid, Grid.Cell<String> startCell, Grid.Cell<String> endCell) {
    checkArgument(grid.contains(startCell.getRow(), startCell.getColumn()));
    checkArgument(grid.contains(endCell.getRow(), endCell.getColumn()));

    Map<Grid.Cell<String>, Grid.Cell<String>> predecessors =
        dijkstrasAlgorithm(startCell, endCell, cell -> neighbours(grid, cell));

    return Stream.iterate(endCell, Objects::nonNull, predecessors::get)
        .collect(toImmutableList())
        .reverse();
  }

  private static <N> Map<N, N> dijkstrasAlgorithm(
      N startNode, N endNode, Function<N, Iterable<? extends N>> neighboursFunction) {
    Map<N, Double> distances = new HashMap<>();
    distances.put(startNode, 0.0);
    var predecessors = new HashMap<N, N>();
    var visited = new HashSet<N>();
    var remaining = new PriorityQueue<N>(comparing(distances::get));
    remaining.add(startNode);
    while (!remaining.isEmpty()) {
      N current = remaining.remove();
      if (current.equals(endNode)) {
        break;
      }
      for (N neighbour : neighboursFunction.apply(current)) {
        if (visited.contains(neighbour)) {
          continue;
        }
        double currentDistanceToNeighbour =
            distances.getOrDefault(neighbour, Double.POSITIVE_INFINITY);
        double proposedDistance = distances.getOrDefault(current, 0.0) + 1.0;
        if (proposedDistance < currentDistanceToNeighbour) {
          distances.put(neighbour, proposedDistance);
          predecessors.put(neighbour, current);
          remaining.remove(neighbour);
          remaining.add(neighbour);
        }
      }
      visited.add(current);
    }
    return predecessors;
  }

  private static List<Grid.Cell<String>> neighbours(Grid<String> grid, Grid.Cell<String> cell) {
    return Stream.of(above(grid, cell), below(grid, cell), left(grid, cell), right(grid, cell))
        .filter(Objects::nonNull)
        .filter(neighbour -> isNavigableWithoutClimbingGear(cell, neighbour))
        .toList();
  }

  private static boolean isNavigableWithoutClimbingGear(
      Grid.Cell<String> cell, Grid.Cell<String> neighbour) {
    char cellChar = toCharacter(cell.getValue());
    char neighbourChar = toCharacter(neighbour.getValue());
    if (cellChar == 'S' && neighbourChar == 'a') {
      return true;
    }
    if (cellChar == 'z' && neighbourChar == 'E') {
      return true;
    }
    return 'a' <= cellChar
        && cellChar <= 'z'
        && 'a' <= neighbourChar
        && neighbourChar <= 'z'
        && (cellChar - neighbourChar) >= -1;
  }

  private static @Nullable Grid.Cell<String> above(Grid<String> grid, Grid.Cell<String> cell) {
    return grid.cell(cell.getRow() - 1, cell.getColumn());
  }

  private static @Nullable Grid.Cell<String> below(Grid<String> grid, Grid.Cell<String> cell) {
    return grid.cell(cell.getRow() + 1, cell.getColumn());
  }

  private static @Nullable Grid.Cell<String> left(Grid<String> grid, Grid.Cell<String> cell) {
    return grid.cell(cell.getRow(), cell.getColumn() - 1);
  }

  private static @Nullable Grid.Cell<String> right(Grid<String> grid, Grid.Cell<String> cell) {
    return grid.cell(cell.getRow(), cell.getColumn() + 1);
  }

  private static char toCharacter(String letter) {
    return letter.charAt(0);
  }

  private static ImmutableGrid<String> toGrid(String input) {
    int x = input.lines().iterator().next().length();
    int y = (int) input.lines().count();
    Grid<String> result = DenseGrid.create(y, x);

    List<String> lines = input.lines().toList();
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      List<String> characters = line.chars().mapToObj(c -> Character.toString((char) c)).toList();
      for (int j = 0; j < characters.size(); j++) {
        String character = characters.get(j);
        result.put(i, j, character);
      }
    }

    return ImmutableGrid.copyOf(result);
  }
}

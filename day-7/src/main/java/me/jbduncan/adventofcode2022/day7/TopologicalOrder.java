package me.jbduncan.adventofcode2022.day7;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Multisets.toMultiset;
import static java.util.stream.Collectors.toCollection;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.graph.Graph;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Set;

// Adapted from my implementation over at https://github.com/jrtom/jung/pull/174/files
public final class TopologicalOrder {
  public static <N> Set<N> topologicalOrder(Graph<N> graph) {
    return new AbstractSet<>() {
      @Override
      public UnmodifiableIterator<N> iterator() {
        // Kahn's algorithm
        Queue<N> roots =
            graph.nodes().stream()
                .filter(n -> graph.inDegree(n) == 0)
                .collect(toCollection(ArrayDeque::new));
        Multiset<N> nonRoots =
            graph.nodes().stream()
                .filter(node -> graph.inDegree(node) > 0)
                .collect(toMultiset(node -> node, graph::inDegree, HashMultiset::create));

        return new AbstractIterator<>() {
          @Override
          protected N computeNext() {
            if (!roots.isEmpty()) {
              N next = roots.remove();
              for (N successor : graph.successors(next)) {
                int newInDegree = nonRoots.count(successor) - 1;
                nonRoots.setCount(successor, newInDegree);
                if (newInDegree == 0) {
                  roots.add(successor);
                }
              }
              return next;
            }
            checkState(nonRoots.isEmpty(), "graph has at least one cycle");
            return endOfData();
          }
        };
      }

      @Override
      public int size() {
        return graph.nodes().size();
      }
    };
  }
}

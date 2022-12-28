package me.jbduncan.adventofcode2022.day7;

public final class Fixtures {

  public static InMemoryFileTree inMemoryFileTree() {
    return InMemoryFileTree.parse(
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
        dir d
        $ cd d
        $ ls
        4000 l.txt
        """);
  }

  public static DirectorySizes directorySizes() {
    return DirectorySizes.from(inMemoryFileTree());
  }

  private Fixtures() {}
}

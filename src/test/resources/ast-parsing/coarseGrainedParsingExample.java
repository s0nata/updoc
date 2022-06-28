public class coarseGrainedParsingExample {
  public static void singleAssignmentStatement() {
    /* some comments
    some comments2
    some comments3*/
    int i =
        someMethod(
            collectedNodes.stream()
                .map(node -> node.getMethodNodesSequence()) // some comments
                .flatMap(List::stream)
                .collect(Collectors.toList())
                .stream()
                .filter(node -> IfNode.class.isInstance(node))
                .count(),
            LOCRanges.stream()
                .anyMatch(
                    loc1 -> // some comments2
                    LOCRanges.stream()
                            .anyMatch(
                                loc2 ->
                                    loc1.getMinimum() > loc2.getMinimum()
                                        && loc1.getMaximum() < loc2.getMaximum())));
  }

  private boolean singleMethodReturnStatement(List<Range<Integer>> LOCRanges) {
    /* some comments
    some comments2
    some comments3*/
    return someMethod(
        collectedNodes.stream()
            .map(node -> node.getMethodNodesSequence())
            .flatMap(List::stream) // some comments
            .collect(Collectors.toList())
            .stream()
            .filter(node -> IfNode.class.isInstance(node))
            .count(),
        LOCRanges.stream()
            .anyMatch(
                loc1 -> // some comments2
                LOCRanges.stream()
                        .anyMatch(
                            loc2 ->
                                loc1.getMinimum() > loc2.getMinimum()
                                    && loc1.getMaximum() < loc2.getMaximum())));
  }

  private void singleMethodCallSequence() {
    /* some comments
    some comments2
    some comments3*/
    someMethod(
        collectedNodes.stream()
            .map(node -> node.getMethodNodesSequence())
            .flatMap(List::stream)
            .collect(Collectors.toList())
            .stream() // some comments
            .filter(node -> IfNode.class.isInstance(node))
            .count(),
        LOCRanges.stream()
            .anyMatch(
                loc1 ->
                    LOCRanges.stream()
                        .anyMatch( // some comments2
                            loc2 ->
                                loc1.getMinimum() > loc2.getMinimum()
                                    && loc1.getMaximum() < loc2.getMaximum())));
  }

  private void forLoop(){
      for (int i = 0; i < someMethod(); i++) {
          doStuff();
      }
  }
}

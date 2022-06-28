public class differentStatements {

  public int foo() {

    list.add(1);

    getUMLDiagramBody();

    addJGraphTVertex(i.next());

    int a = 0;

    float b = func(a);

    if (condition) {
      doStuff();
    } else {
      doOtherStuff();
    }

    return 0;
  }

  private void bar() {


    for (int i = 0; i < 10; i++) {

      if (condition) {
        doStuff();
      } else {
        doOtherStuff();
      }

    }

    switch (var) {
      case 0:
      case 1:
      case 2:
        a = 0;
        break;
      case 3:
        a = 1;
      default:
        throw new Exception();
    }

    while (cond) {
      doStuff();
      a = 1;
      doStuff();
    }

    try {
      try {
        // some comment
      } catch (IOException e2) {
        // some comment
      } finally {
        // some comment
      }
    } catch (IOException e) {
      // some comment
    } finally {
      // some comment
    }

    try (InputStream i = new FileInputStream("file")) {
      // some comment
    } catch (IOException | NullPointerException e) {
      // some comment
    } finally {
      // some comment
    }

    do {
      while (b == 3) {
        // stuff
      }
    } while (a == 0);
  }

}

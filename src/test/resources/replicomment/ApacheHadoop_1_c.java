/**
   * Remove and return n elements from the hashtable.
   * The order in which entries are removed is corresponds
   * to the order in which they were inserted.
   *
   * @return first element
   */
  @Override
  public List<T> pollN(int n) {
    if (n >= size) {
      // if we need to remove all elements then do fast polling
      return pollAll();
    }
    List<T> retList = new ArrayList<T>(n);
    while (n-- > 0 && head != null) {
      T curr = head.element;
      this.removeElem(curr);
      retList.add(curr);
    }
    shrinkIfNecessary();
    return retList;
  }

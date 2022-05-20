  /**
   * Remove and return first element on the linked list of all elements.
   *
   * @return first element
   */
  public T pollFirst() {
    if (head == null) {
      return null;
    }
    T first = head.element;
    this.remove(first);
    return first;
  }

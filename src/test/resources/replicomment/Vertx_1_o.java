
  /**
   * Set whether a hint should be provided that the file to created is sparse
   * @param sparse true if a hint should be provided that the file to created is sparse
   * @return a reference to this, so the API can be used fluently
   */
  public OpenOptions setSparse(boolean sparse) {
    this.sparse = sparse;
    return this;
  }

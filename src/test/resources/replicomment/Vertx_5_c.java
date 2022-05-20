  /**
   * Set the failure. Any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @param result  the result
   * @return false when the future is already completed
   */
  boolean tryComplete(T result);
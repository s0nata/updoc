  /**
   * Set the failure. Any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @param cause  the failure cause
   */
  void fail(Throwable cause);
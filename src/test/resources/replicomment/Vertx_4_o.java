  /**
   * Is the current thread a worker thread?
   * <p>
   * NOTE! This is not always the same as calling {@link Context#isWorkerContext}. If you are running blocking code
   * from an event loop context, then this will return true but {@link Context#isWorkerContext} will return false.
   *
   * @return true if current thread is a worker thread, false otherwise
   */
  static boolean isOnWorkerThread() {
    return ContextImpl.isOnWorkerThread();
  }

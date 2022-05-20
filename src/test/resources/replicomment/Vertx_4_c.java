/**
 * Is the current thread an event thread?
 * <p>
 * NOTE! This is not always the same as calling {@link Context#isEventLoopContext}. If you are running blocking code
 * from an event loop context, then this will return false but {@link Context#isEventLoopContext} will return true.
 *
 * @return true if current thread is a worker thread, false otherwise
 */
static boolean isOnEventLoopThread() {
  return ContextImpl.isOnEventLoopThread();
}

  /**
   * Return the total space used by dfs datanode
   */
  @Override // FSDatasetMBean
  public long getDfsUsed() throws IOException {
    synchronized(statsLock) {
      return volumes.getDfsUsed();
    }
  }
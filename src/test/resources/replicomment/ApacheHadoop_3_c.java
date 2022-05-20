  /**
   * Return the total space used by dfs datanode
   */
  @Override // FSDatasetMBean
  public long getBlockPoolUsed(String bpid) throws IOException {
    synchronized(statsLock) {
      return volumes.getBlockPoolUsed(bpid);
    }
  }
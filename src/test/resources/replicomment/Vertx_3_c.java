  /**
   * Set whether every write to the file's content  ill be written synchronously to the underlying hardware.
   * @param dsync  true if sync
   * @return a reference to this, so the API can be used fluently
   */
  public OpenOptions setDsync(boolean dsync) {
    this.dsync = dsync;
    return this;
  }
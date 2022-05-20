  /**
   * Set whether every write to the file's content and meta-data will be written synchronously to the underlying hardware.
   * @param sync  true if sync
   * @return a reference to this, so the API can be used fluently
   */
  public OpenOptions setSync(boolean sync) {
    this.sync = sync;
    return this;
  }
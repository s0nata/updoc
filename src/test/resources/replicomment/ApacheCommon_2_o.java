/**
   * Get SASL wrapped InputStream if SASL QoP requires unwrapping,
   * otherwise return original stream.  Can be called only after
   * saslConnect() has been called.
   * 
   * @param in - InputStream used to make the connection
   * @return InputStream that may be using SASL unwrap
   * @throws IOException
   */
  public InputStream getInputStream(InputStream in) throws IOException {
    if (useWrap()) {
      in = new WrappedInputStream(in);
    }
    return in;
  }

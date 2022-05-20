  /**
   * Get SASL wrapped OutputStream if SASL QoP requires wrapping,
   * otherwise return original stream.  Can be called only after
   * saslConnect() has been called.
   * 
   * @param in - InputStream used to make the connection
   * @return InputStream that may be using SASL unwrap
   * @throws IOException
   */
  public OutputStream getOutputStream(OutputStream out) throws IOException {
    if (useWrap()) {
      // the client and server negotiate a maximum buffer size that can be
      // wrapped
      String maxBuf = (String)saslClient.getNegotiatedProperty(Sasl.RAW_SEND_SIZE);
      out = new BufferedOutputStream(new WrappedOutputStream(out),
                                     Integer.parseInt(maxBuf));
    }
    return out;
  }
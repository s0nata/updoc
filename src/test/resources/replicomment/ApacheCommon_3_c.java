/**
   * Return protocol version corresponding to protocol interface.
   * @param protocol The classname of the protocol interface
   * @param clientVersion The version of the protocol that the client speaks
   * @param clientMethodsHash the hashcode of client protocol methods
   * @return the server protocol signature containing its version and
   *         a list of its supported methods
   * @see ProtocolSignature#getProtocolSignature(VersionedProtocol, String,
   *                long, int) for a default implementation
   */
  public ProtocolSignature getProtocolSignature(String protocol,
                                 long clientVersion,
                                 int clientMethodsHash) throws IOException;

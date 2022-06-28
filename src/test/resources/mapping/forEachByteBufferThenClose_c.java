/**
     * Read each {@link Resource} in this {@link ResourceList} as a {@link ByteBuffer}, pass the {@link ByteBuffer}
	    * to the given {@link InputStreamConsumer}, then close the {@link InputStream} after the
     * {@link InputStreamConsumer} returns.
	 *
     * @param byteBufferConsumer
     *            The {@link ByteBufferConsumer}.
     * @throws IllegalArgumentException
     *             if trying to load any of the resources results in an {@link IOException} being thrown.
     */
	 
	 
    public void forEachByteBufferThenClose(final ByteBufferConsumer byteBufferConsumer) {
        forEachByteBufferThenClose(byteBufferConsumer, /* ignoreIOExceptions = */ false);
    }
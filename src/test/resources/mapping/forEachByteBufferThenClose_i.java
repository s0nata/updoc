/**
     * Read each {@link Resource} in this {@link ResourceList} as a {@link ByteBuffer}, pass the {@link ByteBuffer}
	  * to the given {@link InputStreamConsumer}, then release the {@link ByteBuffer} after the
     * {@link ByteBufferConsumer} returns, by calling {@link Resource#close()}.
     *    
     * @param byteBufferConsumer
     *            The {@link ByteBufferConsumer}.
     * @throws IllegalArgumentException
     *             if trying to load any of the resources results in an {@link IOException} being thrown.
     */
	 
	 
    public void forEachByteBufferThenClose(final ByteBufferConsumer byteBufferConsumer) {
        forEachByteBufferThenClose(byteBufferConsumer, /* ignoreIOExceptions = */ false);
    }
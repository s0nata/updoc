 /**
     * Read each {@link Resource} in this {@link ResourceList} as a {@link ByteBuffer}, pass the {@link ByteBuffer}
     * to the given {@link InputStreamConsumer}, then close the {@link InputStream} after the
     * {@link InputStreamConsumer} returns.
	  * 
     * @param byteBufferConsumer
     *            The {@link ByteBufferConsumer}.
     * @param ignoreIOExceptions
     *            if true, any {@link IOException} thrown while trying to load any of the resources will be silently
     *            ignored.
     * @throws IllegalArgumentException
     *             if ignoreExceptions is false, and an {@link IOException} is thrown while trying to load any of
     *             the resources.
     */
	 
	 
	   public void forEachByteBufferThenClose(final ByteBufferConsumer byteBufferConsumer,
            final boolean ignoreIOExceptions) {
        for (final Resource resource : this) {
            try {
                final ByteBuffer byteBuffer = resource.read();
                byteBufferConsumer.accept(resource, byteBuffer);
            } catch (final IOException e) {
                if (!ignoreIOExceptions) {
                    throw new IllegalArgumentException("Could not load resource " + this, e);
                }
            } finally {
                resource.close();
            }
        }
    }
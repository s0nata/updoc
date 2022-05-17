/**
 * Fetch the content of each {@link Resource} in this {@link ResourceList} as a byte array, then close the
 * underlying InputStream or ByteBuffer before passing the byte array to the given {@link ByteArrayConsumer}
 * ByteBuffer by calling {@link Resource#close()}.
 *
 * @param byteArrayConsumer
 *            The {@link ByteArrayConsumer}.
 * @param ignoreIOExceptions
 *            if true, any {@link IOException} thrown while trying to load any of the resources will be silently
 *            ignored.
 * @throws IllegalArgumentException
 *             if ignoreExceptions is false, and an {@link IOException} is thrown while trying to load any of
 *             the resources.
 */
public void forEachByteArrayThenClose(final ByteArrayConsumer byteArrayConsumer,
final boolean ignoreIOExceptions) {
        for (final Resource resource : this) {
        try {
final byte[] resourceContent = resource.load();
        byteArrayConsumer.accept(resource, resourceContent);
        } catch (final IOException e) {
        if (!ignoreIOExceptions) {
        throw new IllegalArgumentException("Could not load resource " + this, e);
        }
        } finally {
        resource.close();
        }
        }
        }
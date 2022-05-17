/**
 * Fetch an {@link InputStream} for each {@link Resource} in this {@link ResourceList}, pass the
 * {@link InputStream} to the given {@link InputStreamConsumer}, then close the {@link InputStream} after the
 * {@link InputStreamConsumer} returns, by calling {@link Resource#close()}.
 *
 * @param inputStreamConsumer
 *            The {@link InputStreamConsumer}.
 * @param ignoreIOExceptions
 *            if true, any {@link IOException} thrown while trying to load any of the resources will be silently
 *            ignored.
 * @throws IllegalArgumentException
 *             if ignoreExceptions is false, and an {@link IOException} is thrown while trying to open any of
 *             the resources.
 */
public void forEachInputStreamThenClose(final InputStreamConsumer inputStreamConsumer,
final boolean ignoreIOExceptions) {
        for (final Resource resource : this) {
        try {
final InputStream inputStream = resource.open();
        inputStreamConsumer.accept(resource, inputStream);
        } catch (final IOException e) {
        if (!ignoreIOExceptions) {
        throw new IllegalArgumentException("Could not load resource " + this, e);
        }
        } finally {
        resource.close();
        }
        }
        }
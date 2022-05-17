/**
 * @param needRefresh when {@code false} it won't guarantee the index reader to be affected by "latest" changes
 * @return returns an {@code IndexReader} instance, either pooled or a new one
 */
private IndexReader openIndexReader(final boolean needRefresh) {
        IndexReader indexReader;
        if ( needRefresh ) {
        indexReader = refreshReaders();
        }
        else {
        indexReader = currentReader.get();
        }
        if ( indexReader == null ) {
        writeLock.lock();
        try {
        if ( shutdown ) {
        throw new AssertionFailure( "IndexReader requested after ReaderProvider is shutdown" );
        }
        indexReader = currentReader.get();
        if ( indexReader == null ) {
        indexReader = writerHolder.openDirectoryIndexReader();
        currentReader.set( indexReader );
        }
        }
        finally {
        writeLock.unlock();
        }
        }
        if ( indexReader.tryIncRef() ) {
        return indexReader;
        }
        else {
        //In this case we have a race: the chosen IndexReader was closed before we could increment its reference, so we need
        //to try again. Basically an optimistic lock as the race condition is very unlikely.
        //Changes should be tested at least with ReadWriteParallelismTest (in the performance tests module).
        //In case new writes happened there is no need to refresh again.
        return openIndexReader( false );
        }
        }
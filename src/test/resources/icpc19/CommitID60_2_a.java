/**
* Invoked when a refresh of current {@code IndexReader}s is detected necessary.
        *
        * The implementation is blocking to maximize reuse of a single {@code IndexReader} (better for buffer usage,
        * caching, ..) and to avoid multiple threads trying and opening the same resources at the same time.
        *
        * @return the refreshed {@code IndexReader}
        */
private synchronized IndexReader refreshReaders() {
//double-check for the case we don't need anymore to refresh
        if ( indexReaderIsFresh() ) {
        return currentReader.get();
        }
//order of the following two reads DOES matter:
final long readerGenRequiringFlushWrites = this.readerGenRequiringFlushWrites;
final long readerGenRequiringFlushDeletes = this.readerGenRequiringFlushDeletes;
final boolean flushDeletes = currentReaderGen < readerGenRequiringFlushDeletes;
final long openingGen = Math.max( readerGenRequiringFlushDeletes, readerGenRequiringFlushWrites );

final IndexReader newIndexReader = writerHolder.openNRTIndexReader( flushDeletes );
final IndexReader oldReader = currentReader.getAndSet( newIndexReader );
        this.currentReaderGen = openingGen;
        try {
        if ( oldReader != null ) {
        oldReader.decRef();
        }
        }
        catch ( IOException e ) {
        log.unableToCloseLuceneIndexReader( e );
        }
        return newIndexReader;
        }
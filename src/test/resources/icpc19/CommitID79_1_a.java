/**
 * Checks if the source stream will return a CR+LF sequence next, without
 * actually reading it.
 *
 * @throws IOException
 */
private void checkCRLF() throws IOException {
final int cr = this.source.read();
final int lf = this.source.read();
        if ((cr != '\r') && (lf != '\n')) {
        this.source.unread(lf);
        this.source.unread(cr);
        }
        }
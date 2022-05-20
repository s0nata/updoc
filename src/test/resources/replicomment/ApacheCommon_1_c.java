  /**
   * Get a local file under a directory named by <i>dirsProp</i> with
   * the given <i>path</i>.  If <i>dirsProp</i> contains multiple directories,
   * then one is chosen based on <i>path</i>'s hash code.  If the selected
   * directory does not exist, an attempt is made to create it.
   *
   * @param dirsProp directory in which to locate the file.
   * @param path file-path.
   * @return local file under the directory with the given path.
   */
  public Path getLocalPath(String dirsProp, String path)
    throws IOException {
    String[] dirs = getTrimmedStrings(dirsProp);
    int hashCode = path.hashCode();
    FileSystem fs = FileSystem.getLocal(this);
    for (int i = 0; i < dirs.length; i++) {  // try each local dir
      int index = (hashCode+i & Integer.MAX_VALUE) % dirs.length;
      Path file = new Path(dirs[index], path);
      Path dir = file.getParent();
      if (fs.mkdirs(dir) || fs.exists(dir)) {
        return file;
      }
    }
    LOG.warn("Could not make " + path +
             " in local directories from " + dirsProp);
    for(int i=0; i < dirs.length; i++) {
      int index = (hashCode+i & Integer.MAX_VALUE) % dirs.length;
      LOG.warn(dirsProp + "[" + index + "]=" + dirs[index]);
    }
    throw new IOException("No valid local directories in property: "+dirsProp);
  }

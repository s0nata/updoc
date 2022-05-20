  /**
   * Get a local file name under a directory named in <i>dirsProp</i> with
   * the given <i>path</i>.  If <i>dirsProp</i> contains multiple directories,
   * then one is chosen based on <i>path</i>'s hash code.  If the selected
   * directory does not exist, an attempt is made to create it.
   *
   * @param dirsProp directory in which to locate the file.
   * @param path file-path.
   * @return local file under the directory with the given path.
   */
  public File getFile(String dirsProp, String path)
    throws IOException {
    String[] dirs = getTrimmedStrings(dirsProp);
    int hashCode = path.hashCode();
    for (int i = 0; i < dirs.length; i++) {  // try each local dir
      int index = (hashCode+i & Integer.MAX_VALUE) % dirs.length;
      File file = new File(dirs[index], path);
      File dir = file.getParentFile();
      if (dir.exists() || dir.mkdirs()) {
        return file;
      }
    }
    throw new IOException("No valid local directories in property: "+dirsProp);
  }

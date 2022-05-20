    /**
     * Reads a module constant pool item in {@link #b b}. <i>This method is
     * intended for {@link Attribute} sub classes, and is normally not needed by
     * class generators or adapters.</i>
     *
     * @param index
     *            the start index of an unsigned short value in {@link #b b},
     *            whose value is the index of a module constant pool item.
     * @param buf
     *            buffer to be used to read the item. This buffer must be
     *            sufficiently large. It is not automatically resized.
     * @return the String corresponding to the specified module item.
     */
    public String readModule(final int index, final char[] buf) {
        return readStringish(index, buf);
    }
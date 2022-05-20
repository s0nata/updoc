    /**
     * Converts a byte array to an short.
     *
     * @param arr The byte array to convert to an short
     * @return The int converted
     */
    public static short bytesToShort(byte[] arr) {
        return (short) (((arr[0] & 0xff) << 8) | (arr[1] & 0xff));
    }

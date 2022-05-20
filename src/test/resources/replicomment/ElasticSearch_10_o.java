    /**
     * Converts an int to a byte array.
     *
     * @param val The int to convert to a byte array
     * @return The byte array converted
     */
    public static byte[] intToBytes(int val) {
        byte[] arr = new byte[4];
        arr[0] = (byte) (val >>> 24);
        arr[1] = (byte) (val >>> 16);
        arr[2] = (byte) (val >>> 8);
        arr[3] = (byte) (val);
        return arr;
    }
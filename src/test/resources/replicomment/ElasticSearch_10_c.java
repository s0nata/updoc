    /**
     * Converts an int to a byte array.
     *
     * @param val The int to convert to a byte array
     * @return The byte array converted
     */
    public static byte[] shortToBytes(int val) {
        byte[] arr = new byte[2];
        arr[0] = (byte) (val >>> 8);
        arr[1] = (byte) (val);
        return arr;
    }
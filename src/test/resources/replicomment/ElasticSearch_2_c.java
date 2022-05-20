    /**
     * Converts a byte array to an int.
     *
     * @param arr The byte array to convert to an int
     * @return The int converted
     */
    public static int bytesToInt(byte[] arr) {
        return (arr[0] << 24) | ((arr[1] & 0xff) << 16) | ((arr[2] & 0xff) << 8) | (arr[3] & 0xff);
    }

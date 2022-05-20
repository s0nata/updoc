    /**
     * Returns the index of the type parameter referenced by this type
     * reference. This method must only be used for type references whose sort
     * is {@link #CLASS_TYPE_PARAMETER CLASS_TYPE_PARAMETER},
     * {@link #METHOD_TYPE_PARAMETER METHOD_TYPE_PARAMETER},
     * {@link #CLASS_TYPE_PARAMETER_BOUND CLASS_TYPE_PARAMETER_BOUND} or
     * {@link #METHOD_TYPE_PARAMETER_BOUND METHOD_TYPE_PARAMETER_BOUND}.
     * 
     * @return a type parameter index.
     */
    public int getTypeParameterIndex() {
        return (value & 0x00FF0000) >> 16;
    }
    /**
     * Returns the index of the type argument referenced by this type reference.
     * This method must only be used for type references whose sort is
     * {@link #CAST CAST}, {@link #CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT
     * CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT},
     * {@link #METHOD_INVOCATION_TYPE_ARGUMENT METHOD_INVOCATION_TYPE_ARGUMENT},
     * {@link #CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT
     * CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT}, or
     * {@link #METHOD_REFERENCE_TYPE_ARGUMENT METHOD_REFERENCE_TYPE_ARGUMENT}.
     * 
     * @return a type parameter index.
     */
    public int getTypeArgumentIndex() {
        return value & 0xFF;
    }
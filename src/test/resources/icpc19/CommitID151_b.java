/**
 * Used to check referential integrity.
 * TODO Return value is not yet used, but could allow entities to point to each other directly rather than
 * using indirection through string-keyed maps.
 */
protected <K, V> V getRefField(String column, boolean required, Map<K, V> target) throws IOException {
        String str = getFieldCheckRequired(column, required);
        V val = null;
        if (str != null) {
        val = target.get(str);
        if (val == null) {
        feed.errors.add(new ReferentialIntegrityError(tableName, row, column, str));
        }
        }
        return val;
        }
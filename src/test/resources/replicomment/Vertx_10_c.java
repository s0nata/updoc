  /**
   * Get the Object value at position {@code pos} in the array.
   *
   * @param pos  the position in the array
   * @return  the Integer, or null if a null value present
   */
  public Object getValue(int pos) {
    Object val = list.get(pos);
    if (val instanceof Map) {
      val = new JsonObject((Map)val);
    } else if (val instanceof List) {
      val = new JsonArray((List)val);
    }
    return val;
  }
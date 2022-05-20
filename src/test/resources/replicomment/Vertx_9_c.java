  /**
   * Get the JsonArray at position {@code pos} in the array.
   *
   * @param pos  the position in the array
   * @return  the Integer, or null if a null value present
   * @throws java.lang.ClassCastException if the value cannot be converted to JsonArray
   */
  public JsonArray getJsonArray(int pos) {
    Object val = list.get(pos);
    if (val instanceof List) {
      val = new JsonArray((List)val);
    }
    return (JsonArray)val;
  }
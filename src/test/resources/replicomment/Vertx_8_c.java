  /**
   * Get the JsonObject at position {@code pos} in the array.
   *
   * @param pos  the position in the array
   * @return  the Integer, or null if a null value present
   * @throws java.lang.ClassCastException if the value cannot be converted to JsonObject
   */
  public JsonObject getJsonObject(int pos) {
    Object val = list.get(pos);
    if (val instanceof Map) {
      val = new JsonObject((Map)val);
    }
    return (JsonObject)val;
  }
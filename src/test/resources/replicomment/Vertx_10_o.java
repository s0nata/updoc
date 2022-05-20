  /**
   * Get the Integer at position {@code pos} in the array,
   *
   * @param pos  the position in the array
   * @return  the Integer, or null if a null value present
   * @throws java.lang.ClassCastException if the value cannot be converted to Integer
   */
  public Integer getInteger(int pos) {
    Number number = (Number)list.get(pos);
    if (number == null) {
      return null;
    } else if (number instanceof Integer) {
      return (Integer)number; // Avoids unnecessary unbox/box
    } else {
      return number.intValue();
    }
  }
  /**
   * This method returns the CSS style class that should be applied to
   * the LoggingEvent passed as parameter, which can be null.
   *
   * This information is currently used only by HTMLLayout.
   *
   * @param e null values are accepted
   * @return  the name of the conversion pattern
   */
  public String getStyleClass(Object e) {
    return style;
  }
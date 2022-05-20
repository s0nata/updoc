  /**
   * Query that the state is in a specific state
   * @param proposed proposed new state
   * @return the state
   */
  public boolean isInState(Service.STATE proposed) {
    return state.equals(proposed);
  }

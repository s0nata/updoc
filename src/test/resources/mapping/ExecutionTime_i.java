 /**
     * Provide nearest date for next execution.
	  * @param date - ZonedDateTime instance. If null, a NullPointerException will be raised.
     *  @return ZonedDateTime instance, never null. Next execution time.
     */


    public ZonedDateTime nextExecution(ZonedDateTime date) {
        Validate.notNull(date);
        try {
            ZonedDateTime nextMatch = nextClosestMatch(date);
            if(nextMatch.equals(date)){
                nextMatch = nextClosestMatch(date.plusSeconds(1));
            }
            return nextMatch;
        } catch (NoSuchValueException e) {
            throw new IllegalArgumentException(e);
        }
    }
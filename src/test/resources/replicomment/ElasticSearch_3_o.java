    /**
     * Sets the original query weight for rescoring. The default is <tt>1.0</tt>
     */
    public QueryRescorerBuilder setRescoreQueryWeight(float rescoreQueryWeight) {
        this.rescoreQueryWeight = rescoreQueryWeight;
        return this;
    }
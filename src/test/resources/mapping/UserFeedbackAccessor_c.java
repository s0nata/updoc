
  /**
     * Get user feedback for user
	 * @param username current user name
	 * @return user feedback list
     */
	 
	 
	 public interface UserFeedbackAccessor {
		 
	  @Query("select * from user_feedback where user_id = ?")
    Result<UserFeedback> getUserFeedback(String userId);

	 }
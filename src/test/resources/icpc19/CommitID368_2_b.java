/**
 * Get user feedback for user
 * @param userName current user name
 * @return user feedback list
 */
@Query("select * from user_feedback where user_id = ?")
Result<UserFeedback> getUserFeedback(String userId);
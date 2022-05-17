/**
 * Get user feedback for user
 * @param user_id user id
 * @return user feedback list
 */
@Query("select * from user_feedback where user_id = ?")
Result<UserFeedback> getUserFeedback(String userId);
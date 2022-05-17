/**
 * Create user feedback object
 * @param user_id
 * @param submitted
 * @param score
 * @param comments
 * @param contact_ok
 * @param user_email
 */
@Query("INSERT INTO user_feedback " +
		"(user_id, submitted, score, comments, contact_okay, user_email) " +
		"VALUES (?, ?, ?, ?, ?, ?)")
	void createUserFeedback(String user_id, Date submitted, int score, String comments, boolean contact_ok,
			String user_email);
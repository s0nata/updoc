/**
 * Returns the registered {@link ReportRenderer} whose class matches the passed class
 *
 * @param class - The ReportRenderer implementation class to retrieve
 * @return - The {@link ReportRenderer} that has been registered that matches the passed class
 */
@Transactional(readOnly=true)
public ReportRenderer getReportRenderer(Class<? extends ReportRenderer> clazz);
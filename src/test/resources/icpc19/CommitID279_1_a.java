/**
 * Returns the registered {@link ReportRenderer} whose class matches the passed class name
 *
 * @param class - The ReportRenderer implementation class to retrieve
 * @return - The {@link ReportRenderer} that has been registered that matches the passed class name
 */
@Transactional(readOnly=true)
public ReportRenderer getReportRenderer(String className);
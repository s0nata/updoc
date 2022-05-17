/**
 * Get the report renderer
 *
 * @param key
 * @return
 */
@Transactional(readOnly=true)
public ReportRenderer getReportRenderer(String className);
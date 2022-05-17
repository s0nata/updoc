/**
 * Escape the string for inserting into javascript code.
 * This automatically calls {@link #sanitizeForHtml} so make it safe for HTML too.
 *
 * @param string
 * @return the sanitized string or null (if the parameter was null).
 */
public static String sanitizeForJs(String str){
        if(str == null) return null;
        return Sanitizer.sanitizeForHtml(
        str.replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("'", "\\'")
        .replace("#", "\\#"));
        }

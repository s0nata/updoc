/**
 * Escape the string for inserting into javascript code.
 * This automatically calls {@link #escapeHTML} so make it safe for HTML too.
 */
public static String sanitizeForJs(String str){
        return Sanitizer.sanitizeForHtml(
        str.replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("'", "\\'")
        .replace("#", "\\#"));
        }
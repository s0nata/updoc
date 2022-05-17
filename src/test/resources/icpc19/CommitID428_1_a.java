/**
 * String representation with line and column number
 *
 * @return String version of this object
 */
@Override
public String toUserDebugString()
        {
        return " '" + toString() + "' (line " + getLineNumber() + ", column " + getColumnNumber() +
        ")";
        }
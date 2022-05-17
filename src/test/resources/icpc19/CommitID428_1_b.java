/**
 * Converts this object to a string representation.
 *
 * @return String version of this object
 */
@Override
public String toUserDebugString()
        {
        return " '" + toString() + "' (line " + getLineNumber() + ", column " + getColumnNumber() +
        ")";
        }
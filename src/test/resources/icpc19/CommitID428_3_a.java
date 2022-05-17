/**
 *
 * @param tag
 * @return true, if name is known
 */
private boolean isWellKnown(final ComponentTag tag)
        {
        for (String name : wellKnownTagNames)
        {
        if (xmlTag.getName().equalsIgnoreCase(name))
        if (tag.getName().equalsIgnoreCase(name))
        {
        return true;
        }
        }
        return false;
        }
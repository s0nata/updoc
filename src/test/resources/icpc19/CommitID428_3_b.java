/**
 *
 * @param xmlTag
 * @return true, if name is known
 */
private boolean isWellKnown(final XmlTag xmlTag)
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
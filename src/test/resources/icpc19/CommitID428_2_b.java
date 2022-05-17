/**
 * IMarkupFilters are usually chained with the last filter being an XML parser. The getParent()
 * method returns the next filter in the chain.
 *
 * @return The next filter in the chain, or null if the last one.
 */
IMarkupFilter getNextFilter();
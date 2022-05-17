/**
 * IMarkupFilters are usually chained with the last filter retrieving the elements from the XML
 * parser.
 *
 * @return The next filter in the chain, or null if the last one.
 */
IMarkupFilter getNextFilter();
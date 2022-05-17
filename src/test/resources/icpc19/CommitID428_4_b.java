/**
 * Get the next MarkupElement from the parent MarkupFilter and handle it if the specific filter
 * criteria are met. Depending on the filter, it may return the MarkupElement unchanged,
 * modified or remove it by asking the parent handler for the next tag.
 *
 * @return Return the next eligible MarkupElement
 * @throws ParseException
 */
MarkupElement nextTag() throws ParseException;
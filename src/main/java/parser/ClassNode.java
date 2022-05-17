package parser;

import org.apache.commons.lang3.Range;

/**
 * A base representation of a class AST node as a name and LOC range.
 */
public class ClassNode {

    private final String className;

    private final Range<Integer> LOCs;

    public ClassNode(String name, int LOCbgn, int LOCend) {

        this.className = name;
        this.LOCs = Range.between(LOCbgn, LOCend);

    }

    public String getClassName() {

        return this.className;
    }

    public Range<Integer> getLOC() {

        return this.LOCs;
    }

    public Integer getLOCSize() {

        return LOCs.getMaximum() - LOCs.getMinimum();
    }

    @Override
    public String toString() {
        return this.className + " has LOC range " + this.LOCs.toString();
    }
}

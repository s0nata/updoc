package parser;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import mapper.Identifier;
import org.apache.commons.lang3.Range;

import java.util.ArrayList;


/**
 * A base representation of a method AST node as Javaparser parses it.
 * Note: at the moment only signature is extracted.
 */
public class MethodNode {

    private final String simpleMethodName;

    private final StructuredSignature methodSignature;

    private final Range<Integer> LOC;

    private final StructuredComment docComment;

    public MethodNode(String mname, NodeList<Parameter> params,
                      Type returnType, NodeList<ReferenceType> thrownExceptions,
                      StructuredComment jdoc, int lBegin, int lEnd) {

        this.simpleMethodName = mname;

        this.methodSignature = new StructuredSignature(mname, params, returnType, thrownExceptions);

        this.LOC = Range.between(lBegin, lEnd);

        this.docComment = jdoc;
    }

    public MethodNode(String mname, NodeList<Parameter> params,
                      NodeList<ReferenceType> thrownExceptions,
                      StructuredComment jdoc, int lBegin, int lEnd) {

        this.simpleMethodName = mname;

        this.methodSignature = new StructuredSignature(mname, params, null, thrownExceptions);

        this.LOC = Range.between(lBegin, lEnd);

        this.docComment = jdoc;
    }

    public String getMethodName() {
        return this.simpleMethodName;
    }


    public StructuredSignature getMethodSignature() {
        return this.methodSignature;
    }

    public StructuredComment getDocComment() {
        return docComment;
    }


    public Identifier getReturnTypeAsIdentifier() {
        return this.methodSignature.getReturnTypeIdentifier();
    }


    public ArrayList<Identifier> getExceptionsAsIdentifiers() {
        return this.methodSignature.getExceptionsAsIdentifiers();
    }

    public String printLOCs() {

        return this.LOC.getMinimum() + " to " + this.LOC.getMaximum();
    }

    public Range<Integer> getLOCs() {

        return this.LOC;
    }

    @Override
    public String toString() {

        String ret = "|" + this.simpleMethodName;

        ret += "\n| signature: [ ";
        if (!this.methodSignature.getParametersTyped().isEmpty()) {

            for (Identifier par : this.methodSignature.getParametersTyped().keySet()) {
                // represent signature parameter as Name:Type string
                ret += par.toString() + ":" + this.methodSignature.getParametersTyped().get(par);
            }
        }
        ret += "]\n";

        return ret;
    }

}

package parser;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import mapper.Identifier;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * This class represents a parsed signature of a method.
 *
 * <p>Currently only method name and return type, parameters with their respective types, and thrown
 * exceptions are collected. Access modifiers, annotations and any other information is disregarded.
 */
public class StructuredSignature {

    private final Identifier methodName;

    private final Identifier returnType;

    private final ArrayList<Identifier> thrownExceptionTypes;

    private final Hashtable<Identifier, Identifier> parametersTyped;

    public StructuredSignature(
            String mname,
            NodeList<Parameter> params,
            Type returnType,
            NodeList<ReferenceType> thrownExceptions) {

        this.methodName = new Identifier(mname, Identifier.KindOfID.METHOD_NAME);

        if (returnType != null) {
            this.returnType = new Identifier(returnType.asString(), Identifier.KindOfID.TYPE_NAME);
        } else {
            // Constructor: no return type
            // TODO check this
            this.returnType = new Identifier("constructor", Identifier.KindOfID.TYPE_NAME);
        }

        this.thrownExceptionTypes = new ArrayList<>();
        for (ReferenceType exception : thrownExceptions) {
            thrownExceptionTypes.add(new Identifier(exception.asString(), Identifier.KindOfID.TYPE_NAME));
        }

        this.parametersTyped = new Hashtable<>();
        for (Parameter parameter : params) {
            assignParameters(parameter);
        }
    }

    private void assignParameters(Parameter parameter) {
        String typeExtraInfo = "";
        if (parameter.getType().isArrayType()) {
            typeExtraInfo = "array";
        }
        parametersTyped.put(
                new Identifier(parameter.getNameAsString(), Identifier.KindOfID.VAR_NAME),
                new Identifier(parameter.getTypeAsString() + typeExtraInfo, Identifier.KindOfID.TYPE_NAME));
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof StructuredSignature)) {
            return false;
        }

        StructuredSignature other = (StructuredSignature) obj;

        boolean isEqual =
                this.getMethodName() == other.getMethodName()
                        && this.getReturnTypeIdentifier() == other.getReturnTypeIdentifier();

        for (Identifier this_id : this.getExceptionsAsIdentifiers()) {
            isEqual &= other.getExceptionsAsIdentifiers().contains(this_id);
        }

        for (Map.Entry<Identifier, Identifier> e : this.getParametersTyped().entrySet()) {
            Identifier id1 = e.getKey();
            Identifier id2 = e.getValue();
            isEqual &= other.getParametersTyped().containsKey(id1);
            isEqual &= other.getParametersTyped().containsValue(id2);
        }

        return isEqual;
    }

    public Identifier getMethodName() {
        return methodName;
    }

    public Identifier getReturnTypeIdentifier() {
        return returnType;
    }

    public ArrayList<Identifier> getExceptionsAsIdentifiers() {
        return thrownExceptionTypes;
    }

    public Hashtable<Identifier, Identifier> getParametersTyped() {
        return parametersTyped;
    }

    @Override
    public String toString() {
        return "StructuredSignature{"
                + "methodName="
                + methodName
                + ", returnType="
                + returnType
                + ", thrownExceptionTypes="
                + thrownExceptionTypes
                + ", parametersTyped="
                + parametersTyped
                + '}';
    }
}

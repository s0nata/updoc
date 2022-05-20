package parser;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import mapper.Identifier;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * This class represents a parsed signature of a method.
 * <p>
 * Currently only method name and return type, parameters with their respective types, and thrown
 * exceptions are collected. Access modifiers, annotations and any other information is disregarded.
 */
public class StructuredSignature {

    private final Identifier methodName;

    private final Identifier returnType;

    private final ArrayList<Identifier> thrownExceptionTypes;

    private final Hashtable<Identifier, Identifier> parametersTyped;

    public StructuredSignature(String mname, NodeList<Parameter> params,
                               Type returnType, NodeList<ReferenceType> thrownExceptions) {

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
            // TODO: enhance identifier with relevant key words (as in Toradocu)?
            // TODO the following is an example of relevant keyword, but let's add more with caution
            // TODO (according to how much they disturb the mapping)
            assignParameters(parameter);
        }
    }

    /**
     * yet another special constructor that creates a structured signature from a
     * parameter name, to fairly and smartly compare @param clones against them
     * <p>
     * Useful from RepliComment csv output parsing
     *
     * @param pName the param name, as a String
     * @param pType the param type, as String
     */
    public StructuredSignature(String pName, String pType) {

        this.methodName = new Identifier(pName, Identifier.KindOfID.VAR_NAME);

        // Obviously not really a return type.
        this.returnType = new Identifier(pType, Identifier.KindOfID.TYPE_NAME);

        // Ignore the rest.
        this.thrownExceptionTypes = new ArrayList<>();
        this.parametersTyped = new Hashtable<>();
    }

    /**
     * Special constructor that creates a structured signature from a
     * complete signature string in the form of:
     * return_type-method_name-([parameter type, parameter name])
     * <p>
     * Useful from RepliComment csv output parsing
     *
     * @param completeSignature the complete signature, as a String
     */
    public StructuredSignature(String completeSignature) {

        String firstPart = completeSignature.substring(0, completeSignature.indexOf("("));
        String thrownExceptions = completeSignature.substring(completeSignature.indexOf(")") + 1);

        String[] typeAndName = firstPart.split(" ");
        String returnType = typeAndName[0];
        String mname = typeAndName[1];

        String[] thrownList = thrownExceptions.split(",");

        this.methodName = new Identifier(mname, Identifier.KindOfID.METHOD_NAME);

        if (!returnType.isEmpty()) {
            this.returnType = new Identifier(returnType, Identifier.KindOfID.TYPE_NAME);
        } else {
            // Constructor: no return type
            // TODO check this
            this.returnType = new Identifier("constructor", Identifier.KindOfID.TYPE_NAME);
        }

        this.thrownExceptionTypes = new ArrayList<>();
        for (String exception : thrownList) {
            thrownExceptionTypes.add(new Identifier(exception, Identifier.KindOfID.TYPE_NAME));
        }

        this.parametersTyped = new Hashtable<>();

        String parameterList = completeSignature.substring(completeSignature.indexOf("(") + 1,
                completeSignature.indexOf(")"));

        if (!parameterList.isEmpty()) {
            String[] paramList = parameterList.split(",");
            for (String parameter : paramList) {
                // TODO: enhance identifier with relevant key words (as in Toradocu)?
                // TODO the following is an example of relevant keyword, but let's add more with caution
                // TODO (according to how much they disturb the mapping)
                String[] paramParts = parameter.trim().split(" ");
                assignParameters(paramParts[0], paramParts[1]);
            }
        }
    }

    private void assignParameters(Parameter parameter) {
        String typeExtraInfo = "";
        if (parameter.getType().isArrayType()) {
            typeExtraInfo = "array";
        }
        parametersTyped.put(
                new Identifier(parameter.getNameAsString(), Identifier.KindOfID.VAR_NAME),
                new Identifier(parameter.getTypeAsString() + typeExtraInfo, Identifier.KindOfID.TYPE_NAME)
        );
    }

    private void assignParameters(String paramType, String paramName) {
        String typeExtraInfo = "";
        if (paramType.contains("[")) {
            typeExtraInfo = "array";
        }
        parametersTyped.put(
                new Identifier(paramName, Identifier.KindOfID.VAR_NAME),
                new Identifier(paramType + typeExtraInfo, Identifier.KindOfID.TYPE_NAME)
        );
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
}

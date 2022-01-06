package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

import java.util.ArrayList;
import java.util.List;

public class AssignableArrayField extends AssignableCode implements AssignableOpenBracketType {

    private PersonalityMatrix matrix;
    private int codeindex;
    private Integer[] fields;

    public AssignableArrayField(PersonalityMatrix matrix, String name, int codeindex) {
        super(name);
        this.matrix = matrix;
        this.codeindex = codeindex;
    }


    public Integer[] getFields() {
        if (fields != null)
            return fields;
        List<Integer> indexes = new ArrayList<>();
        int requiredBrackets = 1;
        for (int i = codeindex + 1; i < matrix.getCode().length; i++) {
            if (matrix.getCode()[i] instanceof AssignableCloseBracket) {
                requiredBrackets--;
            }
            if (matrix.getCode()[i] instanceof AssignableOpenBracketType) {
                requiredBrackets++;
            }
            if (requiredBrackets <= 0)
                break;
            if (matrix.getCode()[i] instanceof AssignableField) {
                indexes.add(i);
            }
            if (matrix.getCode()[i] instanceof AssignableRef) {
                int j = ((AssignableRef) matrix.getCode()[i]).getRef();
                if (matrix.getCode().length > j && j > 0) {
                    if (matrix.getCode()[j] instanceof AssignableField) {
                        indexes.add(j);
                    }
                }
            }

        }
        return fields = indexes.toArray(new Integer[indexes.size()]);

    }

    public int getObjectInstanceAt(int index) {
        if (getFields()[index] < matrix.getCode().length) {
            if (matrix.getCode()[getFields()[index]] instanceof AssignableField)
                return ((AssignableField) matrix.getCode()[getFields()[index]]).getObjectInstance();
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("ARRAY (" + getName());
        int requiredBrackets = 1;
        for (int i = codeindex + 1; i < matrix.getCode().length; i++) {
            if (matrix.getCode()[i] instanceof AssignableCloseBracket) {
                requiredBrackets--;
            }
            if (matrix.getCode()[i] instanceof AssignableOpenBracketType) {
                requiredBrackets++;
            }
            if (requiredBrackets <= 0)
                break;
            if (matrix.getCode()[i] instanceof AssignableField) {
                s.append("," + i);
            }
        }
        s.append(") {");
        return s.toString();
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableArrayField(matrix, getName(), codeindex);
    }

    public int getArrayLength() {
        return getFields().length;
    }
}

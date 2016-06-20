package sg.edu.nus.comp.codis.ast;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Equal when the indexes and the subvariables are equal
 */
public class Indexed extends Variable {

    private Variable variable;

    private int index;

    public Type getType() {
        return variable.getType();
    }

    @Override
    public boolean isTestInstantiable() {
        return variable.isTestInstantiable();
    }

    public Indexed(Variable variable, int index) {
        this.variable = variable;
        this.index = index;
    }

    @Override
    public void accept(BottomUpVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(TopDownVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Indexed))
            return false;
        if (obj == this)
            return true;

        Indexed rhs = (Indexed) obj;
        return new EqualsBuilder().
                append(variable, rhs.variable).
                append(index, rhs.index).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(variable).
                append(index).
                toHashCode();
    }


    @Override
    public String toString() {
        return variable + "_" + index;
    }

}

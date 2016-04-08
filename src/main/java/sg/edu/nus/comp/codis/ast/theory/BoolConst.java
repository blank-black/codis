package sg.edu.nus.comp.codis.ast.theory;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import sg.edu.nus.comp.codis.ast.BottomUpVisitor;
import sg.edu.nus.comp.codis.ast.Constant;
import sg.edu.nus.comp.codis.ast.TopDownVisitor;

/**
 * Created by Sergey Mechtaev on 7/4/2016.
 */
public class BoolConst extends Constant {
    private boolean value;

    public static BoolConst TRUE = new BoolConst(true);

    public static BoolConst FALSE = new BoolConst(false);

    public static BoolConst of(boolean value) {
        return new BoolConst(value);
    }

    public boolean getValue() {
        return value;
    }

    private BoolConst(boolean value) {
        this.value = value;
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
        if (!(obj instanceof BoolConst))
            return false;
        if (obj == this)
            return true;

        BoolConst rhs = (BoolConst) obj;
        return new EqualsBuilder().
                append(value, rhs.value).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(value).
                toHashCode();
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

}

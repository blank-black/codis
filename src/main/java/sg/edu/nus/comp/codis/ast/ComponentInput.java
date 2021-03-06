package sg.edu.nus.comp.codis.ast;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Variable used in CBS. Test-instantiated.
 */
public class ComponentInput extends Variable {

    private Component component;

    private Hole hole;

    public ComponentInput(Component component, Hole hole) {
        this.component = component;
        this.hole = hole;
    }

    public Component getComponent() {
        return component;
    }

    public Hole getHole() {
        return hole;
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
    public void accept(BottomUpMemoVisitor visitor) {
        if (visitor.alreadyVisited(this)) {
            visitor.visitAgain(this);
        } else {
            visitor.visit(this);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ComponentInput))
            return false;
        if (obj == this)
            return true;

        ComponentInput rhs = (ComponentInput) obj;
        return new EqualsBuilder().
                append(component, rhs.component).
                append(hole, rhs.hole).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(component).
                append(hole).
                toHashCode();
    }

    @Override
    public String toString() {
        return "ci(" + hole + "->" + component + ")";
    }

    @Override
    public Type getType() {
        return hole.getType();
    }

    @Override
    public boolean isTestInstantiable() {
        return true;
    }
}

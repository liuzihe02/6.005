
package expressivo;

/**
 * An immutable data type representing a variable
 * 
 */
public class Variable implements Expression {

    // Abstraction function
    //    AF(name) = a variable with a name

    // Rep invariant
    //    name != ""
    //    cannot have spaces

    // Safety from rep exposure
    //    name field is private, final and immutable

    private final String name;

    private void checkRep() {
        assert name != "";
        assert !name.matches(".*\\s.*");
    }

    public Variable(String name) {
        this.name = name;
        checkRep();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object thatObject) {
        //check if even the right type
        if (!(thatObject instanceof Variable)) {
            return false;
        } else {
            Variable that = (Variable) thatObject;
            // string is an object, not a primitive type; must use equals comparioson!
            return this.name.equals(that.name);
        }
    }

    /**
     * @return hash code value consistent with the equals() definition of structural
     *         equality, such that for all e1,e2:Expression,
     *         e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    //implement partial derivative rules
    @Override
    public Expression differentiate(Variable V) {
        if (this.equals(V)) {
            return new Number(1.0);
        } else {
            return new Number(0.0);
        }
    }

}

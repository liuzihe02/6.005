
package expressivo;

import java.util.Map;

/**
 * An immutable data type representing a number
 * 
 * stores the number as a double!
 * 
 */
public class Number implements Expression {

    // Abstraction function
    // represents the non-negative integer or floating point number n

    // Rep invariant
    // value>0

    // Safety from rep exposure
    // value is immutable,private,final

    private final Double value;

    private void checkRep() {
        assert value >= 0;
    }

    /**
     * Make a Number, then check rep. Since this object is not mutable; we only need to check rep at construction.
     * 
     * @param n value of a Number
     */
    public Number(Double n) {
        this.value = n;
        checkRep();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object thatObject) {
        //check if even the write type
        if (!(thatObject instanceof Number)) {
            return false;
        }
        //
        else {
            Number that = (Number) thatObject;
            return this.getValue().doubleValue() == that.getValue().doubleValue();
        }
    }

    /**
     * @return hash code value consistent with the equals() definition of structural
     *         equality, such that for all e1,e2:Expression,
     *         e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    };

    // More instance methods, those with override are in the interface

    @Override
    public Expression differentiate(Variable V) {
        return new Number(0.0);
    }

    @Override
    public Expression simplify(Map<Variable, Number> environment) {
        // a number is already in its simplest form! no need to do anything special
        return this;
    };

    @Override
    public boolean isNumber() {
        return true;
    };

    /**
     * Get value of this expression. This method is ONLY FOR NUMBERS!
     * only numbers have values
     * 
     * @return value of this expression, it must be a Number expression
     */
    public Double getValue() {
        return this.value;
    }

}

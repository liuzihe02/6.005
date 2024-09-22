
package expressivo;

/**
 * An immutable data type representing the result of an multiplication (times) operation
 * basically the same as Plus
 * only simplify and differentiate has stuff related the * operator
 * 
 * note that structural equality means that 1*x is not the same as x*1 even though they are mathematically equivalent
 * also (1*(x*1)) not the same as ((1*x)*1) due to the grouping condition
 * 
 */
public class Times implements Expression {

    // Abstraction function
    //    AF(left, right) = the expression left*right

    // Rep invariant
    //    true

    // Safety from rep exposure
    //    all fields are private, final and immutable

    private final Expression left;
    private final Expression right;

    private void checkRep() {
        assert left != null;
        assert right != null;
    }

    /** Make a Times which is the product of left and right. */
    public Times(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        checkRep();
    }

    @Override
    public String toString() {
        //recursive representation
        return "(" + left.toString() + " * " + right.toString() + ")";
    }

    @Override
    public boolean equals(Object thatObject) {
        //check if even the right type
        if (!(thatObject instanceof Times)) {
            return false;
        } else {
            Times that = (Times) thatObject;
            // if that is a Times object, suddenly the private fields are visible!
            return this.left.equals(that.left) && this.right.equals(that.right);
        }
    }

    @Override
    public int hashCode() {
        //just add the two hash codes together
        return 2 * this.left.hashCode() + this.right.hashCode();
    }

}

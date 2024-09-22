
package expressivo;

/**
 * An immutable data type representing the result of an addition operation
 * only simplify and differentiate have methods related to the + operator
 * 
 * order matters! so 1+2 is not the same as 2+1
 * structural equality requires the same order and same grouping
 * also (1+(x+1)) not the same as ((1+x)+1) due to the grouping condition
 * 
 */
public class Plus implements Expression {

    // Abstraction function
    //    AF(left, right) = the expression left+right

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

    /** Make a Plus which is the sum of left and right. */
    public Plus(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        checkRep();
    }

    @Override
    public String toString() {
        //recursive representation
        return "(" + left.toString() + " + " + right.toString() + ")";
    }

    @Override
    public boolean equals(Object thatObject) {
        //check if even the right type
        if (!(thatObject instanceof Plus)) {
            return false;
        } else {
            Plus that = (Plus) thatObject;
            return (this.left.equals(that.left) && this.right.equals(that.right));
        }
    }

    /**
     * @return hash code value consistent with the equals() definition of structural
     *         equality, such that for all e1,e2:Expression,
     *         e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode() {
        //cannot allow simple addition or multiplication; this will not break the symmetry!
        //need to make it assymmetrical
        return 2 * this.left.hashCode() + this.right.hashCode();
    }

    @Override
    public Expression differentiate(Variable V) {
        //differentiate wrt each and return the Plus object
        return new Plus(left.differentiate(V), right.differentiate(V));
    }

}

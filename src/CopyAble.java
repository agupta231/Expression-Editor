public interface CopyAble {
    /**
     * Will create a copy of the current expression, and transfer
     * the Node pointers as well
     * @return a copy of Expression with old Node pointers
     */
    Expression trueCopy();

    /**
     * Will generate a String representation for the operator, with no
     * space though.
     * @return a String containing this expresison.
     */
    String convertToStringFlat();
}

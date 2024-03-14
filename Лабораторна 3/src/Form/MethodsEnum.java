package Form;

public enum MethodsEnum {
    NONE("None"),
    GAUSSIAN_ELIMINATION("Gaussian elimination"),
    JACOBI_METHOD("Jacobi iterative method");

    private final String name;
    MethodsEnum(final String text) {
        this.name = text;
    }

    @Override
    public String toString() {
        return name;
    }
}

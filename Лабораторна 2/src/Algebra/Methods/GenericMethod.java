package Algebra.Methods;

import Algebra.Generic.Matrix;
import Algebra.Generic.Vector;

public interface GenericMethod {
    public Vector getSolution();

    public double relativeError();
}

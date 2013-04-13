package com.guseggert.sensorlogger.feature;

/*************************************************************************
 *  Data type for complex numbers.
 *
 *  The data type is "immutable" so once you create and initialize
 *  a Complex object, you cannot change it. The "final" keyword
 *  when declaring re and im enforces this rule, making it a
 *  compile-time error to change the .re or .im fields after
 *  they've been initialized.
 *  
 *************************************************************************/

public class Complex {
    private final double re;   // the real part
    private final double im;   // the imaginary part
    private final double precision =100000.0d;
    
    /**
     * 
     * @param real The real part of the complex number
     * @param imag The imaginary part of the complex number
     */
    public Complex(double real, double imag) {
    	im = imag;
    	re = real;
    }

    /**
     * @return A string representation of the invoking Complex object
     */
    public String toString() {
    	double im = ((int)(this.im * precision)) / precision;
        double re = ((int)(this.re * precision)) / precision;
        
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    /**
     * 
     * @return abs/modulus/magnitude
     */
    public double abs()   { return Math.hypot(re, im); }  // Math.sqrt(re*re + im*im)
    
    /**
     * 
     * @return angle/phase/argument
     */
    public double phase() { return Math.atan2(im, re); }  // between -pi and pi

    /**
     * Adds a Complex to this Complex 
     * 
     * @param the complex number to be added
     * @return A new Complex object whose value is (this + b)
     */
    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    /**
     * Subtracts a Complex from this Complex
     * 
     * @param b The complex number to be subtracted
     * @return A new Complex object whose value is (this - b)
     */
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    /**
     * Multiplies this Complex with the Complex passed as argument
     * 
     * @param b The complex number for the multiplication
     * @return A new Complex object whose value is (this * b)
     */
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    /**
     * Scalar multiplication
     * 
     * @param alpha a real number to be multiplied with the complex number
     * @return A new Complex object whose value is (this * alpha)
     */
    public Complex times(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    /**
     * 
     * @return A new Complex object whose value is the conjugate of this
     */
    public Complex conjugate() {  return new Complex(re, -im); }

    /**
     * 
     * @return A new Complex object whose value is the reciprocal of this
     */
    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    /**
     * 
     *@return The real part of the complex number
     */
    public double re() { return re; }
    
    /**
     * 
     * @return The imaginary part of the complex number
     */
    public double im() { return im; }

    /**
     * Divides this object by another Complex object
     * 
     * @param b The complex object that this object will be divided by
     * @return this/b
     */
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    /**
     * 
     * @return A new Complex object whose value is the complex exponential of this
     */
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    /**
     * @return a new Complex object whose value is the complex sine of this
     * 
     */
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    /**
     * @return a new Complex object whose value is the complex cosine of this
     * 
     */
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    /**
     * @return a new Complex object whose value is the complex tangent of this
     * 
     */
    public Complex tan() {
        return sin().divides(cos());
    }
    


    /**
     * Adds two Complex objects
     * 
     * @param a The first addend
     * @param b The second addend
     * 
     * @return A Complex object that is the sum of the two addends
     */
    public static Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }
    
}
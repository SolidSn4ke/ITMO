package math;

public class Complex {
    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex add(Complex c) {
        return new Complex(this.re + c.re, this.im + c.im);
    }

    public Complex mul(Complex c) {
        return new Complex(this.re * c.re - this.im * c.im, this.im * c.re + this.re * c.im);
    }

    public double abs() {
        return Math.sqrt(Math.pow(this.re, 2) + Math.pow(this.im, 2));
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }
}

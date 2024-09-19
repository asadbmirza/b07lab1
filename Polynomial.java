class Polynomial {
    double[] coefficients;

    public Polynomial() {
        coefficients = new double[1];
        coefficients[0] = 0;
    }

    public Polynomial(double array[]) {
        coefficients = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            coefficients[i] = array[i];
        };
    }

    public Polynomial add(Polynomial other) {
        Polynomial returnPolynomial;
        double[] addPolynomial;
        int min = other.coefficients.length;
        if (min > coefficients.length) {
            min = coefficients.length;
            returnPolynomial = new Polynomial(other.coefficients);
            addPolynomial = coefficients;
        } 
        else {
            returnPolynomial = new Polynomial(coefficients);
            addPolynomial = other.coefficients;
        }
         
        for (int i = 0; i < min; i++) {
            returnPolynomial.coefficients[i] += addPolynomial[i];
        }


        return returnPolynomial;
    }

    public double evaluate(double x) {
        double evaluation = 0;
        double multiplier = 1;
        for (int i = 0; i < coefficients.length; i ++) {
            multiplier = 1;
            for (int j = 0; j < i; j++) {
                multiplier = multiplier*x;
            }
            evaluation += coefficients[i]*multiplier;
        
        }
        
        return evaluation;
    }

    public boolean hasRoot(double x) {
        double result = evaluate(x);
        return (result == 0);
    }


}
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

class Polynomial {
    double[] coefficients;
    int[] exponents;

    public Polynomial() {
        coefficients = new double[1];
        exponents = new int[1];
        coefficients[0] = 0;
        exponents[0] = 1;
    }

    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = new double[coefficients.length];
        this.exponents = new int[exponents.length];

        for (int i = 0; i < coefficients.length; i++) {
            this.coefficients[i] = coefficients[i];
            this.exponents[i] = exponents[i];
        };
        sort();
    }

    public Polynomial(File file) {
        try {
            Scanner input = new Scanner(file);
            String s = input.nextLine();
            if (s.length() != 0 && (s.charAt(0) != '+' && s.charAt(0) != '-')) {
                s = "+".concat(s);
            }
            String[] polynomial = s.split("(?=[+-])");
            
            this.coefficients = new double[polynomial.length];
            this.exponents = new int[polynomial.length];

            String[] temp;
            for (int i = 0; i < polynomial.length; i++) {
                temp = polynomial[i].split("[x]");
               
                if (temp[0].equals("+") || temp[0].equals("-")) {
                    temp[0] = temp[0].concat("1");
                }
                this.coefficients[i] = Double.parseDouble(temp[0]);
                if (polynomial[i].charAt(polynomial[i].length() - 1) == 'x') {
                    this.exponents[i] = 1;
                }
                else if (temp.length == 1) {
                    this.exponents[i] = 0;
                }
                else {
                    this.exponents[i] = Integer.parseInt(temp[1]);
                }
            }
            input.close();
            sort();
            
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        }
    }

    private void sort() {
        int n = this.exponents.length;

        for (int i = 1; i < n; i++) {
            int keyExponent = exponents[i];
            double keyCoefficient = coefficients[i];
            int j = i - 1;

            while (j >= 0 && exponents[j] > keyExponent) {
                exponents[j + 1] = exponents[j];
                coefficients[j + 1] = coefficients[j];
                j = j - 1;
            }
            exponents[j + 1] = keyExponent;
            coefficients[j + 1] = keyCoefficient;
        }
    }

    private static void shift(double arr[], int start) {
        for (int i  = start; i < arr.length - 1; i++ ) {
            arr[i] = arr[i+1];
        }
    
    }

    private static void shift(int arr[], int start) {
        for (int i  = start; i < arr.length - 1; i++ ) {
            arr[i] = arr[i+1];
        }
    
    }

    private static Polynomial strip(Polynomial trailing) {
        //Count real indices
        int count = 0;
        for (int i = 0; i < trailing.exponents.length; i++) {
            if (trailing.coefficients[i] == 0) {
                shift(trailing.coefficients, i);
                shift(trailing.exponents, i);
                
            }
            else {
                count++;
            }
        }

        int exponents[] = new int[count];
        double coefficients[] = new double[count];
        for (int i = 0; i < count; i++) {
            exponents[i] = trailing.exponents[i];
            coefficients[i] = trailing.coefficients[i];
        }

        return new Polynomial(coefficients, exponents);

    
    }

    public Polynomial add(Polynomial other) {
        //[1,3,4] + [2,4,6]
        //[1,2,3,4,6]
        Polynomial returnPolynomial;
        double[] addPolynomial = new double[other.exponents.length + this.exponents.length];
        int[] addExponents = new int[other.exponents.length + this.exponents.length];
        System.out.println(Arrays.toString(coefficients));
        System.out.println(Arrays.toString(exponents));
        System.out.println(Arrays.toString(other.coefficients));
        System.out.println(Arrays.toString(other.exponents));
        
        int i = 0;
        int j = 0;
        int count = 0;

        while (i < other.exponents.length && j < this.exponents.length) {
            if (other.exponents[i] < this.exponents[j]) {
                addExponents[count] = other.exponents[i];
                addPolynomial[count] = other.coefficients[i];
                i++;
            }
            else if (other.exponents[i] > this.exponents[j]){
                addExponents[count] = this.exponents[j];
                addPolynomial[count] = this.coefficients[j];
                j++;
            }
            //case where equal
            else if (other.exponents[i] == this.exponents[j]) {
                addPolynomial[count] = other.coefficients[i] + this.coefficients[j];
                addExponents[count] = this.exponents[j];
                i++;
                j++;
            }
            count++;
        }

        while (i < other.exponents.length) {
            addExponents[count] = other.exponents[i];
            addPolynomial[count] = other.coefficients[i];
            i++;
            count++;
        }
        while (j < this.exponents.length) {
            addExponents[count] = this.exponents[j];
            addPolynomial[count] = this.coefficients[j];
            j++;
            count++;
        }
        returnPolynomial = new Polynomial(Arrays.copyOf(addPolynomial, count), Arrays.copyOf(addExponents, count));

        return strip(returnPolynomial);
    }

    public Polynomial multiply(Polynomial other) {
        Polynomial product;
        Polynomial[] foils = new Polynomial[this.exponents.length];
        double[] coefficients = new double[other.coefficients.length];
        int[] exponents = new int[other.exponents.length];

        for (int i = 0; i < this.exponents.length; i++) {
            for (int j = 0; j < other.exponents.length; j++) {
                exponents[j] = other.exponents[j] + this.exponents[i];
                coefficients[j] = other.coefficients[j] * this.coefficients[i];
            }

            foils[i] = new Polynomial(coefficients, exponents);
        }

        product = foils[0];
        for (int i = 1; i < this.exponents.length; i++) {
            product = product.add(foils[i]);
        }

        return product;
    
    }

    private double multiplier(double x, int exponent) {
        double multiplied = 1;
        for (int i = 0; i < exponent; i++) {
            multiplied = multiplied * x;
        };

        return multiplied;
    }

    public double evaluate(double x) {
        double evaluation = 0;
        double multiplied = 1;
        for (int i = 0; i < this.exponents.length; i++) {
            multiplied = multiplier(x, this.exponents[i]);
            evaluation += coefficients[i]*multiplied;
        
        }
        
        return evaluation;
    }

    public boolean hasRoot(double x) {
        double result = evaluate(x);
        return (result == 0);
    }

    public void saveToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            System.out.println(Arrays.toString(coefficients));
            System.out.println(Arrays.toString(exponents));
            String s = "";
            String cAdd;
            String eAdd;
            for(int i = 0; i < coefficients.length;i++) {
                cAdd = "" + coefficients[i];
                eAdd = "x" + exponents[i];
                System.out.println(cAdd);
                if (cAdd.equals("-1.0")) {
                    cAdd = "-";
                }
                else if (cAdd.charAt(0) != '-') {
                    if (cAdd.equals("1.0") && !eAdd.equals("x0")) {
                        cAdd = "+";
                        if (i == 0) cAdd = "";
                    }
                    else {
                        if (i != 0) cAdd = "+" + cAdd;
                    }
                }
                if (eAdd.equals("x1")) {
                    eAdd = "x";
                }else if (eAdd.equals("x0")) {
                    eAdd = "";
                }
                s = s + cAdd + eAdd; 
            }
            writer.write(s);

        }
        catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
    }

}
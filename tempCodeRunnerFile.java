omial p = new Polynomial();
        System.out.println(p.evaluate(3));
        double [] c1 = {6,0,0,5};
        Polynomial p1 = new Polynomial(c1);
        double [] c2 = {0,-2,0,0,-9};
        Polynomial p2 = new Polynomial(c2);
        Polynomial s = p1.add(p2);
        System.out.println("s(0.1) = " + s.evaluate(0.1));
        if(s.hasRoot(1))
        System.out.println("1 is a root of s");
        else
        System.out.println("1 is not a root of s");

        
        double c3 [] = {6, -2, 0, 5};
        Polynomial p3 = new Polynomial(c3);
        System.out.println("Evaluating -1 " + p3.evaluate(-1));
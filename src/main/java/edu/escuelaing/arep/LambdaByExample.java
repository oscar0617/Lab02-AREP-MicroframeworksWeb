package edu.escuelaing.arep;

public class LambdaByExample {

    public static void main(String[] args) {
        // FunctionNoParam f = new FunctionNoParam(){
        //      public String execute(){
        //         return "Hello world";
        //     }
        // };

        FunctionNoParam<Double> fPi = () -> Math.PI;

        System.out.println("Function execution: " + fPi.execute());

        FunctionNoParam<String> f = () -> "Hello World!";

        System.out.println("Function execution: " + f.execute());
        
        FunctionOneParameter<Integer, String> size = (str) -> str.length();

        System.out.println("Function Execution: " + size.execute("Hola Mundo"));

        FunctionOneParameter<Double, Double> sin = (angulo) -> Math.sin(angulo);
        
        System.out.println("Function Execution: " + sin.execute(1.5));
    }
}

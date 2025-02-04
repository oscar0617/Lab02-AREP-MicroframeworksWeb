package edu.escuelaing.arep;

import static edu.escuelaing.arep.HttpServer.staticfiles;

import java.io.IOException;
import java.net.URISyntaxException;

import static edu.escuelaing.arep.HttpServer.get;

public class WebApplication {
    public static void main(String[] args) throws IOException, URISyntaxException {
        staticfiles("/www");

        get("/hello", (req, resp) -> "Hello " + req.getValues("name"));
        
        get("/hellopost", (req, resp) -> "Hello " + req.getValues("name"));

        get("/pi", (req, resp) -> {
            return String.valueOf(Math.PI); 
        });

        get("/euler", (req, resp) -> {
            return String.valueOf(Math.E); 
        });
        try{
            System.out.println("Listo para recibir...");
            HttpServer.start(args);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

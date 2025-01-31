package edu.escuelaing.arep;

import static edu.escuelaing.arep.HttpServer.staticfiles;

import java.io.IOException;
import java.net.URISyntaxException;

import static edu.escuelaing.arep.HttpServer.get;

public class WebApplication {
    public static void main(String[] args) throws IOException, URISyntaxException {
        staticfiles("/webroot");
        // get("/hello", (req, resp) -> "Hello " + req.getValues("name"));

        get("/pi", () -> {
            return String.valueOf(Math.PI); 
        });

        HttpServer.start(args);
    }
}

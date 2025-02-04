package edu.escuelaing.arep;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.io.*;

public class HttpServer {

    private static Map<String, BiFunction<HttpRequest, HttpResponse, String>> servicios = new HashMap<>();
    private static String staticFilePath = "www";

    public static void start(String[] args) throws IOException, URISyntaxException {
        int port = 8080;
        startServer(port);
    }

    private static void startServer(int port) throws IOException, URISyntaxException {
        ServerSocket serverSocket = new ServerSocket(port);
        boolean running = true;
    
        while (running) {
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    
            String inputLine;
            boolean isFirstLine = true;
            String file = "";
            String method = ""; // Para capturar si es GET o POST
            boolean responded = false;
    
            while ((inputLine = in.readLine()) != null) {
                if (isFirstLine) {
                    String[] requestParts = inputLine.split(" ");
                    method = requestParts[0]; // GET o POST
                    file = requestParts[1]; // Ruta solicitada
                    isFirstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }
            URI resourceUri = new URI(file);
            String path = resourceUri.getPath();
    
            System.out.println("Requested method: " + method);
            System.out.println("Requested path: " + path);
    
            // Procesar rutas dinámicas bajo /app/ con get()
            if (path.startsWith("/app/") && !responded) {
                HttpRequest req = new HttpRequest(path, resourceUri.getQuery());
                HttpResponse resp = new HttpResponse();
                String outputLine = processRequest(req, resp);
                out.println(outputLine);
                responded = true;
            }
    
            // Servir archivos estáticos si no se ha respondido
            if (!responded) {
                if (path.equals("/") || path.isEmpty()) {
                    path = "/index.html";
                }
                String filePath = staticFilePath + path;
                serveFile(out, filePath);
            }
    
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    static String processRequest(HttpRequest req, HttpResponse resp) {
        BiFunction<HttpRequest, HttpResponse, String> s = servicios.get(req.getPath());
        if (s == null) {
            return "HTTP/1.1 404 Not Found\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + "<h1>404 Not Found</h1>";
        }
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n"
                + "\r\n"
                + s.apply(req, resp);
    }
    

    static void serveFile(PrintWriter out, String filePath) {
        try {
            File file = new File(filePath).getCanonicalFile();
            if (!file.exists() || file.isDirectory()) {
                System.out.println("File not found: " + filePath);
                out.println("HTTP/1.1 404 Not Found");
                out.println("Content-Type: text/html");
                out.println();
                out.println("<h1>404 File Not Found</h1>");
                return;
            }
            String contentType = getContentType(file.getName());
            FileInputStream fileStream = new FileInputStream(file);
            byte[] fileContent = fileStream.readAllBytes();
            fileStream.close();
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + contentType);
            out.println();
            out.write(new String(fileContent));
    
            System.out.println("Serving file: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error serving file: " + filePath);
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-Type: text/html");
            out.println();
            out.println("<h1>404 File Not Found</h1>");
        }
    }
    
    static String getContentType(String filePath) {
        if (filePath.endsWith(".html"))
            return "text/html";
        if (filePath.endsWith(".css"))
            return "text/css";
        if (filePath.endsWith(".js"))
            return "application/javascript";
        if (filePath.endsWith(".png"))
            return "image/png";
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg"))
            return "image/jpeg";
        if (filePath.endsWith(".gif"))
            return "image/gif";
        return "text/plain";
    }

    public static void staticfiles(String path) {
        try {
            staticFilePath = new File("src/main/java/edu/escuelaing/arep" + path).getCanonicalPath();
        } catch (IOException e) {
            System.err.println("Error, no se encontro el archivo");
        }
    }

    public static void get(String route, BiFunction<HttpRequest, HttpResponse, String> s) {
        servicios.put("/app" + route, s);
    }
}

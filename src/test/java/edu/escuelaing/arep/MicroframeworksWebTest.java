package edu.escuelaing.arep;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpServerTest {

    private static Thread serverThread;

    @BeforeAll
    static void setUp() {
        // Levanta el servidor en un hilo separado
        serverThread = new Thread(() -> {
            try {
                WebApplication.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        // Espera a que el servidor esté listo
        boolean serverReady = false;
        int retries = 10;
        while (!serverReady && retries > 0) {
            try {
                Thread.sleep(1000);
                String response = sendGetRequest("http://localhost:8080/app/euler");
                if (response.contains("2.718")) {
                    serverReady = true;
                }
            } catch (IOException | InterruptedException e) {
                retries--;
            }
        }
        assertTrue(serverReady, "El servidor no se levantó correctamente.");
    }

    @AfterAll
    static void tearDown() {
        if (serverThread != null) {
            serverThread.interrupt();
        }
    }

    @Test
    void testHelloEndpoint() throws IOException {
        String response = sendGetRequest("http://localhost:8080/app/hello?name=John");
        assertTrue(response.contains("John"), "El endpoint /app/hello no devolvió el valor esperado.");
    }

    @Test
    void testPiEndpoint() throws IOException {
        String response = sendGetRequest("http://localhost:8080/app/pi");
        assertTrue(response.contains("3.14"), "El endpoint /app/pi no devolvió el JSON esperado.");
    }

    @Test
    void testEulerEndpoint() throws IOException {
        String response = sendGetRequest("http://localhost:8080/app/euler");
        assertTrue(response.contains("2.718"), "El endpoint /app/euler no devolvió el JSON esperado.");
    }

    @Test
    void testGetContentTypeHtml() {
        String contentType = HttpServer.getContentType("index.html");
        assertEquals("text/html", contentType);
    }

    private static String sendGetRequest(String targetUrl) throws IOException {
        URL url = new URL(targetUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);

        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        BufferedReader in = new BufferedReader(reader);
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();
        return content.toString();
    }
}

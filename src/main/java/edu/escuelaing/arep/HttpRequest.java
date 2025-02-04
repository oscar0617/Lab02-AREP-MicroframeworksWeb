package edu.escuelaing.arep;

import java.util.HashMap;
import java.util.Map;

class HttpRequest {
    private String path = null;
    private String query = null;
    private Map<String, String> queryParams = new HashMap<>();

    public HttpRequest(String path, String query) {
        this.path = path;
        this.query = query;
        parseQueryParams(); 
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Recuperar el valor de un parámetro de consulta.
     * 
     * @param var Clave del parámetro a buscar
     * @return Valor asociado a la clave o cadena vacía si no existe
     */
    public String getValues(String var) {
        return queryParams.getOrDefault(var, ""); 
    }

    /**
     * Procesar la consulta (query) y llenar el mapa de parámetros.
     */
    private void parseQueryParams() {
        if (query != null) {
            String[] pairs = query.split("&"); 
            for (String pair : pairs) {
                String[] keyValue = pair.split("="); 
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]); 
                }
            }
        }
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
        parseQueryParams();
    }
}

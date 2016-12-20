package cl.eos.restful;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import cl.eos.persistence.AEntity;
import cl.eos.restful.tables.R_Formas;

/**
 * Clase que realiza todas las operaciones tipo RESTFUL en el sistema.
 * 
 * @author cursor
 *
 */
public class RestfulClient {
    private static final String URL = "http://localhost/tpruebas/%s";
    private static final String BY_ID = URL + "/%d";
    private static final Gson gson = new Gson();
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    /**
     * Obtiene una lista de elementos de la clase T que tengan el ID indicado.
     * @param clazz Clase que se quiere buscar.
     * @param id Identificador de la clase.
     * @return Lista con elementos que coinciden.
     */
    public static <T> List<T> get(Class<T> clazz, Long id) {
        List<T> result = new ArrayList<>();
        String url = String.format(BY_ID, clazz.getSimpleName().toLowerCase(), id);

        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("accept", "application/json");

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() != 200)
                return null;

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String apiOutput = EntityUtils.toString(entity);
                JsonParser parser = new JsonParser();
                JsonArray array = parser.parse(apiOutput).getAsJsonArray();
                for(final JsonElement json: array){
                    T item = gson.fromJson(json, clazz);
                    result.add(item);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    
    public static <T> List<T> get(Class<T> clazz) {
        List<T> result = new ArrayList<>();
        String url = String.format(URL, clazz.getSimpleName().substring(2));

        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("accept", "application/json");

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() != 200)
                return null;

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String apiOutput = EntityUtils.toString(entity);
                JsonParser parser = new JsonParser();
                JsonArray array = parser.parse(apiOutput).getAsJsonArray();
                for(final JsonElement json: array){
                    T item = gson.fromJson(json, clazz);
                    result.add(item);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static <T> List<T> getByParameters(Class<T> clazz, Map<String, Object> map) {
        List<T> result = null;
        String url = URL;
        try {
            HttpGet httpget = new HttpGet(url);
            httpget.addHeader("accept", "application/json");
            URIBuilder uriBuilder = new URIBuilder(url);
            
            for(Entry<String, Object> entry: map.entrySet())
            {
                uriBuilder.addParameter(entry.getKey(), entry.getValue().toString());
            }
            httpget.setURI(uriBuilder.build());
            CloseableHttpResponse response = null;
            response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() != 200)
                return null;

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String apiOutput = EntityUtils.toString(entity);
                result = gson.fromJson(apiOutput, new TypeToken<List<T>>() {
                }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    public static <T> List<T> delete(Class<T> clazz, Long id) {
        List<T> result = null;
        String url = String.format(BY_ID, clazz.getSimpleName().toLowerCase(), id);

        HttpDelete httpdelete = new HttpDelete(url);
        httpdelete.addHeader("accept", "application/json");

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpdelete);
            if (response.getStatusLine().getStatusCode() != 200)
                return null;

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String apiOutput = EntityUtils.toString(entity);
                result = gson.fromJson(apiOutput, new TypeToken<List<T>>() {
                }.getType());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    
    public static <T> boolean post(T element) {
        String url = URL;

        StringEntity postingString;
        try {
            postingString = new StringEntity( gson.toJson(element));
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("accept", "application/json");
            httppost.setEntity(postingString);

            CloseableHttpResponse response = null;

            response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() != 200)
                return false;
            
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return false;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    
    public static <T> boolean put(T element, Long id) {
        String url = String.format(BY_ID, element.getClass().getSimpleName().toLowerCase(), id);

        StringEntity postingString;
        try {
            postingString = new StringEntity( gson.toJson(element));
            HttpPut httpput = new HttpPut(url);
            httpput.addHeader("accept", "application/json");
            httpput.setEntity(postingString);

            CloseableHttpResponse response = null;

            response = httpclient.execute(httpput);
            if (response.getStatusLine().getStatusCode() != 200)
                return false;
            
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return false;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
}

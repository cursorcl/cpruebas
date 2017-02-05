package cl.eos.restful;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Clase que realiza todas las operaciones tipo RESTFUL en el sistema.
 * 
 * @author cursor
 *
 */
public class RestfulClient {
  private static final String URL = "http://www.aplicacionestest.cl/%s";
//  private static final String URL_CONEXION = "http://localhost/tpruebas/connection";
  private static final String URL_CONEXION = "http://www.aplicacionestest.cl/connection";
  private static final String BY_ID = URL + "/%d";
  
  
  static final TypeAdapter<Boolean> booleanAsIntAdapter = new TypeAdapter<Boolean>() {
    @Override public void write(JsonWriter out, Boolean value) throws IOException {
      if (value == null) {
        out.nullValue();
      } else {
        out.value(value);
      }
    }
    @Override public Boolean read(JsonReader in) throws IOException {
      JsonToken peek = in.peek();
      switch (peek) {
      case BOOLEAN:
        return in.nextBoolean();
      case NULL:
        in.nextNull();
        return null;
      case NUMBER:
        return in.nextInt() != 0;
      case STRING:
        return Boolean.parseBoolean(in.nextString());
      default:
        throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
      }
    }
  };
  
  private static final Gson gson =  new GsonBuilder()
      .registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
      .registerTypeAdapter(boolean.class, booleanAsIntAdapter)
      .create();
  
  private static final CloseableHttpClient httpclient = HttpClients.createDefault();

  /**
   * Obtiene una lista de elementos de la clase T que tengan el ID indicado.
   * 
   * @param clazz Clase que se quiere buscar.
   * @param id Identificador de la clase.
   * @return Lista con elementos que coinciden.
   */
  public static <T> List<T> get(Class<T> clazz, Long id) {
    List<T> result = new ArrayList<>();
    String table = getTablName(clazz);
    String url = String.format(BY_ID, table, id);
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
        for (final JsonElement json : array) {
          T item = gson.fromJson(json, clazz);
          result.add(item);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      try {
        response.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static <T> List<T> getByQuery(Class<T> clazz, String query) {
    List<T> result = null;
    String url = String.format(URL, getTablName(clazz));
    CloseableHttpResponse response = null;
    try {
      HttpGet httpget = new HttpGet(url);
      httpget.addHeader("accept", "application/json");
      URIBuilder uriBuilder = new URIBuilder(url);
      uriBuilder.addParameter("query", query);
      httpget.setURI(uriBuilder.build());
      response = httpclient.execute(httpget);
      if (response.getStatusLine().getStatusCode() != 200)
        return null;
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        String apiOutput = EntityUtils.toString(entity);
        result = gson.fromJson(apiOutput, new TypeToken<List<T>>() {}.getType());
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    finally {
      try {
        response.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static <T> List<T> get(Class<T> clazz) {
    List<T> result = new ArrayList<>();
    String url = String.format(URL, getTablName(clazz));
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
        for (final JsonElement json : array) {
          T item = gson.fromJson(json, clazz);
          result.add(item);
        }
      }
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Comuníquese con el administrador del servicio de red.",
          "Error en conexión al servicio de red.", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
    finally {
      try {
        response.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static <T> List<T> getByParameters(Class<T> clazz, Map<String, Object> map) {
    List<T> result = null;
    String url = String.format(URL, getTablName(clazz));
    CloseableHttpResponse response = null;
    try {
      HttpGet httpget = new HttpGet(url);
      httpget.addHeader("accept", "application/json");
      URIBuilder uriBuilder = new URIBuilder(url);
      for (Entry<String, Object> entry : map.entrySet()) {
        uriBuilder.addParameter(entry.getKey(), entry.getValue().toString());
      }
      httpget.setURI(uriBuilder.build());
      
      response = httpclient.execute(httpget);
      if (response.getStatusLine().getStatusCode() != 200)
        return null;
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        String apiOutput = EntityUtils.toString(entity);
        Type tType = new ListParameterizedType(clazz);
        result = gson.fromJson(apiOutput, tType);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    finally {
      try {
        response.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static <T> List<T> delete(Class<T> clazz, Long id) {
    List<T> result = null;
    String url = String.format(BY_ID, getTablName(clazz), id);
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
        result = gson.fromJson(apiOutput, new TypeToken<List<T>>() {}.getType());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      try {
        response.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static <T> boolean post(T element) {
    String url = String.format(URL, getTablName(element.getClass()));
    StringEntity postingString;
    CloseableHttpResponse response = null;
    try {
      postingString = new StringEntity(gson.toJson(element));
      HttpPost httppost = new HttpPost(url);
      httppost.addHeader("accept", "application/json");
      httppost.setEntity(postingString);
      
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
    finally {
      try {
        response.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return true;
  }

  public static <T> boolean put(T element, Long id) {
    String url = String.format(BY_ID, getTablName(element.getClass()), id);
    StringEntity postingString;
    CloseableHttpResponse response = null;
    try {
      postingString = new StringEntity(gson.toJson(element));
      HttpPut httpput = new HttpPut(url);
      httpput.addHeader("accept", "application/json");
      httpput.setEntity(postingString);
      
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
    finally {
      try {
        response.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return true;
  }

  public static boolean existService() {
    String url = URL_CONEXION;
    HttpGet httpget = new HttpGet(url);
    httpget.addHeader("accept", "application/json");
    CloseableHttpResponse response = null;
    try {
      response = httpclient.execute(httpget);
      int status = response.getStatusLine().getStatusCode();
      if (status != 200) {
        JOptionPane.showMessageDialog(null,
            "El servidor retorna código de estado:" + status + ".\n["
                + response.getStatusLine().getReasonPhrase() + "]",
            "Error en conexión al servicio.", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        String apiOutput = EntityUtils.toString(entity);
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(apiOutput).getAsJsonArray();
        for (final JsonElement json : array) {
          int value = json.getAsJsonObject().get("exist").getAsInt();
          System.out.println("Conexión a la BD:" + (value == 1));
        }
      }
    } catch (HttpHostConnectException ex) {
      JOptionPane.showMessageDialog(null, "El servidor no responde:" + ex.getMessage(),
          "Error en conexión al servicio.", JOptionPane.ERROR_MESSAGE);
      return false;
    } catch (JsonSyntaxException ex) {
      JOptionPane.showMessageDialog(null, "Excepción:" + ex.getMessage(),
          "Base de datos no activa.", JOptionPane.ERROR_MESSAGE);
      return false;
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Excepción:" + ex.getMessage(),
          "Se ha producido una excepción.", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    finally {
      try {
        response.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return true;
  }

  private static String getTablName(Class<?> clazz) {
    return clazz.getSimpleName().substring(2).toLowerCase();
  }

  private static class ListParameterizedType implements ParameterizedType {

    private Type type;

    private ListParameterizedType(Type type) {
      this.type = type;
    }

    @Override
    public Type[] getActualTypeArguments() {
      return new Type[] {type};
    }

    @Override
    public Type getRawType() {
      return ArrayList.class;
    }

    @Override
    public Type getOwnerType() {
      return null;
    }

    // implement equals method too! (as per javadoc)
  }
  
}

package cpruebas.pruebas.reportes;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapaDesordenado {

    public static void main(String[] args) {
        LinkedHashMap<String, Object> unsortMap = new LinkedHashMap<String, Object>();
        
        unsortMap.put("B", new Integer(2));
        unsortMap.put("Z", new Integer(1));
        unsortMap.put("A", new Integer(9));
 
        for (Map.Entry entry : unsortMap.entrySet()) {
            System.out.println("Clave : " + entry.getKey()
                    + " Valor : " + entry.getValue());
        }
    }

}

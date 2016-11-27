package cl.eos.restful;

import cl.eos.restful.tables.Prueba;

public class Ejemplo {
    public static void main(String[] args) {
        
        Object result = RestfulClient.get(Prueba.class, 446L);
        
        System.out.println(result);
    }
}

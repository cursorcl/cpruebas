package cl.eos.restful;

import cl.eos.restful.tables.R_Prueba;

public class Ejemplo {
    public static void main(String[] args) {
        
        Object result = RestfulClient.get(R_Prueba.class, 446L);
        
        System.out.println(result);
    }
}

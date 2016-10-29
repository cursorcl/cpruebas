//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen.
// Generado el: 2016.06.22 a las 01:06:30 AM CLT
//

package cl.eos.cliente;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "cliente" })
@XmlRootElement(name = "Clientes")
public class Clientes {

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "rut", "nombre", "direccion", "fono", "email", "alias" })
    public static class Cliente {

        @XmlElement(required = true)
        protected String rut;
        @XmlElement(required = true)
        protected String nombre;
        @XmlElement(required = true)
        protected String direccion;
        protected int fono;
        @XmlElement(required = true)
        protected String email;
        @XmlElement(required = true)
        protected String alias;

        public String getAlias() {
            return alias;
        }

        public String getDireccion() {
            return direccion;
        }

        public String getEmail() {
            return email;
        }

        public int getFono() {
            return fono;
        }

        public String getNombre() {
            return nombre;
        }

        public String getRut() {
            return rut;
        }

        public void setAlias(String value) {
            alias = value;
        }

        public void setDireccion(String value) {
            direccion = value;
        }

        public void setEmail(String value) {
            email = value;
        }

        public void setFono(int value) {
            fono = value;
        }

        public void setNombre(String value) {
            nombre = value;
        }

        public void setRut(String value) {
            rut = value;
        }

        @Override
        public String toString() {
            return nombre;
        }

    }

    @XmlElement(name = "Cliente", required = true)
    protected List<Clientes.Cliente> cliente;

    public List<Clientes.Cliente> getCliente() {
        if (cliente == null) {
            cliente = new ArrayList<Clientes.Cliente>();
        }
        return cliente;
    }

}

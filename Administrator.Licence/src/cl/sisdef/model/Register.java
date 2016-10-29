package cl.sisdef.model;

public class Register {
    private String serial;

    private String licence;

    private long date;

    /**
     * Constructor de la clase.
     */
    public Register() {
    }

    /**
     * Constructor de la clase.
     * 
     * @param serial
     * @param licence
     * @param date
     */
    public Register(String serial, String licence, long date) {
        this.serial = serial;
        this.licence = licence;
        this.date = date;
    }

    /**
     * Metodo que retorna el valor de date.
     * 
     * @return valor de date
     */
    public long getDate() {
        return date;
    }

    /**
     * Metodo que retorna el valor de licence.
     * 
     * @return valor de licence
     */
    public String getLicence() {
        return licence;
    }

    /**
     * Metodo que retorna el valor de serial.
     * 
     * @return valor de serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * Metodo que asigna el valor de date.
     * 
     * @param date
     *            El valor de date
     */
    public void setDate(long date) {
        this.date = date;
    }

    /**
     * Metodo que asigna el valor de licence.
     * 
     * @param licence
     *            El valor de licence
     */
    public void setLicence(String licence) {
        this.licence = licence;
    }

    /**
     * Metodo que asigna el valor de serial.
     * 
     * @param serial
     *            El valor de serial
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

}

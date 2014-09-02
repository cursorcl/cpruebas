package cl.eos.ot;

import javafx.beans.property.SimpleStringProperty;

public class OTAlumno {
	private final SimpleStringProperty name;
    private final SimpleStringProperty aPaterno;
    private final SimpleStringProperty aMaterno;
    private final SimpleStringProperty rut;
	private final SimpleStringProperty curso;
 
    public OTAlumno(String rut, String name, String aPaterno, String aMaterno, String curso) {
        this.rut = new SimpleStringProperty(rut);
        this.name = new SimpleStringProperty(name);
        this.aPaterno = new SimpleStringProperty(aPaterno);
        this.aMaterno = new SimpleStringProperty(aMaterno);
        this.curso = new SimpleStringProperty(curso);
    }

    public String getRut() {
        return rut.get();
    }
    public void setRut(String fRut) {
    	rut.set(fRut);    
    }
    public String getName() {
        return name.get();
    }
    
    public void setName(String fName) {
    	name.set(fName);    
    }
        
    public String getAPaterno() {
        return aPaterno.get();
    }
    public void setAPaterno(String fAPaterno) {
    	aPaterno.set(fAPaterno);
    }
    
    public String getAMaterno() {
        return aMaterno.get();
    }
    public void setAMaterno(String fAMaterno) {
    	aMaterno.set(fAMaterno);
    }
  
  public String getCurso(){
	  return curso.get();
  }
  
  public void setCurso(String fCurso){
	  curso.set(fCurso);
  }
    
}

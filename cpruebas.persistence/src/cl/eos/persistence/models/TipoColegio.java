package cl.eos.persistence.models;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import cl.eos.interfaces.entity.IEntity;

@Entity(name = "tipocolegio")
@NamedQueries({ @NamedQuery(name = "TipoColegio.findAll", query = "SELECT e FROM tipocolegio e order by e.name") })
public class TipoColegio implements IEntity {

  private static final long serialVersionUID = 1L;
  
  @Id 
  @GeneratedValue(strategy=GenerationType.IDENTITY)  
  private Long id;
  private String name;
  
  @OneToMany(mappedBy = "tipoColegio", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
  private Collection<Colegio> colegios;
  
  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public String toString() {
    return name;
  }

  
}

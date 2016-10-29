package cl.eos.persistence.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import cl.eos.persistence.AEntity;

@Entity(name = "dl_comprensionlectora")
@NamedQueries({
        @NamedQuery(name = "ComprensionLectora.findAll", query = "SELECT c FROM dl_comprensionlectora c order by c.name") })
public class ComprensionLectora extends AEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    /**
     * Se crea para el manejo de multiusuarios
     */
    @Version
    protected int version;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public final int getVersion() {
        return version;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public final void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean validate() {
        return true;
    }

}

package cl.eos.interfaces.entity;

import java.io.Serializable;

public interface IEntity extends Serializable {

    Long getId();

    String getName();

    int getVersion();

    void setId(Long id);

    void setName(String name);

    void setVersion(int version);

    boolean validate();
}

package cl.eos.interfaces.entity;

public interface IBaseEntity {
    
    Long getId();
    void setId(Long id);
    
    String getName();
    void setName(String name);
    
    Integer getVersion();
    void setVersion(Integer version);
}

package cl.eos.provider.persistence;

import cl.eos.persistence.IPersistenceService;

public class PersistenceServiceFactory {

    public static final String MYSQL = "MYSQL";
    public static final String RESTFUL = "RESTFUL";
    private static IPersistenceService persistenceService;
    private static IPersistenceService commonPersistenceService;

    
    public static IPersistenceService getPersistenceService() {
        if (PersistenceServiceFactory.persistenceService == null) {

            PersistenceServiceFactory.persistenceService = new PersistenceServiceMYSQL();
        }
        return PersistenceServiceFactory.persistenceService;
    }
    
    public static IPersistenceService getCommonPersistenceService() {
        if (PersistenceServiceFactory.commonPersistenceService == null) {

            PersistenceServiceFactory.commonPersistenceService = new CommonPersistenceServiceMYSQL();
        }
        return PersistenceServiceFactory.commonPersistenceService;
    }

    public static IPersistenceService getPersistenceService(String type) {
        if (PersistenceServiceFactory.persistenceService == null) {

            if(MYSQL.equalsIgnoreCase(type))
            {
                PersistenceServiceFactory.persistenceService = new PersistenceServiceMYSQL();
            }
            else if(RESTFUL.equalsIgnoreCase(type))
            {
                PersistenceServiceFactory.persistenceService = new RestPersistenceServiceRESTFUL();
            }
        }
        return PersistenceServiceFactory.persistenceService;
    }

}

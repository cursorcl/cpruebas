package cl.eos.provider.persistence;

import cl.eos.persistence.IPersistenceService;

public class PersistenceServiceFactory {
    private static IPersistenceService persistenceService;
    
    public static IPersistenceService getPersistenceService() {
        if (persistenceService == null) {
            
            persistenceService = new PersistenceService();
        }
        return persistenceService;
    }

}

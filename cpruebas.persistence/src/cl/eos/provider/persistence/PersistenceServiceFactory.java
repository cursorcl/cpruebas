package cl.eos.provider.persistence;

import cl.eos.persistence.IPersistenceService;

public class PersistenceServiceFactory {
    private static IPersistenceService persistenceService;

    public static IPersistenceService getPersistenceService() {
        if (PersistenceServiceFactory.persistenceService == null) {

            PersistenceServiceFactory.persistenceService = new PersistenceService();
        }
        return PersistenceServiceFactory.persistenceService;
    }

}

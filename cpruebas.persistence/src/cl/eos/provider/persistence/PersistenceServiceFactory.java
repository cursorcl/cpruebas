package cl.eos.provider.persistence;

import cl.eos.persistence.IPersistenceService;

public class PersistenceServiceFactory {
	private static IPersistenceService persistenceService = new PersistenceService();
	
	public static IPersistenceService getPersistenceService()
	{
		return persistenceService;
	}
}


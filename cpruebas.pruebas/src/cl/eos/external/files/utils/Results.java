package cl.eos.external.files.utils;

import java.util.List;

/**
 * Clase contenedora de los resultados.
 * 
 * Resultados correctos e incorrectos.
 * 
 * @author eosorio
 *
 */
public class  Results {
	public List<RegisterForView> results;
	public List<RegisterForView> badResults;

	public Results(List<RegisterForView> results, List<RegisterForView> badResults) {
		this.results = results;
		this.badResults = badResults;

	}
}
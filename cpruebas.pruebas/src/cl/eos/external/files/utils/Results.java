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
	public List<Register> results;
	public List<Register> badResults;

	public Results(List<Register> results, List<Register> badResults) {
		this.results = results;
		this.badResults = badResults;

	}
}
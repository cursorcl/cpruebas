package cl.eos.api;

public interface IImporter {

	/**
	 * Establece el origen de los datos, el cual debe ser un elemento que el
	 * importador debe reconocer.
	 * 
	 * @param source
	 *            Objeto que el importador reconoce.
	 */
	void setSource(Object source);

	/**
	 * Realiza la operación de importación propiemente tal.
	 * 
	 * @return Retorna el estado del proceso de importación.
	 * @throws ExceptionImport
	 */
	EnumImport exceute() throws ExceptionImport;

}

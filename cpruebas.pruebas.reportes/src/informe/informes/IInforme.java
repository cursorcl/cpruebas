package informe.informes;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.TipoAlumno;

/**
 * Representa a los informes que se deben generar dentro del documento word. Se
 * debe crear una clase por cada informe que debe ir en el documento.
 * 
 * @author cursor
 *
 */
public interface IInforme {

    public static interface ProcessFinishedListener {
        void onFinished(IInforme source);
    }

    /**
     * Es el metodo de entrada al informe. Los parametros son conocidos por el
     * informe, quien lo solicite debe saber que parametros debe enviar.
     * 
     * @param tipoAlumno
     *            Tipo de alumno que se considera en el informe.
     * @param colegio
     *            El colegio que se está evaluando.
     * @param asignatura
     *            Asignatura para la que se procesa este informe.
     * @param parameters
     *            Otros parámetros que se requieran.
     */
    void execute(TipoAlumno tipoAlumno, Colegio colegio, Asignatura asignatura);

    /**
     * Construye la sección de página asociada al inoforme. Se asume que utiliza
     * la información que se generó en el metodo Procesar.
     */
    void page(XWPFDocument document);

    /**
     * Construye la sección de página asociada al gráfico. Se asume que utiliza
     * la información que se generó en el método procesar.
     * 
     * @param document
     */
    void graph(XWPFDocument document);

}
package cl.eos.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cl.eos.persistence.AEntity;
import cl.eos.persistence.models.SAlumno;
import cl.eos.persistence.models.SAsignatura;
import cl.eos.persistence.models.SCiclo;
import cl.eos.persistence.models.SColegio;
import cl.eos.persistence.models.SCurso;
import cl.eos.persistence.models.SEjeTematico;
import cl.eos.persistence.models.SEvaluacionEjeTematico;
import cl.eos.persistence.models.SEvaluacionPrueba;
import cl.eos.persistence.models.SFormas;
import cl.eos.persistence.models.SHabilidad;
import cl.eos.persistence.models.SImagenes;
import cl.eos.persistence.models.SNivelEvaluacion;
import cl.eos.persistence.models.SObjetivo;
import cl.eos.persistence.models.SProfesor;
import cl.eos.persistence.models.SPrueba;
import cl.eos.persistence.models.SRangoEvaluacion;
import cl.eos.persistence.models.SRespuestasEsperadasPrueba;
import cl.eos.persistence.models.STipoAlumno;
import cl.eos.persistence.models.STipoColegio;
import cl.eos.persistence.models.STipoCurso;
import cl.eos.persistence.models.STipoPrueba;
import cl.eos.restful.tables.R_Alumno;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_Ciclo;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_Curso;
import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_EvaluacionEjetematico;
import cl.eos.restful.tables.R_EvaluacionPrueba;
import cl.eos.restful.tables.R_Formas;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_Imagenes;
import cl.eos.restful.tables.R_NivelEvaluacion;
import cl.eos.restful.tables.R_Objetivo;
import cl.eos.restful.tables.R_Profesor;
import cl.eos.restful.tables.R_Prueba;
import cl.eos.restful.tables.R_RangoEvaluacion;
import cl.eos.restful.tables.R_RespuestasEsperadasPrueba;
import cl.eos.restful.tables.R_TipoAlumno;
import cl.eos.restful.tables.R_TipoColegio;
import cl.eos.restful.tables.R_TipoCurso;
import cl.eos.restful.tables.R_TipoPrueba;

public class RestFul2Entity {

    @SuppressWarnings("unchecked")
    public static <T> List<T> getAll(Class<T> clazz) {
        if (clazz.equals(SAlumno.class)) {
            return (List<T>) getAllAlumnos();
        } else if (clazz.equals(STipoAlumno.class)) {
            return (List<T>) getAllTipoAlumnos();
        } else if (clazz.equals(SColegio.class)) {
            return (List<T>) getAllColegios();
        } else if (clazz.equals(SCurso.class)) {
            return (List<T>) getAllCursos();
        } else if (clazz.equals(STipoColegio.class)) {
            return (List<T>) getAllTipoColegios();
        } else if (clazz.equals(STipoPrueba.class)) {
            return (List<T>) getAllTipoPruebas();
        } else if (clazz.equals(SAsignatura.class)) {
            return (List<T>) getAllAsignaturas();
        } else if (clazz.equals(SCiclo.class)) {
            return (List<T>) getAllCiclos();
        } else if (clazz.equals(SEjeTematico.class)) {
            return (List<T>) getAllEjesTematicos();
        } else if (clazz.equals(SEvaluacionEjeTematico.class)) {
            return (List<T>) getAllEvaluacionEjeTematico();
        } else if (clazz.equals(SEvaluacionPrueba.class)) {
            return (List<T>) getAllEvaluacionPrueba();
        } else if (clazz.equals(SFormas.class)) {
            return (List<T>) getAllFormas();
        } else if (clazz.equals(SHabilidad.class)) {
            return (List<T>) getAllHabilidades();
        } else if (clazz.equals(SImagenes.class)) {
            return (List<T>) getAllImagenes();
        } else if (clazz.equals(SNivelEvaluacion.class)) {
            return (List<T>) getAllNivelEvaluacion();
        } else if (clazz.equals(SRangoEvaluacion.class)) {
            return (List<T>) getAllRangoEvaluacion();
        } else if (clazz.equals(SObjetivo.class)) {
            return (List<T>) getAllObjetivos();
        } else if (clazz.equals(SProfesor.class)) {
            return (List<T>) getAllProfesores();
        } else if (clazz.equals(SRespuestasEsperadasPrueba.class)) {
            return (List<T>) getAllRespuestasEsperadasPrueba();
        } else if (clazz.equals(SPrueba.class)) {
            return (List<T>) getAllPrueba();
        }

        return null;
    }

    private static List<SFormas> getAllFormas() {
        ArrayList<SFormas> lstEntity = new ArrayList<>();
        List<R_Formas> lstREnity = RestfulClient.get(R_Formas.class);
        for (R_Formas rEntity : lstREnity) {
            SFormas entity = new SFormas.Builder().id(rEntity.getId()).name(rEntity.getName()).forma(rEntity.getForma())
                    .orden(rEntity.getOrden()).build();
            lstEntity.add(entity);
        }
        return lstEntity;
    }

    private static List<SPrueba> getAllPrueba() {
        Map<Long, SAsignatura> mapAsig = getMap(getAllAsignaturas());
        Map<Long, STipoCurso> mapCurso = getMap(getAllTipoCursos());
        Map<Long, STipoPrueba> mapTpoPrueba = getMap(getAllTipoPruebas());
        Map<Long, SProfesor> mapProfesor = getMap(getAllProfesores());
        Map<Long, SNivelEvaluacion> mapNEval = getMap(getAllNivelEvaluacion());
        
        List<R_Formas> lstRFormas = RestfulClient.get(R_Formas.class);
        Map<Long, List<R_Formas>> mapFormas = lstRFormas.stream()
                .collect(Collectors.groupingBy(R_Formas::getPrueba_id, Collectors.toList()));
        
        
        List<R_RespuestasEsperadasPrueba> lstRRespuestas = RestfulClient.get(R_RespuestasEsperadasPrueba.class);
        Map<Long, List<R_RespuestasEsperadasPrueba>> mapRespuestas = lstRRespuestas.stream()
                .collect(Collectors.groupingBy(R_RespuestasEsperadasPrueba::getPrueba_id, Collectors.toList()));
        
        ArrayList<SPrueba> lstEntity = new ArrayList<>();
        List<R_Prueba> lstREnity = RestfulClient.get(R_Prueba.class);
        for (R_Prueba rEntity : lstREnity) {
            
            List<R_Formas> formas = mapFormas.get(rEntity.getId());
            List<R_RespuestasEsperadasPrueba> respuestas = mapRespuestas.get(rEntity.getId());
            
            SAsignatura asignatura = mapAsig.get(rEntity.getAsignatura_id());
            STipoCurso curso = mapCurso.get(rEntity.getCurso_id());
            STipoPrueba tpoPrueba = mapTpoPrueba.get(rEntity.getTipoprueba_id());
            SProfesor profesor = mapProfesor.get(rEntity.getProfesor_id());
            SNivelEvaluacion nivelEval = mapNEval.get(rEntity.getNivelevaluacion_id());
            
            SPrueba entity = new SPrueba.Builder().id(rEntity.getId()).name(rEntity.getName())
                    .exigencia(rEntity.getExigencia()).alternativas(rEntity.getAlternativas()).fecha(rEntity.getFecha())
                    .puntajeBase(rEntity.getPuntajebase()).responses(rEntity.getResponses())
                    .nroPreguntas(rEntity.getNropreguntas()).asignatura(asignatura).curso(curso).tipoPrueba(tpoPrueba)
                    .profesor(profesor).nivelEvaluacion(nivelEval).nroFormas(formas.size()).build();
            
            
            List<SFormas> sFormas = new ArrayList<>();
            for(R_Formas f: formas)
            {
                SFormas forma = new SFormas.Builder().id(f.getId()).name(f.getName())
                        .forma(f.getForma()).orden(f.getOrden()).prueba(entity).build();
                sFormas.add(forma);
            }
            entity.setFormas(sFormas);
            
            
            lstEntity.add(entity);
        }
        return lstEntity;

    }

    private static List<SRespuestasEsperadasPrueba> getAllRespuestasEsperadasPrueba() {

        Map<Long, SHabilidad> mapHab = getMap(getAllHabilidades());
        Map<Long, SEjeTematico> mapEje = getMap(getAllEjesTematicos());
        Map<Long, SObjetivo> mapObj = getMap(getAllObjetivos());
        Map<Long, SPrueba> mapPrb = getMap(getAllPrueba());

        ArrayList<SRespuestasEsperadasPrueba> lstEntity = new ArrayList<>();
        List<R_RespuestasEsperadasPrueba> lstREnity = RestfulClient.get(R_RespuestasEsperadasPrueba.class);
        for (R_RespuestasEsperadasPrueba rEntity : lstREnity) {
            SHabilidad habilidad = mapHab.get(rEntity.getHabilidad_id());
            SEjeTematico ejeTematico = mapEje.get(rEntity.getEjetematico_id());
            SObjetivo objetivo = mapObj.get(rEntity.getObjetivo_id());
            SPrueba prueba = mapPrb.get(rEntity.getPrueba_id());
            SRespuestasEsperadasPrueba entity = new SRespuestasEsperadasPrueba.Builder().id(rEntity.getId())
                    .name(rEntity.getName()).numero(rEntity.getNumero()).verdaderoFalso(rEntity.getVerdaderofalso())
                    .anulada(rEntity.getAnulada()).respuesta(rEntity.getRespuesta()).mental(rEntity.getMental())
                    .habilidad(habilidad).ejeTematico(ejeTematico).objetivo(objetivo).prueba(prueba).build();
            lstEntity.add(entity);
        }
        return lstEntity;
    }

    private static List<SImagenes> getAllImagenes() {
        
        Map<Long, SRespuestasEsperadasPrueba> mapResp = getMap(getAllRespuestasEsperadasPrueba());
        ArrayList<SImagenes> lstEntity = new ArrayList<>();
        List<R_Imagenes> lstREnity = RestfulClient.get(R_Imagenes.class);
        for (R_Imagenes rEntity : lstREnity) {
            SRespuestasEsperadasPrueba respuesta = mapResp.get(rEntity.getRespuesta_id());
            SImagenes entity = new SImagenes.Builder().id(rEntity.getId()).name(rEntity.getName()).eliminada(false)
                    .numero(rEntity.getNumero()).respuesta(respuesta).build();
            lstEntity.add(entity);
        }
        return lstEntity;
    }

    private static List<SProfesor> getAllProfesores() {
        ArrayList<SProfesor> lstEntity = new ArrayList<>();
        List<R_Profesor> lstREnity = RestfulClient.get(R_Profesor.class);
        for (R_Profesor rEntity : lstREnity) {
            SProfesor entity = new SProfesor.Builder().id(rEntity.getId()).name(rEntity.getName())
                    .paterno(rEntity.getPaterno()).materno(rEntity.getMaterno()).rut(rEntity.getRut()).build();
            lstEntity.add(entity);
        }
        return lstEntity;

    }

    private static List<SObjetivo> getAllObjetivos() {

        Map<Long, STipoCurso> mapTipoCurso = getMap(getAllTipoCursos());
        Map<Long, SAsignatura> mapAsignatura = getMap(getAllAsignaturas());

        ArrayList<SObjetivo> lstEntity = new ArrayList<>();
        List<R_Objetivo> lstREnity = RestfulClient.get(R_Objetivo.class);
        for (R_Objetivo rEntity : lstREnity) {
            SAsignatura asignatura = mapAsignatura.get(rEntity.getAsignatura_id());
            STipoCurso tipoCurso = mapTipoCurso.get(rEntity.getTipocurso_id());
            SObjetivo entity = new SObjetivo.Builder().id(rEntity.getId()).name(rEntity.getName())
                    .descripcion(rEntity.getDescripcion()).asignatura(asignatura).tipoCurso(tipoCurso).build();
            lstEntity.add(entity);
        }
        return lstEntity;

    }

    private static List<SRangoEvaluacion> getAllRangoEvaluacion() {

        Map<Long, SNivelEvaluacion> mapNivelEvaluacion = new HashMap<>();
        List<R_NivelEvaluacion> lstRNivelEnity = RestfulClient.get(R_NivelEvaluacion.class);
        for (R_NivelEvaluacion rEntity : lstRNivelEnity) {
            SNivelEvaluacion entity = new SNivelEvaluacion.Builder().id(rEntity.getId()).name(rEntity.getName())
                    .nroRangos(rEntity.getNrorangos()).rangos(new ArrayList<>()).build();
            mapNivelEvaluacion.put(entity.getId(), entity);
        }

        ArrayList<SRangoEvaluacion> lstEntity = new ArrayList<>();
        List<R_RangoEvaluacion> lstREnity = RestfulClient.get(R_RangoEvaluacion.class);
        for (R_RangoEvaluacion rEntity : lstREnity) {
            SNivelEvaluacion nivelEvaluacion = mapNivelEvaluacion.get(rEntity.getNivelevaluacion_id());
            SRangoEvaluacion entity = new SRangoEvaluacion.Builder().id(rEntity.getId()).name(rEntity.getName())
                    .abreviacion(rEntity.getAbreviacion()).minimo(rEntity.getMinimo()).maximo(rEntity.getMaximo())
                    .nivelEvaluacion(nivelEvaluacion).build();
            nivelEvaluacion.getRangos().add(entity);
            lstEntity.add(entity);
        }
        return lstEntity;

    }

    private static List<SNivelEvaluacion> getAllNivelEvaluacion() {
        Map<Long, SNivelEvaluacion> mapNivelEvaluacion = new HashMap<>();
        ArrayList<SNivelEvaluacion> lstEntity = new ArrayList<>();
        List<R_NivelEvaluacion> lstRNivelEnity = RestfulClient.get(R_NivelEvaluacion.class);
        for (R_NivelEvaluacion rEntity : lstRNivelEnity) {

            SNivelEvaluacion entity = new SNivelEvaluacion.Builder().id(rEntity.getId()).name(rEntity.getName())
                    .nroRangos(rEntity.getNrorangos()).rangos(new ArrayList<>()).build();
            mapNivelEvaluacion.put(entity.getId(), entity);
            lstEntity.add(entity);
        }
        List<R_RangoEvaluacion> lstREnity = RestfulClient.get(R_RangoEvaluacion.class);
        for (R_RangoEvaluacion rEntity : lstREnity) {
            SNivelEvaluacion nivelEvaluacion = mapNivelEvaluacion.get(rEntity.getNivelevaluacion_id());
            SRangoEvaluacion entity = new SRangoEvaluacion.Builder().id(rEntity.getId()).name(rEntity.getName())
                    .abreviacion(rEntity.getAbreviacion()).minimo(rEntity.getMinimo()).maximo(rEntity.getMaximo())
                    .nivelEvaluacion(nivelEvaluacion).build();
            nivelEvaluacion.getRangos().add(entity);
        }
        return lstEntity;

    }

    private static List<SHabilidad> getAllHabilidades() {
        ArrayList<SHabilidad> lstEntity = new ArrayList<>();
        List<R_Habilidad> lstREnity = RestfulClient.get(R_Habilidad.class);
        for (R_Habilidad rEntity : lstREnity) {
            SHabilidad entity = new SHabilidad.Builder().id(rEntity.getId()).name(rEntity.getName())
                    .descripcion(rEntity.getDescripcion()).build();
            lstEntity.add(entity);
        }
        return lstEntity;
    }

    private static List<SEvaluacionPrueba> getAllEvaluacionPrueba() {
        ArrayList<SEvaluacionPrueba> lstEntity = new ArrayList<>();
        List<R_EvaluacionPrueba> lstREnity = RestfulClient.get(R_EvaluacionPrueba.class);
        for (R_EvaluacionPrueba rEntity : lstREnity) {
            SEvaluacionPrueba entity = new SEvaluacionPrueba.Builder().id(rEntity.getId()).name(rEntity.getName())
                    .build();
            lstEntity.add(entity);
        }
        return lstEntity;

    }

    private static List<SEvaluacionEjeTematico> getAllEvaluacionEjeTematico() {
        ArrayList<SEvaluacionEjeTematico> lstEntity = new ArrayList<>();
        List<R_EvaluacionEjetematico> lstREnity = RestfulClient.get(R_EvaluacionEjetematico.class);
        for (R_EvaluacionEjetematico rEntity : lstREnity) {
            SEvaluacionEjeTematico entity = new SEvaluacionEjeTematico.Builder().id(rEntity.getId())
                    .name(rEntity.getName()).nroRangoMax(rEntity.getNrorangomax()).nroRangoMin(rEntity.getNrorangomin())
                    .build();
            lstEntity.add(entity);
        }
        return lstEntity;
    }

    private static List<STipoPrueba> getAllTipoPruebas() {
        ArrayList<STipoPrueba> lstEntity = new ArrayList<>();
        List<R_TipoPrueba> lstREntity = RestfulClient.get(R_TipoPrueba.class);
        for (R_TipoPrueba rEntity : lstREntity) {
            STipoPrueba entity = new STipoPrueba.Builder().id(rEntity.getId()).name(rEntity.getName()).build();
            lstEntity.add(entity);
        }
        return lstEntity;
    }

    private static List<SEjeTematico> getAllEjesTematicos() {
        List<SEjeTematico> lstEntity = new ArrayList<>();
        List<R_Ejetematico> lstREntity = RestfulClient.get(R_Ejetematico.class);

        Map<Long, SAsignatura> mapAsignatura = getMap(getAllAsignaturas());
        Map<Long, STipoPrueba> mapTipoPrueba = getMap(getAllTipoPruebas());

        for (R_Ejetematico rEntity : lstREntity) {
            SAsignatura asignatura = mapAsignatura.get(rEntity.getAsignatura_id());
            STipoPrueba tipoPrueba = mapTipoPrueba.get(rEntity.getAsignatura_id());
            SEjeTematico entity = new SEjeTematico.Builder().asignatura(asignatura).id(rEntity.getId())
                    .name(rEntity.getName()).tipoprueba(tipoPrueba).build();
            lstEntity.add(entity);
        }
        return lstEntity;
    }

    private static List<SAsignatura> getAllAsignaturas() {
        List<SAsignatura> lstEntity = new ArrayList<>();
        List<R_Asignatura> lstREntity = RestfulClient.get(R_Asignatura.class);
        for (R_Asignatura rEntity : lstREntity) {
            SAsignatura entity = new SAsignatura.Builder().id(rEntity.getId()).name(rEntity.getName()).build();
            lstEntity.add(entity);
        }
        return lstEntity;
    }

    /**
     * 
     * @return
     */
    public static List<STipoCurso> getAllTipoCursos() {
        List<STipoCurso> lstEntity = new ArrayList<>();
        List<R_TipoCurso> lstREntity = RestfulClient.get(R_TipoCurso.class);
        for (R_TipoCurso rEntity : lstREntity) {
            STipoCurso entity = new STipoCurso(rEntity.getId(), rEntity.getName());
            lstEntity.add(entity);
        }
        return lstEntity;
    }

    public static List<STipoAlumno> getAllTipoAlumnos() {
        List<STipoAlumno> lstEntity = new ArrayList<>();
        List<R_TipoAlumno> lstREntity = RestfulClient.get(R_TipoAlumno.class);
        for (R_TipoAlumno rEntity : lstREntity) {
            STipoAlumno entity = new STipoAlumno(rEntity.getId(), rEntity.getName());
            lstEntity.add(entity);
        }
        return lstEntity;
    }

    public static List<STipoColegio> getAllTipoColegios() {
        ArrayList<STipoColegio> lstEntity = new ArrayList<STipoColegio>();
        List<R_TipoColegio> tipoColegios = RestfulClient.get(R_TipoColegio.class);
        for (R_TipoColegio tpoAlumno : tipoColegios) {
            STipoColegio tpo = new STipoColegio(tpoAlumno.getId(), tpoAlumno.getName());
            lstEntity.add(tpo);
        }
        return lstEntity;
    }

    public static List<SCiclo> getAllCiclos() {
        ArrayList<SCiclo> lstEntity = new ArrayList<SCiclo>();
        List<R_Ciclo> lstCiclos = RestfulClient.get(R_Ciclo.class);
        for (R_Ciclo rciclo : lstCiclos) {
            SCiclo ciclo = new SCiclo(rciclo.getId(), rciclo.getName());
            lstEntity.add(ciclo);
        }
        return lstEntity;
    }

    public static List<SAlumno> getAllAlumnos() {
        Map<Long, STipoAlumno> mapTpo = getMap(getAllTipoAlumnos());
        Map<Long, SCurso> mapCurso = getMap(getAllCursos());
        Map<Long, SColegio> mapCol = getMap(getAllColegios());

        List<SAlumno> lstEntity = new ArrayList<>();
        List<R_Alumno> lstRAlumnos = RestfulClient.get(R_Alumno.class);
        for (R_Alumno ralumno : lstRAlumnos) {
            SCurso curso = mapCurso.get(ralumno.getCurso_id());
            SColegio colegio = mapCol.get(ralumno.getColegio_id());
            STipoAlumno tipoAlumno = mapTpo.get(ralumno.getTipoalumno_id());

            SAlumno alumno = new SAlumno.Builder().colegio(colegio).curso(curso).tipoAlumno(tipoAlumno)
                    .direccion(ralumno.getDireccion()).materno(ralumno.getMaterno()).name(ralumno.getName())
                    .rut(ralumno.getRut()).id(ralumno.getId()).build();
            lstEntity.add(alumno);
        }

        return lstEntity;
    }

    public static List<SCurso> getAllCursos() {
        Map<Long, SColegio> mapColegios = getMap(getAllColegios());
        Map<Long, SCiclo> mapCiclos = getMap(getAllCiclos());
        Map<Long, STipoCurso> mapTipoCurso = getMap(getAllTipoCursos());

        List<SCurso> lstEntity = new ArrayList<SCurso>();
        List<R_Curso> r_cursos = RestfulClient.get(R_Curso.class);
        for (R_Curso rCur : r_cursos) {
            SCiclo ciclo = mapCiclos.get(rCur.getCiclo_id());
            SColegio colegio = mapColegios.get(rCur.getColegio_id());
            STipoCurso tipoCurso = mapTipoCurso.get(rCur.getTipocurso_id());

            SCurso curso = new SCurso.Builder().ciclo(ciclo).colegio(colegio).id(rCur.getId()).name(rCur.getName())
                    .tipoCurso(tipoCurso).build();
            lstEntity.add(curso);
        }
        return lstEntity;
    }

    public static List<SColegio> getAllColegios() {
        Map<Long, STipoColegio> mapTpo = getMap(getAllTipoColegios());

        List<SColegio> lstEntity = new ArrayList<SColegio>();
        List<R_Colegio> colegios = RestfulClient.get(R_Colegio.class);
        for (R_Colegio rCol : colegios) {
            SColegio col = new SColegio();
            col.setName(rCol.getName());
            col.setCiudad(rCol.getCiudad());
            col.setDireccion(rCol.getDireccion());
            col.setId(rCol.getId());
            col.setTipoColegio(mapTpo.get(rCol.getTipocolegio_id()));
            lstEntity.add(col);
        }
        return lstEntity;
    }

    /**
     * Utilitario que permite construir un mapa indexado por el id de un
     * AEntity, a contar de una lista.
     * 
     * @param list
     *            Lista con los datos.
     * @return Mapa de los datos.
     */
    static <S extends AEntity> Map<Long, S> getMap(List<S> list) {
        if (list == null || list.isEmpty())
            return null;
        Map<Long, S> map = list.stream().collect(Collectors.toMap(S::getId, Function.identity()));
        return map;
    }
}

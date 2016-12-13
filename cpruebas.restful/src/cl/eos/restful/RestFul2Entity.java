package cl.eos.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import cl.eos.persistence.models.SRangoEvaluacion;
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
import cl.eos.restful.tables.R_RangoEvaluacion;
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
        }
        return null;
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
        List<STipoCurso> allTipoCurso = getAllTipoCursos();
        Map<Long, STipoCurso> mapTipoCurso = allTipoCurso.stream()
                .collect(Collectors.toMap(STipoCurso::getId, Function.identity()));
        List<SAsignatura> allAsignaturas = getAllAsignaturas();
        Map<Long, SAsignatura> mapAsignatura = allAsignaturas.stream()
                .collect(Collectors.toMap(SAsignatura::getId, Function.identity()));

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

    private static List<SImagenes> getAllImagenes() {
        ArrayList<SImagenes> lstEntity = new ArrayList<>();
        List<R_Imagenes> lstREnity = RestfulClient.get(R_Imagenes.class);
        for (R_Imagenes rEntity : lstREnity) {
            SImagenes entity = new SImagenes.Builder().id(rEntity.getId()).name(rEntity.getName()).eliminada(false)
                    .numero(rEntity.getNumero()).respuesta(respuesta).build();
            lstEntity.add(entity);
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

    private static List<SFormas> getAllFormas() {
        ArrayList<SFormas> lstEntity = new ArrayList<>();
        List<R_Formas> lstREnity = RestfulClient.get(R_Formas.class);
        for (R_Formas rEntity : lstREnity) {
            SFormas entity = new SFormas.Builder().id(rEntity.getId()).name(rEntity.getName()).build();
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

        List<SAsignatura> allAsignaturas = getAllAsignaturas();
        Map<Long, SAsignatura> mapAsignatura = allAsignaturas.stream()
                .collect(Collectors.toMap(SAsignatura::getId, Function.identity()));

        List<STipoPrueba> allTipoPruebas = getAllTipoPruebas();
        Map<Long, STipoPrueba> mapTipoPrueba = allTipoPruebas.stream()
                .collect(Collectors.toMap(STipoPrueba::getId, Function.identity()));

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
        List<STipoAlumno> allTipoAlumnos = getAllTipoAlumnos();
        Map<Long, STipoAlumno> mapTpo = allTipoAlumnos.stream()
                .collect(Collectors.toMap(STipoAlumno::getId, Function.identity()));

        List<SCurso> allCursos = getAllCursos();
        Map<Long, SCurso> mapCurso = allCursos.stream().collect(Collectors.toMap(SCurso::getId, Function.identity()));

        List<SColegio> allColegios = getAllColegios();
        Map<Long, SColegio> mapCol = allColegios.stream()
                .collect(Collectors.toMap(SColegio::getId, Function.identity()));

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
        List<SColegio> allColegios = getAllColegios();
        Map<Long, SColegio> mapColegios = allColegios.stream()
                .collect(Collectors.toMap(SColegio::getId, Function.identity()));

        List<SCiclo> allCiclos = getAllCiclos();
        Map<Long, SCiclo> mapCiclos = allCiclos.stream().collect(Collectors.toMap(SCiclo::getId, Function.identity()));

        List<STipoCurso> allTipoCurso = getAllTipoCursos();
        Map<Long, STipoCurso> mapTipoCurso = allTipoCurso.stream()
                .collect(Collectors.toMap(STipoCurso::getId, Function.identity()));

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
        List<STipoColegio> allTipoColegios = getAllTipoColegios();
        Map<Long, STipoColegio> mapTpo = allTipoColegios.stream()
                .collect(Collectors.toMap(STipoColegio::getId, Function.identity()));

        List<SColegio> lstEntity = new ArrayList<SColegio>();
        List<R_Colegio> colegios = RestfulClient.get(R_Colegio.class);
        for (R_Colegio rCol : colegios) {
            SColegio col = new SColegio();
            col.setCiudad(rCol.getCiudad());
            col.setDireccion(rCol.getDireccion());
            col.setId(rCol.getId());
            col.setTipoColegio(mapTpo.get(rCol.getTipocolegio_id()));
            lstEntity.add(col);
        }
        return lstEntity;
    }

}

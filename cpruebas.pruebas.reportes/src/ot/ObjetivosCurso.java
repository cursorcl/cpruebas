package ot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cl.eos.persistence.models.Curso;

/**
 * Contiene una lista de todos los resultados de los objetivosCurso de un curso.
 * 
 * @author cursor
 */
public class ObjetivosCurso implements IResultado {

    Curso curso;
    List<ObjetivosAlumno> objetivosAlumnos = new ArrayList<>();

    public List<ObjetivosAlumno> getObjetivosAlumnos() {
        return objetivosAlumnos;
    }

    public void setObjetivosAlumnos(List<ObjetivosAlumno> objetivosAlumnos) {
        this.objetivosAlumnos = objetivosAlumnos;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    /**
     * Se obtiene el resultado para el curso completo.
     * 
     * @return List<ItemObjetivo> con el acumulado del curso.
     */
    @Override
    public List<TitledItemObjetivo> getResultados() {
        List<ItemObjetivo> resultado = new ArrayList<>();
        for (ObjetivosAlumno obj : objetivosAlumnos) {
            List<ItemObjetivo> items = obj.getItems();
            for (ItemObjetivo item : items) {
                if (!resultado.contains(item)) {
                    resultado.add(item);
                } else {
                    ItemObjetivo rItem = resultado.get(resultado.indexOf(item));
                    rItem.add(item);
                }
            }
        }
        return Stream.of(new TitledItemObjetivo(resultado, curso)).collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((curso == null) ? 0 : curso.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ObjetivosCurso other = (ObjetivosCurso) obj;
        if (curso == null) {
            if (other.curso != null)
                return false;
        } else if (!curso.equals(other.curso))
            return false;
        return true;
    }

    public static class Builder {
        private List<ObjetivosAlumno> objetivosAlumnos;
        private Curso curso;

        public Builder alumnos(List<ObjetivosAlumno> objetivosAlumnos) {
            this.objetivosAlumnos = objetivosAlumnos;
            return this;
        }

        public Builder curso(Curso curso) {
            this.curso = curso;
            return this;
        }

        public ObjetivosCurso build() {
            ObjetivosCurso oTxObjCurso = new ObjetivosCurso();
            oTxObjCurso.objetivosAlumnos = objetivosAlumnos;
            oTxObjCurso.curso = curso;
            return oTxObjCurso;
        }
    }

}

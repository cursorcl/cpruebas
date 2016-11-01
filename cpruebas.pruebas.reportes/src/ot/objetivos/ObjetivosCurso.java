package ot.objetivos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cl.eos.persistence.models.Curso;
import ot.IResultado;
import ot.ItemObjetivo;
import ot.TitledItemObjetivo;

/**
 * Contiene una lista de todos los resultados de los objetivosCurso de un curso.
 *
 * @author cursor
 */
public class ObjetivosCurso implements IResultado {

    public static class Builder {
        private List<ObjetivosAlumno> objetivosAlumnos;
        private Curso curso;

        public Builder alumnos(List<ObjetivosAlumno> objetivosAlumnos) {
            this.objetivosAlumnos = objetivosAlumnos;
            return this;
        }

        public ObjetivosCurso build() {
            final ObjetivosCurso oTxObjCurso = new ObjetivosCurso();
            oTxObjCurso.objetivosAlumnos = objetivosAlumnos;
            oTxObjCurso.curso = curso;
            return oTxObjCurso;
        }

        public Builder curso(Curso curso) {
            this.curso = curso;
            return this;
        }
    }

    Curso curso;

    List<ObjetivosAlumno> objetivosAlumnos = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ObjetivosCurso other = (ObjetivosCurso) obj;
        if (curso == null) {
            if (other.curso != null)
                return false;
        } else if (!curso.equals(other.curso))
            return false;
        return true;
    }

    public Curso getCurso() {
        return curso;
    }

    public List<ObjetivosAlumno> getObjetivosAlumnos() {
        return objetivosAlumnos;
    }

    /**
     * Se obtiene el resultado para el curso completo.
     *
     * @return List<ItemObjetivo> con el acumulado del curso.
     */
    @Override
    public List<TitledItemObjetivo> getResultados() {
        final List<ItemObjetivo> resultado = new ArrayList<>();
        for (final ObjetivosAlumno obj : objetivosAlumnos) {
            final List<ItemObjetivo> items = obj.getItems();
            for (final ItemObjetivo item : items) {
                if (!resultado.contains(item)) {
                    resultado.add(item);
                } else {
                    final ItemObjetivo rItem = resultado.get(resultado.indexOf(item));
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
        result = prime * result + (curso == null ? 0 : curso.hashCode());
        return result;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void setObjetivosAlumnos(List<ObjetivosAlumno> objetivosAlumnos) {
        this.objetivosAlumnos = objetivosAlumnos;
    }

}

package ot;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cl.eos.persistence.models.Alumno;

/**
 * Contiene el resultado de cada uno de los items que conforman el alumno.
 * @author colegio
 */
public class ObjetivosAlumno implements IResultado{
    Alumno alumno;
    List<ItemObjetivo> items;

    public List<ItemObjetivo> getItems() {
        return items;
    }

    public void setItems(List<ItemObjetivo> alumnos) {
        this.items = alumnos;
    }

    
    
    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alumno == null) ? 0 : alumno.hashCode());
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
        ObjetivosAlumno other = (ObjetivosAlumno) obj;
        if (alumno == null) {
            if (other.alumno != null)
                return false;
        } else if (!alumno.equals(other.alumno))
            return false;
        return true;
    }



    public static class Builder {
        private List<ItemObjetivo> items;
        private Alumno alumno;

        public Builder items(List<ItemObjetivo> items) {
            this.items = items;
            return this;
        }

        public Builder alumno(Alumno alumno) {
            this.alumno = alumno;
            return this;
        }

        public ObjetivosAlumno build() {
            ObjetivosAlumno oTxObjCurso = new ObjetivosAlumno();
            oTxObjCurso.items = items;
            oTxObjCurso.alumno = alumno;
            return oTxObjCurso;
        }
    }



    @Override
    public List<TitledItemObjetivo> getResultados() {
        return Stream.of(new TitledItemObjetivo(items, alumno)).collect(Collectors.toList());
    }
}

package cl.eos.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cl.eos.persistence.models.EjeTematico;
import cl.eos.persistence.models.Habilidad;
import cl.eos.persistence.models.Objetivo;
import javafx.scene.image.Image;

public class ItemList {
    public static class Builder {
        private String question;
        private EjeTematico thematic;
        private Habilidad hability;
        private Objetivo objetive;
        private List<String> alternatives;
        private List<Image> images;
        private String rightAnswer;

        public Builder hability(Habilidad hability) {
            this.hability = hability;
            return this;
        }

        public Builder alternatives(List<String> alternatives) {
            this.alternatives = alternatives;
            return this;
        }

        public ItemList build(int nro) {
            final ItemList itemList = new ItemList(nro);
            itemList.question = question;
            itemList.thematic = thematic;
            itemList.hability = hability;
            itemList.objetive = objetive;
            itemList.alternatives = alternatives;
            itemList.images = images;
            itemList.rightAnswer = rightAnswer;
            return itemList;
        }

        public Builder images(List<Image> images) {
            this.images = images;
            return this;
        }

        public Builder objetive(Objetivo objetive) {
            this.objetive = objetive;
            return this;
        }

        public Builder question(String question) {
            this.question = question;
            return this;
        }

        public Builder rightAnswer(String rightAnswer) {
            this.rightAnswer = rightAnswer;
            return this;
        }

        public Builder thematic(EjeTematico thematic) {
            this.thematic = thematic;
            return this;
        }
    }

    int nro = 0;
    String question;
    EjeTematico thematic;
    Habilidad hability;
    Objetivo objetive;
    List<String> alternatives;
    List<Image> images;

    String rightAnswer;

    public ItemList() {
        this.nro = 1;
        alternatives = Stream.generate(String::new).limit(5).collect(Collectors.toList());
        images = new ArrayList<>(5);
        for (int n = 0; n < 5; n++)
            images.add(null);
    }
    
    public ItemList(int nro) {
        this.nro = nro;
        alternatives = Stream.generate(String::new).limit(5).collect(Collectors.toList());
        images = new ArrayList<>(5);
        for (int n = 0; n < 5; n++)
            images.add(null);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("Pegunta %02d", nro));
        return builder.toString();
    }
}

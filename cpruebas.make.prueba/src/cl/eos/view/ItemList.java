package cl.eos.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cl.eos.restful.tables.R_Ejetematico;
import cl.eos.restful.tables.R_Habilidad;
import cl.eos.restful.tables.R_Objetivo;
import javafx.scene.image.Image;

public class ItemList {
  public ItemList() {
    rightAnswer = "A";
  }

  
  public static class Builder {
    private String question;
    private R_Ejetematico thematic;
    private R_Habilidad ability;
    private R_Objetivo objetive;
    private List<String> alternatives;
    private List<Image> images;
    private String rightAnswer;

    
    
    public Builder hability(R_Habilidad ability) {
      this.ability = ability;
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
      itemList.ability = ability;
      itemList.objetive = objetive;
      itemList.alternatives = alternatives;
      if(itemList.alternatives == null || itemList.alternatives.size() == 0)
      {
        alternatives = Stream.generate(String::new).limit(5).collect(Collectors.toList());
      }
      itemList.images = images;
      itemList.rightAnswer = rightAnswer;
      return itemList;
    }

    public Builder images(List<Image> images) {
      this.images = images;
      return this;
    }

    public Builder objetive(R_Objetivo objetive) {
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

    public Builder thematic(R_Ejetematico thematic) {
      this.thematic = thematic;
      return this;
    }
  }

  int nro = 0;
  String question;
  R_Ejetematico thematic;
  R_Habilidad ability;
  R_Objetivo objetive;
  List<String> alternatives;
  List<Image> images;

  String rightAnswer;

  public ItemList(int nro) {
    this.nro = nro;
    alternatives = Stream.generate(String::new).limit(5).collect(Collectors.toList());
    rightAnswer="A";
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

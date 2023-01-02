package at.bif3.swe1.kisin.httpServer.serializer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardData {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("Name")
    private String Name;

    @JsonProperty("Damage")
    private Integer Damage;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getDamage() {
        return Damage;
    }

    public void setDamage(Integer damage) {
        Damage = damage;
    }

    @Override
    public String toString(){
        return "ID: " + this.Id + "\nName: " + this.Name + "\nDamage: " + this.Damage;
    }
}

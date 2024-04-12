package EngineStuff;

import java.util.UUID;

public abstract class Service {
    private UUID id;
    private String name;
    private String description;
    private Float price;
    private Location location;
    private Language[] languages;
    private boolean childSafe;
    private boolean available;

    enum Language {
        ICELANDIC,
        ENGLISH,
        FRENCH,
        GERMAN,
        POLISH
    }

  public UUID getId(){return this.id;}
  public String getName(){return this.name;}
  public String getDescription(){return this.description;}
  public Float getPrice(){return this.price;}
  public Location getLocation(){return this.location;}
  public boolean getChildSafe(){return this.childSafe;}
  public Language[] getLanguages(){return this.languages;}
  public boolean getAvailable(){return this.available;}

  public void setId(UUID id){this.id = id;}
  public void setName(String name){this.name = name;}
  public void setDescription(String description){this.description = description;}
  public void setPrice(Float price){this.price = price;}
  public void setLocation(Location location){this.location = location;}
  public void setLanguages (Language[] languages){this.languages = languages;}
  public void setChildSafe(boolean childSafe){this.childSafe = childSafe;}
  public void setAvailable(boolean available){this.available = available;}
}

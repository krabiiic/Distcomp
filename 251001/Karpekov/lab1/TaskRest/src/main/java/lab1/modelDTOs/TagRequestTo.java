package lab1.modelDTOs;


public class TagRequestTo {
    private long Id;
    private String Name = "";
   
    public TagRequestTo() {
    }

    public TagRequestTo(long id, String name) {
        Id = id;
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}

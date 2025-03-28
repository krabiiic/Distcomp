package lab1.modelDTOs;


public class TagResponseTo {
    private long Id;
    private String Name = "";

    public TagResponseTo() {
    }

    public TagResponseTo(long id, String name) {
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

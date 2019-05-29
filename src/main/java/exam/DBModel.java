package exam;

public class DBModel {
    private int idModel;
    private String name;

    public DBModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIdModel() {
        return idModel;
    }
}

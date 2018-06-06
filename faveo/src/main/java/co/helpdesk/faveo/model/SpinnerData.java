package co.helpdesk.faveo.model;

/**
 * Created by narendra on 10/03/17.
 * Model class for spinner data.
 */

public class SpinnerData {
    private String id;
    private String name;

    public SpinnerData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpinnerData) {
            SpinnerData c = (SpinnerData) obj;
            if (c.getName().equals(name) && c.getId() == id) return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}

package co.helpdesk.faveo.model;

/**
 * Created by narendra on 26/05/17.
 * Model class for displaying the data in spinner list.
 */

public class Data {

    public int ID;
    public String name;

    public Data(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;            // What to display in the Spinner list.
    }

}

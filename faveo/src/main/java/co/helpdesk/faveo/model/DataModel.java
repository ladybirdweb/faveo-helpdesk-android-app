package co.helpdesk.faveo.model;

/**
 * Created by Lenovo on 6/29/2017.
 */

public class DataModel {

    public int icon;
    public String name;
    private String count;

    public DataModel(int icon, String name, String count) {
        this.icon = icon;
        this.name = name;
        this.count = count;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}

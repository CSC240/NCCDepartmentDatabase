package edu.ncc.nccdepartmentdatabase;
public class DepartmentEntry {
    private long id;
    private String name;
    private String location;
    private String phone;

    public DepartmentEntry()
    {

    }

    public DepartmentEntry(String name, String location, String phone)
    {
        this.name = name;
        this.location = location;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean equals(Object otherDept)
    {
        return this.id == ((DepartmentEntry)otherDept).id;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return id + ": " + name + " - " + phone + " - " + location;
    }
}


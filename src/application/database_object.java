package application;

public class database_object {

    int ID;
    String name;
    String table;

    public database_object()
    {
        ID=0;
        name=null;
        table=null;
    }

    public database_object(int ID, String name, String table)
    {
        this.ID=ID;
        this.name=name;
        this.table=table;
    }

    public String getTable()
    {
        return table;
    }

    public String getName()
    {
        return name;
    }

    public int getID()
    {
        return ID;
    }
}

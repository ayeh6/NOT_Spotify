package application;

public class database_object {

    int ID;
    String songname;
    String albumname;
    String artistname;
    String genrename;
    String playlistname;
    String username;
    String table;
    String songlanguage;
//user stuff
    String fullname;
    int age;
    String country;
    boolean admin;


    public database_object()
    {
        ID=0;
        songname=null;
        albumname=null;
        artistname=null;
        genrename=null;
        playlistname=null;
        username=null;
        table=null;
        songlanguage=null;

        fullname=null;
        age=0;
        country=null;
        admin=false;
    }

    public database_object(int ID, String songname, String albumname, String artistname, String genrename, String playlistname, String username, String table, String songlanguage)
    {
        this.ID=ID;
        this.songname=songname;
        this.albumname=albumname;
        this.artistname=artistname;
        this.genrename=genrename;
        this.playlistname=playlistname;
        this.username=username;
        this.table=table;
        this.songlanguage=songlanguage;

        fullname=null;
        age=0;
        country=null;
        admin=false;
    }

    public void setUser(int ID, String username, String fullname, int age, String country, int admin)
    {
        this.ID = ID;
        this.username = username;
        this.fullname = fullname;
        this.age = age;
        this.country = country;
        if(admin==0)
        {
            this.admin = false;
        }
        else if(admin==1)
        {
            this.admin = true;
        }
    }

    public int getID()
    {
        return ID;
    }

    public String getTable()
    {
        return table;
    }

    public String getSongname()
    {
        return songname;
    }

    public String getAlbumname() {
        return albumname;
    }

    public String getArtistname() {
        return artistname;
    }

    public String getGenrename() {
        return genrename;
    }

    public String getPlaylistname() {
        return playlistname;
    }

    public String getUsername() {
        return username;
    }

    public String getSonglanguage() {
        return songlanguage;
    }

    public String getFullname() {
        return fullname;
    }

    public int getAge() {
        return age;
    }

    public String getCountry() {
        return country;
    }

    public boolean isAdmin() {
        return admin;
    }
}

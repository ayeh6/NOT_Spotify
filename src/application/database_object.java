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

    String id_param;


    public database_object() {
        ID = 0;
        songname = null;
        albumname = null;
        artistname = null;
        genrename = null;
        playlistname = null;
        username = null;
        table = null;
        songlanguage = null;

        fullname = null;
        age = 0;
        country = null;
        admin = false;

        id_param = null;
    }

    public database_object(int ID, String songname, String albumname, String artistname, String genrename, String playlistname, String username, String table, String songlanguage) {
        this.ID = ID;
        this.songname = songname;
        this.albumname = albumname;
        this.artistname = artistname;
        this.genrename = genrename;
        this.playlistname = playlistname;
        this.username = username;
        this.table = table;
        this.songlanguage = songlanguage;

        fullname = null;
        age = 0;
        country = null;
        admin = false;

        if (table.equals("songs")) {
            id_param = "s_songID";
        } else if (table.equals("albums")) {
            id_param = "al_albID";
        } else if (table.equals("artists")) {
            id_param = "ar_artID";
        } else if (table.equals("genres")) {
            id_param = "g_genID";
        } else if (table.equals("playlists")) {
            id_param = "p_playlistID";
        } else if (table.equals("users")) {
            id_param = "u_userID";
        }

    }

    public void setUser(int ID, String username, String fullname, int age, String country, int admin, String table) {
        this.ID = ID;
        this.username = username;
        this.fullname = fullname;
        this.age = age;
        this.country = country;
        if (admin == 0) {
            this.admin = false;
        } else if (admin == 1) {
            this.admin = true;
        }
        this.table = table;
        id_param = "u_userID";
    }

    public int getID() {
        return ID;
    }

    public String getTable() {
        return table;
    }

    public String getSongname() {
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

    public String getId_param() {
        return id_param;
    }
}

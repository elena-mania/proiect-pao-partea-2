DROP TABLE IF EXISTS dbproiectpao.album_song;
DROP TABLE IF EXISTS dbproiectpao.playlist_song;
DROP TABLE IF EXISTS dbproiectpao.song;
DROP TABLE IF EXISTS dbproiectpao.album;
DROP TABLE IF EXISTS dbproiectpao.artist;
DROP TABLE IF EXISTS dbproiectpao.playlist;
DROP TABLE IF EXISTS dbproiectpao.listener;


CREATE TABLE dbproiectpao.listener (
                                       id INT NOT NULL,
                                       name VARCHAR(100) NULL,
                                       email VARCHAR(100) NULL,
                                       password VARCHAR(100) NULL,
                                       hasPaid BOOLEAN,
                                       monthlySubscription DOUBLE DEFAULT 12.99,
                                       PRIMARY KEY (id)
);
CREATE TABLE dbproiectpao.artist (
                                     id INT NOT NULL,
                                     name VARCHAR(100) NULL,
                                     email VARCHAR(100) NULL,
                                     password VARCHAR(100) NULL,
                                     genre VARCHAR(50) NULL,
                                     monthlyEarnings DOUBLE NULL,
                                     PRIMARY KEY (id)
);
CREATE TABLE dbproiectpao.album (
                                    id INT NOT NULL,
                                    releaseDate VARCHAR(50) NULL,
                                    genre VARCHAR(50) NULL,
                                    title VARCHAR(50) NULL,
                                    userId INT NOT NULL,
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (userId) REFERENCES artist(id)
);

CREATE TABLE dbproiectpao.playlist (
                                       id INT NOT NULL,
                                       userId INT NOT NULL,
                                       PRIMARY KEY (id),
                                       FOREIGN KEY (userId) REFERENCES listener(id)
);

CREATE TABLE dbproiectpao.song (
                                   id INT NOT NULL,
                                   idArtist INT NOT NULL,
                                   length DOUBLE NULL,
                                   title VARCHAR(100) NULL,
                                   PRIMARY KEY (id),
                                   FOREIGN KEY (idArtist) REFERENCES artist(id)
);

CREATE TABLE dbproiectpao.album_song (
                                         idAlbum INT NOT NULL,
                                         idSong INT NOT NULL,
                                         PRIMARY KEY (idAlbum, idSong),
                                         FOREIGN KEY (idAlbum) REFERENCES album(id),
                                         FOREIGN KEY (idSong) REFERENCES song(id)
);

CREATE TABLE dbproiectpao.playlist_song (
                                            idPlaylist INT NOT NULL,
                                            idSong INT NOT NULL,
                                            PRIMARY KEY (idPlaylist, idSong),
                                            FOREIGN KEY (idPlaylist) REFERENCES playlist(id),
                                            FOREIGN KEY (idSong) REFERENCES song(id)
);

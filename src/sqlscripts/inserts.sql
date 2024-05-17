insert into dbproiectpao.artist (id,name,email,password,genre,monthlyEarnings)
values (1,'Madonna','maddy@gmail.com','password','pop',100);
insert into dbproiectpao.artist (id,name,email,password,genre,monthlyEarnings)
values (2,'Bon jovi','john@jovi.com','pa55','rock',200);
insert into dbproiectpao.artist (id,name,email,password,genre,monthlyEarnings)
values (3,'Roxette','roxette@yahoo.com','pass','rock',75);
insert into dbproiectpao.artist (id,name,email,password,genre,monthlyEarnings)
values (4,'Pink floyd','pink@floyd.uk','p1nk','psychedelic rock',80);
insert into dbproiectpao.artist (id,name,email,password,genre,monthlyEarnings)
values (5,'Metallica','metal@ica.co','metal','heay metal',73);
insert into dbproiectpao.artist (id,name,email,password,genre,monthlyEarnings)
values (6,'Lady Gaga','lady@gaga.ru','ldygg','pop',54);
insert into dbproiectpao.artist (id,name,email,password,genre,monthlyEarnings)
values (7,'Parazitii','cheloo@yahoo.ro','parazitii','hip-hop',38);
insert into dbproiectpao.artist (id,name,email,password,genre,monthlyEarnings)
values (8,'Salt n Pepa','record@label.fr','parole','hip-hop',26);

insert into dbproiectpao.listener (id, name, email, password, hasPaid)
values(9,'admin','admin@yahoo.com','admin',true);
insert into dbproiectpao.listener (id, name, email, password, hasPaid)
values(10,'ana-maria','anutza@gmail.com','pass',true);

insert into dbproiectpao.song (id,idArtist,length,title)
values (1,4,6.50,'Time');
insert into dbproiectpao.song (id,idArtist,length,title)
values (2,4,44.4,'Money');
insert into dbproiectpao.song (id,idArtist,length,title)
values (3,1,4.54,'Vogue');
insert into dbproiectpao.song (id,idArtist,length,title)
values (4,1,5.43,'Like a Prayer');
insert into dbproiectpao.song (id,idArtist,length,title)
values (5,6,3.58,'Poker Face');

insert into dbproiectpao.album (id,releaseDate,genre,title,userId)
values (1,'01/03/1973','psychedelic rock','The dark side of the moon',4);




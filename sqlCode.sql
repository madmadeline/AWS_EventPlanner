drop table ChoiceAltMatch;
drop table Choice;
drop table Feedback;
drop table Report;
drop table Review;
drop table User;
drop table Alternative;


create table User(
username varchar(30),
password varchar(30),
isAdmin bool,
PRIMARY Key(username)
);
create table Alternative(
id varchar(10),
numLikes int,
numDislikes int,
description varchar(60),
primary key(id)
);
create table Feedback(
altID varchar(10),
userID varchar(30),
message varchar(60),
timeStamp date,
foreign key (altID) references Alternative(id),
foreign key (userID) references User(username)
);
create table Review(
reviewer varchar(30),
altID varchar(5),
foreign key (reviewer) references User(username),
foreign key (altID) references Alternative(id)
);
create table Report(
id varchar(10),
reporter varchar(30),
dateOfCompletion date,
isComplete bool,
primary key(id),
foreign key(reporter) references User(username)
);
create table Choice(
id varchar(10),
description varchar(60),
dateOfCreation datetime,
winningAlt varchar(10),
primary key(id),
foreign key(winningAlt) references Alternative(id)
);
create table ChoiceAltMatch(
choiceID varchar(10),
altID varchar(10),
foreign key(choiceID) references Choice(id),
foreign key(altID) references Alternative(id)
);
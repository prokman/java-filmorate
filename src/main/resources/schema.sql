CREATE TABLE IF NOT EXISTS "FILMS" (
	"FILM_ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	"NAME" VARCHAR NOT NULL,
	"DESCRIPTIONS" VARCHAR NOT NULL,
	"RELEASEDATE" DATE NOT NULL,
	"DURATION" INTEGER NOT NULL,
	"MPA_ID" INTEGER NOT NULL,
	PRIMARY KEY("FILM_ID")
);


CREATE TABLE IF NOT EXISTS "FILM_GENRE" (
	"FILM_ID" INTEGER NOT NULL,
	"GENRE_ID" INTEGER NOT NULL,
	PRIMARY KEY("FILM_ID", "GENRE_ID")
);


CREATE TABLE IF NOT EXISTS "GENRE" (
	"GENRE_ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	"GENRE_NAME" VARCHAR NOT NULL,
	PRIMARY KEY("GENRE_ID")
);


CREATE TABLE IF NOT EXISTS "MPARATING" (
	"MPA_ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	"MPA_NAME" VARCHAR NOT NULL,
	PRIMARY KEY("MPA_ID")
);


CREATE TABLE IF NOT EXISTS "USERS" (
	"USER_ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	"NAME" VARCHAR NOT NULL,
	"LOGIN" VARCHAR NOT NULL,
	"EMAIL" VARCHAR NOT NULL,
	"BIRTHDAY" DATE NOT NULL,
	PRIMARY KEY("USER_ID")
);


CREATE TABLE IF NOT EXISTS "FRIENDS" (
	"USER_ID" INTEGER NOT NULL,
	"FRIEND_ID" INTEGER NOT NULL,
	PRIMARY KEY("USER_ID", "FRIEND_ID")
);


CREATE TABLE IF NOT EXISTS "FILM_LIKES" (
	"FILM_ID" INTEGER NOT NULL,
	"USER_ID" INTEGER NOT NULL,
	PRIMARY KEY("FILM_ID", "USER_ID")
);

ALTER TABLE "FILMS" ADD CONSTRAINT IF NOT EXISTS "FK_FILM_MPA_ID" FOREIGN KEY("MPA_ID")
REFERENCES "MPARATING" ("MPA_ID");

ALTER TABLE "FILM_GENRE" ADD CONSTRAINT IF NOT EXISTS "FK_FILM_GENRE_FILM_ID" FOREIGN KEY("FILM_ID")
REFERENCES "FILMS" ("FILM_ID");

ALTER TABLE "FILM_GENRE" ADD CONSTRAINT IF NOT EXISTS "FK_FILM_GENRE_GENRE_ID" FOREIGN KEY("GENRE_ID")
REFERENCES "GENRE" ("GENRE_ID");

ALTER TABLE "FRIENDS" ADD CONSTRAINT IF NOT EXISTS "FK_FRIENDS_USER_ID" FOREIGN KEY("USER_ID")
REFERENCES "USERS" ("USER_ID");

ALTER TABLE "FRIENDS" ADD CONSTRAINT IF NOT EXISTS "FK_FRIENDS_FRIEND_ID" FOREIGN KEY("FRIEND_ID")
REFERENCES "USERS" ("USER_ID");

ALTER TABLE "FILM_LIKES" ADD CONSTRAINT IF NOT EXISTS "FK_FILM_LIKES_FILM_ID" FOREIGN KEY("FILM_ID")
REFERENCES "FILMS" ("FILM_ID");

ALTER TABLE "FILM_LIKES" ADD CONSTRAINT IF NOT EXISTS  "FK_FILM_LIKES_USER_ID" FOREIGN KEY("USER_ID")
REFERENCES "USERS" ("USER_ID");
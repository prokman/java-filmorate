--рейтинг
MERGE INTO MPARATING (MPA_ID, MPA_NAME)
    VALUES (1,'G');
MERGE INTO MPARATING (MPA_ID, MPA_NAME)
    VALUES (2,'PG');
MERGE INTO MPARATING (MPA_ID, MPA_NAME)
    VALUES (3,'PG-13');
MERGE INTO MPARATING (MPA_ID, MPA_NAME)
    VALUES (4,'R');
MERGE INTO MPARATING (MPA_ID, MPA_NAME)
    VALUES (5,'NC-17');

--жанры
MERGE INTO GENRE (GENRE_ID, GENRE_NAME)
    VALUES (1,'Комедия');
MERGE INTO GENRE (GENRE_ID, GENRE_NAME)
    VALUES (2,'Драма');
MERGE INTO GENRE (GENRE_ID, GENRE_NAME)
    VALUES (3,'Мультфильм');
MERGE INTO GENRE (GENRE_ID, GENRE_NAME)
    VALUES (4,'Триллер');
MERGE INTO GENRE (GENRE_ID, GENRE_NAME)
    VALUES (5,'Документальный');
MERGE INTO GENRE (GENRE_ID, GENRE_NAME)
    VALUES (6,'Боевик');
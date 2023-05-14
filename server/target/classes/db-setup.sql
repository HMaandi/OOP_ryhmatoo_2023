CREATE TABLE IF NOT EXISTS tulemused (
    isikukood varchar(100) NOT NULL PRIMARY KEY,
    nimi varchar(100) NOT NULL,
    email varchar(100),
    tulemus numeric,
    uuendatud DATE);
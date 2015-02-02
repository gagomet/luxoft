CREATE TABLE IF NOT EXISTS BANKS (
    ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS CLIENTS (
    ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    BANK_ID BIGINT,
    NAME VARCHAR(255) NOT NULL,
    OVERDRAFT REAL,
    GENDER TINYINT(1) DEFAULT 1,
    EMAIL VARCHAR(50),
    PHONE VARCHAR(50),
    CITY VARCHAR(255),
    FOREIGN KEY(BANK_ID)
    REFERENCES BANKS(ID)
);

CREATE TABLE IF NOT EXISTS ACCOUNTS(
   ID  INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   CLIENT_ID INT NOT NULL,
   OVERDRAFT REAL,
   BALANCE REAL NOT NULL DEFAULT 0.00,
    FOREIGN KEY(CLIENT_ID)
    REFERENCES CLIENTS(ID),
    FOREIGN KEY(OVERDRAFT)
    REFERENCES CLIENTS(OVERDRAFT)
);

INSERT INTO BANKS VALUES (1, 'MyBank');

INSERT INTO CLIENTS VALUES (1, 1, 'Ivan Ivanov', 1000, 1, 'ivan@server.mail', +3801234567, 'Hatsapetovka');
INSERT INTO CLIENTS VALUES (2, 1, 'Petra Petrova', 1500, 0, 'petrova@girl.here', null, 'Odessa');

INSERT INTO ACCOUNTS VALUES (1, 1, 1000, -100);
INSERT INTO ACCOUNTS VALUES (2, 2, 1500, 2450);
INSERT INTO ACCOUNTS VALUES (3, 1, 1111, 2222);
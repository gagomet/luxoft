-- Create Banks table --
CREATE TABLE BANKS (
    ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL
);

-- Create Clients table --
CREATE TABLE CLIENTS (
    ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    BANK_ID BIGINT,
    NAME VARCHAR(255) NOT NULL,
    OVERDRAFT REAL,
    GENDER TINYINT(1),
    EMAIL VARCHAR(50),
    PHONE VARCHAR(50),
    CITY VARCHAR(255),
    FOREIGN KEY(BANK_ID)
    REFERENCES BANKS(ID)
);

-- Create Checked Accounts table --
   CREATE TABLE ACCOUNTS(
   ID  INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   CLIENT_ID INT NOT NULL,
   OVERDRAFT REAL,
   BALANCE REAL NOT NULL DEFAULT 0.00,
    FOREIGN KEY(CLIENT_ID)
    REFERENCES CLIENTS(ID),
    FOREIGN KEY(OVERDRAFT)
    REFERENCES CLIENTS(OVERDRAFT)
);

-- Create CheckedAccount table --
CREATE TABLE C_ACCOUNTS(
   ID  BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   CLIENT_ID BIGINT NOT NULL,
   OVERDRAFT REAL NOT NULL,
   BALANCE DOUBLE NOT NULL DEFAULT 0.00,
    FOREIGN KEY(CLIENT_ID)
    REFERENCES CLIENTS(ID),
);

-- Create SavingAccount table --
CREATE TABLE S_ACCOUNTS(
   ID  BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   CLIENT_ID BIGINT NOT NULL,
   BALANCE REAL NOT NULL DEFAULT 0.00,
    FOREIGN KEY(CLIENT_ID)
    REFERENCES CLIENTS(ID),
);

-- stubs data --
INSERT INTO BANKS VALUES (1, 'MyBank')

INSERT INTO CLIENTS VALUES (8, 1, 'Barbie Girl', 4000, 0, 'pinkWorld@server.mail', null, 'Springfield')
INSERT INTO CLIENTS VALUES (7, 1, 'Master Splinter', 4000, 1, 'turtles@server.mail', null, 'Sewers')
INSERT INTO CLIENTS VALUES (6, 1, 'Dart Waider', NULL, 1, 'imperial@server.mail', +666666666, 'DeathStar')
INSERT INTO CLIENTS VALUES (5, 1, 'Homer Simpson', 5000, 1, 'ivan@server.mail', +3801234567, 'Springfileld')
INSERT INTO CLIENTS VALUES (4, 1, 'Shumacher Michael', 1000, 1, 'f1@server.mail', null, 'Springfileld')
INSERT INTO CLIENTS VALUES (3, 1, 'Sidor Sidorov', NULL, 1, 'sidor@server.mail', +3807654321, null)
INSERT INTO CLIENTS VALUES (2, 1, 'Petra Petrova', 1500, 0, 'petrova@girl.here', null, 'Odessa')
INSERT INTO CLIENTS VALUES (1, 1, 'Ivan Ivanov', 1000, 1, 'ivan@server.mail', +3801234567, 'Hatsapetovka')

INSERT INTO ACCOUNTS VALUES (1, 1, 1000, -100)
INSERT INTO ACCOUNTS VALUES (2, 2, 1500, 2450)
INSERT INTO ACCOUNTS VALUES (3, 3, NULL, 345)
INSERT INTO ACCOUNTS VALUES (4, 4, 1000, 35)
INSERT INTO ACCOUNTS VALUES (5, 5, 5000, 3045)
INSERT INTO ACCOUNTS VALUES (6, 6, NULL, -345)
INSERT INTO ACCOUNTS VALUES (7, 7, 4000, 25468)
INSERT INTO ACCOUNTS VALUES (8, 8, 4000, 345)

-- найти всех клиентов и отобразить остаток на их счетах --
SELECT
    CLIENTS.NAME AS CLIENT,
    ACCOUNTS.BALANCE AS BALANCE
FROM CLIENTS JOIN ACCOUNTS
ON (CLIENTS.ID=ACCOUNTS.CLIENT_ID)
;

-- найти всех клиентов-должников, сортировать по размеру долга --
SELECT
    CLIENTS.NAME AS CLIENT,
    ACCOUNTS.BALANCE AS BALANCE
FROM CLIENTS JOIN ACCOUNTS
ON (CLIENTS.ID=ACCOUNTS.CLIENT_ID)
WHERE(ACCOUNTS.OVERDRAFT<>NULL AND ACCOUNTS.BALANCE<0) ORDER BY ACCOUNTS.BALANCE ASC
;

-- вывести всех клиентов, чьи вклады превышают 1000 --
SELECT
    CLIENTS.NAME AS CLIENT,
    ACCOUNTS.BALANCE AS BALANCE
FROM CLIENTS JOIN ACCOUNTS
    ON (CLIENTS.ID=ACCOUNTS.CLIENT_ID)
    WHERE (ACCOUNTS.BALANCE>1000) ORDER BY ACCOUNTS.BALANCE DESC
;

-- положить деньги на накопительный счет клиента, сняв со сберегательного --
--We can do it with using 2 UPDATE statements and hardcoding account and transaction data like this:--
UPDATE ACCOUNTS SET ACCOUNTS.BALANCE=BALANCE-100 WHERE (ACCOUNTS.BALANCE>=100) AND (ACCOUNTS.OVERDRAFT IS NULL ) AND (ACCOUNTS.ID=1);
UPDATE ACCOUNTS SET ACCOUNTS.BALANCE=BALANCE+100 WHERE ACCOUNTS.ID=2

--But most universally is to use the SQL stored procedure with arguments like this:--

DELIMITER $$
CREATE PROCEDURE 'MOVE TO DEPOSIT'(
IN 'SENDER_ID' INT,
IN 'RECEPIENT_ID' INT,
IN 'SUMM' REAL UNSIGNED
) MODIFIES SQL DATA
BEGIN
 START TRANSACTION
  SET @is_allowed = FALSE;
  SELECT SUMM > 0 AND BALANCE >= SUMM
  INTO @is_allowed
  FROM ACCOUNTS
  WHERE ID = SENDER_ID
  FOR UPDATE;

  IF @is_allowed IS TRUE THEN
   UPDATE ACCOUNTS SET ACCOUNTS.BALANCE=BALANCE-SUMM WHERE ACCOUNTS.ID=SENDER_ID
   UPDATE ACCOUNTS SET ACCOUNTS.BALANCE=BALANCE+SUMM WHERE ACCOUNTS.ID=RECEPIENT_ID
   COMMIT;
  ELSE
   ROLLBACK;
  END IF;
END$$


--On H2 engine stored procedures creates using Java classes (http://www.h2database.com/html/features.html#user_defined_functions) --
--Java code injecting into INFORMATION_SCHEMA.FUNCTION_ALIASES in H2 through H2 console annotated as @CODE --
--optionally I purpose to adding the logging of all transactions into TRANSACTIONS table
-- this one without prepared statements (working) --
--we can use this user definde function by calling CALL TODEPOSITE(amountToTransfer, senderAccountID, recipientAccountID)--
DROP ALIAS IF EXISTS ToDeposite;
CREATE ALIAS ToDeposite AS $$
	import java.sql.Statement;
	@CODE
    String addMoneyToDeposite(java.sql.Connection con, float summ, long SENDER_ID, long RECIPIENT_ID) throws Exception {
        Float senderAccountSumm=null;
        Float senderAccountOverdraft=null;
        String updateAccount = "UPDATE ACCOUNTS SET ACCOUNTS.BALANCE=BALANCE-" + summ + " WHERE ACCOUNTS.ID=" + SENDER_ID;
        String updateDeposit = "UPDATE ACCOUNTS SET ACCOUNTS.BALANCE=BALANCE+" + summ + " WHERE ACCOUNTS.ID=" + RECIPIENT_ID;
        String transactionSuccess = "INSERT INTO TRANSACTIONS(SENDER_ID, RECIPIENT_ID, SUMM, DATE, STATUS) VALUES(" +
                " " + SENDER_ID +", " + RECIPIENT_ID +", " + summ +", CURRENT_TIMESTAMP, 1)";
        String transactionFail = "INSERT INTO TRANSACTIONS(SENDER_ID, RECIPIENT_ID, SUMM, DATE, STATUS) VALUES(" +
                " " + SENDER_ID +", " + RECIPIENT_ID +", " + summ +", CURRENT_TIMESTAMP, 0)";
        java.sql.ResultSet rs = con.createStatement().executeQuery(
                "SELECT BALANCE, OVERDRAFT FROM ACCOUNTS WHERE ACCOUNTS.ID = "+SENDER_ID);
        if (rs.next()) {
            senderAccountSumm = rs.getFloat("BALANCE");
        }
        Statement statementTransaction = con.createStatement();
        if(senderAccountSumm>=summ){
            con.setAutoCommit(false);
            Statement statementAccount = con.createStatement();
            Statement statementDeposit = con.createStatement();
            statementTransaction.executeUpdate(transactionSuccess);
            statementAccount.executeUpdate(updateAccount);
            statementDeposit.executeUpdate(updateDeposit);
            con.commit();
            con.setAutoCommit(true);
            return "Updated";
        }
        statementTransaction.executeUpdate(transactionFail);
        return "Not updated";
    }
    $$;

-- CREATE TRANSACTIONS table --
CREATE TABLE IF NOT EXISTS TRANSACTIONS (
  ID BIGINT NOT NULL AUTO_INCREMENT,
  SENDER_ID bigINT NOT NULL,
  RECIPIENT_ID BIGINT NOT NULL,
  SUMM REAL NOT NULL DEFAULT 0.00,
  DATE TIMESTAMP NOT NULL,
  STATUS TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (ID),
  UNIQUE INDEX ID_UNIQUE (ID ASC))



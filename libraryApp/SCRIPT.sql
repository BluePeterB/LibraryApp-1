UPDATE patron
SET loan_id = 0;

DELETE FROM loan
WHERE loan_id = 17;

UPDATE patron
SET loan_id = 8
WHERE patron_id = 30;

ALTER TABLE patron
DROP COLUMN peter_bessey;

ALTER TABLE patron RENAME COLUMN patron_id TO patronId;

INSERT INTO loan(loan_id, book_id, date_in, date_out, patron_id)
VALUES(91, 92, null, "8/5/2021", 93);

UPDATE loan
SET date_in = ""
WHERE loan_id = 91;

ALTER TABLE loan RENAME COLUMN id TO loan_id;

ALTER TABLE patron RENAME COLUMN id TO patron_id;

ALTER TABLE book
DROP COLUMN genre_id;

CREATE TABLE Aatron (
   patron_id INT AUTO_INCREMENT PRIMARY KEY,
   first_name VARCHAR(255),
   last_name VARCHAR(255),
   loan_id INT
);

CREATE TABLE loan (
   loan_id INT AUTO_INCREMENT PRIMARY KEY,
   patron_id INT,
   date_out DATE,
   date_in DATE,
   book_id INT
);

INSERT INTO test_table_two(test_table_two_id, name)
VALUES(3, "test table two 3");

INSERT INTO test_table_one(test_table_one_id, name, test_table_two_test_table_two_id)
VALUES(4, "test table one name", 3);

INSERT INTO book(book_id, author_id, available, genre_id, isbn, title)
VALUES(6,	3,	1,	2,	464069772,	"The Alchemist");
VALUES(6,	3,	1,	2,	464069772,	"The Alchemist");
2,	2,	1,	3,	988535196,	"Romeo and Juliet"
1,2,1,23,978149413,"A Midsummer's Night Dream"
3,	1,	1,	1,	451664226	"The Golden Compass"
5,	1,	1,	1,	94387895,	"The Amber Spyglass"
6,	3,	1,	2,	464069772,	"The Alchemist"

SET FOREIGN_KEY_CHECKS = 0;


LOAD DATA INFILE "test.csv"
INTO TABLE book
COLUMNS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
ESCAPED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

SHOW VARIABLES LIKE "secure_file_priv"; 
# shows location of secure files

alter table book change available available varchar(1);

LOAD DATA INFILE "test-library-book.csv"
INTO TABLE book
COLUMNS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES;


LOAD DATA INFILE "test-library-bookAuthor.csv"
INTO TABLE book
COLUMNS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

LOAD DATA INFILE "test-library-bookGenre.csv"
INTO TABLE book
COLUMNS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

UPDATE book
SET available = 3
WHERE book_id = 35;


#  below used

SET FOREIGN_KEY_CHECKS = 0;   #Done

SHOW VARIABLES LIKE "secure_file_priv"; 
# shows location of secure files
# 'secure_file_priv', 'C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\'
# In C:\ProgramData\MySQL\MySQL Server 8.0\my.ini changed per next lines:
#   # Secure File Priv.
#   #secure-file-priv="C:/ProgramData/MySQL/MySQL Server 8.0/Uploads"
#   # In windows explorer this is "C:\ProgramData\MySQL\MySQL Server 8.0\Uploads"
#   # My csv files are at "C:\Users\peter\Documents\MyWebProgs\libraryApp (1)\libraryApp"
#   secure-file-priv=""C:/Users/peter/Documents/MyWebProgs/libraryApp (1)/libraryApp"
#   and restart MySQL
#


LOAD DATA INFILE "test-library-book.csv"
INTO TABLE book
COLUMNS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES;
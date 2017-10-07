DROP TABLE IF EXISTS MealNutrition;
DROP TABLE IF EXISTS Meals;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS DailyIntakes;

CREATE TABLE Users (
    id int NOT NULL AUTO_INCREMENT,
    username varchar(20),
    password varchar(20),
    age int,
    sex ENUM("M", "F"),
    PRIMARY KEY(id)
);

CREATE TABLE Meals (
    id int NOT NULL AUTO_INCREMENT,
    userId int,
    name varchar(30),
    PRIMARY KEY(id),
    FOREIGN KEY(userId) REFERENCES Users(id)
);

CREATE TABLE MealNutrition (
    mealId int,
    nutrientId int,
    amount float,
    PRIMARY KEY(mealId, nutrientId),
    FOREIGN KEY(mealId) REFERENCES Meals(id)
);

CREATE TABLE DailyIntakes (
    nutrientId int,
    lowerAge int,
    upperAge int,
    sex ENUM("M", "F"),
    amount float,
    PRIMARY KEY(nutrientId, lowerAge, upperAge, sex)
);

INSERT INTO DailyIntakes VALUES (301, 9, 18, "M", 1300);
INSERT INTO DailyIntakes VALUES (301, 9, 18, "F", 1300);
INSERT INTO DailyIntakes VALUES (301, 19, 70, "M", 1000);
INSERT INTO DailyIntakes VALUES (301, 19, 50, "F", 1000);
INSERT INTO DailyIntakes VALUES (301, 51, 300, "F", 1200);
INSERT INTO DailyIntakes VALUES (301, 71, 300, "M", 1200);

INSERT INTO DailyIntakes VALUES (203, 9, 13, "M", 34);
INSERT INTO DailyIntakes VALUES (203, 14, 18, "M", 52);
INSERT INTO DailyIntakes VALUES (203, 19, 300, "M", 56);
INSERT INTO DailyIntakes VALUES (203, 9, 13, "F", 34);
INSERT INTO DailyIntakes VALUES (203, 14, 300, "F", 46);

INSERT INTO DailyIntakes VALUES (320, 9, 13, "M", 600);
INSERT INTO DailyIntakes VALUES (203, 14, 300, "M", 900);
INSERT INTO DailyIntakes VALUES (203, 9, 13, "F", 600);
INSERT INTO DailyIntakes VALUES (203, 14, 300, "F", 700);

INSERT INTO DailyIntakes VALUES (401, 9, 13, "M", 45);
INSERT INTO DailyIntakes VALUES (401, 9, 13, "F", 45);
INSERT INTO DailyIntakes VALUES (401, 14, 18, "M", 75);
INSERT INTO DailyIntakes VALUES (401, 14, 18, "F", 65);
INSERT INTO DailyIntakes VALUES (401, 19, 300, "M", 90);
INSERT INTO DailyIntakes VALUES (401, 19, 300, "F", 75);

INSERT INTO DailyIntakes VALUES (324, 9, 70, "M", 15);
INSERT INTO DailyIntakes VALUES (324, 9, 70, "F", 15);
INSERT INTO DailyIntakes VALUES (324, 71, 300, "M", 20);
INSERT INTO DailyIntakes VALUES (324, 71, 300, "F", 20);

INSERT INTO DailyIntakes VALUES (323, 9, 13, "M", 11);
INSERT INTO DailyIntakes VALUES (323, 9, 13, "F", 11);
INSERT INTO DailyIntakes VALUES (323, 14, 300, "M", 15);
INSERT INTO DailyIntakes VALUES (323, 14, 300, "F", 15);
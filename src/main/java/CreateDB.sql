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

-- INSERT INTO DailyIntakes VALUES (301, 5, 8, "M", 5);
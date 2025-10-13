CREATE TYPE Color AS ENUM ('RED', 'ORANGE', 'WHITE', 'BROWN');
CREATE TYPE Position AS ENUM ('DIRECTOR', 'COOK', 'CLEANER', 'MANAGER_OF_CLEANING');
CREATE TYPE Status AS ENUM ('FIRED', 'RECOMMENDED_FOR_PROMOTION', 'REGULAR', 'PROBATION');
CREATE TYPE OrganizationType AS ENUM ('COMMERCIAL', 'GOVERNMENT', 'TRUST', 'PRIVATE_LIMITED_COMPANY', 'OPEN_JOINT_STOCK_COMPANY');
CREATE TYPE Country AS ENUM ('UNITED_KINGDOM', 'SPAIN', 'THAILAND');

CREATE TABLE Location(
    x DOUBLE PRECISION,
    y INT,
    z INT,
    name TEXT NOT NULL,
	PRIMARY KEY(x,y,z)
);

CREATE TABLE Address (
    street TEXT,
    zipCode TEXT CHECK (LENGTH(zipCode) >= 9),
    x DOUBLE PRECISION,
    y INT,
    z INT,
    PRIMARY KEY(x, y, z),
    FOREIGN KEY(x, y, z) REFERENCES Location(x, y, z)
);

CREATE TABLE Person (
    eyeColor VARCHAR(50),
    hairColor VARCHAR(50) NOT NULL,
    x DOUBLE PRECISION,
    y INT,
    z INT,
    passportID TEXT PRIMARY KEY CHECK(LENGTH(passportID) >= 6),
    nationality VARCHAR(50),
    FOREIGN KEY(x, y, z) REFERENCES Location(x, y, z)
);

CREATE TABLE Coordinates(
    x DOUBLE PRECISION,
    y INT,
    PRIMARY KEY(x, y)
);

CREATE TABLE Organization(
    id SERIAL PRIMARY KEY,
    x DOUBLE PRECISION NOT NULL,
    y INT NOT NULL,
    z INT NOT NULL,
    annualTurnover BIGINT CHECK (annualTurnover > 0),
    employeesCount BIGINT CHECK (employeesCount > 0),
    rating DOUBLE PRECISION CHECK (rating > 0),
    organizationType VARCHAR(50) NOT NULL,
    FOREIGN KEY(x, y, z) REFERENCES Address(x, y, z)
);

CREATE TABLE Worker(
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL CHECK (LENGTH(NAME) > 0),
    x DOUBLE PRECISION NOT NULL,
    y INT NOT NULL,
    creationDate DATE NOT NULL,
    organizationID INT,
    salary DOUBLE PRECISION CHECK (salary > 0),
    rating DOUBLE PRECISION CHECK (rating > 0),
    startDate DATE NOT NULL,
    position VARCHAR(50) NOT NULL,
    status VARCHAR(50),
    passportID TEXT,
    FOREIGN KEY(x, y) REFERENCES Coordinates(x,y),
    FOREIGN KEY(organizationID) REFERENCES Organization(id),
    FOREIGN KEY(passportID) REFERENCES Person(passportID)
); 

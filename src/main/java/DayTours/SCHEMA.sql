DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Booking;
DROP TABLE IF EXISTS Tours;

CREATE TABLE Users
	(
		userID VARCHAR(36) PRIMARY Key,
		name VARCHAR(128) NOT NULL,
		email VARCHAR(256),
		userType VARCHAR(128)
	);
CREATE TABLE Tours
            (
		        tourID	VARCHAR(36),
        		name VARCHAR(256),
        		description text,
        		price INTEGER NOT NULL,
        		category VARCHAR(128),
        		rating INTEGER CHECK (rating IN (0, 1, 2, 3, 4, 5)),
        		timedateTour DATETIME,
        		tourAddress VARCHAR(256),
        		familyFriendly BOOLEAN,
        		wheelchairAccesible BOOLEAN,
        		availability BOOLEAN,
        		capacity INTEGER,
        		PRIMARY KEY(tourID, name)

        	);
CREATE TABLE Booking
	(
		bookingID VARCHAR(36) PRIMARY Key,
		tourID VARCHAR(36),
		userID VARCHAR(36),
		pickUpLocation VARCHAR(256),
		FOREIGN KEY (tourID) REFERENCES Tours(TourID),
		FOREIGN KEY (userID) REFERENCES Users(UserID)
	);
	

CREATE MEMORY TABLE Member_Course_Registration (
  MemberID INT(11) NOT NULL,
  ReferMemberID INT(11),
  CourseID INT(11) NOT NULL,
  DateRegistered DATE);

CREATE MEMORY TABLE Member (
  MemberID INT(11) NOT NULL AUTO_INCREMENT,
  MemberFirstName VARCHAR(25) NOT NULL,
  MemberLastName VARCHAR(25) NOT NULL,
  MemberAddress VARCHAR(30),
  MemberAddressApartment VARCHAR(10),
  MemberCity VARCHAR(25),
  MemberState VARCHAR(2),
  MemberZipCode VARCHAR(5),
  MemberEntryDate DATE,
  MemberPhoneNumber CHAR(10),
  MemberEmailAddress VARCHAR(35),
  MemberIsActive TINYINT DEFAULT 1);

CREATE MEMORY TABLE `Charges` (
  `ChargeID` int(11) NOT NULL AUTO_INCREMENT,
  `ChargeAmount` decimal(19,2) DEFAULT NULL,
  `CourseID` int(11) DEFAULT NULL,
  `CycleStartDate` date DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `DiscountAmount` decimal(19,2) DEFAULT NULL,
  `MemberID` int(11) DEFAULT NULL,
  `MonthlyFinancesSummaryID` int(11) DEFAULT NULL,
  `NumChargeLines` int(11) DEFAULT NULL,
  `PaymentID` int(11) DEFAULT NULL,
  `SeasonID` int(11) DEFAULT NULL,
  `BillableUnitType` varchar(255) DEFAULT NULL,
  `BillableUnitsBilled` decimal(19,2) DEFAULT NULL,
PRIMARY KEY (`ChargeID`)
);

CREATE TABLE `Course` (
  `CourseID` int(11) NOT NULL AUTO_INCREMENT,
  `CourseEndDate` date DEFAULT NULL,
  `CourseName` varchar(255) NOT NULL,
  `CourseStartDate` date DEFAULT NULL,
  `CourseType` varchar(255) NOT NULL,
  `IsActive` bit(1) DEFAULT NULL,
  `NumEnrolled` int(11) DEFAULT NULL,
  `PricePerHour` decimal(19,2) DEFAULT NULL,
  `SeasonID` int(11) DEFAULT NULL,
  `TotalNumSessions` int(11) DEFAULT NULL,
  `EmployeeID` int(11) DEFAULT NULL,
  `BillableUnitDuration` decimal(19,2) DEFAULT NULL,
  `BillableUnitType` varchar(255) DEFAULT NULL,
  `ClassDuration` decimal(19,2) DEFAULT NULL,
  `MemberPricePerBillableUnit` decimal(19,2) NOT NULL,
  `NonMemberPricePerBillableUnit` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`CourseID`));

CREATE TABLE `Monthly_Finances_Summary` (
  `MonthlyFinancesSummaryID` int(11) NOT NULL AUTO_INCREMENT,
  `CycleStartDate` date DEFAULT NULL,
  `NumChargesFulfilled` int(11) DEFAULT NULL,
  `NumCourses` int(11) DEFAULT NULL,
  `NumMembers` int(11) DEFAULT NULL,
  `NumTotalCharges` int(11) DEFAULT NULL,
  `SeasonID` int(11) DEFAULT NULL,
  `TotalChargeAmount` decimal(19,2) DEFAULT NULL,
  `TotalPaymentAmount` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`MonthlyFinancesSummaryID`)
) ;

CREATE TABLE `Season` (
  `SeasonID` int(11) NOT NULL AUTO_INCREMENT,
  `EndDate` date DEFAULT NULL,
  `SeasonDescription` varchar(255) DEFAULT NULL,
  `SeasonTitle` varchar(255) DEFAULT NULL,
  `StartDate` date DEFAULT NULL,
  PRIMARY KEY (`SeasonID`)
);

CREATE TABLE `Member_Monthly_Registration` (
  `MemberMonthlyRegistrationID` int(11) NOT NULL AUTO_INCREMENT,
  `CycleStartDate` date DEFAULT NULL,
  `MemberID` int(11) DEFAULT NULL,
  `SeasonID` int(11) DEFAULT NULL,
  `MembershipCharge` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`MemberMonthlyRegistrationID`),
  CONSTRAINT `Member_Monthly_Registration_ibfk_1` FOREIGN KEY (`MemberID`) REFERENCES `Member` (`MemberID`) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO `User` (`username`, `password`, `enabled`)
    SELECT * FROM (SELECT 'admin', '', 1) AS tmp
    WHERE NOT EXISTS (SELECT `username` FROM `User` WHERE `username` = 'admin')
    LIMIT 1;

INSERT INTO `Authority` (`username`, `authority`)
   SELECT * FROM (SELECT 'admin', 'ROLE_ADMIN') AS tmp
   WHERE NOT EXISTS (SELECT `username` FROM `Authority` WHERE `username` = 'admin')
   LIMIT 1;

INSERT INTO `Member` (`MemberID`, `MemberFirstName`, `MemberLastName`)
    SELECT * FROM (SELECT 1 AS `MemberID`, '' AS `MemberFirstName`, '' AS `MemberLastName`) AS tmp
    WHERE NOT EXISTS (SELECT `MemberID` FROM `Member` WHERE `MemberID` = 1)
    LIMIT 1;

INSERT INTO `Season` (`SeasonID`, `StartDate`, `EndDate`, `SeasonTitle`)
    SELECT * FROM (SELECT 1 AS `SeasonID`, '0001-01-01' AS `StartDate`, '0001-01-01' AS `EndDate`, 'None' AS `SeasonTitle`) AS tmp
    WHERE NOT EXISTS(SELECT `SeasonID` FROM `Season` WHERE `SeasonID` = 1)
    LIMIT 1;

INSERT INTO `Course` (`CourseID`, `CourseName`, `CourseType`, `IsActive`, `NumEnrolled`, `TotalNumSessions`, `SeasonID`,`BillableUnitType`, `BillableUnitDuration`, `ClassDuration`, `MemberPricePerBillableUnit`, `NonMemberPricePerBillableUnit`)
    SELECT * FROM (SELECT 1 as `CourseID`, 'Other' AS `CourseName`, 'Other' AS `CourseType`, 0 As `IsActive`, 0 AS `NumEnrolled`, 0 AS `TotalNumSessions`, 1 AS `SeasonID`, 'session' AS `BillableUnitType`, 0 AS `BillableUnitDuration`, 0 AS `ClassDuration`, 0 AS `MemberPricePerBillableUnit`, 0 AS `NonMemberPricePerBillableUnit`) AS tmp
    WHERE NOT EXISTS (SELECT `CourseID` FROM `Course` WHERE `CourseID` = 1)
    LIMIT 1;

INSERT INTO `Course` (`CourseID`, `CourseName`, `CourseType`, `IsActive`, `NumEnrolled`, `TotalNumSessions`, `SeasonID`,`BillableUnitType`, `BillableUnitDuration`, `ClassDuration`, `MemberPricePerBillableUnit`, `NonMemberPricePerBillableUnit`)
    SELECT * FROM (SELECT 2 AS `CourseID`, 'Open Study' AS `CourseName`, 'Membership' AS `CourseType`, 1 AS `IsActive`, 0 AS `NumEnrolled`, 0 AS `TotalNumSessions`, 1 AS `SeasonID`, 'session' AS `BillableUnitType`, 1 AS `BillableUnitDuration`, 0 AS `ClassDuration`, 450 AS `MemberPricePerBillableUnit`, 450 AS `NonMemberPricePerBillableUnit`) AS tmp
    WHERE NOT EXISTS (SELECT `CourseID` FROM `Course` WHERE `CourseID` = 2 )
    LIMIT 1;

INSERT INTO `Payment` (`PaymentID`, `MemberID`, `PaymentAmount`, `DatePaymentReceived`, `CycleStartDate`)
    SELECT * FROM (SELECT 1 AS `PaymentID`, 1 AS `MemberID`, 0 `PaymentAmount`, '0001-01-01' AS `DatePaymentReceived`, '0001-01-01' AS `CycleStartDate`) AS tmp
    WHERE NOT EXISTS(SELECT `PaymentID` FROM `Payment` WHERE `PaymentID` = 1)
    LIMIT 1;




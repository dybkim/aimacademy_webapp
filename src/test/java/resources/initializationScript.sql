CREATE MEMORY TABLE Member_Course_Registration (MemberID INT(10) NOT NULL, ReferMemberID INT(10), CourseID INT(10) NOT NULL, DateRegistered DATE);
CREATE MEMORY TABLE Member (MemberID INT(10) NOT NULL AUTO_INCREMENT, MemberFirstName VARCHAR(25) NOT NULL, MemberLastName VARCHAR(25) NOT NULL, MemberAddress VARCHAR(30), MemberAddressApartment VARCHAR(10), MemberCity VARCHAR(25), MemberState VARCHAR(2), MemberZipCode VARCHAR(5), MemberEntryDate DATE, MemberPhoneNumber CHAR(10), MemberEmailAddress VARCHAR(35), MemberIsActive TINYINT DEFAULT 1);
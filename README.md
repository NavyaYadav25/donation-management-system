# Donation Management System

A Java Swing-based desktop application for managing donations between donors and NGOs. The application allows users to register donation details, categorize donations, and store records in an Oracle SQL database using JDBC.

## Features

- Register donor information
- Select hostel, donation type, and NGO
- Dynamic NGO filtering based on donation type
- Custom item entry for "Others" category
- Store donor and donation records in Oracle SQL
- Track donation quantity
- Display submitted donations in a table
- Input validation and error handling

## Technologies Used

- Java
- Java Swing
- JDBC
- Oracle SQL
- AWT

## Database

The project uses three database tables:

- Donor
- Donation
- Quantity_Log

Data is inserted using JDBC PreparedStatements to ensure secure database operations.

## How to Run

1. Install Java JDK.
2. Install Oracle Database (XE).
3. Create the required database tables.
4. Update the database credentials in the source code if necessary.
5. Compile and run:

```bash
javac DonationManagementGUI.java
java DonationManagementGUI
```

## Screenshots

You can add screenshots of the application interface here.

## Future Improvements

- User authentication
- Admin dashboard
- Search and filter donations
- Update and delete donation records
- Export donation reports
- Better UI using JavaFX

## Author

**Navya Yadav**

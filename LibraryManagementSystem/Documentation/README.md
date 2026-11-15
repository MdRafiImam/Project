\# Library Management System

\## Setup and Installation Guide



\### 1. Requirements

\- Java JDK/JRE (JDK 25 recommended)

\- XAMPP with MySQL/MariaDB

\- IntelliJ IDEA (for source code)



\### 2. Database Setup

1\. Install and open XAMPP.

2\. Start the MySQL service.

3\. Open phpMyAdmin in your browser.

4\. Create a database named `library\_management`.

5\. Select the database and open the Import tab.

6\. Import the `library\_management.sql` file from the project's Database folder.



\### 3. Application Setup

1\. Open the project's SourceCode folder.

2\. Locate the generated JAR file inside:

&#x20;  `LibraryManagementSystem/out/artifacts/librarymanagementsystem\_jar/`

3\. Keep `librarymanagementsystem.jar` and `mariadb-java-client-3.5.11.jar` together in the same folder.

4\. Ensure that Java is installed on the computer.



\### 4. Running the Application

Run the application using Java with the MariaDB JDBC Driver available in the same folder.



\### 5. Admin Credentials

\- Admin login credentials are stored in the database.

\- The Admin can change the username and password from the Admin Dashboard.

\- Keep the updated credentials secure.



\### 6. Important Notes

\- The database name must remain `library\_management`.

\- XAMPP MySQL/MariaDB service must be running before launching the application.

\- Do not delete the database backup file.

\- Keep the application JAR and JDBC Driver together.






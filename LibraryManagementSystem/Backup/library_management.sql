-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 09, 2026 at 10:33 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `library_management`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `admin_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`admin_id`, `username`, `password`, `full_name`) VALUES
(1, 'Admin', '120000:r6P7jsDLKKiRSmlWwQu7Mg==:9Ks/9ayHndRI/aCJO/25xptpYUU8WAZ/ST+huacoAi8=', 'Rafi Imam');

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `book_id` int(11) NOT NULL,
  `isbn` varchar(20) DEFAULT NULL,
  `title` varchar(200) NOT NULL,
  `author` varchar(150) NOT NULL,
  `publisher` varchar(150) DEFAULT NULL,
  `publication_year` int(11) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  `shelf_id` int(11) NOT NULL,
  `total_copies` int(11) NOT NULL DEFAULT 1,
  `available_copies` int(11) NOT NULL DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`book_id`, `isbn`, `title`, `author`, `publisher`, `publication_year`, `category_id`, `shelf_id`, `total_copies`, `available_copies`, `created_at`) VALUES
(1, '9780134685991', 'Effective Java', 'Joshua Bloch', 'Addison-Wesley', 2018, 1, 1, 5, 5, '2026-11-07 06:44:40'),
(2, '9780132350884', 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 2008, 4, 2, 4, 3, '2026-11-07 06:44:40'),
(3, '9780321751041', 'The Art of Computer Programming', 'Donald Knuth', 'Addison-Wesley', 2011, 6, 6, 3, 3, '2026-11-07 06:44:40'),
(4, '9780073523323', 'Database System Concepts', 'Abraham Silberschatz', 'McGraw-Hill', 2019, 2, 3, 5, 5, '2026-11-07 06:44:40'),
(5, '9780132126953', 'Computer Networking', 'James Kurose', 'Pearson', 2016, 3, 4, 4, 4, '2026-11-07 06:44:40'),
(6, '9780134610993', 'Artificial Intelligence: A Modern Approach', 'Stuart Russell', 'Pearson', 2020, 5, 5, 3, 3, '2026-11-07 06:44:40'),
(8, '9780134685990', 'Java Test Book Updated', 'Test Author', 'Test Publisher', 2024, 6, 4, 3, 3, '2026-11-07 11:28:38');

-- --------------------------------------------------------

--
-- Table structure for table `book_requests`
--

CREATE TABLE `book_requests` (
  `request_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `requested_title` varchar(200) NOT NULL,
  `requested_author` varchar(150) DEFAULT NULL,
  `reason` text DEFAULT NULL,
  `request_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` enum('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
  `admin_note` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `book_requests`
--

INSERT INTO `book_requests` (`request_id`, `student_id`, `requested_title`, `requested_author`, `reason`, `request_date`, `status`, `admin_note`) VALUES
(1, 2, 'Story Book', 'unknown', 'For reading', '2026-11-08 09:19:19', 'APPROVED', 'Done'),
(2, 8, 'Python King', 'unknown', 'Learn', '2026-11-08 09:37:57', 'REJECTED', 'Not found!');

-- --------------------------------------------------------

--
-- Table structure for table `borrow_records`
--

CREATE TABLE `borrow_records` (
  `borrow_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `borrow_date` date NOT NULL,
  `due_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `fine_amount` decimal(10,2) DEFAULT 0.00,
  `status` enum('BORROWED','RETURNED','OVERDUE') DEFAULT 'BORROWED'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `borrow_records`
--

INSERT INTO `borrow_records` (`borrow_id`, `student_id`, `book_id`, `borrow_date`, `due_date`, `return_date`, `fine_amount`, `status`) VALUES
(1, 2, 1, '2026-11-07', '2026-11-21', '2026-11-07', 0.00, 'RETURNED'),
(2, 10, 5, '2026-11-09', '2026-11-23', '2026-11-09', 0.00, 'RETURNED'),
(3, 6, 4, '2026-11-09', '2026-11-23', '2026-11-09', 0.00, 'RETURNED'),
(4, 6, 4, '2026-11-09', '2026-11-23', '2026-11-09', 0.00, 'RETURNED'),
(5, 3, 2, '2026-11-09', '2026-11-23', '2026-11-09', 0.00, 'RETURNED'),
(6, 6, 2, '2026-11-09', '2026-11-23', NULL, 0.00, 'BORROWED');

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `category_name`, `description`) VALUES
(1, 'Programming', 'Programming and Software Development Books'),
(2, 'Database', 'Database Management and SQL Books'),
(3, 'Networking', 'Computer Networking Books'),
(4, 'Software Engineering', 'Software Engineering Books'),
(5, 'Artificial Intelligence', 'AI and Machine Learning Books'),
(6, 'Computer Science', 'General Computer Science Books'),
(7, 'Comics', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `shelves`
--

CREATE TABLE `shelves` (
  `shelf_id` int(11) NOT NULL,
  `shelf_code` varchar(30) NOT NULL,
  `category_id` int(11) NOT NULL,
  `capacity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `shelves`
--

INSERT INTO `shelves` (`shelf_id`, `shelf_code`, `category_id`, `capacity`) VALUES
(1, 'A-101', 1, 50),
(2, 'A-102', 1, 50),
(3, 'B-101', 2, 40),
(4, 'B-102', 3, 45),
(5, 'C-101', 5, 50),
(6, 'C-102', 6, 50),
(8, 'D-101', 7, 30);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `student_id` int(11) NOT NULL,
  `student_id_number` varchar(30) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `registration_status` enum('PENDING','APPROVED','REJECTED','CANCELLED') NOT NULL DEFAULT 'PENDING',
  `membership_id` varchar(30) DEFAULT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `department` varchar(100) DEFAULT NULL,
  `join_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`student_id`, `student_id_number`, `password`, `registration_status`, `membership_id`, `full_name`, `email`, `phone`, `department`, `join_date`) VALUES
(2, '20255103046', '120000:7OcO9TqF0fTFmmajVbRu9Q==:Mkw9OHxXeX0fD64qcCsGwpyiy4tZ/zF/QpkhiPhZ9hc=', 'APPROVED', 'LIB-T-046', 'Taimur Jim', 'jim@gmail.com', '01333333333', 'CSE', '2026-11-08'),
(3, '20255103061', '120000:rqyw3tkWRpoFZ4q+sUmktA==:7pvArXl7YuhzjqZLBWPTJfFcBUJxOCFaywg5/y4h3Bs=', 'APPROVED', 'LIB-R-061', 'Md. Rafi Imam', 'rafi@gmail.com', '01300000000', 'CSE', '2026-11-08'),
(4, '20255103069', NULL, 'CANCELLED', NULL, 'Siam Khan', 'siam@gmail.com', '01345678990', 'CSE', '2026-11-07'),
(6, '20255103060', '120000:PuFCTX13TeURZuPQQdTp/Q==:zeMcd35g2epNaX9DbjPLGaTHsm6EifOVbEFpBhd6VIs=', 'APPROVED', 'LIB-S-060', 'Shimul Islam', 'shimul@gmail.com', '01344444444', 'CSE', '2026-11-09'),
(8, '20255103058', '120000:PoSCaOKcHp1oISqEJ6q/BQ==:qChHAurVXP+4u3k+awGCfXFBXhGW3if3snWYua0fFzo=', 'APPROVED', 'LIB-S-058', 'Showkotul Islam', 'sakin@gmail.com', '01300000002', 'CSE', '2026-11-08'),
(9, '20255103042', '120000:ICtQE8BHLGthmB20TgHTbw==:CXhhXKNkuryJ8XfTrWNIUYZuVklJN6AyGck03CeiHW0=', 'APPROVED', 'LIB-L-042', 'Luckman Rafique', 'ornob@gmail.com', '01344444444', 'CSE', '2026-11-08'),
(10, '20255103072', '120000:Qw3YCs5oJN26CnDfJlRj1g==:L79JXk881qrRw1eZ8DXVV/lkgTEbvKBZq1RYq2SVoe4=', 'APPROVED', 'LIB-F-072', 'Fahim Islam', 'fahim@gmail.com', '01609090909', 'CSE', '2026-11-08');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`admin_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`book_id`),
  ADD UNIQUE KEY `isbn` (`isbn`),
  ADD KEY `fk_book_category` (`category_id`),
  ADD KEY `fk_book_shelf` (`shelf_id`);

--
-- Indexes for table `book_requests`
--
ALTER TABLE `book_requests`
  ADD PRIMARY KEY (`request_id`),
  ADD KEY `fk_request_student` (`student_id`);

--
-- Indexes for table `borrow_records`
--
ALTER TABLE `borrow_records`
  ADD PRIMARY KEY (`borrow_id`),
  ADD KEY `fk_borrow_student` (`student_id`),
  ADD KEY `fk_borrow_book` (`book_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `category_name` (`category_name`);

--
-- Indexes for table `shelves`
--
ALTER TABLE `shelves`
  ADD PRIMARY KEY (`shelf_id`),
  ADD UNIQUE KEY `shelf_code` (`shelf_code`),
  ADD KEY `fk_shelf_category` (`category_id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`student_id`),
  ADD UNIQUE KEY `membership_id` (`membership_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `university_student_id` (`student_id_number`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `admin_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `book_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `book_requests`
--
ALTER TABLE `book_requests`
  MODIFY `request_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `borrow_records`
--
ALTER TABLE `borrow_records`
  MODIFY `borrow_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `shelves`
--
ALTER TABLE `shelves`
  MODIFY `shelf_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `student_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `books`
--
ALTER TABLE `books`
  ADD CONSTRAINT `fk_book_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`),
  ADD CONSTRAINT `fk_book_shelf` FOREIGN KEY (`shelf_id`) REFERENCES `shelves` (`shelf_id`);

--
-- Constraints for table `book_requests`
--
ALTER TABLE `book_requests`
  ADD CONSTRAINT `fk_request_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`);

--
-- Constraints for table `borrow_records`
--
ALTER TABLE `borrow_records`
  ADD CONSTRAINT `fk_borrow_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`),
  ADD CONSTRAINT `fk_borrow_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`);

--
-- Constraints for table `shelves`
--
ALTER TABLE `shelves`
  ADD CONSTRAINT `fk_shelf_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

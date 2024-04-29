CREATE DATABASE eLecture;
USE eLecture;

CREATE TABLE `Comments` (
  `Comment_ID` int(11) NOT NULL,
  `Content` text NOT NULL,
  `Post_ID` int(11) NOT NULL,
  `User_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `Posts` (
  `Post_ID` int(11) NOT NULL,
  `Content` text NOT NULL,
  `User_ID` int(11) NOT NULL,
  `Likes` int(11) DEFAULT 0,
  `Inappropriate` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `Users` (
  `User_ID` int(20) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `Role` varchar(100) NOT NULL CHECK (`Role` in ('Student','Lecturer'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `Comments`
  ADD PRIMARY KEY (`Comment_ID`),
  ADD KEY `Post_ID` (`Post_ID`),
  ADD KEY `User_ID` (`User_ID`);

ALTER TABLE `Posts`
  ADD PRIMARY KEY (`Post_ID`),
  ADD KEY `User_ID` (`User_ID`);

ALTER TABLE `Users`
  ADD PRIMARY KEY (`User_ID`);

ALTER TABLE `Comments`
  MODIFY `Comment_ID` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `Posts`
  MODIFY `Post_ID` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `Users`
  MODIFY `User_ID` int(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `Comments`
  ADD CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`Post_ID`) REFERENCES `Posts` (`Post_ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`User_ID`) REFERENCES `Users` (`User_ID`) ON DELETE CASCADE;

ALTER TABLE `Posts`
  ADD CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`User_ID`) REFERENCES `Users` (`User_ID`) ON DELETE CASCADE;

COMMIT;

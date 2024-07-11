use sql12715206;

-- drop table `users`;
-- drop table `authorities`;
-- drop table `customer`;

CREATE TABLE `customer` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mobile_number` varchar(20) NOT NULL,
  `pwd` varchar(500) NOT NULL,
  `role` varchar(100) NOT NULL,
  `create_dt` date DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
);

INSERT INTO `customer` (`name`,`email`,`mobile_number`, `pwd`, `role`,`create_dt`)
 VALUES ('Happy','happy@example.com','9876548337', '$2y$12$oRRbkNfwuR8ug4MlzH5FOeui.//1mkd.RsOAJMbykTSupVy.x/vb2', 'admin',CURDATE());

CREATE TABLE Product (
                         ID INT PRIMARY KEY,
                         Name VARCHAR(255) NOT NULL,
                         Description TEXT,
                         Price DECIMAL(10, 2) NOT NULL,
                         StockQuantity INT NOT NULL
);


CREATE TABLE `Order` (
                         ID INT PRIMARY KEY,
                         OrderDate DATE NOT NULL,
                         CustomerName VARCHAR(255) NOT NULL,
                         Address TEXT NOT NULL,
                         Email VARCHAR(255) NOT NULL,
                         PhoneNumber VARCHAR(15) NOT NULL,
                         Status VARCHAR(50) NOT NULL,
                         TotalAmount DECIMAL(10, 2) NOT NULL
);


CREATE TABLE OrderDetail (
                             ID INT PRIMARY KEY,
                             OrderID INT,
                             ProductID INT,
                             Quantity INT NOT NULL,
                             UnitPrice DECIMAL(10, 2) NOT NULL,
                             TotalPrice DECIMAL(10, 2) NOT NULL,
                             FOREIGN KEY (OrderID) REFERENCES `Order`(ID),
                             FOREIGN KEY (ProductID) REFERENCES Product(ID)
);

-- Thêm sản phẩm
INSERT INTO Product (ID, Name, Description, Price, StockQuantity)
VALUES (2001, 'Sản phẩm A', 'Mô tả sản phẩm A', 150000, 100);

INSERT INTO Product (ID, Name, Description, Price, StockQuantity)
VALUES (2002, 'Sản phẩm B', 'Mô tả sản phẩm B', 200000, 50);

-- Thêm đơn hàng
INSERT INTO `Order` (ID, OrderDate, CustomerName, Address, Email, PhoneNumber, Status, TotalAmount)
VALUES (101, '2024-07-04', 'Nguyễn Văn A', 'Địa chỉ A', 'a@example.com', '0123456789', 'Processing', 500000);

-- Thêm chi tiết đơn hàng
INSERT INTO OrderDetail (ID, OrderID, ProductID, Quantity, UnitPrice, TotalPrice)
VALUES (1, 101, 2001, 2, 150000, 300000);

INSERT INTO OrderDetail (ID, OrderID, ProductID, Quantity, UnitPrice, TotalPrice)
VALUES (2, 101, 2002, 1, 200000, 200000);



CREATE TABLE `authorities` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
);

INSERT INTO `authorities` (`customer_id`, `name`)
 VALUES (1, 'VIEWACCOUNT');

INSERT INTO `authorities` (`customer_id`, `name`)
 VALUES (1, 'VIEWCARDS');

 INSERT INTO `authorities` (`customer_id`, `name`)
  VALUES (1, 'VIEWLOANS');

 INSERT INTO `authorities` (`customer_id`, `name`)
   VALUES (1, 'VIEWBALANCE');

 DELETE FROM `authorities`;

 INSERT INTO `authorities` (`customer_id`, `name`)
  VALUES (1, 'ROLE_USER');

 INSERT INTO `authorities` (`customer_id`, `name`)
  VALUES (1, 'ROLE_ADMIN');

INSERT INTO `authorities` (`customer_id`, `name`)
VALUES (1, 'ORDER');

INSERT INTO `authorities` (`customer_id`, `name`)
VALUES (1, 'ORDER_DETAIL');

INSERT INTO `authorities` (`customer_id`, `name`)
VALUES (1, 'PRODUCT');

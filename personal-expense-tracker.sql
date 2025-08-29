CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_user VARCHAR(50),
    update_user VARCHAR(50)
);
CREATE TABLE Category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL,
    type ENUM('Income', 'Expense') NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_user VARCHAR(50),
    update_user VARCHAR(50)
);
CREATE TABLE PaymentMethod (
    payment_method_id INT AUTO_INCREMENT PRIMARY KEY,
    method_name VARCHAR(50) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_user VARCHAR(50),
    update_user VARCHAR(50)
);
CREATE TABLE Income (
    income_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT,
    payment_method_id INT,
    amount DECIMAL(10,2) NOT NULL,
    date DATE NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_user VARCHAR(50),
    update_user VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (category_id) REFERENCES Category(category_id),
    FOREIGN KEY (payment_method_id) REFERENCES PaymentMethod(payment_method_id)
);
CREATE TABLE Expense (
    expense_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT,
    payment_method_id INT,
    amount DECIMAL(10,2) NOT NULL,
    date DATE NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    create_user VARCHAR(50),
    update_user VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (category_id) REFERENCES Category(category_id),
    FOREIGN KEY (payment_method_id) REFERENCES PaymentMethod(payment_method_id)
);


-- Income Categories
INSERT INTO Category (category_name, type, create_user) VALUES
('Salary', 'Income', 'system'),
('Bonus', 'Income', 'system'),
('Investment', 'Income', 'system'),
('Gift', 'Income', 'system'),
('Interest', 'Income', 'system'),
('Freelance', 'Income', 'system'),
('Rental Income', 'Income', 'system'),
('Other Income', 'Income', 'system');

-- Expense Categories
INSERT INTO Category (category_name, type, create_user) VALUES
('Food', 'Expense', 'system'),
('Transport', 'Expense', 'system'),
('Shopping', 'Expense', 'system'),
('Bills', 'Expense', 'system'),
('Entertainment', 'Expense', 'system'),
('Healthcare', 'Expense', 'system'),
('Education', 'Expense', 'system'),
('Insurance', 'Expense', 'system'),
('Taxes', 'Expense', 'system'),
('Other Expense', 'Expense', 'system');

INSERT INTO PaymentMethod (method_name, create_user, update_user) VALUES
('Cash', 'system', 'system'),
('Credit Card', 'system', 'system'),
('Debit Card', 'system', 'system'),
('Bank Transfer', 'system', 'system'),
('Mobile Payment', 'system', 'system'),
('Cheque', 'system', 'system');

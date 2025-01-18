-- Insert permissions
INSERT INTO permission (id, name) VALUES
                                      (1, 'READ_ALL_PRIVILEGE'),
                                      (2, 'WRITE_ALL_PRIVILEGE'),
                                      (3, 'UPDATE_ALL_PRIVILEGE'),
                                      (4, 'DELETE_ALL_PRIVILEGE'),
                                      (5, 'READ_DEP_PRIVILEGE'),
                                      (6, 'WRITE_DEP_PRIVILEGE'),
                                      (7, 'UPDATE_DEP_PRIVILEGE'),
                                      (8, 'DELETE_DEP_PRIVILEGE'),
                                      (9, 'ROOT_PRIVILEGE');

-- Insert roles
INSERT INTO role (id, name) VALUES
                                (1, 'ROLE_HR'),
                                (2, 'ROLE_MANAGER'),
                                (3, 'ROLE_ADMIN');

-- Map roles to permissions
INSERT INTO role_permissions (role_id, permission_id) VALUES
                                                          (1, 1),
                                                          (1, 2),
                                                          (1, 3),
                                                          (1, 4),
                                                          (2, 5),
                                                          (2, 6),
                                                          (2, 7),
                                                          (2, 8),
                                                          (3, 9);
-- Insert departments
INSERT INTO department (id, name) VALUES (1, 'Engineering');
INSERT INTO department (id, name) VALUES (2, 'Human Resources');
INSERT INTO department (id, name) VALUES (3, 'Marketing');
INSERT INTO department (id, name) VALUES (4, 'Sales');
INSERT INTO department (id, name) VALUES (5, 'Operations');

-- Insert employees
INSERT INTO employee (
    full_name,
    employee_id,
    job_title,
    department_id,
    hire_date,
    employment_status,
    address,
    email,
    phone_number,
    contract_type,
    gender
)
VALUES
    ('John Smith', 'EMP001', 'Senior Software Engineer', 1, '2023-01-15', 'ACTIVE', '123 Tech Street, Silicon Valley', 'john.smith@company.com', '0666666666', 'FULL_TIME', 'MALE'),
    ('Jane Doe', 'EMP002', 'Software Architect', 1, '2023-02-01', 'ACTIVE', '456 Code Avenue, Tech City', 'jane.doe@company.com', '06666666662', 'FULL_TIME', 'FEMALE'),
    ('Mike Johnson', 'EMP003', 'DevOps Engineer', 1, '2023-03-10', 'ACTIVE', '789 Docker Lane, Cloud City', 'mike.johnson@company.com', '06666666663', 'PART_TIME', 'MALE'),
    ('Sarah Wilson', 'EMP004', 'HR Manager', 2, '2023-01-20', 'ACTIVE', '321 People Street, HR Town', 'sarah.wilson@company.com', '06666666664', 'FULL_TIME', 'FEMALE'),
    ('Tom Brown', 'EMP005', 'HR Specialist', 2, '2023-04-01', 'ACTIVE', '654 Recruitment Road, HR City', 'tom.brown@company.com', '06666666665', 'FREELANCE', 'MALE');

--
-- INSERT INTO employee (id, full_name, employee_id, job_title, department_id, hire_date, employment_status, address, email, phone_number, role_id)
-- VALUES (6, 'Emily Davis', 'EMP006', 'Marketing Director', 3, '2023-02-15', 'ACTIVE', '987 Brand Boulevard, Marketing Valley', 'emily.davis@company.com', '06666666666', 3);
--
-- INSERT INTO employee (id, full_name, employee_id, job_title, department_id, hire_date, employment_status, address, email, phone_number, role_id)
-- VALUES (7, 'Chris Anderson', 'EMP007', 'Content Specialist', 3, '2023-03-15', 'ACTIVE', '147 Social Media Street, Digital City', 'chris.anderson@company.com', '06666666667',2);
--
-- INSERT INTO employee (id, full_name, employee_id, job_title, department_id, hire_date, employment_status, address, email, phone_number, role_id)
-- VALUES (8, 'Lisa Martinez', 'EMP008', 'Sales Director', 4, '2023-01-10', 'ACTIVE', '258 Sales Avenue, Revenue City', 'lisa.martinez@company.com', '06666666668',2);
--
-- INSERT INTO employee (id, full_name, employee_id, job_title, department_id, hire_date, employment_status, address, email, phone_number, role_id)
-- VALUES (9, 'David Wilson', 'EMP009', 'Account Manager', 4, '2023-04-15', 'ACTIVE', '369 Client Street, Sales Town', 'david.wilson@company.com', '06666666669',3);
--
-- INSERT INTO employee (id, full_name, employee_id, job_title, department_id, hire_date, employment_status, address, email, phone_number, role_id)
-- VALUES (10, 'Robert Taylor', 'EMP010', 'Operations Manager', 5, '2023-02-20', 'ACTIVE', '741 Ops Road, Process City', 'robert.taylor@company.com', '0666666660', 3);

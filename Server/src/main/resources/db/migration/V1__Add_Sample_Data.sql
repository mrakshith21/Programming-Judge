-- Add Users
INSERT INTO users (username, password) VALUES ('alice', 'abc');
INSERT INTO users (username, password) VALUES ('bob', 'securepass');

-- Add Problems
INSERT INTO problems (problem_id, problem_name, problem_description, time_limit, memory_limit, created) 
VALUES (1, 'Two Sum', 'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\n\nInput Format:\nFirst line contains n (number of elements) and target.\nSecond line contains n space-separated integers.\n\nOutput Format:\nTwo space-separated indices.', 1, 256, CURRENT_TIMESTAMP);

INSERT INTO problems (problem_id, problem_name, problem_description, time_limit, memory_limit, created) 
VALUES (2, 'Palindrome Number', 'Given an integer x, return true if x is a palindrome, and false otherwise.\n\nInput Format:\nA single integer x.\n\nOutput Format:\ntrue or false.', 1, 256, CURRENT_TIMESTAMP);

-- Update sequences to avoid conflicts with manual IDs (PostgreSQL IDENTITY default names)
SELECT setval(pg_get_serial_sequence('problems', 'problem_id'), 50);
SELECT setval(pg_get_serial_sequence('submissions', 'submission_id'), 50);

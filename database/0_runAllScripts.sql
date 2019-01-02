-- Setup for the database for the discordbot for postgresql database

\echo Running all scripts

-- setup database and user
-- hidden because, you know passwords and stuff... ;D
-- \ir 01_setup.sql

-- create extensions
-- \ir 1_extensions.sql

-- create schema
\ir 2_schema.sql

-- insert data
-- \ir 3_inserts.sql

-- create primary keys, constraints, indexes
\ir 4_constraints.sql

\encoding 'auto'

-- \set ECHO queries
-- query the database
-- \ir 5_queries.sql
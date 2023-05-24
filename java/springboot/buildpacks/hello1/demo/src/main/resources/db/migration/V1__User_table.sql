CREATE TABLE IF NOT EXISTS person (
      id UUID PRIMARY KEY,
      mail text UNIQUE NOT NULL,
      name text NOT NULL);
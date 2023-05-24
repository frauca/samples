CREATE TABLE IF NOT EXISTS person (
      id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
      mail text UNIQUE NOT NULL,
      name text NOT NULL);
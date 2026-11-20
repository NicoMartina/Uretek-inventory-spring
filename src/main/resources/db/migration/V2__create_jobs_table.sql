CREATE TABLE jobs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    presupuesto_number VARCHAR(255),
    job_date DATE,
    notes VARCHAR(255),
    created_at TIMESTAMP,
    mix_total FLOAT8,
    client_name VARCHAR(255),
    status VARCHAR(50),
    quoted_amount FLOAT8
);
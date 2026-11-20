 CREATE TABLE items (                                                                                                                                                                                 ─╯
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255),
    category VARCHAR(255),
    unit VARCHAR(255),
    current_stock FLOAT8,
    minimum_stock FLOAT8,
    created_at TIMESTAMP
);

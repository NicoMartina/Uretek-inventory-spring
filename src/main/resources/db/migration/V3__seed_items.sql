INSERT INTO items (name, category, unit, current_stock, minimum_stock, created_at)
SELECT 'ISO Tank A', 'material', 'kg', 10000.0, 500.0, NOW()
WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'ISO Tank A');

INSERT INTO items (name, category, unit, current_stock, minimum_stock, created_at)
SELECT 'Resina Tank B', 'material', 'kg', 10000.0, 500.0, NOW()
WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Resina Tank B');

spring.jpa.defer-datasource-initialization=true
-- inserting mock suppliers
INSERT INTO supplier (id, name, email) VALUES (1, 'Dirt Co', 'contact@dirt.com') ON CONFLICT (id) DO NOTHING;
INSERT INTO supplier (id, name, email) VALUES (2, 'Bedding Supplies', 'info@bedding.com') ON CONFLICT (id) DO NOTHING;

-- inserting mock clients
INSERT INTO client (id, name, email) VALUES (1, 'Hilton', 'contact@hilton.com') ON CONFLICT (id) DO NOTHING;
INSERT INTO client (id, name, email) VALUES (2, 'McDonalds', 'info@mcdonalds.com') ON CONFLICT (id) DO NOTHING;

--inserting mock relationships
INSERT INTO relationship (supplier_id, client_id, supplier_status) VALUES (1, 1, 'ACTIVE');
INSERT INTO relationship (supplier_id, client_id, supplier_status) VALUES (2, 2, 'PENDING');

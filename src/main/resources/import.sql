INSERT INTO fruitEntity(id, name, sku) VALUES (1, 'Cherry', 'gyb4993');
INSERT INTO fruitEntity(id, name, sku) VALUES (2, 'Apple', 'thr1002');
INSERT INTO fruitEntity(id, name, sku) VALUES (3, 'Banana', 'xrp3388');
ALTER SEQUENCE fruitEntity_seq RESTART WITH 4;

-- force using the same if for entity and repository to facilitate testing
INSERT INTO fruit(id, name) VALUES (1, 'Cherry');
INSERT INTO fruit(id, name) VALUES (2, 'Apple');
INSERT INTO fruit(id, name) VALUES (3, 'Banana');
ALTER SEQUENCE fruit_seq RESTART WITH 4;

CREATE TABLE orders (
  id SERIAL PRIMARY KEY,
  user_id INT,
  total DECIMAL(10, 2),
  status VARCHAR(50),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
  id SERIAL PRIMARY KEY,
  order_id INT REFERENCES orders(id),
  product_id INT,
  quantity INT,
  unit_price DECIMAL(10, 2)
);

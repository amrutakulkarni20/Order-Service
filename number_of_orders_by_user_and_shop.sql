SELECT user_id, shop_id, COUNT(*) AS total_orders
FROM orders
GROUP BY user_id, shop_id;
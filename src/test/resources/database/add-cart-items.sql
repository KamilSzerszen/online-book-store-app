insert into users (id, email, password, first_name, last_name) values (1, 'user@user.com', 'password123', 'user', 'user');
insert into shopping_cart (id, user_id) values (1, 1);
insert into books (id, title, author, isbn, price) values (1, 'Title1', 'Author1', 'Isbn1', 12.99);
insert into cart_item (id, shopping_cart_id, book_id, quantity) values (1, 1, 1, 2);
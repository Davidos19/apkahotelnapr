-- ✅ UŻYTKOWNICY
INSERT INTO users (username, email, password, first_name, last_name, phone_number, role, is_active, created_at, updated_at) VALUES
                                                                                                                                ('admin', 'admin@example.com', '$2a$12$wOy3w9MV3jy9PM2iy4qS6ejPY2VVyIj.O2tVLgs1vokCdOCBzA.mm', 'Admin', 'Administrator', '+48 123 456 789', 'ADMIN', true, NOW(), NOW()),
                                                                                                                                ('manager', 'manager@example.com', '$2a$12$HvorXCcpYbZXy9IeTfWdXOEycg5wT7T5N/GbB/XzEVBCrD6cJbwOm', 'Marek', 'Manager', '+48 123 456 788', 'ADMIN', true, NOW(), NOW()),
                                                                                                                                ('user', 'user@example.com', '$2a$12$tBv1ESxiP6leehLKrCv/P.k8hBxdAqANPkDY2V2xkUakhZS7kArvC', 'Jan', 'Kowalski', '+48 987 654 321', 'CLIENT', true, NOW(), NOW()),
                                                                                                                                ('anna.nowak', 'anna.nowak@gmail.com', '$2a$12$tBv1ESxiP6leehLKrCv/P.k8hBxdAqANPkDY2V2xkUakhZS7kArvC', 'Anna', 'Nowak', '+48 501 234 567', 'CLIENT', true, NOW(), NOW()),
                                                                                                                                ('piotr.wisniewski', 'piotr.w@outlook.com', '$2a$12$tBv1ESxiP6leehLKrCv/P.k8hBxdAqANPkDY2V2xkUakhZS7kArvC', 'Piotr', 'Wiśniewski', '+48 602 345 678', 'CLIENT', true, NOW(), NOW()),
                                                                                                                                ('katarzyna.dabrowska', 'k.dabrowska@yahoo.com', '$2a$12$tBv1ESxiP6leehLKrCv/P.k8hBxdAqANPkDY2V2xkUakhZS7kArvC', 'Katarzyna', 'Dąbrowska', '+48 503 456 789', 'CLIENT', true, NOW(), NOW()),
                                                                                                                                ('marcin.lewandowski', 'marcin.lew@gmail.com', '$2a$12$tBv1ESxiP6leehLKrCv/P.k8hBxdAqANPkDY2V2xkUakhZS7kArvC', 'Marcin', 'Lewandowski', '+48 604 567 890', 'CLIENT', true, NOW(), NOW()),
                                                                                                                                ('magdalena.wojcik', 'magda.wojcik@interia.pl', '$2a$12$tBv1ESxiP6leehLKrCv/P.k8hBxdAqANPkDY2V2xkUakhZS7kArvC', 'Magdalena', 'Wójcik', '+48 505 678 901', 'CLIENT', true, NOW(), NOW());

-- ✅ HOTELE
INSERT INTO hotels (name, city, description, stars, address, phone_number, email, image_url, created_at) VALUES
                                                                                                             ('Grand Hotel Warsaw', 'Warszawa', 'Luksusowy hotel w centrum Warszawy z widokiem na Pałac Kultury i Nauki', '5', 'ul. Krakowskie Przedmieście 13', '+48 22 583 21 00', 'info@grandhotel.pl', 'https://images.unsplash.com/photo-1566073771259-6a8506099945', NOW()),
                                                                                                             ('Hotel Krakow Plaza', 'Kraków', 'Elegancki hotel blisko Rynku Głównego w zabytkowej kamienicy', '4', 'ul. Powiśle 7', '+48 12 374 30 00', 'reservations@krakowplaza.pl', 'https://images.unsplash.com/photo-1571896349842-33c89424de2d', NOW()),
                                                                                                             ('Seaside Resort Gdansk', 'Gdańsk', 'Nadmorski resort z prywatną plażą i widokiem na Bałtyk', '4', 'ul. Jelitkowska 20', '+48 58 554 70 00', 'info@seasideresort.pl', 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4', NOW()),
                                                                                                             ('Mountain Lodge Zakopane', 'Zakopane', 'Przytulny hotel górski w sercu Tatr z widokiem na Giewont', '3', 'ul. Krupówki 42', '+48 18 201 40 00', 'lodge@mountain.pl', 'https://images.unsplash.com/photo-1445019980597-93fa8acb246c', NOW()),
                                                                                                             ('Business Hotel Wroclaw', 'Wrocław', 'Nowoczesny hotel dla podróży biznesowych w centrum miasta', '4', 'ul. Świdnicka 53', '+48 71 797 98 00', 'business@wroclaw.pl', 'https://images.unsplash.com/photo-1578683010236-d716f9a3f461', NOW()),
                                                                                                             ('Hotel Sopot Prestige', 'Sopot', 'Ekskluzywny hotel nad morzem z pięknym widokiem na zatokę', '5', 'ul. Bohaterów Monte Cassino 56', '+48 58 551 00 00', 'prestige@sopot.pl', 'https://images.unsplash.com/photo-1564501049412-61c2a3083791', NOW()),
                                                                                                             ('Łódź City Hotel', 'Łódź', 'Nowoczesny hotel w centrum Łodzi, idealny dla biznesu', '4', 'ul. Piotrkowska 67', '+48 42 630 66 00', 'info@lodzcity.pl', 'https://images.unsplash.com/photo-1551882547-ff40c63fe5fa', NOW()),
                                                                                                             ('Poznań Palace Hotel', 'Poznań', 'Historyczny hotel w centrum Poznania z nowoczesnymi udogodnieniami', '4', 'ul. Stary Rynek 91', '+48 61 858 81 00', 'palace@poznan.pl', 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b', NOW()),
                                                                                                             ('Lublin Boutique', 'Lublin', 'Kameralny hotel butikowy w zabytkowej części Lublina', '3', 'ul. Krakowskie Przedmieście 78', '+48 81 532 61 00', 'boutique@lublin.pl', 'https://images.unsplash.com/photo-1551882547-ff40c63fe5fa', NOW()),
                                                                                                             ('Katowice Business Center', 'Katowice', 'Hotel biznesowy w centrum Katowic z salami konferencyjnymi', '4', 'ul. Uniwersytecka 13', '+48 32 259 04 00', 'business@katowice.pl', 'https://images.unsplash.com/photo-1578683010236-d716f9a3f461', NOW()),
                                                                                                             ('Resort Białka Tatrzańska', 'Białka Tatrzańska', 'Rodzinny resort w górach z basenem i SPA', '4', 'ul. Środkowa 181c', '+48 18 265 93 00', 'resort@bialka.pl', 'https://images.unsplash.com/photo-1445019980597-93fa8acb246c', NOW()),
                                                                                                             ('Hotel Marina Gdynia', 'Gdynia', 'Nowoczesny hotel przy marinie z widokiem na port', '4', 'ul. Żeromskiego 2', '+48 58 620 60 00', 'marina@gdynia.pl', 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4', NOW());

-- ✅ POKOJE - WSZYSTKIE HOTELE MAJĄ POKOJE!
INSERT INTO room (hotel_id, room_number, room_type, capacity, price, image_url) VALUES
-- Grand Hotel Warsaw (id=1) - 15 pokoi
(1, '101', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(1, '102', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(1, '103', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(1, '104', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(1, '105', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(1, '201', 'Deluxe', 2, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(1, '202', 'Deluxe', 3, 499.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(1, '203', 'Deluxe', 2, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(1, '204', 'Deluxe', 3, 499.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(1, '205', 'Deluxe', 2, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(1, '301', 'Suite', 4, 799.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(1, '302', 'Suite', 4, 799.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(1, '303', 'Presidential Suite', 6, 1299.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(1, '401', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(1, '402', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),

-- Hotel Krakow Plaza (id=2) - 12 pokoi
(2, '101', 'Standard', 2, 249.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(2, '102', 'Standard', 2, 249.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(2, '103', 'Standard', 2, 249.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(2, '104', 'Standard', 2, 249.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(2, '201', 'Deluxe', 3, 349.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(2, '202', 'Deluxe', 3, 349.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(2, '203', 'Deluxe', 2, 329.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(2, '204', 'Deluxe', 3, 349.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(2, '301', 'Suite', 4, 649.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(2, '302', 'Suite', 4, 649.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(2, '303', 'Suite', 6, 849.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(2, '401', 'Standard', 2, 249.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),

-- Seaside Resort Gdansk (id=3) - 18 pokoi
(3, '101', 'Standard', 2, 279.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(3, '102', 'Standard', 2, 279.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(3, '103', 'Standard', 2, 279.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(3, '104', 'Standard', 4, 379.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(3, '105', 'Standard', 4, 379.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(3, '106', 'Standard', 2, 279.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(3, '201', 'Deluxe', 4, 449.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(3, '202', 'Deluxe', 4, 449.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(3, '203', 'Deluxe', 2, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(3, '204', 'Deluxe', 4, 449.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(3, '205', 'Deluxe', 2, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(3, '301', 'Suite', 6, 699.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(3, '302', 'Suite', 6, 699.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(3, '303', 'Presidential Suite', 8, 999.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(3, '401', 'Standard', 2, 279.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(3, '402', 'Standard', 2, 279.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(3, '403', 'Standard', 4, 379.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(3, '404', 'Deluxe', 4, 449.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),

-- Mountain Lodge Zakopane (id=4) - 10 pokoi
(4, '101', 'Standard', 2, 189.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(4, '102', 'Standard', 2, 189.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(4, '103', 'Standard', 4, 289.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(4, '104', 'Standard', 4, 289.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(4, '201', 'Deluxe', 2, 259.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(4, '202', 'Deluxe', 4, 359.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(4, '203', 'Deluxe', 4, 359.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(4, '301', 'Suite', 6, 599.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(4, '302', 'Suite', 6, 599.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(4, '303', 'Mountain Suite', 8, 799.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),

-- Business Hotel Wroclaw (id=5) - 14 pokoi
(5, '101', 'Standard', 1, 199.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(5, '102', 'Standard', 1, 199.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(5, '103', 'Standard', 2, 239.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(5, '104', 'Standard', 2, 239.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(5, '105', 'Standard', 2, 239.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(5, '201', 'Business', 1, 259.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(5, '202', 'Business', 1, 259.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(5, '203', 'Business', 2, 319.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(5, '204', 'Business', 2, 319.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(5, '205', 'Business', 2, 319.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(5, '301', 'Executive Suite', 2, 499.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(5, '302', 'Executive Suite', 4, 699.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(5, '303', 'Conference Suite', 6, 899.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(5, '401', 'Standard', 1, 199.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),

-- Hotel Sopot Prestige (id=6) - 12 pokoi
(6, '101', 'Standard', 2, 349.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(6, '102', 'Standard', 2, 349.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(6, '103', 'Standard', 2, 349.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(6, '201', 'Premium', 2, 449.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(6, '202', 'Premium', 3, 549.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(6, '203', 'Premium', 2, 449.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(6, '301', 'Sea View Suite', 4, 799.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(6, '302', 'Sea View Suite', 4, 799.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(6, '303', 'Presidential Ocean', 6, 1199.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(6, '401', 'Standard', 2, 349.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(6, '402', 'Premium', 2, 449.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(6, '403', 'Premium', 3, 549.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),

-- ✅ ŁÓDŹ CITY HOTEL (id=7) - 11 pokoi
(7, '101', 'Standard', 1, 169.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(7, '102', 'Standard', 2, 199.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(7, '103', 'Standard', 2, 199.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(7, '104', 'Standard', 2, 199.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(7, '201', 'Business', 1, 219.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(7, '202', 'Business', 2, 259.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(7, '203', 'Business', 2, 259.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(7, '204', 'Business', 3, 319.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(7, '301', 'Suite', 4, 459.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(7, '302', 'Suite', 4, 459.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(7, '303', 'Executive Suite', 6, 649.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),

-- ✅ POZNAŃ PALACE HOTEL (id=8) - 13 pokoi
(8, '101', 'Standard', 2, 229.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(8, '102', 'Standard', 2, 229.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(8, '103', 'Standard', 2, 229.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(8, '104', 'Standard', 3, 289.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(8, '201', 'Palace', 2, 329.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(8, '202', 'Palace', 2, 329.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(8, '203', 'Palace', 3, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(8, '204', 'Palace', 4, 479.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(8, '301', 'Royal Suite', 4, 699.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(8, '302', 'Royal Suite', 6, 899.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(8, '303', 'Presidential Palace', 8, 1299.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(8, '401', 'Standard', 2, 229.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(8, '402', 'Palace', 2, 329.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),

-- ✅ LUBLIN BOUTIQUE (id=9) - 8 pokoi (mniejszy hotel)
(9, '101', 'Boutique', 2, 189.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(9, '102', 'Boutique', 2, 189.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(9, '103', 'Boutique', 2, 189.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(9, '201', 'Deluxe Boutique', 2, 249.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(9, '202', 'Deluxe Boutique', 3, 309.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(9, '203', 'Deluxe Boutique', 2, 249.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(9, '301', 'Boutique Suite', 4, 459.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(9, '302', 'Premium Suite', 6, 649.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),

-- ✅ KATOWICE BUSINESS CENTER (id=10) - 16 pokoi
(10, '101', 'Standard', 1, 179.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(10, '102', 'Standard', 1, 179.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(10, '103', 'Standard', 2, 219.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(10, '104', 'Standard', 2, 219.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(10, '105', 'Standard', 2, 219.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(10, '201', 'Business', 1, 259.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(10, '202', 'Business', 1, 259.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(10, '203', 'Business', 2, 319.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(10, '204', 'Business', 2, 319.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(10, '205', 'Business', 3, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(10, '301', 'Executive', 2, 459.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(10, '302', 'Executive', 4, 659.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(10, '303', 'Conference Suite', 6, 859.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(10, '401', 'Standard', 1, 179.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(10, '402', 'Standard', 2, 219.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(10, '403', 'Business', 2, 319.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),

-- ✅ RESORT BIAŁKA TATRZAŃSKA (id=11) - 20 pokoi (duży resort)
(11, '101', 'Standard', 2, 219.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(11, '102', 'Standard', 2, 219.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(11, '103', 'Standard', 4, 319.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(11, '104', 'Standard', 4, 319.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(11, '105', 'Standard', 4, 319.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(11, '106', 'Standard', 2, 219.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(11, '201', 'Family', 4, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(11, '202', 'Family', 4, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(11, '203', 'Family', 6, 519.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(11, '204', 'Family', 6, 519.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(11, '205', 'Family', 4, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(11, '206', 'Family', 6, 519.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(11, '301', 'Resort Suite', 6, 799.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(11, '302', 'Resort Suite', 6, 799.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(11, '303', 'Resort Suite', 8, 999.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(11, '401', 'Standard', 2, 219.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(11, '402', 'Standard', 4, 319.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(11, '403', 'Family', 4, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(11, '404', 'Family', 6, 519.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(11, '405', 'Resort Suite', 8, 999.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),

-- ✅ HOTEL MARINA GDYNIA (id=12) - 14 pokoi
(12, '101', 'Standard', 2, 259.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(12, '102', 'Standard', 2, 259.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(12, '103', 'Standard', 2, 259.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(12, '104', 'Standard', 3, 329.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(12, '201', 'Marina', 2, 359.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(12, '202', 'Marina', 2, 359.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(12, '203', 'Marina', 4, 479.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(12, '204', 'Marina', 4, 479.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(12, '301', 'Port View Suite', 4, 699.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(12, '302', 'Port View Suite', 4, 699.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(12, '303', 'Admiral Suite', 6, 899.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
(12, '401', 'Standard', 2, 259.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(12, '402', 'Marina', 2, 359.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(12, '403', 'Marina', 4, 479.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39');

-- ✅ WIĘCEJ RECENZJI
INSERT INTO reviews (hotel_id, username, rating, comment, created_at) VALUES
-- Grand Hotel Warsaw
(1, 'user', 5, 'Wspaniały hotel! Obsługa na najwyższym poziomie, piękne wnętrza i doskonała lokalizacja.', NOW()),
(1, 'admin', 4, 'Bardzo dobre śniadania i czyste pokoje. Polecam szczególnie pokoje Deluxe.', NOW()),
(1, 'anna.nowak', 5, 'Fantastyczny widok z okna i świetna restauracja. Na pewno wrócę!', NOW()),
(1, 'piotr.wisniewski', 4, 'Dobra lokalizacja, czyste pokoje. Jedyna wada to wysoka cena.', NOW()),

-- Hotel Krakow Plaza
(2, 'user', 5, 'Idealna lokalizacja w centrum Krakowa. 5 minut piechotą do Rynku Głównego!', NOW()),
(2, 'katarzyna.dabrowska', 4, 'Zabytkowa atmosfera i miła obsługa. Polecam na romantyczny weekend.', NOW()),
(2, 'marcin.lewandowski', 5, 'Przepiękny hotel w sercu miasta. Wszystko na najwyższym poziomie.', NOW()),

-- Seaside Resort Gdansk
(3, 'user', 4, 'Piękny widok na morze, polecam! Śniadania mogłyby być lepsze.', NOW()),
(3, 'magdalena.wojcik', 5, 'Cudowny resort nad morzem. Prywatna plaża to ogromny plus!', NOW()),
(3, 'anna.nowak', 4, 'Świetny hotel dla rodzin z dziećmi. Dużo atrakcji i basen.', NOW()),

-- Mountain Lodge Zakopane
(4, 'piotr.wisniewski', 5, 'Góry tuż za oknem! Idealne miejsce na zimowy wypoczynek.', NOW()),
(4, 'marcin.lewandowski', 4, 'Przytulny hotel górski. Bardzo dobra kuchnia regionalna.', NOW()),

-- Business Hotel Wroclaw
(5, 'admin', 4, 'Doskonały do podróży służbowych. Szybki internet i sale konferencyjne.', NOW()),
(5, 'katarzyna.dabrowska', 3, 'Ok na biznes, ale brak klimatu. Pokoje funkcjonalne.', NOW()),

-- Hotel Sopot Prestige
(6, 'magdalena.wojcik', 5, 'Luksus nad morzem! Niesamowity widok z tarasu.', NOW()),
(6, 'user', 5, 'Najlepszy hotel w Sopocie. SPA na najwyższym poziomie.', NOW()),

-- Nowe hotele
(7, 'anna.nowak', 4, 'Solidny hotel biznesowy w Łodzi. Dobra lokalizacja przy Piotrkowskiej.', NOW()),
(8, 'piotr.wisniewski', 5, 'Wspaniały hotel pałacowy! Historia i nowoczesność w jednym.', NOW()),
(9, 'katarzyna.dabrowska', 4, 'Urocze miejsce w Lublinie. Kameralnie i z klimatem.', NOW()),
(10, 'marcin.lewandowski', 3, 'Hotel biznesowy jak należy - funkcjonalny i praktyczny.', NOW()),
(11, 'magdalena.wojcik', 5, 'Fantastyczny resort dla całej rodziny! Dzieciaki zachwycone.', NOW()),
(12, 'user', 4, 'Świetny widok na port w Gdyni. Polecam pokoje Marina.', NOW());

-- ✅ DUŻO WIĘCEJ REZERWACJI - RÓŻNE HOTELE I DATY
INSERT INTO reservations (hotel_id, room_id, username, check_in, check_out, total_price, status, created_at) VALUES
-- Przeszłe rezerwacje
(1, 1, 'user', '2025-01-15', '2025-01-17', 599.98, 'CONFIRMED', NOW()),
(2, 16, 'anna.nowak', '2025-01-20', '2025-01-22', 499.98, 'CONFIRMED', NOW()),
(3, 29, 'piotr.wisniewski', '2025-02-01', '2025-02-03', 559.98, 'CONFIRMED', NOW()),
(7, 82, 'katarzyna.dabrowska', '2025-02-10', '2025-02-12', 399.98, 'CONFIRMED', NOW()),
(9, 106, 'marcin.lewandowski', '2025-02-15', '2025-02-17', 379.98, 'CONFIRMED', NOW()),

-- Obecne rezerwacje (aktywne)
(1, 2, 'katarzyna.dabrowska', '2025-12-10', '2025-12-14', 1199.96, 'CONFIRMED', NOW()),
(4, 47, 'marcin.lewandowski', '2025-12-11', '2025-12-15', 1159.96, 'CONFIRMED', NOW()),
(11, 120, 'magdalena.wojcik', '2025-12-11', '2025-12-16', 1999.95, 'CONFIRMED', NOW()),

-- Przyszłe rezerwacje - styczeń 2026
(1, 3, 'user', '2026-01-05', '2026-01-08', 1199.97, 'CONFIRMED', NOW()),
(2, 17, 'magdalena.wojcik', '2026-01-10', '2026-01-13', 999.97, 'CONFIRMED', NOW()),
(3, 30, 'anna.nowak', '2026-01-15', '2026-01-18', 1259.97, 'CONFIRMED', NOW()),
(5, 57, 'admin', '2026-01-20', '2026-01-23', 959.97, 'CONFIRMED', NOW()),
(6, 72, 'piotr.wisniewski', '2026-01-25', '2026-01-28', 1349.97, 'CONFIRMED', NOW()),
(8, 96, 'katarzyna.dabrowska', '2026-01-30', '2026-02-02', 999.97, 'CONFIRMED', NOW()),

-- Przyszłe rezerwacje - luty 2026
(10, 115, 'marcin.lewandowski', '2026-02-05', '2026-02-07', 639.98, 'CONFIRMED', NOW()),
(12, 138, 'magdalena.wojcik', '2026-02-10', '2026-02-12', 719.98, 'CONFIRMED', NOW()),
(7, 83, 'user', '2026-02-15', '2026-02-17', 519.98, 'CONFIRMED', NOW()),
(9, 107, 'anna.nowak', '2026-02-20', '2026-02-22', 499.98, 'CONFIRMED', NOW()),

-- Długie pobyty
(6, 73, 'user', '2026-03-01', '2026-03-08', 3149.93, 'CONFIRMED', NOW()),
(11, 121, 'piotr.wisniewski', '2026-03-10', '2026-03-17', 2799.93, 'CONFIRMED', NOW()),
(3, 31, 'katarzyna.dabrowska', '2026-03-20', '2026-03-25', 2249.95, 'CONFIRMED', NOW()),

-- Wakacyjne rezerwacje - lato 2025
(3, 32, 'magdalena.wojcik', '2025-06-15', '2025-06-22', 3499.93, 'CONFIRMED', NOW()),
(6, 74, 'user', '2025-07-01', '2025-07-08', 3849.93, 'CONFIRMED', NOW()),
(11, 122, 'anna.nowak', '2025-07-15', '2025-07-20', 2599.95, 'CONFIRMED', NOW()),
(12, 139, 'piotr.wisniewski', '2025-08-01', '2025-08-05', 2199.95, 'CONFIRMED', NOW()),

-- Zimowe wyjazdy górskie
(4, 48, 'katarzyna.dabrowska', '2026-01-18', '2026-01-21', 1079.97, 'CONFIRMED', NOW()),
(4, 49, 'marcin.lewandowski', '2026-02-08', '2026-02-12', 1439.96, 'CONFIRMED', NOW()),
(11, 123, 'magdalena.wojcik', '2026-02-22', '2026-02-26', 1599.96, 'CONFIRMED', NOW()),

-- Biznesowe podróże
(5, 58, 'admin', '2026-02-03', '2026-02-05', 639.98, 'CONFIRMED', NOW()),
(10, 116, 'katarzyna.dabrowska', '2026-02-17', '2026-02-19', 639.98, 'CONFIRMED', NOW()),
(7, 84, 'marcin.lewandowski', '2026-02-24', '2026-02-26', 519.98, 'CONFIRMED', NOW()),

-- Weekendowe wypady
(8, 97, 'user', '2026-03-15', '2026-03-16', 329.99, 'CONFIRMED', NOW()),
(9, 108, 'anna.nowak', '2026-03-22', '2026-03-23', 189.99, 'CONFIRMED', NOW()),
(12, 140, 'piotr.wisniewski', '2026-03-29', '2026-03-30', 359.99, 'CONFIRMED', NOW());
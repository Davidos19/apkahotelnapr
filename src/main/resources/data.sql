
-- Wstaw użytkowników
INSERT INTO users (username, email, password, first_name, last_name, phone_number, role, is_active, created_at, updated_at) VALUES
                                                                                                                                ('admin', 'admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Admin', 'Administrator', '+48 123 456 789', 'ADMIN', true, NOW(), NOW()),
                                                                                                                                ('manager', 'manager@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Marek', 'Manager', '+48 123 456 788', 'HOTEL_MANAGER', true, NOW(), NOW()),
                                                                                                                                ('receptionist', 'receptionist@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Anna', 'Recepcjonistka', '+48 123 456 787', 'RECEPTIONIST', true, NOW(), NOW()),
                                                                                                                                ('user', 'user@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Jan', 'Kowalski', '+48 987 654 321', 'CLIENT', true, NOW(), NOW());

-- Wstaw hotele
INSERT INTO hotels (name, city, description, stars, address, phone_number, email, image_url, created_at) VALUES
                                                                                                             ('Grand Hotel Warsaw', 'Warszawa', 'Luksusowy hotel w centrum Warszawy z widokiem na Pałac Kultury i Nauki', '5', 'ul. Krakowskie Przedmieście 13', '+48 22 583 21 00', 'info@grandhotel.pl', 'https://images.unsplash.com/photo-1566073771259-6a8506099945', NOW()),
                                                                                                             ('Hotel Krakow Plaza', 'Kraków', 'Elegancki hotel blisko Rynku Głównego w zabytkowej kamienicy', '4', 'ul. Powiśle 7', '+48 12 374 30 00', 'reservations@krakowplaza.pl', 'https://images.unsplash.com/photo-1571896349842-33c89424de2d', NOW()),
                                                                                                             ('Seaside Resort Gdansk', 'Gdańsk', 'Nadmorski resort z prywatną plażą i widokiem na Bałtyk', '4', 'ul. Jelitkowska 20', '+48 58 554 70 00', 'info@seasideresort.pl', 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4', NOW()),
                                                                                                             ('Mountain Lodge Zakopane', 'Zakopane', 'Przytulny hotel górski w sercu Tatr z widokiem na Giewont', '3', 'ul. Krupówki 42', '+48 18 201 40 00', 'lodge@mountain.pl', 'https://images.unsplash.com/photo-1445019980597-93fa8acb246c', NOW()),
                                                                                                             ('Business Hotel Wroclaw', 'Wrocław', 'Nowoczesny hotel dla podróży biznesowych w centrum miasta', '4', 'ul. Świdnicka 53', '+48 71 797 98 00', 'business@wroclaw.pl', 'https://images.unsplash.com/photo-1578683010236-d716f9a3f461', NOW());

-- Pokoje dla Grand Hotel Warsaw (id=1)
INSERT INTO room (hotel_id, room_number, room_type, capacity, price, image_url) VALUES
                                                                                    (1, '101', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (1, '102', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (1, '103', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (1, '201', 'Deluxe', 2, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
                                                                                    (1, '202', 'Deluxe', 3, 499.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
                                                                                    (1, '301', 'Suite', 4, 799.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),
                                                                                    (1, '302', 'Suite', 4, 799.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b');

-- Pokoje dla Hotel Krakow Plaza (id=2)
INSERT INTO room (hotel_id, room_number, room_type, capacity, price, image_url) VALUES
                                                                                    (2, '101', 'Standard', 2, 249.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (2, '102', 'Standard', 2, 249.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (2, '103', 'Standard', 2, 249.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (2, '201', 'Deluxe', 3, 349.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
                                                                                    (2, '202', 'Deluxe', 3, 349.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
                                                                                    (2, '301', 'Suite', 4, 649.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b');

-- Pokoje dla Seaside Resort Gdansk (id=3)
INSERT INTO room (hotel_id, room_number, room_type, capacity, price, image_url) VALUES
                                                                                    (3, '101', 'Standard', 2, 279.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (3, '102', 'Standard', 2, 279.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (3, '103', 'Standard', 3, 329.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (3, '201', 'Deluxe', 4, 449.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
                                                                                    (3, '202', 'Deluxe', 4, 449.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
                                                                                    (3, '301', 'Suite', 6, 699.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b');

-- Pokoje dla Mountain Lodge Zakopane (id=4)
INSERT INTO room (hotel_id, room_number, room_type, capacity, price, image_url) VALUES
                                                                                    (4, '101', 'Standard', 2, 199.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (4, '102', 'Standard', 2, 199.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (4, '103', 'Standard', 3, 249.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (4, '104', 'Standard', 3, 249.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (4, '201', 'Deluxe', 4, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
                                                                                    (4, '202', 'Deluxe', 4, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39');

-- Pokoje dla Business Hotel Wroclaw (id=5)
INSERT INTO room (hotel_id, room_number, room_type, capacity, price, image_url) VALUES
                                                                                    (5, '101', 'Standard', 1, 229.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (5, '102', 'Standard', 2, 289.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (5, '103', 'Standard', 2, 289.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
                                                                                    (5, '201', 'Deluxe', 2, 389.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
                                                                                    (5, '202', 'Deluxe', 3, 449.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
                                                                                    (5, '301', 'Suite', 4, 699.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b');

-- Przykładowe recenzje
INSERT INTO reviews (hotel_id, username, rating, comment, created_at) VALUES
                                                                          (1, 'user', 5, 'Wspaniały hotel! Obsługa na najwyższym poziomie, piękne wnętrza i doskonała lokalizacja.', NOW()),
                                                                          (1, 'admin', 4, 'Bardzo dobre śniadania i czyste pokoje. Polecam szczególnie pokoje Deluxe.', NOW()),
                                                                          (2, 'user', 5, 'Idealna lokalizacja w centrum Krakowa. 5 minut piechotą do Rynku Głównego!', NOW()),
                                                                          (2, 'admin', 4, 'Klimatyczny hotel w zabytkowej kamienicy. Bardzo miła obsługa.', NOW()),
                                                                          (3, 'user', 4, 'Piękny widok na morze, polecam! Śniadania mogłyby być lepsze.', NOW()),
                                                                          (3, 'admin', 5, 'Rewelacyjny resort nad morzem. Prywatna plaża to ogromny plus!', NOW()),
                                                                          (4, 'user', 4, 'Przytulny hotel w górach. Widok na Tatry zapiera dech w piersiach.', NOW()),
                                                                          (5, 'admin', 3, 'Dobry hotel dla podróży służbowych. Profesjonalna obsługa.', NOW());

-- Przykładowe rezerwacje (tylko w przyszłości)
INSERT INTO reservations (hotel_id, room_id, username, check_in, check_out, total_price, status, created_at) VALUES
                                                                                                                 (1, 1, 'user', '2025-06-15', '2025-06-18', 899.97, 'CONFIRMED', NOW()),
                                                                                                                 (2, 8, 'admin', '2025-07-10', '2025-07-12', 499.98, 'CONFIRMED', NOW()),
                                                                                                                 (3, 14, 'user', '2025-08-20', '2025-08-25', 2249.95, 'CONFIRMED', NOW());

INSERT INTO user_roles (user_id, role) VALUES
                                           (1, 'ADMIN'),
                                           (2, 'HOTEL_MANAGER'),
                                           (3, 'RECEPTIONIST'),
                                           (4, 'CLIENT');

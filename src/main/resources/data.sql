
-- ✅ UŻYWAJ WŁAŚCIWYCH NAZW TABEL Z HIBERNATE!

-- Wstaw użytkowników do tabeli USERS (nie APP_USER!)
INSERT INTO users (username, email, password, first_name, last_name, phone_number, role, is_active, created_at, updated_at) VALUES
                                                                                                                                ('admin', 'admin@example.com', '$2a$12$wOy3w9MV3jy9PM2iy4qS6ejPY2VVyIj.O2tVLgs1vokCdOCBzA.mm', 'Admin', 'Administrator', '+48 123 456 789', 'ADMIN', true, NOW(), NOW()),
                                                                                                                                ('manager', 'manager@example.com', '$2a$12$HvorXCcpYbZXy9IeTfWdXOEycg5wT7T5N/GbB/XzEVBCrD6cJbwOm', 'Marek', 'Manager', '+48 123 456 788', 'ADMIN', true, NOW(), NOW()),
                                                                                                                                ('user', 'user@example.com', '$2a$12$tBv1ESxiP6leehLKrCv/P.k8hBxdAqANPkDY2V2xkUakhZS7kArvC', 'Jan', 'Kowalski', '+48 987 654 321', 'CLIENT', true, NOW(), NOW());

-- Wstaw hotele do tabeli HOTELS (nie HOTEL!)
INSERT INTO hotels (name, city, description, stars, address, phone_number, email, image_url, created_at) VALUES
                                                                                                             ('Grand Hotel Warsaw', 'Warszawa', 'Luksusowy hotel w centrum Warszawy z widokiem na Pałac Kultury i Nauki', '5', 'ul. Krakowskie Przedmieście 13', '+48 22 583 21 00', 'info@grandhotel.pl', 'https://images.unsplash.com/photo-1566073771259-6a8506099945', NOW()),
                                                                                                             ('Hotel Krakow Plaza', 'Kraków', 'Elegancki hotel blisko Rynku Głównego w zabytkowej kamienicy', '4', 'ul. Powiśle 7', '+48 12 374 30 00', 'reservations@krakowplaza.pl', 'https://images.unsplash.com/photo-1571896349842-33c89424de2d', NOW()),
                                                                                                             ('Seaside Resort Gdansk', 'Gdańsk', 'Nadmorski resort z prywatną plażą i widokiem na Bałtyk', '4', 'ul. Jelitkowska 20', '+48 58 554 70 00', 'info@seasideresort.pl', 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4', NOW()),
                                                                                                             ('Mountain Lodge Zakopane', 'Zakopane', 'Przytulny hotel górski w sercu Tatr z widokiem na Giewont', '3', 'ul. Krupówki 42', '+48 18 201 40 00', 'lodge@mountain.pl', 'https://images.unsplash.com/photo-1445019980597-93fa8acb246c', NOW()),
                                                                                                             ('Business Hotel Wroclaw', 'Wrocław', 'Nowoczesny hotel dla podróży biznesowych w centrum miasta', '4', 'ul. Świdnicka 53', '+48 71 797 98 00', 'business@wroclaw.pl', 'https://images.unsplash.com/photo-1578683010236-d716f9a3f461', NOW());

-- Pokoje do tabeli ROOM (już jest dobrze!)
INSERT INTO room (hotel_id, room_number, room_type, capacity, price, image_url) VALUES
-- Grand Hotel Warsaw (id=1)
(1, '101', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(1, '102', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(1, '103', 'Standard', 2, 299.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(1, '201', 'Deluxe', 2, 399.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(1, '202', 'Deluxe', 3, 499.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(1, '301', 'Suite', 4, 799.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),

-- Hotel Krakow Plaza (id=2)
(2, '101', 'Standard', 2, 249.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(2, '102', 'Standard', 2, 249.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(2, '201', 'Deluxe', 3, 349.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(2, '301', 'Suite', 4, 649.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b'),

-- Seaside Resort Gdansk (id=3)
(3, '101', 'Standard', 2, 279.99, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(3, '201', 'Deluxe', 4, 449.99, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39'),
(3, '301', 'Suite', 6, 699.99, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b');

-- Recenzje do tabeli REVIEWS (nie REVIEW!)
INSERT INTO reviews (hotel_id, username, rating, comment, created_at) VALUES
                                                                          (1, 'user', 5, 'Wspaniały hotel! Obsługa na najwyższym poziomie, piękne wnętrza i doskonała lokalizacja.', NOW()),
                                                                          (1, 'admin', 4, 'Bardzo dobre śniadania i czyste pokoje. Polecam szczególnie pokoje Deluxe.', NOW()),
                                                                          (2, 'user', 5, 'Idealna lokalizacja w centrum Krakowa. 5 minut piechotą do Rynku Głównego!', NOW()),
                                                                          (3, 'user', 4, 'Piękny widok na morze, polecam! Śniadania mogłyby być lepsze.', NOW());

-- Rezerwacje do tabeli RESERVATIONS (nie RESERVATION!)
INSERT INTO reservations (hotel_id, room_id, username, check_in, check_out, total_price, status, created_at) VALUES
                                                                                                                 (1, 1, 'user', '2025-06-15', '2025-06-17', 899.97, 'CONFIRMED', NOW()),
                                                                                                                 (2, 7, 'admin', '2025-07-10', '2025-07-12', 499.98, 'CONFIRMED', NOW()),
                                                                                                                 (3, 11, 'user', '2025-08-20', '2025-08-25', 2249.95, 'CONFIRMED', NOW());
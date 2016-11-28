-- GESTORE con id 1 e Locale in PiazzaMaggiore
INSERT INTO gestore (NOME, MAIL) VALUES ('GianGiacomo', '1_PiazzaMaggiore@mail.bo');
INSERT INTO posizione (LONGITUDINE, LATITUDINE, INDIRIZZO, NUMERO, CITTA, CAP) VALUES (11.343134, 44.493702, 'Piazza Maggiore', '12', 'Bologna', '40127');
INSERT INTO locale (ID_POSIZIONE, ID_GESTORE, NOME, LOGO, DESCRIZIONE) VALUES (1, 1, 'La Linea', null, 'Questo locale e'' del gestore con id 1 e si trova nella posizione con id 1 che corrisponde a piazza maggiore', false);

-- GESTORE con id 2 e Locale in PiazzaMaggiore
INSERT INTO gestore (NOME, MAIL) VALUES ('GianGiovanni', '2_PiazzaVerdi@mail.bo');
INSERT INTO posizione (LONGITUDINE, LATITUDINE, INDIRIZZO, NUMERO, CITTA, CAP) VALUES (11.350776, 44.496207, 'Piazza Verdi', '12', 'Bologna', '40127');
INSERT INTO locale (ID_POSIZIONE, ID_GESTORE, NOME, LOGO, DESCRIZIONE) VALUES (2, 2, 'Il Piccolo e Sublime', null, 'Questo locale e'' del gestore con id 2 e si trova nella posizione con id 2 che corrisponde a piazza verdi', true);

-- GESTORE con id 3 e Locale in DueTorri
INSERT INTO gestore (NOME, MAIL) VALUES ('GianGiorgio', '3_DueTorri@mail.bo');
INSERT INTO posizione (LONGITUDINE, LATITUDINE, INDIRIZZO, NUMERO, CITTA, CAP) VALUES (11.346596, 44.494366, 'Due Torri', '12', 'Bologna', '40127');
INSERT INTO locale (ID_POSIZIONE, ID_GESTORE, NOME, LOGO, DESCRIZIONE) VALUES (3, 3, 'Bar Lime', null, 'Questo locale e'' del gestore con id 3 e si trova nella posizione con id 3 che corrisponde a due torri', false);


--PRODOTTO 
INSERT INTO prodotto (DESCRIZIONE, IMMAGINE) VALUES ('Beera', null);
INSERT INTO prodotto (DESCRIZIONE, IMMAGINE) VALUES ('Wine', null);
INSERT INTO prodotto (DESCRIZIONE, IMMAGINE) VALUES ('Cocktail', null);


--OFFERTE
INSERT INTO offerta (ID, TITOLO, DESCRIZIONE, ID_PRODOTTO, DATA_INIZIO, DATA_FINE, QUANTITA, PREZZO_UNITARIO_PIENO, PREZZO_UNITARIO_SCONTATO, ID_LOCALE, STATO_OFFERTA) VALUES (1, 'Birra', 'Il Piccolo stasera ti offre la birra a 3,00. Affrettati', 1, '2014-07-15 00:30:00', '2015-07-22 01:30:00', 47, 5.00, 3.00, 2, 'PUBBLICATA');
INSERT INTO offerta (ID, TITOLO, DESCRIZIONE, ID_PRODOTTO, DATA_INIZIO, DATA_FINE, QUANTITA, PREZZO_UNITARIO_PIENO, PREZZO_UNITARIO_SCONTATO, ID_LOCALE, STATO_OFFERTA) VALUES (2, 'Cocktail', 'Bar Lime e il Cocktail che costa meno', 3, '2014-07-15 00:15:00', '2015-07-22 01:15:00', 39, 7.00, 5.00, 3, 'PUBBLICATA');
INSERT INTO offerta (ID, TITOLO, DESCRIZIONE, ID_PRODOTTO, DATA_INIZIO, DATA_FINE, QUANTITA, PREZZO_UNITARIO_PIENO, PREZZO_UNITARIO_SCONTATO, ID_LOCALE, STATO_OFFERTA) VALUES (3, 'Beer', 'Vieni a gustare la nostra birra', 1, '2014-07-15 00:00:00', '2015-07-22 01:00:00', 39, 5.00, 3.00, 2, 'PUBBLICATA');
INSERT INTO offerta (ID, TITOLO, DESCRIZIONE, ID_PRODOTTO, DATA_INIZIO, DATA_FINE, QUANTITA, PREZZO_UNITARIO_PIENO, PREZZO_UNITARIO_SCONTATO, ID_LOCALE, STATO_OFFERTA) VALUES (4, 'Cocktail', 'descr', 1, '2014-07-20 13:30:00', '2015-07-22 01:00:00', 11, 11.00, 5.00, 2, 'PUBBLICATA');



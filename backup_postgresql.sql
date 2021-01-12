--
-- PostgreSQL database dump
--

-- Dumped from database version 10.3
-- Dumped by pg_dump version 10.3

-- Started on 2021-01-05 12:20:33 +08

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3115 (class 0 OID 76057)
-- Dependencies: 196
-- Data for Name: account; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.account VALUES ('1111', 'Classic Saving', 'My salary account', 999823.00);
INSERT INTO public.account VALUES ('2222', 'Green Saving', 'My second account', 8177.00);
INSERT INTO public.account VALUES ('3333', 'Classic Saving', 'My investment account', 5999999.00);
INSERT INTO public.account VALUES ('4444', 'Green Saving', NULL, 5999999.00);
INSERT INTO public.account VALUES ('5555', 'Classic Saving', 'My loan payment account', 5999999.00);


--
-- TOC entry 3116 (class 0 OID 76060)
-- Dependencies: 197
-- Data for Name: transaction; Type: TABLE DATA; Schema: public; Owner: postgres
--



-- Completed on 2021-01-05 12:20:34 +08

--
-- PostgreSQL database dump complete
--


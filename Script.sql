--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-04-09 10:34:51

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 221 (class 1259 OID 16556)
-- Name: consulta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.consulta (
    id bigint NOT NULL,
    paciente_cpf character varying(14) NOT NULL,
    medico_crm character varying(20) NOT NULL,
    data_hora timestamp without time zone NOT NULL,
    cancelada boolean DEFAULT false NOT NULL,
    motivo_cancelamento character varying(50)
);


ALTER TABLE public.consulta OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16555)
-- Name: consulta_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.consulta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.consulta_id_seq OWNER TO postgres;

--
-- TOC entry 4877 (class 0 OID 0)
-- Dependencies: 220
-- Name: consulta_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.consulta_id_seq OWNED BY public.consulta.id;


--
-- TOC entry 218 (class 1259 OID 16540)
-- Name: medico; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.medico (
    id bigint NOT NULL,
    crm character varying(20) NOT NULL,
    nome character varying(100) NOT NULL,
    email character varying(100),
    telefone character varying(20),
    especialidade character varying(50) NOT NULL,
    ativo boolean DEFAULT true NOT NULL
);


ALTER TABLE public.medico OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16539)
-- Name: medico_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.medico_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.medico_id_seq OWNER TO postgres;

--
-- TOC entry 4878 (class 0 OID 0)
-- Dependencies: 217
-- Name: medico_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.medico_id_seq OWNED BY public.medico.id;


--
-- TOC entry 219 (class 1259 OID 16549)
-- Name: paciente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paciente (
    cpf character varying(14) NOT NULL,
    nome character varying(100) NOT NULL,
    email character varying(100),
    telefone character varying(20),
    ativo boolean DEFAULT true NOT NULL
);


ALTER TABLE public.paciente OWNER TO postgres;

--
-- TOC entry 4707 (class 2604 OID 16559)
-- Name: consulta id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consulta ALTER COLUMN id SET DEFAULT nextval('public.consulta_id_seq'::regclass);


--
-- TOC entry 4704 (class 2604 OID 16543)
-- Name: medico id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medico ALTER COLUMN id SET DEFAULT nextval('public.medico_id_seq'::regclass);


--
-- TOC entry 4871 (class 0 OID 16556)
-- Dependencies: 221
-- Data for Name: consulta; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.consulta (id, paciente_cpf, medico_crm, data_hora, cancelada, motivo_cancelamento) FROM stdin;
\.


--
-- TOC entry 4868 (class 0 OID 16540)
-- Dependencies: 218
-- Data for Name: medico; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.medico (id, crm, nome, email, telefone, especialidade, ativo) FROM stdin;
\.


--
-- TOC entry 4869 (class 0 OID 16549)
-- Dependencies: 219
-- Data for Name: paciente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.paciente (cpf, nome, email, telefone, ativo) FROM stdin;
\.


--
-- TOC entry 4879 (class 0 OID 0)
-- Dependencies: 220
-- Name: consulta_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.consulta_id_seq', 1, false);


--
-- TOC entry 4880 (class 0 OID 0)
-- Dependencies: 217
-- Name: medico_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.medico_id_seq', 1, false);


--
-- TOC entry 4716 (class 2606 OID 16562)
-- Name: consulta consulta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consulta
    ADD CONSTRAINT consulta_pkey PRIMARY KEY (id);


--
-- TOC entry 4710 (class 2606 OID 16548)
-- Name: medico medico_crm_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medico
    ADD CONSTRAINT medico_crm_key UNIQUE (crm);


--
-- TOC entry 4712 (class 2606 OID 16546)
-- Name: medico medico_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medico
    ADD CONSTRAINT medico_pkey PRIMARY KEY (id);


--
-- TOC entry 4714 (class 2606 OID 16554)
-- Name: paciente paciente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paciente
    ADD CONSTRAINT paciente_pkey PRIMARY KEY (cpf);


--
-- TOC entry 4717 (class 1259 OID 16575)
-- Name: idx_consulta_data; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_consulta_data ON public.consulta USING btree (data_hora);


--
-- TOC entry 4718 (class 1259 OID 16573)
-- Name: idx_consulta_medico_data; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_consulta_medico_data ON public.consulta USING btree (medico_crm, data_hora);


--
-- TOC entry 4719 (class 1259 OID 16574)
-- Name: idx_consulta_paciente_data; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_consulta_paciente_data ON public.consulta USING btree (paciente_cpf, data_hora);


--
-- TOC entry 4720 (class 2606 OID 16568)
-- Name: consulta fk_medico; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consulta
    ADD CONSTRAINT fk_medico FOREIGN KEY (medico_crm) REFERENCES public.medico(crm);


--
-- TOC entry 4721 (class 2606 OID 16563)
-- Name: consulta fk_paciente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consulta
    ADD CONSTRAINT fk_paciente FOREIGN KEY (paciente_cpf) REFERENCES public.paciente(cpf);


-- Completed on 2025-04-09 10:34:51

--
-- PostgreSQL database dump complete
--


CREATE TABLE public.carro(
    id SERIAL PRIMARY KEY,
    modelo VARCHAR(255) NOT NULL,
    marca VARCHAR(255) NOT NULL,
    ano INT NOT NULL
)
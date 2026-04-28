CREATE DATABASE msclientes;

\connect msclientes;

CREATE TABLE IF NOT EXISTS clientes (
    cliente_id      VARCHAR(36)  NOT NULL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    genero          VARCHAR(20),
    edad            INTEGER,
    identificacion  VARCHAR(20)  NOT NULL UNIQUE,
    direccion       VARCHAR(200),
    telefono        VARCHAR(20),
    contrasena      VARCHAR(255) NOT NULL,
    estado          BOOLEAN      NOT NULL DEFAULT TRUE
);


CREATE INDEX IF NOT EXISTS idx_clientes_identificacion ON clientes (identificacion);


CREATE DATABASE mscuentas;

\connect mscuentas;

CREATE TABLE IF NOT EXISTS cuentas (
    cuenta_id       VARCHAR(36)    NOT NULL PRIMARY KEY,
    numero_cuenta   VARCHAR(20)    NOT NULL UNIQUE,
    tipo_cuenta     VARCHAR(20)    NOT NULL,
    saldo           DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    estado          BOOLEAN        NOT NULL DEFAULT TRUE,
    cliente_id      VARCHAR(36)    NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_cuentas_cliente_id ON cuentas (cliente_id);

CREATE TABLE IF NOT EXISTS movimientos (
    movimiento_id   VARCHAR(36)    NOT NULL PRIMARY KEY,
    fecha           TIMESTAMP      NOT NULL,
    tipo_movimiento VARCHAR(20)    NOT NULL,
    valor           DECIMAL(15, 2) NOT NULL,
    saldo           DECIMAL(15, 2) NOT NULL,
    cuenta_id       VARCHAR(36)    NOT NULL,
    CONSTRAINT fk_movimientos_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuentas (cuenta_id)
);

CREATE INDEX IF NOT EXISTS idx_movimientos_cuenta_fecha ON movimientos (cuenta_id, fecha);

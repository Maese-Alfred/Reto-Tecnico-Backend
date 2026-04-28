# Reto Técnico Backend — Sofka

Sistema de gestión bancaria implementado con una arquitectura de microservicios desacoplados, comunicados de forma asíncrona a través de RabbitMQ.

---

## Tabla de contenidos

1. [Descripción general](#descripción-general)
2. [Arquitectura](#arquitectura)
3. [Patrones de diseño](#patrones-de-diseño)
4. [Estructura de proyectos](#estructura-de-proyectos)
5. [Endpoints](#endpoints)
6. [Funcionamiento del sistema](#funcionamiento-del-sistema)
7. [Ejecución con Docker](#ejecución-con-docker)
8. [Ejecución local](#ejecución-local)
9. [Pruebas](#pruebas)

---

## Descripción general

| Microservicio | Puerto | Responsabilidad |
|---|---|---|
| **ms-clientes** | `8080` | Gestión del ciclo de vida de clientes |
| **ms-cuentas** | `8081` | Gestión de cuentas, movimientos y reportes |

**Stack tecnológico:** Java 17 · Spring Boot · PostgreSQL · RabbitMQ · Docker · Gradle

---

## Arquitectura

El proyecto implementa **Arquitectura Hexagonal** (Ports & Adapters) combinada con los principios de **Domain-Driven Design (DDD)**. Cada microservicio es completamente autónomo: tiene su propia base de datos, sus propios modelos de dominio y se comunica con el exterior únicamente a través de interfaces bien definidas.

```
┌──────────────────────────────────────────────────────────────┐
│                        ms-clientes                           │
│  ┌──────────┐    ┌──────────────┐    ┌─────────────────────┐ │
│  │interfaces│───▶│  application │───▶│      domain         │ │
│  │controller│    │  use cases   │    │ model / service /   │ │
│  │ handler  │    │    DTOs      │    │ repository (port)   │ │
│  └──────────┘    └──────────────┘    └─────────────────────┘ │
│                                               ▲               │
│  ┌────────────────────────────────────────────┘               │
│  │              infrastructure                                │
│  │   JPA adapter · RabbitMQ publisher · BeansConfig          │
│  └────────────────────────────────────────────────────────────┘
└──────────────────────────────────────────────────────────────┘
          │ ClienteCreadoEvent (RabbitMQ)
          ▼
┌──────────────────────────────────────────────────────────────┐
│                        ms-cuentas                            │
│  (misma estructura de capas)                                 │
│  + Strategy pattern para movimientos                         │
└──────────────────────────────────────────────────────────────┘
```

### Capas

| Capa | Paquete | Contenido |
|---|---|---|
| **Domain** | `domain/` | Entidades de dominio puras, interfaces de repositorio (ports), servicios de dominio, eventos, excepciones |
| **Application** | `application/` | Use Cases (casos de uso), DTOs de entrada/salida |
| **Infrastructure** | `infrastructure/` | Adaptadores JPA, publishers RabbitMQ, configuración de beans |
| **Interfaces** | `interfaces/` | Controllers REST, manejadores globales de excepciones |

**Regla de dependencia:** las capas externas dependen de las internas; nunca al revés. El dominio no conoce Spring ni JPA.

---

## Patrones de diseño

### 1. Hexagonal Architecture (Ports & Adapters)

Las interfaces de repositorio (`ClienteRepository`, `CuentaRepository`, `MovimientoRepository`) son **ports** definidos en el dominio. Los adaptadores JPA (`ClienteRepositoryAdapter`, etc.) en la capa de infraestructura son los **adapters** que los implementan. Los use cases solo dependen del port, lo que permite sustituir la implementación sin tocar el dominio.

```java
// Port (dominio — no sabe nada de JPA)
public interface CuentaRepository {
    Cuenta save(Cuenta cuenta);
    Optional<Cuenta> findById(String id);
    // ...
}

// Adapter (infraestructura)
@Component
public class CuentaRepositoryAdapter implements CuentaRepository {
    private final JpaCuentaRepository jpa;
    // delega en JPA y usa el mapper para convertir entidades
}
```

### 2. Factory Method — `reconstituir()`

Los constructores de las entidades de dominio (`Cliente`, `Cuenta`, `Movimiento`) siempre generan un UUID nuevo. Para reconstruir una entidad desde la base de datos sin crear un nuevo ID, cada modelo expone un método estático `reconstituir(...)` que actúa como **Factory Method**:

```java
// Constructor normal — siempre genera nuevo UUID
public Cuenta(String numeroCuenta, TipoCuenta tipo, BigDecimal saldo, String clienteId) {
    this.cuentaId = UUID.randomUUID().toString();
    // ...
}

// Factory para reconstituir desde persistencia
public static Cuenta reconstituir(String cuentaId, String numeroCuenta, ...) {
    Cuenta c = new Cuenta(numeroCuenta, tipo, saldo, clienteId);
    c.cuentaId = cuentaId; // restaura el ID original
    return c;
}
```

### 3. Strategy — Movimientos

El procesamiento de `DEPOSITO` y `RETIRO` está encapsulado en estrategias intercambiables:

```
MovimientoStrategy (interfaz)
    ├── DepositoStrategy  →  cuenta.depositar(valor)
    └── RetiroStrategy    →  cuenta.retirar(valor)  // valida saldo
```

`MovimientoDomainService` selecciona la estrategia según el `TipoMovimiento` recibido, manteniendo el `if/switch` fuera del modelo y permitiendo agregar nuevos tipos sin modificar código existente (Open/Closed Principle).

### 4. Repository Pattern

Separa la lógica de acceso a datos del dominio. El dominio opera con objetos `Cliente`, `Cuenta` y `Movimiento`; la infraestructura traduce entre esas entidades de dominio y las entidades JPA usando **Mappers** dedicados.

### 5. Domain Events + Mensajería Asíncrona

Cuando se crea un cliente en `ms-clientes`, se publica un `ClienteCreadoEvent` en RabbitMQ (`clientes.exchange` / routing key `cliente.creado`). `ms-cuentas` consume ese evento de forma desacoplada. Esto garantiza que los microservicios no se llamen directamente entre sí.

```
ms-clientes ──► [clientes.exchange] ──► cliente.creado.queue ──► ms-cuentas
```

### 6. Use Case Pattern (Application Layer)

Cada operación del sistema es un caso de uso independiente con una única responsabilidad:

| ms-clientes | ms-cuentas |
|---|---|
| `CrearClienteUseCase` | `CrearCuentaUseCase` |
| `ActualizarClienteUseCase` | `ActualizarCuentaUseCase` |
| `ObtenerClienteUseCase` | `ObtenerCuentaUseCase` |
| `EliminarClienteUseCase` | `EliminarCuentaUseCase` |
| | `RegistrarMovimientoUseCase` |
| | `ObtenerMovimientoUseCase` |
| | `GenerarReporteUseCase` |

---

## Estructura de proyectos

```
Reto tecnico/
├── docker-compose.yml
├── BaseDatos.sql
├── Reto-Tecnico-Backend.postman_collection.json
│
├── ms-clientes/                          (puerto 8080)
│   └── src/main/java/com/msclientes/
│       ├── domain/
│       │   ├── model/         Cliente.java
│       │   ├── repository/    ClienteRepository.java  (port)
│       │   ├── service/       ClienteDomainService.java
│       │   └── event/         ClienteCreadoEvent.java, ClienteEliminadoEvent.java
│       ├── application/
│       │   ├── dto/           ClienteRequestDTO, ClienteResponseDTO, ...
│       │   └── usecase/       CrearClienteUseCase, ActualizarClienteUseCase, ...
│       ├── infrastructure/
│       │   ├── persistence/   ClienteEntity, ClienteMapper, ClienteRepositoryAdapter
│       │   ├── messaging/     RabbitEventPublisher, RabbitConfig
│       │   └── config/        BeansConfig
│       └── interfaces/
│           ├── controller/    ClienteController
│           └── handler/       GlobalExceptionHandler
│
└── ms-cuentas/                           (puerto 8081)
    └── src/main/java/com/mscuentas/
        ├── domain/
        │   ├── model/         Cuenta.java, Movimiento.java, TipoCuenta, TipoMovimiento
        │   ├── repository/    CuentaRepository.java, MovimientoRepository.java
        │   ├── service/       MovimientoDomainService.java
        │   ├── strategy/      MovimientoStrategy, DepositoStrategy, RetiroStrategy
        │   └── event/         EventPublisher (port)
        ├── application/
        │   ├── dto/           CuentaRequestDTO, MovimientoRequestDTO, ReporteResponseDTO, ...
        │   └── usecase/       CrearCuentaUseCase, RegistrarMovimientoUseCase, GenerarReporteUseCase, ...
        ├── infrastructure/
        │   ├── persistence/   CuentaEntity, MovimientoEntity, mappers, adapters
        │   ├── messaging/     ClienteCreadoConsumer, RabbitEventPublisher, RabbitConfig
        │   └── config/        BeansConfig
        └── interfaces/
            ├── controller/    CuentaController, MovimientoController, ReporteController
            └── handler/       GlobalExceptionHandler
```

---

## Endpoints

### ms-clientes — `http://localhost:8080`

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| `GET` | `/clientes` | Lista todos los clientes | `200 OK` · `List<ClienteDetalleResponseDTO>` |
| `GET` | `/clientes/{id}` | Obtiene un cliente por UUID | `200 OK` · `ClienteDetalleResponseDTO` |
| `POST` | `/clientes` | Crea un nuevo cliente | `201 Created` · `ClienteResponseDTO` |
| `PUT` | `/clientes/{id}` | Actualiza dirección y/o teléfono | `200 OK` · `ClienteResponseDTO` |
| `DELETE` | `/clientes/{id}` | Desactiva el cliente (soft delete) | `204 No Content` |

**Body `POST /clientes`:**
```json
{
  "nombre": "Juan Perez",
  "genero": "Masculino",
  "edad": 30,
  "identificacion": "1234567890",
  "direccion": "Av. Amazonas 123",
  "telefono": "0991234567",
  "contrasena": "Pass1234"
}
```

### ms-cuentas — `http://localhost:8081`

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| `GET` | `/cuentas` | Lista todas las cuentas | `200 OK` |
| `GET` | `/cuentas/{id}` | Obtiene cuenta por UUID | `200 OK` |
| `POST` | `/cuentas` | Crea una cuenta | `201 Created` |
| `PUT` | `/cuentas/{id}` | Actualiza tipo/estado | `200 OK` |
| `DELETE` | `/cuentas/{id}` | Desactiva la cuenta | `204 No Content` |
| `GET` | `/movimientos/{id}` | Obtiene movimiento por UUID | `200 OK` |
| `GET` | `/movimientos?cuentaId={id}` | Historial de movimientos | `200 OK` |
| `POST` | `/movimientos` | Registra un movimiento | `201 Created` |
| `GET` | `/reportes?clienteId={id}&fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD` | Estado de cuenta | `200 OK` |

**Body `POST /movimientos`:**
```json
{
  "cuentaId": "uuid-de-la-cuenta",
  "tipoMovimiento": "DEPOSITO",
  "valor": 200.00
}
```
> Los tipos válidos son `DEPOSITO` y `RETIRO`. El campo `valor` siempre es positivo; el tipo determina si suma o resta.

**Respuesta `GET /reportes`:**
```json
{
  "clienteId": "uuid-cliente",
  "cuentas": [
    {
      "numeroCuenta": "478758",
      "tipoCuenta": "AHORRO",
      "saldo": 1000.00,
      "movimientos": [
        {
          "fecha": "2024-06-01T10:00:00",
          "tipo": "DEPOSITO",
          "valor": 200.00,
          "saldo": 1200.00
        }
      ]
    }
  ]
}
```

---

## Funcionamiento del sistema

### Registro de movimientos (F2)

1. El cliente envía `POST /movimientos` con `cuentaId`, `tipoMovimiento` y `valor`.
2. `RegistrarMovimientoUseCase` carga la `Cuenta` desde el repositorio.
3. Delega en `MovimientoDomainService`, que selecciona la `MovimientoStrategy` adecuada.
4. La estrategia llama a `cuenta.depositar(valor)` o `cuenta.retirar(valor)`.
5. Se persisten la `Cuenta` actualizada y el nuevo `Movimiento`.
6. Se publica un evento en RabbitMQ (`movimientos.exchange`).
7. Se retorna `movimientoId` + `saldoDisponible` actualizado.

### Control de saldo insuficiente (F3)

Si el saldo de la cuenta es menor que el valor del retiro, `Cuenta.retirar()` lanza `SaldoNoDisponibleException`. El `GlobalExceptionHandler` intercepta esta excepción y retorna:

```
HTTP 409 Conflict
{
  "message": "Saldo no disponible",
  "errorCode": "SALDO_NO_DISPONIBLE",
  "timestamp": "2024-06-01T10:00:00"
}
```

### Comunicación entre microservicios (RabbitMQ)

```
POST /clientes
    └─► ClienteDomainService.crearCliente()
        └─► ClienteCreadoEvent publicado en clientes.exchange
            └─► cliente.creado.queue
                └─► ClienteCreadoConsumer (ms-cuentas)
                    └─► log del evento recibido
```

### Validación de datos (Bean Validation)

Todos los DTOs de entrada tienen anotaciones `@NotBlank`, `@NotNull`, `@DecimalMin`, etc. Si alguna falla, Spring retorna automáticamente `400 Bad Request` con el detalle de cada campo inválido.

---

## Ejecución con Docker

### Prerrequisitos
- Docker Desktop instalado y en ejecución
- Gradle disponible (o usar el wrapper `./gradlew`)

### Pasos

```bash
# 1. Compilar ambos microservicios
cd ms-clientes && ./gradlew bootJar && cd ..
cd ms-cuentas  && ./gradlew bootJar && cd ..

# 2. Levantar toda la infraestructura
docker-compose up --build -d

# 3. Verificar que los contenedores están corriendo
docker-compose ps
```

| Servicio | URL |
|---|---|
| ms-clientes | http://localhost:8080 |
| ms-cuentas | http://localhost:8081 |
| RabbitMQ Management | http://localhost:15672 (guest / guest) |
| PostgreSQL | localhost:5432 |

### Variables de entorno

| Variable | Valor por defecto | Descripción |
|---|---|---|
| `DB_HOST` | `localhost` | Host de PostgreSQL |
| `DB_NAME` | `msclientes` / `mscuentas` | Nombre de la base de datos |
| `DB_USER` | `postgres` | Usuario |
| `DB_PASS` | `postgres` | Contraseña |
| `RABBIT_HOST` | `localhost` | Host de RabbitMQ |

---

## Ejecución local

```bash
# Requiere PostgreSQL y RabbitMQ corriendo localmente

# ms-clientes
cd ms-clientes
./gradlew bootRun

# ms-cuentas (en otra terminal)
cd ms-cuentas
./gradlew bootRun
```

La documentación OpenAPI (Swagger UI) queda disponible en:
- http://localhost:8080/swagger-ui.html
- http://localhost:8081/swagger-ui.html

---

## Pruebas

Cada microservicio incluye tres niveles de pruebas:

| Tipo | Herramientas | Qué cubre |
|---|---|---|
| **Unitarias - Dominio** | JUnit 5 · AssertJ | Lógica pura de entidades y servicios de dominio sin dependencias externas |
| **Unitarias - Casos de uso** | JUnit 5 · Mockito | Use cases con repositorios y publishers mockeados |
| **Integración - JPA** | `@DataJpaTest` · H2 | Consultas y persistencia sobre base de datos en memoria |
| **Integración - Web** | `@SpringBootTest` · MockMvc · `@MockBean RabbitTemplate` | Flujo HTTP completo con H2, sin RabbitMQ real |

```bash
# Ejecutar todas las pruebas
cd ms-clientes && ./gradlew test
cd ms-cuentas  && ./gradlew test

# Ver reportes HTML
# ms-clientes/build/reports/tests/test/index.html
# ms-cuentas/build/reports/tests/test/index.html
```

# Bankify Back — Módulo de Transferencias 

## Arquitectura

En vez de la separación clásica en capas (`config` / `controller` / `service` /
`repository` / `dto` regados por todo el proyecto), todo el módulo vive en
**un solo paquete**, `transferencias`, pensado como un Modelo-Controlador
(sin Vista, porque esto es solo backend):

```
src/main/java/edu/eci/byteprogramming/bankify/
├── BankifyApplication.java        (bootstrap + titulo de swagger)
├── SecurityConfig.java            (abre todos los endpoints, sin login)
└── transferencias/
    ├── Cuenta.java                 [Modelo] documento Mongo: numeroCuenta, titular, saldo
    ├── Transaccion.java            [Modelo] documento Mongo: registro de cada transferencia
    ├── TransferenciaRequest.java   [Modelo] cuerpo de la petición POST
    ├── CuentaRepository.java       acceso a Mongo (Spring Data, sin código propio)
    ├── TransaccionRepository.java  acceso a Mongo (Spring Data, sin código propio)
    ├── CuentaSeeder.java           crea 2 cuentas de prueba al iniciar la app
    └── TransferenciaController.java [Controlador] los 2 endpoints + la lógica de negocio
```

La lógica de la transferencia (la que contiene el error) vive **directamente en
el controlador**, sin una capa de "Service" intermedia, para que sea muy fácil
de encontrar y de leer durante el reto: todo el comportamiento de la
transferencia está en un solo método, `TransferenciaController.transferir(...)`.

Los repositorios (`CuentaRepository`, `TransaccionRepository`) son interfaces de
Spring Data Mongo sin ningún método escrito a mano (salvo un `findByNumeroCuenta`):
es la forma más simple de hablar con MongoDB, no una capa de arquitectura a
explicar.

Las clases `Cuenta`, `Transaccion` y `TransferenciaRequest` son POJOs comunes
(con constructor, getters y setters escritos a mano, sin Lombok) para que se
pueda leer exactamente qué hace cada método con solo abrir el archivo.

## Persistencia: MongoDB

El proyecto ya traía configurado `spring-boot-starter-data-mongodb` y lee la
conexión desde variables de entorno (`application.properties`):

```properties
spring.data.mongodb.database=${MONGO_DATABASE}
spring.data.mongodb.uri=${MONGO_URI}
```

> **Nota:** `.env.example` tenía `MONGODB_URI` / `MONGODB_DATABASE`, pero
> `application.properties` lee `MONGO_URI` / `MONGO_DATABASE` (sin el `DB`).
> Con el nombre original la app no arrancaba (placeholder no resuelto). Se
> corrigió `.env.example` para que coincida con lo que realmente usa la app.

Para correr el proyecto local, crea un `.env` en la raíz (no se sube a git)
basado en `.env.example`, por ejemplo apuntando a un Mongo en Docker:

```bash
docker run -d --name bankify-mongo -p 27017:27017 mongo:7
```

```env
MONGO_URI=mongodb://localhost:27017
MONGO_DATABASE=bankify_demo
JWT_SECRET=no-se-usa-en-este-modulo-pero-la-propiedad-debe-existir
JWT_EXPIRATION=86400000
```

Al iniciar, `CuentaSeeder` crea automáticamente 2 cuentas de prueba si la
colección `cuentas` está vacía:

| numeroCuenta | titular      | saldo     |
|--------------|--------------|-----------|
| 1001         | Ana Perez    | 1.000.000 |
| 1002         | Carlos Gomez | 500.000   |

Así no se necesita un tercer endpoint solo para crear cuentas de prueba.

## Seguridad

El `pom.xml` ya incluía `spring-boot-starter-security` y dependencias de JWT
(seguramente para otras fallas del reto). Si se deja tal cual, Spring Security
bloquea todo con un login por defecto. Como este módulo **no debe pedir
autenticación**, se agregó `SecurityConfig.java` que desactiva CSRF, HTTP
Basic y el login, y deja todas las rutas en `permitAll()`. No hay JWT, no hay
usuarios, no hay tokens en este módulo.

## Los 2 endpoints

### `GET /api/cuentas`
Lista todas las cuentas con su saldo actual. Se usa para "ver" el saldo antes
y después de la transferencia.

### `POST /api/transferencias`
Recibe:
```json
{ "cuentaOrigen": "1001", "cuentaDestino": "1002", "monto": 100000 }
```
Descuenta el monto de la cuenta origen, lo acredita en la cuenta destino y
registra la transacción. **Esta es la operación que en el estado inicial del
repositorio tiene el error.**

Ambos endpoints están documentados con anotaciones de springdoc-openapi y se
pueden ver/probar en Swagger UI: **http://localhost:8080/swagger-ui/index.html**
(JSON crudo en `/v3/api-docs`). No requieren login.

## Dónde está el bug (estado actual del repo)

Todo el problema está en `TransferenciaController.transferir(...)`
(`src/main/java/edu/eci/byteprogramming/bankify/transferencias/TransferenciaController.java`,
líneas 44 a 53):

```java
// Descuenta el monto de la cuenta origen
origen.setSaldo(origen.getSaldo() + monto);
cuentaRepository.save(origen);

// Acredita el monto en la cuenta destino
origen.setSaldo(origen.getSaldo() + monto);
cuentaRepository.save(origen);

Transaccion transaccion = new Transaccion(null, origen.getNumeroCuenta(), destino.getNumeroCuenta(),
        monto, "PENDIENTE", LocalDateTime.now());

return transaccion;
```

Hay 4 errores, calcados de los 4 que pide el enunciado:

1. **Descuenta mal el saldo**: usa `+` en vez de `-` sobre `origen`. En lugar
   de restar, el monto se *suma* a la cuenta que está enviando el dinero.
2. **Suma el valor a la cuenta equivocada**: la segunda actualización (la que
   debería acreditar `destino`) vuelve a tocar `origen` por error de copiar y
   pegar la variable. `destino` nunca se guarda ni cambia.
3. **No valida fondos suficientes**: no existe ningún `if` que compare
   `origen.getSaldo()` contra `monto` antes de operar, así que se puede
   transferir más dinero del que hay en la cuenta.
4. **No registra adecuadamente la transacción**: se crea el objeto
   `Transaccion`, pero nunca se llama a `transaccionRepository.save(...)`, y
   el estado se deja en `"PENDIENTE"` (nunca pasa a `"COMPLETADA"`). El
   objeto se devuelve igual al cliente aunque nunca quedó guardado en Mongo
   (por eso su `id` siempre sale `null`).

Los comentarios (`// Descuenta...`, `// Acredita...`) describen lo que el
código *debería* hacer; la lógica de abajo no cumple eso. Es justo lo que se
le pide encontrar al aspirante durante el reto: a simple vista el código
"parece" hacer lo correcto, pero al leerlo con cuidado no coincide con lo que
dice el comentario.

### Evidencia (con el bug, probado contra Mongo real)

```
GET /api/cuentas        -> 1001 = 1.000.000   1002 = 500.000
POST /api/transferencias {cuentaOrigen:1001, cuentaDestino:1002, monto:100000}
  -> {"id":null,...,"estado":"PENDIENTE"}
GET /api/cuentas        -> 1001 = 1.200.000 (¡subió!)   1002 = 500.000 (no cambió)
```

Y una transferencia de 50.000.000 (mucho más que el saldo) se acepta sin
ningún error, porque no se valida nada.

## La corrección

Dentro del mismo método, reemplazar esas líneas por:

```java
if (origen.getSaldo() < monto) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fondos insuficientes en la cuenta origen");
}

// Descuenta el monto de la cuenta origen
origen.setSaldo(origen.getSaldo() - monto);
cuentaRepository.save(origen);

// Acredita el monto en la cuenta destino
destino.setSaldo(destino.getSaldo() + monto);
cuentaRepository.save(destino);

Transaccion transaccion = new Transaccion(null, origen.getNumeroCuenta(), destino.getNumeroCuenta(),
        monto, "COMPLETADA", LocalDateTime.now());
transaccionRepository.save(transaccion);

return transaccion;
```

Los 4 cambios, uno por cada error:
1. `-` en vez de `+` para `origen`.
2. `destino.setSaldo(...)` (y `cuentaRepository.save(destino)`) en vez de
   volver a tocar `origen`.
3. Se agrega la validación de fondos suficientes antes de mover el dinero.
4. Se llama a `transaccionRepository.save(transaccion)` y el estado se marca
   `"COMPLETADA"`.

### Evidencia (ya corregido, probado contra Mongo real)

```
GET /api/cuentas        -> 1001 = 1.000.000   1002 = 500.000
POST /api/transferencias {cuentaOrigen:1001, cuentaDestino:1002, monto:100000}
  -> {"id":"<generado>",...,"estado":"COMPLETADA"}
GET /api/cuentas        -> 1001 = 900.000 (-100.000)   1002 = 600.000 (+100.000)
```

## Pruebas unitarias

`src/test/java/.../transferencias/TransferenciaControllerTest.java` usa
JUnit 5 + Mockito (sin levantar Spring ni Mongo: se simulan los dos
repositorios) y traduce el criterio de aceptación a 7 pruebas:

- `listarCuentas_debeRetornarLasCuentasDelRepositorio`
- `transferir_debeRechazarMontosMenoresOIgualesACero`
- `transferir_debeRechazarCuentaOrigenInexistente`
- `transferir_debeDescontarElMontoDeLaCuentaOrigen`
- `transferir_debeAcreditarElMontoEnLaCuentaDestino`
- `transferir_debeRechazarSiNoHayFondosSuficientes`
- `transferir_debeRegistrarLaTransaccionComoCompletada`

**Importante:** con el código tal como está hoy en el repo (con el bug), al
correr:

```bash
./mvnw test
```

4 de esas 7 pruebas **fallan a propósito** (descuento de saldo, acreditación
en destino, validación de fondos y registro de la transacción). Eso es
correcto y esperado: es la evidencia automatizada de la falla, además de la
evidencia manual por HTTP. Las otras 3 (monto inválido, cuenta inexistente,
listar cuentas) ya pasan porque esa parte no tiene el bug.

Cuando se aplica la corrección de la sección anterior, las 7 pruebas pasan
(se verificó localmente antes de entregar este módulo, aplicando el fix de
forma temporal).

## Cómo correr el reto de punta a punta

1. Levantar Mongo y crear el `.env` (ver sección de Persistencia).
2. `./mvnw spring-boot:run`
3. Abrir Swagger en `http://localhost:8080/swagger-ui/index.html` o usar `curl`:
   ```bash
   curl http://localhost:8080/api/cuentas
   curl -X POST http://localhost:8080/api/transferencias \
     -H "Content-Type: application/json" \
     -d '{"cuentaOrigen":"1001","cuentaDestino":"1002","monto":100000}'
   curl http://localhost:8080/api/cuentas
   ```
4. Confirmar que el saldo queda mal (paso "Falla").
5. Abrir `TransferenciaController.java`, aplicar la corrección indicada arriba.
6. Detener la app, volver a correr `./mvnw spring-boot:run`.
7. Repetir el mismo `curl` de transferencia.
8. Confirmar que ahora el saldo de origen baja, el de destino sube y la
   transacción queda registrada con `estado: "COMPLETADA"` y un `id` real.
9. `./mvnw test` debería mostrar las 7 pruebas en verde.

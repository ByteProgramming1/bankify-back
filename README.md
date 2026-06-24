# Bankify Back — Módulo de Transferencias

## Arquitectura

En vez de la separación clásica en capas (`config` / `controller` / `service` /
`repository` / `dto` regados por todo el proyecto), todo el módulo vive en
**un solo paquete**, `transferencias`, siguiendo la misma idea de
[POOBkemon](./POOBkemon): clases de **Modelo** simples (POJOs con su propio
estado) más una clase que hace de **Controlador** y coordina todo el
ejercicio (sin Vista, porque esto es solo backend):

```
src/main/java/edu/eci/byteprogramming/bankify/
├── BankifyApplication.java        (bootstrap + titulo de swagger)
└── transferencias/
    ├── Cuenta.java                 [Modelo] numeroCuenta, titular, saldo
    ├── Transaccion.java            [Modelo] registro de cada transferencia
    ├── TransferenciaRequest.java   [Modelo] cuerpo de la petición POST
    ├── TransferenciaController.java [Controlador] guarda las cuentas y
    │   transacciones en memoria y contiene los 2 endpoints + la lógica
    │   de negocio
    └── TransferenciaConsola.java   menu interactivo por consola (ver mas
        abajo "Probar desde la consola"), llama al mismo controlador
```

La lógica de la transferencia (la que contiene el error) vive **directamente en
el controlador**, sin una capa de "Service" intermedia, para que sea muy fácil
de encontrar y de leer durante el reto: todo el comportamiento de la
transferencia está en un solo método, `TransferenciaController.transferir(...)`.

Las clases `Cuenta`, `Transaccion` y `TransferenciaRequest` son POJOs comunes
(con constructor, getters y setters escritos a mano, sin Lombok, sin
anotaciones de base de datos) para que se pueda leer exactamente qué hace
cada método con solo abrir el archivo — igual que `Cuenta`, `Pokemon` o
`Entrenador` en POOBkemon.

## Persistencia: en memoria, sin base de datos ni `.env`

A diferencia de la versión anterior (que usaba MongoDB y leía la conexión
desde variables de entorno), este módulo **no se conecta a ninguna base de
datos**. `TransferenciaController` guarda las cuentas y las transacciones en
dos listas (`List<Cuenta>`, `List<Transaccion>`) que viven en memoria mientras
la aplicación está corriendo, exactamente como `MainGame` en POOBkemon guarda
sus `Pokemon` o sus `Entrenador` en listas en memoria en vez de en una base de
datos:

```java
private final List<Cuenta> cuentas = new ArrayList<>();
private final List<Transaccion> transacciones = new ArrayList<>();

public TransferenciaController() {
    cuentas.add(new Cuenta("1001", "Ana Perez", 1_000_000));
    cuentas.add(new Cuenta("1002", "Carlos Gomez", 500_000));
}
```

Esto tiene dos consecuencias importantes para el reto:

- **No hace falta instalar ni levantar Mongo.** No hay Docker, no hay cadena
  de conexión que configurar.
- **No hace falta archivo `.env`.** Ya no existen `MONGO_URI`, `MONGO_DATABASE`,
  `JWT_SECRET` ni `JWT_EXPIRATION`: se eliminaron `.env`, `.env.example` y el
  `spring.config.import` que los leía en `application.properties`.
- **Los datos no persisten entre ejecuciones.** Cada vez que se reinicia la
  app, `TransferenciaController` vuelve a arrancar con las 2 cuentas de
  prueba de siempre. Para este ejercicio es justo lo que se necesita: no hay
  que sembrar nada a mano ni preocuparse por "limpiar" datos de pruebas
  anteriores.

| numeroCuenta | titular      | saldo     |
|--------------|--------------|-----------|
| 1001         | Ana Perez    | 1.000.000 |
| 1002         | Carlos Gomez | 500.000   |

## Seguridad

El proyecto anterior incluía `spring-boot-starter-security` y dependencias de
JWT (`jjwt-*`) que no se usaban en este módulo: solo servían para que
`SecurityConfig` desactivara el login por defecto que Spring Security activa
con solo estar en el classpath. Como este módulo **nunca pidió autenticación**,
se eliminaron del `pom.xml` esa dependencia, las de `jjwt`, `spring-security-test`
y el archivo `SecurityConfig.java`. Sin Spring Security en el classpath no
hay nada que abrir ni que configurar: todas las rutas quedan disponibles
directamente.

## Cómo se ejecuta

Solo se necesitan dos comandos, sin nada externo que levantar antes:

```bash
mvn clean install
mvn spring-boot:run
```

(o `./mvnw clean install` / `./mvnw spring-boot:run` con el wrapper incluido
en el repo).

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

## Probar desde la consola (sin Swagger)

Al ejecutar `mvn spring-boot:run`, además de quedar levantada la API, aparece
un menú interactivo en la misma terminal (`TransferenciaConsola`, un
`CommandLineRunner` al estilo del `Scanner` que usa `MainGame` en POOBkemon):

```
=== Bankify - prueba de transferencias por consola ===
(la API REST sigue disponible en http://localhost:8080/api y en Swagger)

1. Listar cuentas
2. Hacer una transferencia
3. Salir
Elige una opcion:
```

- **Opción 1**: imprime `numeroCuenta | titular | saldo` de cada cuenta.
- **Opción 2**: pide cuenta origen, cuenta destino y monto, y muestra el
  resultado (`estado` e `id` de la transacción, o el mensaje de error si la
  cuenta no existe o el monto es inválido).
- **Opción 3**: cierra el menú y apaga la aplicación (Spring Boot incluido).

`TransferenciaConsola` recibe por inyección de dependencias el mismo
`TransferenciaController` que atiende la API, así que el bug (o la
corrección) se ve exactamente igual desde los dos lados. Por ejemplo, con el
bug presente:

```
Elige una opcion: 1
1001 | Ana Perez | saldo: 1000000.0
1002 | Carlos Gomez | saldo: 500000.0

Elige una opcion: 2
Cuenta origen: 1001
Cuenta destino: 1002
Monto a transferir: 100000
Transferencia procesada -> estado: PENDIENTE, id: null

Elige una opcion: 1
1001 | Ana Perez | saldo: 1200000.0
1002 | Carlos Gomez | saldo: 500000.0
```

> Este menú se desactiva automáticamente durante `mvn test` (perfil `test`,
> ver `BankifyApplicationTests`) para que las pruebas automatizadas no se
> queden esperando una entrada de consola que nunca llega.

## Dónde está el bug (estado actual del repo)

Todo el problema está en `TransferenciaController.transferir(...)`
(`src/main/java/edu/eci/byteprogramming/bankify/transferencias/TransferenciaController.java`,
líneas 50 a 59):

```java
// Descuenta el monto de la cuenta origen
origen.setSaldo(origen.getSaldo() + monto);

// Acredita el monto en la cuenta destino
origen.setSaldo(origen.getSaldo() + monto);

Transaccion transaccion = new Transaccion(null, origen.getNumeroCuenta(), destino.getNumeroCuenta(),
        monto, "PENDIENTE", LocalDateTime.now());

return transaccion;
```

Hay 4 errores, calcados de los 4 que pide el enunciado:

1. **Descuenta mal el saldo**: usa `+` en vez de `-` sobre `origen`. En lugar
   de restar, el monto se *suma* a la cuenta que está enviando el dinero.
2. **Suma el valor a la cuenta equivocada**: la segunda actualización (la que
   debería acreditar `destino`) vuelve a tocar `origen` por error de copiar y
   pegar la variable. `destino` nunca cambia.
3. **No valida fondos suficientes**: no existe ningún `if` que compare
   `origen.getSaldo()` contra `monto` antes de operar, así que se puede
   transferir más dinero del que hay en la cuenta.
4. **No registra adecuadamente la transacción**: se crea el objeto
   `Transaccion`, pero nunca se agrega a la lista `transacciones` en memoria,
   y el estado se deja en `"PENDIENTE"` (nunca pasa a `"COMPLETADA"`). El
   objeto se devuelve igual al cliente aunque nunca quedó registrado (por eso
   su `id` siempre sale `null`: el `id` solo se asigna cuando la transacción
   se agrega a la lista).

Los comentarios (`// Descuenta...`, `// Acredita...`) describen lo que el
código *debería* hacer; la lógica de abajo no cumple eso. Es justo lo que se
le pide encontrar al aspirante durante el reto: a simple vista el código
"parece" hacer lo correcto, pero al leerlo con cuidado no coincide con lo que
dice el comentario.

> **¿Por qué el saldo de origen sube el doble del monto?** Los bugs 1 y 2 se
> combinan sobre la misma variable: las dos líneas le suman `monto` a
> `origen` (en vez de restarle una vez y acreditarle a `destino`), así que
> `origen` termina aumentado en **2×monto**, no en 1×monto. Por eso, si
> transfieres 100.000 sobre un saldo de 1.000.000, el resultado es 1.200.000
> (no 1.100.000); y si transfieres el saldo completo (1.000.000), el
> resultado es 3.000.000 (no 2.000.000). No es un quinto error: es el efecto
> combinado de los bugs 1 y 2 ya descritos arriba.

### Evidencia (con el bug)

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

// Acredita el monto en la cuenta destino
destino.setSaldo(destino.getSaldo() + monto);

Transaccion transaccion = new Transaccion(null, origen.getNumeroCuenta(), destino.getNumeroCuenta(),
        monto, "COMPLETADA", LocalDateTime.now());
transacciones.add(transaccion);

return transaccion;
```

Los 4 cambios, uno por cada error:
1. `-` en vez de `+` para `origen`.
2. `destino.setSaldo(...)` en vez de volver a tocar `origen`.
3. Se agrega la validación de fondos suficientes antes de mover el dinero.
4. Se llama a `transacciones.add(transaccion)` y el estado se marca
   `"COMPLETADA"`.

### Evidencia (ya corregido)

```
GET /api/cuentas        -> 1001 = 1.000.000   1002 = 500.000
POST /api/transferencias {cuentaOrigen:1001, cuentaDestino:1002, monto:100000}
  -> {"id":"1",...,"estado":"COMPLETADA"}
GET /api/cuentas        -> 1001 = 900.000 (-100.000)   1002 = 600.000 (+100.000)
```

## Pruebas unitarias

`src/test/java/.../transferencias/TransferenciaControllerTest.java` usa
JUnit 5 (sin mocks: como las cuentas y transacciones viven en memoria dentro
del propio controlador, cada prueba crea un `TransferenciaController` nuevo
con `new TransferenciaController()` y verifica directamente su estado) y
traduce el criterio de aceptación a 7 pruebas:

- `listarCuentas_debeRetornarLasCuentasSembradas`
- `transferir_debeRechazarMontosMenoresOIgualesACero`
- `transferir_debeRechazarCuentaOrigenInexistente`
- `transferir_debeDescontarElMontoDeLaCuentaOrigen`
- `transferir_debeAcreditarElMontoEnLaCuentaDestino`
- `transferir_debeRechazarSiNoHayFondosSuficientes`
- `transferir_debeRegistrarLaTransaccionComoCompletada`

Además, `BankifyApplicationTests` ahora levanta el contexto completo de
Spring con `@SpringBootTest`: como ya no hay Mongo ni `.env` de por medio,
no hay ningún riesgo de que la prueba falle por no tener algo externo
configurado.

**Importante:** con el código tal como está hoy en el repo (con el bug), al
correr:

```bash
mvn test
```

4 de esas 7 pruebas de `TransferenciaControllerTest` **fallan a propósito**
(descuento de saldo, acreditación en destino, validación de fondos y registro
de la transacción). Eso es correcto y esperado: es la evidencia automatizada
de la falla, además de la evidencia manual por HTTP. Las otras 3 (monto
inválido, cuenta inexistente, listar cuentas) ya pasan porque esa parte no
tiene el bug.

Cuando se aplica la corrección de la sección anterior, las 7 pruebas pasan
(se verificó localmente antes de entregar este módulo, aplicando el fix de
forma temporal).

## Cómo correr el reto de punta a punta

1. `mvn spring-boot:run` (no hay nada más que instalar ni configurar).
2. Probar la transferencia por cualquiera de las tres vías (las tres hablan
   con el mismo estado en memoria):
   - El menú de consola que aparece en la misma terminal (ver "Probar desde
     la consola" más arriba).
   - Swagger en `http://localhost:8080/swagger-ui/index.html`.
   - `curl`:
     ```bash
     curl http://localhost:8080/api/cuentas
     curl -X POST http://localhost:8080/api/transferencias \
       -H "Content-Type: application/json" \
       -d '{"cuentaOrigen":"1001","cuentaDestino":"1002","monto":100000}'
     curl http://localhost:8080/api/cuentas
     ```
3. Confirmar que el saldo queda mal (paso "Falla").
4. Abrir `TransferenciaController.java`, aplicar la corrección indicada arriba.
5. Detener la app, volver a correr `mvn spring-boot:run`.
6. Repetir el mismo `curl` de transferencia.
7. Confirmar que ahora el saldo de origen baja, el de destino sube y la
   transacción queda registrada con `estado: "COMPLETADA"` y un `id` real.
8. `mvn test` debería mostrar las 7 pruebas de `TransferenciaControllerTest`
   en verde (más `BankifyApplicationTests`).

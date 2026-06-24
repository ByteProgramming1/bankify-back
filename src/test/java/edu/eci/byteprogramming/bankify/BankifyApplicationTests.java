package edu.eci.byteprogramming.bankify;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Como el proyecto ya no depende de Mongo ni de un .env, el contexto completo
 * de Spring se puede levantar en la prueba sin necesidad de nada externo.
 * El perfil "test" desactiva TransferenciaConsola para que la prueba no se
 * quede esperando una entrada de consola que nunca va a llegar.
 */
@SpringBootTest
@ActiveProfiles("test")
class BankifyApplicationTests {

	@Test
	void contextLoads() {
	}

}

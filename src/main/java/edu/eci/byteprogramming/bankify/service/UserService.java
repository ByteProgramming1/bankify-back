package edu.eci.byteprogramming.bankify.service;

import edu.eci.byteprogramming.bankify.model.User;
import edu.eci.byteprogramming.bankify.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Servicio para la lógica de negocio relacionada con los usuarios.
 * Implementa UserDetailsService para la integración con Spring Security.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor para inyectar el repositorio de usuarios.
     * @param userRepository Repositorio para las operaciones de base de datos de usuarios.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carga los detalles de un usuario por su nombre de usuario.
     * Este método es utilizado por Spring Security para la autenticación.
     * @param username El nombre de usuario.
     * @return Un objeto UserDetails con la información del usuario.
     * @throws UsernameNotFoundException Si el usuario no se encuentra.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username El nombre de usuario a buscar.
     * @return El usuario si se encuentra, o null si no.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}

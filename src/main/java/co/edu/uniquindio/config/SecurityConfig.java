package co.edu.uniquindio.config;

import co.edu.uniquindio.security.AuthenticationEntryPoint;
import co.edu.uniquindio.security.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Filtro personalizado para procesar los tokens JWT antes de la autenticación estándar
    private final JWTFilter jwtFilter;


    // Método de seguridad, configura el manejo de sesiones y donde estas pueden ingresar.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configura la seguridad de la aplicación

        http
                //  Desactiva CSRF (se usa JWT
                .csrf(AbstractHttpConfigurer::disable)
                //  Habilita CORS con configuración previa
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Define sesiones sin estado (stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Reglas de acceso por endpoint y rol
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // Docs públicas
                        .requestMatchers("/api/auth/**").permitAll() // Login/registro públicos
                        .requestMatchers("/api/store-it/**").permitAll() // Público store-it
                        .requestMatchers("/api/gestor-bodega/**").hasAnyAuthority("ROLE_GESTOR_BODEGA")
                        .requestMatchers("/api/auxiliar-bodega/**").hasAnyAuthority("ROLE_AUXILIAR_BODEGA")
                        .anyRequest().authenticated() // Resto requiere login
                )
                // Manejo de errores de autenticación
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new AuthenticationEntryPoint()))
                // Filtro JWT antes de auth por usuario/clave
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Devuelve la cadena de seguridad configurada
        return http.build();
    }



    // Configuración de los CORS ( Cross - Origin Resource Sharing )
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Configura CORS (Cross-Origin Resource Sharing) para permitir solicitudes desde otros orígenes

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "*"
        ));
        // Permite solicitudes desde cualquier origen (en producción es mejor restringir esto)

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Métodos HTTP permitidos

        config.setAllowedHeaders(List.of("*"));
        // Permite cualquier encabezado

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // Aplica esta configuración a todas las rutas

        return source;
    }



    // Define el bean que Spring usará para inyectar PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Devuelve el AuthenticationManager que Spring Security usa internamente
        return configuration.getAuthenticationManager();
    }
}

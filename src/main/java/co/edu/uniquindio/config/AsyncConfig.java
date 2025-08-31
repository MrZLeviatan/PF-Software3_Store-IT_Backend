package co.edu.uniquindio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuración para habilitar la ejecución asíncrona en la aplicación.
 * Esta clase permite que los métodos anotados con @Async se ejecuten en hilos separados sin bloquear el flujo principal.
 */
@Configuration
@EnableAsync    // Habílita el soporte para @Async en los servicios.
public class AsyncConfig implements AsyncConfigurer {
}

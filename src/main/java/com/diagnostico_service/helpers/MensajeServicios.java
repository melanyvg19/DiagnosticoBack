package com.diagnostico_service.helpers;

import lombok.Getter;

@Getter
public enum MensajeServicios {

    ASUNTO_FORM_TERMINADO("Diagnóstico Terminado"),
    MENSAJE_NIVEL_1("Nivel 1: La transformación digital es un área que requiere atención. Contáctenos para recibir una evaluación inicial y desarrollar un plan estratégico que le permita iniciar su camino hacia la digitalización."),
    MENSAJE_NIVEL_2("Nivel 2: Está comenzando a explorar la transformación digital. Agende una sesión de asesoramiento para definir y estructurar procesos clave que le ayuden a avanzar de manera efectiva."),
    MENSAJE_NIVEL_3("Nivel 3: La transformación digital es una parte integral de su cultura organizacional. Para profundizar y fortalecer su estrategia digital, solicite una revisión detallada de sus procesos y tecnologías actuales."),
    MENSAJE_NIVEL_4("Nivel 4: Está en buen camino hacia una transformación digital. Contacte a nuestro equipo para recibir recomendaciones sobre cómo consolidar y expandir sus iniciativas digitales."),
    MENSAJE_NIVEL_5("Nivel 5: ¡Excelente trabajo! Para seguir optimizando su rendimiento digital, puedes programar una consulta avanzada con nuestros expertos para explorar nuevas oportunidades de innovación."),
    NOMBRE_ARCHIVO_RESULTADOS_PDF("Resultados diagnóstico");

    private String mensaje;

    MensajeServicios() {
    }

    MensajeServicios(String mensaje) {
        this.mensaje = mensaje;
    }

}

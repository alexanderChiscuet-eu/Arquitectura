package com.example.arquitectura.dto;

import java.util.Map;

public record ResultadoFigura(
        String clase,
        Map<String, Object> parametros,
        double area,
        double perimetro
) {
}

package com.example.arquitectura.figuras;

/**
 * Contrato común para todas las figuras, tanto las clases Java nativas
 * como las clases Groovy cargadas en tiempo de ejecución.
 */
public interface Figura {

    double area();

    double perimetro();
}

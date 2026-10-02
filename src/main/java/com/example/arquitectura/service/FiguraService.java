package com.example.arquitectura.service;

import com.example.arquitectura.dto.ResultadoFigura;
import com.example.arquitectura.figuras.Figura;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class FiguraService {

    private static final String PAQUETE_FIGURAS = "com.example.arquitectura.figuras.";

    public ResultadoFigura crearFigura(String nombreClase, Map<String, Object> parametros) throws Exception {

        // Se busca la clase usando su nombre en tiempo de ejecucion.
        Class<?> clase = Class.forName(PAQUETE_FIGURAS + nombreClase);

        if (!Figura.class.isAssignableFrom(clase)) {
            throw new IllegalArgumentException("La clase no implementa Figura");
        }

        // Reflection: se crea el objeto sin usar new Circulo(), new Cuadrado(), etc.
        Object objeto = clase.getDeclaredConstructor().newInstance();

        // Reflection: se asignan los valores a los atributos de la figura.
        for (Map.Entry<String, Object> parametro : parametros.entrySet()) {
            Field campo = clase.getDeclaredField(parametro.getKey());
            campo.setAccessible(true);
            campo.set(objeto, convertirNumero(parametro.getValue(), campo.getType()));
        }

        // Reflection: se ejecutan los metodos area() y perimetro().
        Method metodoArea = clase.getMethod("area");
        Method metodoPerimetro = clase.getMethod("perimetro");

        double area = ((Number) metodoArea.invoke(objeto)).doubleValue();
        double perimetro = ((Number) metodoPerimetro.invoke(objeto)).doubleValue();

        return new ResultadoFigura(
                nombreClase,
                new LinkedHashMap<>(parametros),
                area,
                perimetro
        );
    }

    public Map<String, Class<?>> obtenerParametros(String nombreClase) throws ClassNotFoundException {
        Class<?> clase = Class.forName(PAQUETE_FIGURAS + nombreClase);
        Map<String, Class<?>> campos = new LinkedHashMap<>();

        for (Field campo : clase.getDeclaredFields()) {
            if (!Modifier.isStatic(campo.getModifiers()) && !campo.isSynthetic()) {
                campos.put(campo.getName(), campo.getType());
            }
        }

        return campos;
    }

    private Object convertirNumero(Object valor, Class<?> tipo) {
        Number numero = (Number) valor;

        if (tipo == double.class || tipo == Double.class) return numero.doubleValue();
        if (tipo == float.class || tipo == Float.class) return numero.floatValue();
        if (tipo == int.class || tipo == Integer.class) return numero.intValue();
        if (tipo == long.class || tipo == Long.class) return numero.longValue();

        return valor;
    }
}

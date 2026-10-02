package com.example.arquitectura.console;

import com.example.arquitectura.dto.ResultadoFigura;
import com.example.arquitectura.service.FiguraService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class FiguraConsole implements CommandLineRunner {

    private final FiguraService figuraService;

    public FiguraConsole(FiguraService figuraService) {
        this.figuraService = figuraService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            mostrarMenu();
            System.out.print("Opcion: ");
            String opcion = scanner.nextLine().trim();

            if (opcion.equals("0")) {
                System.out.println("Programa finalizado.");
                break;
            }

            String nombreClase = obtenerFigura(opcion);

            if (nombreClase == null) {
                System.out.println("Opcion no valida.\n");
                continue;
            }

            try {
                crearFigura(scanner, nombreClase);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private void mostrarMenu() {
        System.out.println("=========================");
        System.out.println("      FIGURAS");
        System.out.println("=========================");
        System.out.println("1. Circulo");
        System.out.println("2. Cuadrado");
        System.out.println("3. Rectangulo");
        System.out.println("4. Triangulo");
        System.out.println("0. Salir");
    }

    private String obtenerFigura(String opcion) {
        return switch (opcion) {
            case "1" -> "Circulo";
            case "2" -> "Cuadrado";
            case "3" -> "Rectangulo";
            case "4" -> "Triangulo";
            default -> null;
        };
    }

    private void crearFigura(Scanner scanner, String nombreClase) throws Exception {
        Map<String, Class<?>> campos = figuraService.obtenerParametros(nombreClase);
        Map<String, Object> parametros = new LinkedHashMap<>();

        System.out.println("\n" + nombreClase);

        for (Map.Entry<String, Class<?>> campo : campos.entrySet()) {
            double valor = leerNumero(scanner, campo.getKey());
            parametros.put(campo.getKey(), valor);
        }

        ResultadoFigura resultado = figuraService.crearFigura(nombreClase, parametros);

        System.out.println("\nResultado:");
        System.out.printf("Area: %.2f%n", resultado.area());
        System.out.printf("Perimetro: %.2f%n", resultado.perimetro());
    }

    private double leerNumero(Scanner scanner, String nombre) {
        while (true) {
            System.out.print("Ingrese " + nombre + ": ");
            String texto = scanner.nextLine().trim().replace(',', '.');

            try {
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        }
    }
}

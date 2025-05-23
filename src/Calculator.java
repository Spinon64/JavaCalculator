import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Clase Calculator - Implementa una calculadora gráfica usando Java Swing
 *
 * Esta calculadora simula el diseño y funcionalidad básica de una calculadora iOS,
 * con botones organizados en una cuadrícula y diferentes colores según su función.
 *
 * Funcionalidades implementadas:
 * - Operaciones básicas: suma, resta, multiplicación, división
 * - Funciones especiales: AC (limpiar), +/- (cambiar signo), % (porcentaje)
 * - Función raíz cuadrada
 * - Manejo de decimales
 * - Manejo de errores (división por cero)
 * - Interfaz gráfica con colores personalizados
 *
 * @author Salvador Pinon
 */
public class Calculator {

    // ==================== CONSTANTES DE DISEÑO ====================

    /** Ancho de la ventana de la calculadora en píxeles */
    int boardWidth = 360;

    /** Alto de la ventana de la calculadora en píxeles */
    int boardHeight = 540;

    // Definición de colores personalizados para simular el estilo iOS
    /** Color gris claro para botones de funciones especiales (AC, +/-, %) */
    Color customLightGray = new Color(212, 212, 210);

    /** Color gris oscuro para botones numéricos */
    Color customDarkGray = new Color(80, 80, 80);

    /** Color negro para el fondo y bordes */
    Color customBlack = new Color(28, 28, 28);

    /** Color naranja para botones de operadores matemáticos */
    Color customOrange = new Color(255, 149, 0);

    // ==================== CONFIGURACIÓN DE BOTONES ====================

    /**
     * Array que define el texto y orden de todos los botones de la calculadora
     * Organizados en 5 filas x 4 columnas = 20 botones total
     */
    String[] buttonValues = {
            "AC", "+/-", "%", "÷",    // Fila 1: Funciones especiales y división
            "7", "8", "9", "×",       // Fila 2: Números 7-9 y multiplicación
            "4", "5", "6", "-",       // Fila 3: Números 4-6 y resta
            "1", "2", "3", "+",       // Fila 4: Números 1-3 y suma
            "0", ".", "√", "="        // Fila 5: Cero, decimal, raíz y igual
    };

    /** Array con los símbolos de operadores matemáticos (lado derecho, color naranja) */
    String[] rightSymbols = {"÷", "×", "-", "+", "="};

    /** Array con los símbolos de funciones especiales (fila superior, color gris claro) */
    String[] topSymbols = {"AC", "+/-", "%"};

    // ==================== COMPONENTES DE LA INTERFAZ ====================

    /** Ventana principal de la aplicación */
    JFrame frame = new JFrame("Calculadora");

    /** Etiqueta que muestra los números y resultados */
    JLabel displayLabel = new JLabel();

    /** Panel contenedor de la pantalla */
    JPanel displayPanel = new JPanel();

    /** Panel contenedor de todos los botones */
    JPanel buttonsPanel = new JPanel();

    // ==================== VARIABLES DE ESTADO ====================

    /**
     * Primer operando (número ingresado antes del operador)
     * Se inicializa como "0"
     */
    String A = "0";

    /**
     * Operador matemático seleccionado (+, -, ×, ÷)
     * null indica que no hay operador seleccionado
     */
    String operator = null;

    /**
     * Segundo operando (número ingresado después del operador)
     * null hasta que se selecciona un operador
     */
    String B = null;

    /**
     * Bandera para controlar si se debe limpiar la pantalla en la siguiente entrada
     * Útil después de realizar un cálculo
     */
    boolean shouldClearDisplay = false;

    /**
     * Constructor de la clase Calculator
     * Inicializa y configura todos los componentes de la interfaz gráfica
     */
    Calculator() {

        // ==================== CONFIGURACIÓN DEL LOOK AND FEEL ====================

        // Establecer el Look and Feel del sistema para consistencia visual
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            System.err.println("Error setting Look and Feel: " + e.getMessage());
        }

        // ==================== CONFIGURACIÓN DE LA VENTANA PRINCIPAL ====================

        // Configurar propiedades básicas de la ventana
        frame.setSize(boardWidth, boardHeight);           // Establecer tamaño
        frame.setLocationRelativeTo(null);                // Centrar en pantalla
        frame.setResizable(false);                        // No permitir redimensionar
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Cerrar aplicación al cerrar ventana
        frame.setLayout(new BorderLayout());              // Usar BorderLayout para organizar componentes

        // ==================== CONFIGURACIÓN DE LA PANTALLA ====================

        // Configurar la etiqueta que muestra números y resultados
        displayLabel.setBackground(customBlack);          // Fondo negro
        displayLabel.setForeground(Color.white);          // Texto blanco
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));  // Fuente grande para visibilidad
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);  // Alinear texto a la derecha (como calculadoras reales)
        displayLabel.setText("0");                        // Valor inicial
        displayLabel.setOpaque(true);                     // Hacer visible el fondo

        // Configurar el panel de la pantalla y agregarlo a la ventana
        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        frame.add(displayPanel, BorderLayout.NORTH);      // Colocar en la parte superior

        // ==================== CONFIGURACIÓN DEL PANEL DE BOTONES ====================

        // Configurar el panel que contendrá todos los botones
        buttonsPanel.setLayout(new GridLayout(5, 4));     // Cuadrícula de 5 filas x 4 columnas
        buttonsPanel.setBackground(customBlack);          // Fondo negro
        frame.add(buttonsPanel);                          // Agregar al centro de la ventana

        // ==================== CREACIÓN DINÁMICA DE BOTONES ====================

        // Iterar sobre cada valor de botón para crear y configurar botones individualmente
        for (int i = 0; i < buttonValues.length; i++) {

            // Crear nuevo botón
            JButton button = new JButton();
            String buttonValue = buttonValues[i];          // Obtener el texto del botón actual

            // Configuración básica del botón
            button.setFont(new Font("Arial", Font.PLAIN, 30));  // Fuente mediana
            button.setText(buttonValue);                   // Establecer texto
            button.setFocusable(false);                   // Desactivar foco del teclado
            button.setOpaque(true);                       // Hacer visible el fondo
            button.setBorder(new LineBorder(customBlack)); // Borde negro delgado

            // ==================== ASIGNACIÓN DE COLORES SEGÚN FUNCIÓN ====================

            // Colorear botones según su función usando los arrays de clasificación
            if (Arrays.asList(topSymbols).contains(buttonValue)) {
                // Botones de funciones especiales (AC, +/-, %) - Gris claro
                button.setBackground(customLightGray);
                button.setForeground(customBlack);
            } else if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                // Botones de operadores matemáticos (÷, ×, -, +, =) - Naranja
                button.setBackground(customOrange);
                button.setForeground(Color.white);
            } else {
                // Botones numéricos y otros (0-9, ., √) - Gris oscuro
                button.setBackground(customDarkGray);
                button.setForeground(Color.white);
            }

            // Agregar botón al panel
            buttonsPanel.add(button);

            // ==================== MANEJO DE EVENTOS (ACTION LISTENER) ====================

            // Agregar funcionalidad al botón mediante ActionListener
            button.addActionListener(new ActionListener() {
                /**
                 * Método que se ejecuta cuando se presiona cualquier botón
                 * Contiene toda la lógica de la calculadora
                 */
                public void actionPerformed(ActionEvent e) {
                    // Obtener referencia al botón presionado y su texto
                    JButton button = (JButton) e.getSource();
                    String buttonValue = button.getText();

                    // ==================== PROCESAMIENTO DE OPERADORES MATEMÁTICOS ====================

                    if (Arrays.asList(rightSymbols).contains(buttonValue)) {

                        // CASO 1: Botón de IGUAL (=) - Realizar cálculo
                        if (buttonValue.equals("=")) {
                            // Solo calcular si hay un primer operando y un operador
                            if (A != null && operator != null) {
                                B = displayLabel.getText();  // El segundo operando es lo que está en pantalla

                                try {
                                    // Convertir strings a números para realizar operaciones
                                    double numA = Double.parseDouble(A);
                                    double numB = Double.parseDouble(B);
                                    double result = 0;

                                    // Realizar operación según el operador almacenado
                                    if (operator.equals("+")) {
                                        result = numA + numB;
                                    } else if (operator.equals("-")) {
                                        result = numA - numB;
                                    } else if (operator.equals("×")) {
                                        result = numA * numB;
                                    } else if (operator.equals("÷")) {
                                        // Verificar división por cero
                                        if (numB == 0) {
                                            displayLabel.setText("Error");
                                            clearAll();
                                            return;
                                        }
                                        result = numA / numB;
                                    }

                                    // Mostrar resultado y preparar para nueva operación
                                    displayLabel.setText(removeZeroDecimal(result));
                                    A = removeZeroDecimal(result);  // El resultado se convierte en el nuevo operando A
                                    operator = null;
                                    B = null;
                                    shouldClearDisplay = true;      // Limpiar en la siguiente entrada numérica

                                } catch (NumberFormatException ex) {
                                    // Manejo de errores de conversión
                                    displayLabel.setText("Error");
                                    clearAll();
                                }
                            }
                        }
                        // CASO 2: Operadores matemáticos (+, -, ×, ÷)
                        else if ("+-×÷".contains(buttonValue)) {
                            // Si ya hay una operación pendiente, calcularla primero
                            if (operator != null && A != null && !shouldClearDisplay) {
                                B = displayLabel.getText();
                                try {
                                    double numA = Double.parseDouble(A);
                                    double numB = Double.parseDouble(B);
                                    double result = 0;

                                    if (operator.equals("+")) {
                                        result = numA + numB;
                                    } else if (operator.equals("-")) {
                                        result = numA - numB;
                                    } else if (operator.equals("×")) {
                                        result = numA * numB;
                                    } else if (operator.equals("÷")) {
                                        if (numB == 0) {
                                            displayLabel.setText("Error");
                                            clearAll();
                                            return;
                                        }
                                        result = numA / numB;
                                    }

                                    displayLabel.setText(removeZeroDecimal(result));
                                    A = removeZeroDecimal(result);
                                } catch (NumberFormatException ex) {
                                    displayLabel.setText("Error");
                                    clearAll();
                                    return;
                                }
                            } else {
                                // Guardar el número actual como primer operando
                                A = displayLabel.getText();
                            }

                            operator = buttonValue;          // Guardar el operador seleccionado
                            shouldClearDisplay = true;       // Limpiar pantalla para el siguiente número
                        }
                    }

                    // ==================== PROCESAMIENTO DE FUNCIONES ESPECIALES ====================

                    else if (Arrays.asList(topSymbols).contains(buttonValue)) {

                        // CASO 1: All Clear (AC) - Reiniciar calculadora
                        if (buttonValue.equals("AC")) {
                            clearAll();                      // Limpiar todas las variables
                            displayLabel.setText("0");       // Mostrar 0 en pantalla
                        }
                        // CASO 2: Cambio de signo (+/-)
                        else if (buttonValue.equals("+/-")) {
                            try {
                                double numDisplay = Double.parseDouble(displayLabel.getText());
                                numDisplay *= -1;                // Multiplicar por -1 para cambiar signo
                                displayLabel.setText(removeZeroDecimal(numDisplay));
                            } catch (NumberFormatException ex) {
                                displayLabel.setText("Error");
                            }
                        }
                        // CASO 3: Porcentaje (%)
                        else if (buttonValue.equals("%")) {
                            try {
                                double numDisplay = Double.parseDouble(displayLabel.getText());
                                numDisplay /= 100;               // Dividir entre 100 para convertir a porcentaje
                                displayLabel.setText(removeZeroDecimal(numDisplay));
                            } catch (NumberFormatException ex) {
                                displayLabel.setText("Error");
                            }
                        }
                    }

                    // ==================== PROCESAMIENTO DE NÚMEROS Y CARACTERES ====================

                    else {
                        // CASO 1: Punto decimal (.)
                        if (buttonValue.equals(".")) {
                            // Si debe limpiar la pantalla, empezar con "0."
                            if (shouldClearDisplay) {
                                displayLabel.setText("0.");
                                shouldClearDisplay = false;
                            }
                            // Solo agregar punto si no existe ya uno en el número actual
                            else if (!displayLabel.getText().contains(".")) {
                                displayLabel.setText(displayLabel.getText() + ".");
                            }
                        }
                        // CASO 2: Raíz cuadrada (√)
                        else if (buttonValue.equals("√")) {
                            try {
                                double numDisplay = Double.parseDouble(displayLabel.getText());
                                if (numDisplay < 0) {
                                    displayLabel.setText("Error");  // No se puede calcular raíz de número negativo
                                } else {
                                    double result = Math.sqrt(numDisplay);
                                    displayLabel.setText(removeZeroDecimal(result));
                                    shouldClearDisplay = true;      // Limpiar en la siguiente entrada
                                }
                            } catch (NumberFormatException ex) {
                                displayLabel.setText("Error");
                            }
                        }
                        // CASO 3: Dígitos numéricos (0-9)
                        else if ("0123456789".contains(buttonValue)) {
                            // Si debe limpiar la pantalla o si muestra "0", reemplazar con el nuevo dígito
                            if (shouldClearDisplay || displayLabel.getText().equals("0")) {
                                displayLabel.setText(buttonValue);
                                shouldClearDisplay = false;
                            }
                            // Si no, agregar el dígito al final del número existente
                            else {
                                displayLabel.setText(displayLabel.getText() + buttonValue);
                            }
                        }
                    }
                }
            });
        }

        // ==================== MOSTRAR VENTANA ====================

        // Hacer visible la ventana (corregido: fuera del bucle for)
        frame.setVisible(true);
    }

    /**
     * Método para reiniciar todas las variables de estado de la calculadora
     * Se llama cuando se presiona el botón AC (All Clear) o cuando ocurre un error
     */
    void clearAll() {
        A = "0";                    // Reiniciar primer operando
        operator = null;            // Limpiar operador
        B = null;                   // Limpiar segundo operando
        shouldClearDisplay = false; // Resetear bandera de limpieza
    }

    /**
     * Método utilitario para formatear números decimales
     * Elimina ".0" de números enteros para mostrar formato limpio
     *
     * @param numDisplay el número a formatear
     * @return String representación formateada del número
     *
     * Ejemplo:
     * - 5.0 se convierte en "5"
     * - 5.5 permanece como "5.5"
     */
    String removeZeroDecimal(double numDisplay) {
        // Verificar si el número es entero (sin parte decimal)
        if (numDisplay % 1 == 0) {
            // Convertir a entero y luego a string para eliminar ".0"
            return Integer.toString((int) numDisplay);
        }
        // Si tiene decimales, mantener formato original
        return Double.toString(numDisplay);
    }

}
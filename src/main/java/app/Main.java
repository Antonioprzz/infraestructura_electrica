package app;

import model.Contador;
import model.ContratoSuministro;
import model.LecturaConsumo;
import model.LecturaConsumo.OrigenLectura;
import model.LineaTransporte;
import model.Subestacion;
import model.Titular;
import service.ContadorService;
import service.ContratoSuministroService;
import service.LecturaConsumoService;
import service.LineaTransporteService;
import service.SubestacionService;
import service.TitularService;
import util.JPAUtil;
import util.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal del programa.
 * Muestra un menú por consola para gestionar la red eléctrica:
 * subestaciones, líneas de transporte, titulares, contratos de
 * suministro, contadores y lecturas de consumo. Usa los servicios
 * para validar datos y guardar/leer información en la BBDD.
 *
 * @author Antonio Pérez Díaz, Daniel Del Toro, Antonio Beltrán, Juan María Alanis y Sergio Ojeda
 * @version 1.0
 */
public class Main {

    /** Scanner para leer datos por teclado. */
    static Scanner sc = new Scanner(System.in);
    /** Formato para introducir fechas (día/mes/año). */
    static DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    /** Formato para introducir fechas con hora (día/mes/año hora:minuto). */
    static DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /** Servicio para gestionar las subestaciones. */
    static SubestacionService svcSubestacion = new SubestacionService();
    /** Servicio para gestionar las líneas de transporte. */
    static LineaTransporteService svcLinea = new LineaTransporteService();
    /** Servicio para gestionar los titulares. */
    static TitularService svcTitular = new TitularService();
    /** Servicio para gestionar los contratos de suministro. */
    static ContratoSuministroService svcContrato = new ContratoSuministroService();
    /** Servicio para gestionar los contadores. */
    static ContadorService svcContador = new ContadorService();
    /** Servicio para gestionar las lecturas de consumo. */
    static LecturaConsumoService svcLectura = new LecturaConsumoService();

    /**
     * Punto de entrada del programa. Inicia la BBDD, muestra el menú
     * principal en bucle hasta que el usuario elige salir, y al final
     * cierra la conexión.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        System.out.println("+==============================================+");
        System.out.println("|     GESTION DE RED ELECTRICA  - ObjectDB    |");
        System.out.println("+==============================================+");
        System.out.println("  Iniciando base de datos...");
        try {
            JPAUtil.init();
            System.out.println("  Base de datos lista.");
        } catch (Exception e) {
            System.out.println("  Error al conectar con la base de datos: " + e.getMessage());
            return;
        }

        int opcion;
        do {
            System.out.println("\n+==============================================+");
            System.out.println("|              MENU PRINCIPAL                  |");
            System.out.println("+==============================================+");
            System.out.println("| 1. Subestaciones                             |");
            System.out.println("| 2. Lineas de transporte                      |");
            System.out.println("| 3. Titulares                                 |");
            System.out.println("| 4. Contratos de suministro                   |");
            System.out.println("| 5. Contadores                                |");
            System.out.println("| 6. Lecturas de consumo                       |");
            System.out.println("| 0. Salir                                     |");
            System.out.println("+==============================================+");
            System.out.print("  Elige una opcion: ");
            opcion = leerInt();

            if (opcion == 1) {
                menuSubestaciones();
            } else if (opcion == 2) {
                menuLineas();
            } else if (opcion == 3) {
                menuTitulares();
            } else if (opcion == 4) {
                menuContratos();
            } else if (opcion == 5) {
                menuContadores();
            } else if (opcion == 6) {
                menuLecturas();
            } else if (opcion == 0) {
                System.out.println("\n  Hasta luego!");
            } else {
                System.out.println("  Opcion no valida.");
            }
        } while (opcion != 0);

        JPAUtil.shutdown();
    }

    // -------------------------------------------------------
    // SUBESTACIONES
    // -------------------------------------------------------

    /** Muestra el submenú de gestión de subestaciones y llama a la opción que elija el usuario. */
    static void menuSubestaciones() {
        int opcion;
        do {
            System.out.println("\n+----------------------------------------------+");
            System.out.println("|           GESTION DE SUBESTACIONES           |");
            System.out.println("+----------------------------------------------+");
            System.out.println("| 1. Registrar subestacion                     |");
            System.out.println("| 2. Consultar por ID                          |");
            System.out.println("| 3. Listar todas                              |");
            System.out.println("| 4. Actualizar                                |");
            System.out.println("| 5. Eliminar                                  |");
            System.out.println("| 6. Ver subestaciones conectadas              |");
            System.out.println("| 0. Volver                                    |");
            System.out.println("+----------------------------------------------+");
            System.out.print("  Elige una opcion: ");
            opcion = leerInt();

            if (opcion == 1) {
                registrarSubestacion();
            } else if (opcion == 2) {
                consultarSubestacionPorId();
            } else if (opcion == 3) {
                listarSubestaciones();
            } else if (opcion == 4) {
                actualizarSubestacion();
            } else if (opcion == 5) {
                eliminarSubestacion();
            } else if (opcion == 6) {
                verSubestacionesConectadas();
            } else if (opcion != 0) {
                System.out.println("  Opcion no valida.");
            }
        } while (opcion != 0);
    }

    /** Pide al usuario los datos de una subestación nueva y la registra. */
    static void registrarSubestacion() {
        System.out.println("\n--- Nueva subestacion ---");
        try {
            System.out.print("  Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("  Provincia: ");
            String provincia = sc.nextLine();
            System.out.print("  Latitud: ");
            double latitud = Double.parseDouble(sc.nextLine().replace(",", "."));
            System.out.print("  Longitud: ");
            double longitud = Double.parseDouble(sc.nextLine().replace(",", "."));
            System.out.print("  Capacidad maxima (MW): ");
            double capacidad = Double.parseDouble(sc.nextLine().replace(",", "."));

            Subestacion s = svcSubestacion.registrar(nombre, provincia, latitud, longitud, capacidad);
            System.out.println("  OK - Subestacion registrada con ID=" + s.getId());
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("  Error: debes introducir un numero valido.");
        }
    }

    /** Consulta una subestación por id y muestra sus datos. */
    static void consultarSubestacionPorId() {
        System.out.println("\n--- Consultar subestacion ---");
        System.out.print("  ID de la subestacion: ");
        long id = leerLong();
        Subestacion s = svcSubestacion.buscarPorId(id).orElse(null);
        if (s != null) {
            System.out.println("  " + s);
        } else {
            System.out.println("  No se encontro ninguna subestacion con ID=" + id);
        }
    }

    /** Muestra por pantalla la lista de todas las subestaciones. */
    static void listarSubestaciones() {
        System.out.println("\n--- Listado de subestaciones ---");
        List<Subestacion> lista = svcSubestacion.listarTodas();
        if (lista.isEmpty()) {
            System.out.println("  No hay subestaciones registradas.");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println("  " + lista.get(i));
            }
        }
    }

    /** Pide los datos nuevos de una subestación y la actualiza. */
    static void actualizarSubestacion() {
        System.out.println("\n--- Actualizar subestacion ---");
        System.out.print("  ID de la subestacion a actualizar: ");
        long id = leerLong();
        Subestacion actual = svcSubestacion.buscarPorId(id).orElse(null);
        if (actual == null) {
            System.out.println("  No se encontro ninguna subestacion con ID=" + id);
            return;
        }
        System.out.println("  Datos actuales: " + actual);
        System.out.println("  (Deja en blanco para mantener el valor actual)");
        try {
            System.out.print("  Nombre [" + actual.getNombre() + "]: ");
            String nombre = sc.nextLine();
            if (nombre.isEmpty()) nombre = actual.getNombre();

            System.out.print("  Provincia [" + actual.getProvincia() + "]: ");
            String provincia = sc.nextLine();
            if (provincia.isEmpty()) provincia = actual.getProvincia();

            System.out.print("  Latitud [" + actual.getLatitud() + "]: ");
            String latStr = sc.nextLine();
            double latitud = latStr.isEmpty() ? actual.getLatitud() : Double.parseDouble(latStr.replace(",", "."));

            System.out.print("  Longitud [" + actual.getLongitud() + "]: ");
            String lonStr = sc.nextLine();
            double longitud = lonStr.isEmpty() ? actual.getLongitud() : Double.parseDouble(lonStr.replace(",", "."));

            System.out.print("  Capacidad maxima (MW) [" + actual.getCapacidadMaximaMW() + "]: ");
            String capStr = sc.nextLine();
            double capacidad = capStr.isEmpty() ? actual.getCapacidadMaximaMW() : Double.parseDouble(capStr.replace(",", "."));

            svcSubestacion.actualizar(id, nombre, provincia, latitud, longitud, capacidad);
            System.out.println("  OK - Subestacion actualizada correctamente.");
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("  Error: debes introducir un numero valido.");
        }
    }

    /** Borra una subestación pidiendo su id por teclado. */
    static void eliminarSubestacion() {
        System.out.println("\n--- Eliminar subestacion ---");
        System.out.print("  ID de la subestacion a eliminar: ");
        long id = leerLong();
        try {
            boolean eliminado = svcSubestacion.eliminar(id);
            if (eliminado) {
                System.out.println("  OK - Subestacion eliminada.");
            } else {
                System.out.println("  No se pudo eliminar.");
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    /** Muestra las subestaciones conectadas a una dada (por líneas de transporte). */
    static void verSubestacionesConectadas() {
        System.out.println("\n--- Subestaciones conectadas ---");
        System.out.print("  ID de la subestacion: ");
        long id = leerLong();
        try {
            List<Subestacion> conectadas = svcSubestacion.listarConectadas(id);
            if (conectadas.isEmpty()) {
                System.out.println("  Esta subestacion no tiene conexiones.");
            } else {
                System.out.println("  Subestaciones conectadas:");
                for (int i = 0; i < conectadas.size(); i++) {
                    System.out.println("  " + conectadas.get(i));
                }
            }
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // LINEAS DE TRANSPORTE
    // -------------------------------------------------------

    /** Submenú de gestión de líneas de transporte. */
    static void menuLineas() {
        int opcion;
        do {
            System.out.println("\n+----------------------------------------------+");
            System.out.println("|        GESTION DE LINEAS DE TRANSPORTE       |");
            System.out.println("+----------------------------------------------+");
            System.out.println("| 1. Registrar linea                           |");
            System.out.println("| 2. Consultar por ID                          |");
            System.out.println("| 3. Listar todas                              |");
            System.out.println("| 4. Actualizar                                |");
            System.out.println("| 5. Eliminar                                  |");
            System.out.println("| 0. Volver                                    |");
            System.out.println("+----------------------------------------------+");
            System.out.print("  Elige una opcion: ");
            opcion = leerInt();

            if (opcion == 1) {
                registrarLinea();
            } else if (opcion == 2) {
                consultarLineaPorId();
            } else if (opcion == 3) {
                listarLineas();
            } else if (opcion == 4) {
                actualizarLinea();
            } else if (opcion == 5) {
                eliminarLinea();
            } else if (opcion != 0) {
                System.out.println("  Opcion no valida.");
            }
        } while (opcion != 0);
    }

    /** Pide los datos para registrar una línea de transporte nueva. */
    static void registrarLinea() {
        System.out.println("\n--- Nueva linea de transporte ---");
        try {
            System.out.print("  Codigo: ");
            String codigo = sc.nextLine();
            System.out.print("  Longitud (km): ");
            double longKm = Double.parseDouble(sc.nextLine().replace(",", "."));
            System.out.print("  Voltaje (kV): ");
            double voltaje = Double.parseDouble(sc.nextLine().replace(",", "."));
            System.out.print("  Anio de instalacion: ");
            int anio = Integer.parseInt(sc.nextLine());
            System.out.print("  Tramo: ");
            String tramo = sc.nextLine();
            System.out.print("  ID subestacion origen: ");
            long idOrigen = leerLong();
            System.out.print("  ID subestacion destino: ");
            long idDestino = leerLong();

            LineaTransporte l = svcLinea.registrar(codigo, longKm, voltaje, anio, tramo, idOrigen, idDestino);
            System.out.println("  OK - Linea registrada con ID=" + l.getId());
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("  Error: debes introducir un numero valido.");
        }
    }

    /** Consulta una línea por id y muestra sus datos. */
    static void consultarLineaPorId() {
        System.out.println("\n--- Consultar linea ---");
        System.out.print("  ID de la linea: ");
        long id = leerLong();
        LineaTransporte l = svcLinea.buscarPorId(id).orElse(null);
        if (l != null) {
            System.out.println("  " + l);
        } else {
            System.out.println("  No existe ninguna linea con ID=" + id);
        }
    }

    /** Muestra la lista de todas las líneas de transporte. */
    static void listarLineas() {
        System.out.println("\n--- Listado de lineas de transporte ---");
        List<LineaTransporte> lista = svcLinea.listarTodas();
        if (lista.isEmpty()) {
            System.out.println("  No hay lineas registradas.");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println("  " + lista.get(i));
            }
        }
    }

    /** Pide datos nuevos para actualizar una línea existente. */
    static void actualizarLinea() {
        System.out.println("\n--- Actualizar linea de transporte ---");
        System.out.print("  ID de la linea a actualizar: ");
        long id = leerLong();
        LineaTransporte actual = svcLinea.buscarPorId(id).orElse(null);
        if (actual == null) {
            System.out.println("  No existe ninguna linea con ID=" + id);
            return;
        }
        System.out.println("  Datos actuales: " + actual);
        System.out.println("  (Deja en blanco para mantener el valor actual)");
        try {
            System.out.print("  Codigo [" + actual.getCodigo() + "]: ");
            String codigo = sc.nextLine();
            if (codigo.isEmpty()) codigo = actual.getCodigo();

            System.out.print("  Longitud km [" + actual.getLongitudKm() + "]: ");
            String longStr = sc.nextLine();
            double longKm = longStr.isEmpty() ? actual.getLongitudKm() : Double.parseDouble(longStr.replace(",", "."));

            System.out.print("  Voltaje kV [" + actual.getVoltajeKV() + "]: ");
            String voltStr = sc.nextLine();
            double voltaje = voltStr.isEmpty() ? actual.getVoltajeKV() : Double.parseDouble(voltStr.replace(",", "."));

            System.out.print("  Anio instalacion [" + actual.getAnioInstalacion() + "]: ");
            String anioStr = sc.nextLine();
            int anio = anioStr.isEmpty() ? actual.getAnioInstalacion() : Integer.parseInt(anioStr);

            System.out.print("  Tramo [" + actual.getTramo() + "]: ");
            String tramo = sc.nextLine();
            if (tramo.isEmpty()) tramo = actual.getTramo();

            System.out.print("  ID subestacion origen [" + actual.getSubestacionOrigen().getId() + "]: ");
            String origenStr = sc.nextLine();
            long idOrigen = origenStr.isEmpty() ? actual.getSubestacionOrigen().getId() : Long.parseLong(origenStr);

            System.out.print("  ID subestacion destino [" + actual.getSubestacionDestino().getId() + "]: ");
            String destinoStr = sc.nextLine();
            long idDestino = destinoStr.isEmpty() ? actual.getSubestacionDestino().getId() : Long.parseLong(destinoStr);

            svcLinea.actualizar(id, codigo, longKm, voltaje, anio, tramo, idOrigen, idDestino);
            System.out.println("  OK - Linea actualizada correctamente.");
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("  Error: debes introducir un numero valido.");
        }
    }

    /** Borra una línea pidiendo el id por teclado. */
    static void eliminarLinea() {
        System.out.println("\n--- Eliminar linea ---");
        System.out.print("  ID de la linea a eliminar: ");
        long id = leerLong();
        try {
            boolean eliminado = svcLinea.eliminar(id);
            if (eliminado) {
                System.out.println("  OK - Linea eliminada.");
            } else {
                System.out.println("  No se pudo eliminar.");
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // TITULARES
    // -------------------------------------------------------

    static void menuTitulares() {
        int opcion;
        do {
            System.out.println("\n+----------------------------------------------+");
            System.out.println("|           GESTION DE TITULARES               |");
            System.out.println("+----------------------------------------------+");
            System.out.println("| 1. Registrar titular                         |");
            System.out.println("| 2. Consultar por ID                          |");
            System.out.println("| 3. Listar todos                              |");
            System.out.println("| 4. Actualizar                                |");
            System.out.println("| 5. Eliminar                                  |");
            System.out.println("| 0. Volver                                    |");
            System.out.println("+----------------------------------------------+");
            System.out.print("  Elige una opcion: ");
            opcion = leerInt();

            if (opcion == 1) {
                registrarTitular();
            } else if (opcion == 2) {
                consultarTitularPorId();
            } else if (opcion == 3) {
                listarTitulares();
            } else if (opcion == 4) {
                actualizarTitular();
            } else if (opcion == 5) {
                eliminarTitular();
            } else if (opcion != 0) {
                System.out.println("  Opcion no valida.");
            }
        } while (opcion != 0);
    }

    static void registrarTitular() {
        System.out.println("\n--- Nuevo titular ---");
        try {
            System.out.print("  Nombre o razon social: ");
            String nombre = sc.nextLine();
            System.out.print("  NIF: ");
            String nif = sc.nextLine();
            System.out.print("  Direccion (opcional, INTRO para dejar vacio): ");
            String direccion = sc.nextLine();
            System.out.print("  Email (opcional, INTRO para dejar vacio): ");
            String email = sc.nextLine();

            Titular t = svcTitular.registrar(nombre, nif, direccion, email);
            System.out.println("  OK - Titular registrado con ID=" + t.getId());
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    static void consultarTitularPorId() {
        System.out.println("\n--- Consultar titular ---");
        System.out.print("  ID del titular: ");
        long id = leerLong();
        Titular t = svcTitular.buscarPorId(id).orElse(null);
        if (t != null) {
            System.out.println("  " + t);
        } else {
            System.out.println("  No existe ningun titular con ID=" + id);
        }
    }

    static void listarTitulares() {
        System.out.println("\n--- Listado de titulares ---");
        List<Titular> lista = svcTitular.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("  No hay titulares registrados.");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println("  " + lista.get(i));
            }
        }
    }

    static void actualizarTitular() {
        System.out.println("\n--- Actualizar titular ---");
        System.out.print("  ID del titular a actualizar: ");
        long id = leerLong();
        Titular actual = svcTitular.buscarPorId(id).orElse(null);
        if (actual == null) {
            System.out.println("  No existe ningun titular con ID=" + id);
            return;
        }
        System.out.println("  Datos actuales: " + actual);
        System.out.println("  (Deja en blanco para mantener el valor actual)");
        try {
            System.out.print("  Nombre [" + actual.getNombreCompleto() + "]: ");
            String nombre = sc.nextLine();
            if (nombre.isEmpty()) nombre = actual.getNombreCompleto();

            System.out.print("  NIF [" + actual.getNif() + "]: ");
            String nif = sc.nextLine();
            if (nif.isEmpty()) nif = actual.getNif();

            System.out.print("  Direccion [" + actual.getDireccion() + "]: ");
            String direccion = sc.nextLine();
            if (direccion.isEmpty()) direccion = actual.getDireccion();

            System.out.print("  Email [" + actual.getEmail() + "]: ");
            String email = sc.nextLine();
            if (email.isEmpty()) email = actual.getEmail();

            svcTitular.actualizar(id, nombre, nif, direccion, email);
            System.out.println("  OK - Titular actualizado correctamente.");
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    static void eliminarTitular() {
        System.out.println("\n--- Eliminar titular ---");
        System.out.print("  ID del titular a eliminar: ");
        long id = leerLong();
        try {
            boolean eliminado = svcTitular.eliminar(id);
            if (eliminado) {
                System.out.println("  OK - Titular eliminado.");
            } else {
                System.out.println("  No se pudo eliminar.");
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // CONTRATOS DE SUMINISTRO
    // -------------------------------------------------------

    static void menuContratos() {
        int opcion;
        do {
            System.out.println("\n+----------------------------------------------+");
            System.out.println("|       GESTION DE CONTRATOS DE SUMINISTRO     |");
            System.out.println("+----------------------------------------------+");
            System.out.println("| 1. Registrar contrato                        |");
            System.out.println("| 2. Consultar por ID                          |");
            System.out.println("| 3. Listar contratos de un titular            |");
            System.out.println("| 4. Actualizar                                |");
            System.out.println("| 5. Eliminar                                  |");
            System.out.println("| 0. Volver                                    |");
            System.out.println("+----------------------------------------------+");
            System.out.print("  Elige una opcion: ");
            opcion = leerInt();

            if (opcion == 1) {
                registrarContrato();
            } else if (opcion == 2) {
                consultarContratoPorId();
            } else if (opcion == 3) {
                listarContratosPorTitular();
            } else if (opcion == 4) {
                actualizarContrato();
            } else if (opcion == 5) {
                eliminarContrato();
            } else if (opcion != 0) {
                System.out.println("  Opcion no valida.");
            }
        } while (opcion != 0);
    }

    static void registrarContrato() {
        System.out.println("\n--- Nuevo contrato de suministro ---");
        try {
            System.out.print("  Codigo de contrato: ");
            String codigo = sc.nextLine();
            System.out.print("  Tarifa (ej: 2.0TD): ");
            String tarifa = sc.nextLine();
            System.out.print("  Fecha de alta (dd/MM/yyyy): ");
            LocalDate fechaAlta = LocalDate.parse(sc.nextLine(), formatoFecha);
            System.out.print("  Potencia contratada (kW): ");
            double potencia = Double.parseDouble(sc.nextLine().replace(",", "."));
            System.out.print("  ID del titular: ");
            long titularId = leerLong();

            ContratoSuministro c = svcContrato.registrar(codigo, tarifa, fechaAlta, potencia, titularId);
            System.out.println("  OK - Contrato registrado con ID=" + c.getId());
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("  Error: formato de fecha incorrecto. Usa dd/MM/yyyy.");
        } catch (NumberFormatException e) {
            System.out.println("  Error: debes introducir un numero valido.");
        }
    }

    static void consultarContratoPorId() {
        System.out.println("\n--- Consultar contrato ---");
        System.out.print("  ID del contrato: ");
        long id = leerLong();
        ContratoSuministro c = svcContrato.buscarPorId(id).orElse(null);
        if (c != null) {
            System.out.println("  " + c);
        } else {
            System.out.println("  No existe ningun contrato con ID=" + id);
        }
    }

    static void listarContratosPorTitular() {
        System.out.println("\n--- Contratos de un titular ---");
        System.out.print("  ID del titular: ");
        long titularId = leerLong();
        try {
            List<ContratoSuministro> lista = svcContrato.listarPorTitular(titularId);
            if (lista.isEmpty()) {
                System.out.println("  Este titular no tiene contratos.");
            } else {
                for (int i = 0; i < lista.size(); i++) {
                    System.out.println("  " + lista.get(i));
                }
            }
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    static void actualizarContrato() {
        System.out.println("\n--- Actualizar contrato ---");
        System.out.print("  ID del contrato a actualizar: ");
        long id = leerLong();
        ContratoSuministro actual = svcContrato.buscarPorId(id).orElse(null);
        if (actual == null) {
            System.out.println("  No existe ningun contrato con ID=" + id);
            return;
        }
        System.out.println("  Datos actuales: " + actual);
        System.out.println("  (Deja en blanco para mantener el valor actual)");
        try {
            System.out.print("  Codigo [" + actual.getCodigoContrato() + "]: ");
            String codigo = sc.nextLine();
            if (codigo.isEmpty()) codigo = actual.getCodigoContrato();

            System.out.print("  Tarifa [" + actual.getTarifa() + "]: ");
            String tarifa = sc.nextLine();
            if (tarifa.isEmpty()) tarifa = actual.getTarifa();

            System.out.print("  Fecha de alta (dd/MM/yyyy) [" + actual.getFechaAlta().format(formatoFecha) + "]: ");
            String fechaStr = sc.nextLine();
            LocalDate fechaAlta = fechaStr.isEmpty() ? actual.getFechaAlta() : LocalDate.parse(fechaStr, formatoFecha);

            System.out.print("  Potencia (kW) [" + actual.getPotenciaContratadaKW() + "]: ");
            String potStr = sc.nextLine();
            double potencia = potStr.isEmpty() ? actual.getPotenciaContratadaKW() : Double.parseDouble(potStr.replace(",", "."));

            System.out.print("  ID del titular [" + actual.getTitular().getId() + "]: ");
            String titStr = sc.nextLine();
            long titularId = titStr.isEmpty() ? actual.getTitular().getId() : Long.parseLong(titStr);

            svcContrato.actualizar(id, codigo, tarifa, fechaAlta, potencia, titularId);
            System.out.println("  OK - Contrato actualizado correctamente.");
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("  Error: formato de fecha incorrecto. Usa dd/MM/yyyy.");
        } catch (NumberFormatException e) {
            System.out.println("  Error: debes introducir un numero valido.");
        }
    }

    static void eliminarContrato() {
        System.out.println("\n--- Eliminar contrato ---");
        System.out.print("  ID del contrato a eliminar: ");
        long id = leerLong();
        try {
            boolean eliminado = svcContrato.eliminar(id);
            if (eliminado) {
                System.out.println("  OK - Contrato eliminado.");
            } else {
                System.out.println("  No se pudo eliminar.");
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // CONTADORES
    // -------------------------------------------------------

    static void menuContadores() {
        int opcion;
        do {
            System.out.println("\n+----------------------------------------------+");
            System.out.println("|           GESTION DE CONTADORES              |");
            System.out.println("+----------------------------------------------+");
            System.out.println("| 1. Registrar contador                        |");
            System.out.println("| 2. Consultar por ID                          |");
            System.out.println("| 3. Consultar contador de un contrato         |");
            System.out.println("| 4. Actualizar                                |");
            System.out.println("| 5. Eliminar                                  |");
            System.out.println("| 0. Volver                                    |");
            System.out.println("+----------------------------------------------+");
            System.out.print("  Elige una opcion: ");
            opcion = leerInt();

            if (opcion == 1) {
                registrarContador();
            } else if (opcion == 2) {
                consultarContadorPorId();
            } else if (opcion == 3) {
                consultarContadorPorContrato();
            } else if (opcion == 4) {
                actualizarContador();
            } else if (opcion == 5) {
                eliminarContador();
            } else if (opcion != 0) {
                System.out.println("  Opcion no valida.");
            }
        } while (opcion != 0);
    }

    static void registrarContador() {
        System.out.println("\n--- Nuevo contador ---");
        try {
            System.out.print("  Numero de serie: ");
            String serie = sc.nextLine();
            System.out.print("  Modelo: ");
            String modelo = sc.nextLine();
            System.out.print("  Fecha de instalacion (dd/MM/yyyy): ");
            LocalDate fecha = LocalDate.parse(sc.nextLine(), formatoFecha);
            System.out.print("  ID del contrato: ");
            long contratoId = leerLong();

            Contador c = svcContador.registrar(serie, modelo, fecha, contratoId);
            System.out.println("  OK - Contador registrado con ID=" + c.getId());
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("  Error: formato de fecha incorrecto. Usa dd/MM/yyyy.");
        }
    }

    static void consultarContadorPorId() {
        System.out.println("\n--- Consultar contador ---");
        System.out.print("  ID del contador: ");
        long id = leerLong();
        Contador c = svcContador.buscarPorId(id).orElse(null);
        if (c != null) {
            System.out.println("  " + c);
        } else {
            System.out.println("  No existe ningun contador con ID=" + id);
        }
    }

    static void consultarContadorPorContrato() {
        System.out.println("\n--- Contador de un contrato ---");
        System.out.print("  ID del contrato: ");
        long contratoId = leerLong();
        try {
            Contador c = svcContador.buscarPorContrato(contratoId).orElse(null);
            if (c != null) {
                System.out.println("  " + c);
            } else {
                System.out.println("  Este contrato no tiene contador asignado.");
            }
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    static void actualizarContador() {
        System.out.println("\n--- Actualizar contador ---");
        System.out.print("  ID del contador a actualizar: ");
        long id = leerLong();
        Contador actual = svcContador.buscarPorId(id).orElse(null);
        if (actual == null) {
            System.out.println("  No existe ningun contador con ID=" + id);
            return;
        }
        System.out.println("  Datos actuales: " + actual);
        System.out.println("  (Deja en blanco para mantener el valor actual)");
        try {
            System.out.print("  Numero de serie [" + actual.getNumeroSerie() + "]: ");
            String serie = sc.nextLine();
            if (serie.isEmpty()) serie = actual.getNumeroSerie();

            System.out.print("  Modelo [" + actual.getModelo() + "]: ");
            String modelo = sc.nextLine();
            if (modelo.isEmpty()) modelo = actual.getModelo();

            System.out.print("  Fecha de instalacion (dd/MM/yyyy) [" + actual.getFechaInstalacion().format(formatoFecha) + "]: ");
            String fechaStr = sc.nextLine();
            LocalDate fecha = fechaStr.isEmpty() ? actual.getFechaInstalacion() : LocalDate.parse(fechaStr, formatoFecha);

            System.out.print("  ID del contrato [" + actual.getContrato().getId() + "]: ");
            String contratoStr = sc.nextLine();
            long contratoId = contratoStr.isEmpty() ? actual.getContrato().getId() : Long.parseLong(contratoStr);

            svcContador.actualizar(id, serie, modelo, fecha, contratoId);
            System.out.println("  OK - Contador actualizado correctamente.");
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("  Error: formato de fecha incorrecto. Usa dd/MM/yyyy.");
        } catch (NumberFormatException e) {
            System.out.println("  Error: debes introducir un numero valido.");
        }
    }

    static void eliminarContador() {
        System.out.println("\n--- Eliminar contador ---");
        System.out.print("  ID del contador a eliminar: ");
        long id = leerLong();
        try {
            boolean eliminado = svcContador.eliminar(id);
            if (eliminado) {
                System.out.println("  OK - Contador eliminado.");
            } else {
                System.out.println("  No se pudo eliminar.");
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // LECTURAS DE CONSUMO
    // -------------------------------------------------------

    static void menuLecturas() {
        int opcion;
        do {
            System.out.println("\n+----------------------------------------------+");
            System.out.println("|        GESTION DE LECTURAS DE CONSUMO        |");
            System.out.println("+----------------------------------------------+");
            System.out.println("| 1. Registrar lectura                         |");
            System.out.println("| 2. Consultar por ID                          |");
            System.out.println("| 3. Listar lecturas de un contador            |");
            System.out.println("| 4. Actualizar                                |");
            System.out.println("| 5. Eliminar                                  |");
            System.out.println("| 0. Volver                                    |");
            System.out.println("+----------------------------------------------+");
            System.out.print("  Elige una opcion: ");
            opcion = leerInt();

            if (opcion == 1) {
                registrarLectura();
            } else if (opcion == 2) {
                consultarLecturaPorId();
            } else if (opcion == 3) {
                listarLecturasPorContador();
            } else if (opcion == 4) {
                actualizarLectura();
            } else if (opcion == 5) {
                eliminarLectura();
            } else if (opcion != 0) {
                System.out.println("  Opcion no valida.");
            }
        } while (opcion != 0);
    }

    static void registrarLectura() {
        System.out.println("\n--- Nueva lectura de consumo ---");
        try {
            System.out.print("  Fecha y hora (dd/MM/yyyy HH:mm): ");
            LocalDateTime fechaHora = LocalDateTime.parse(sc.nextLine(), formatoFechaHora);
            System.out.print("  Valor (kWh): ");
            double valor = Double.parseDouble(sc.nextLine().replace(",", "."));
            OrigenLectura origen = pedirOrigen();
            System.out.print("  ID del contador: ");
            long contadorId = leerLong();

            LecturaConsumo l = svcLectura.registrar(fechaHora, valor, origen, contadorId);
            System.out.println("  OK - Lectura registrada con ID=" + l.getId());
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("  Error: formato de fecha incorrecto. Usa dd/MM/yyyy HH:mm.");
        } catch (NumberFormatException e) {
            System.out.println("  Error: debes introducir un numero valido.");
        }
    }

    static void consultarLecturaPorId() {
        System.out.println("\n--- Consultar lectura ---");
        System.out.print("  ID de la lectura: ");
        long id = leerLong();
        LecturaConsumo l = svcLectura.buscarPorId(id).orElse(null);
        if (l != null) {
            System.out.println("  " + l);
        } else {
            System.out.println("  No existe ninguna lectura con ID=" + id);
        }
    }

    static void listarLecturasPorContador() {
        System.out.println("\n--- Lecturas de un contador ---");
        System.out.print("  ID del contador: ");
        long contadorId = leerLong();
        try {
            List<LecturaConsumo> lista = svcLectura.listarPorContador(contadorId);
            if (lista.isEmpty()) {
                System.out.println("  No hay lecturas para este contador.");
            } else {
                for (int i = 0; i < lista.size(); i++) {
                    System.out.println("  " + lista.get(i));
                }
            }
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    static void actualizarLectura() {
        System.out.println("\n--- Actualizar lectura ---");
        System.out.print("  ID de la lectura a actualizar: ");
        long id = leerLong();
        LecturaConsumo actual = svcLectura.buscarPorId(id).orElse(null);
        if (actual == null) {
            System.out.println("  No existe ninguna lectura con ID=" + id);
            return;
        }
        System.out.println("  Datos actuales: " + actual);
        System.out.println("  (Deja en blanco para mantener el valor actual)");
        try {
            System.out.print("  Fecha y hora (dd/MM/yyyy HH:mm) [" + actual.getFechaHora().format(formatoFechaHora) + "]: ");
            String fechaStr = sc.nextLine();
            LocalDateTime fechaHora = fechaStr.isEmpty() ? actual.getFechaHora() : LocalDateTime.parse(fechaStr, formatoFechaHora);

            System.out.print("  Valor (kWh) [" + actual.getValorKWh() + "]: ");
            String valorStr = sc.nextLine();
            double valor = valorStr.isEmpty() ? actual.getValorKWh() : Double.parseDouble(valorStr.replace(",", "."));

            OrigenLectura origen = pedirOrigenOpcional(actual.getOrigen());

            System.out.print("  ID del contador [" + actual.getContador().getId() + "]: ");
            String contStr = sc.nextLine();
            long contadorId = contStr.isEmpty() ? actual.getContador().getId() : Long.parseLong(contStr);

            svcLectura.actualizar(id, fechaHora, valor, origen, contadorId);
            System.out.println("  OK - Lectura actualizada correctamente.");
        } catch (ValidationException e) {
            System.out.println("  Error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("  Error: formato de fecha incorrecto. Usa dd/MM/yyyy HH:mm.");
        } catch (NumberFormatException e) {
            System.out.println("  Error: debes introducir un numero valido.");
        }
    }

    static void eliminarLectura() {
        System.out.println("\n--- Eliminar lectura ---");
        System.out.print("  ID de la lectura a eliminar: ");
        long id = leerLong();
        try {
            boolean eliminado = svcLectura.eliminar(id);
            if (eliminado) {
                System.out.println("  OK - Lectura eliminada.");
            } else {
                System.out.println("  No se pudo eliminar.");
            }
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // METODOS AUXILIARES
    // -------------------------------------------------------

    static OrigenLectura pedirOrigen() {
        while (true) {
            System.out.println("  Origen de la lectura:");
            System.out.println("  1. AUTOMATICO");
            System.out.println("  2. MANUAL");
            System.out.print("  Elige una opcion: ");
            int op = leerInt();
            if (op == 1) {
                return OrigenLectura.AUTOMATICO;
            } else if (op == 2) {
                return OrigenLectura.MANUAL;
            } else {
                System.out.println("  Opcion no valida, introduce 1 o 2.");
            }
        }
    }

    static OrigenLectura pedirOrigenOpcional(OrigenLectura origenActual) {
        while (true) {
            System.out.println("  Origen [" + origenActual + "]:");
            System.out.println("  1. AUTOMATICO");
            System.out.println("  2. MANUAL");
            System.out.println("  0. Mantener actual");
            System.out.print("  Elige una opcion: ");
            int op = leerInt();
            if (op == 0) {
                return origenActual;
            } else if (op == 1) {
                return OrigenLectura.AUTOMATICO;
            } else if (op == 2) {
                return OrigenLectura.MANUAL;
            } else {
                System.out.println("  Opcion no valida.");
            }
        }
    }

    static int leerInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  Debes introducir un numero entero: ");
            }
        }
    }

    static long leerLong() {
        while (true) {
            try {
                return Long.parseLong(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  Debes introducir un numero entero: ");
            }
        }
    }
}
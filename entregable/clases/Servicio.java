package entregable.Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Servicio{

    private final Connection con;

    public Servicio(Connection con) {
        this.con = con;
    }

    public void comprarActivo(Scanner ent) {
        // Listado de criptomonedas
        System.out.println("Criptomonedas disponibles para comprar: ");
        Criptomoneda c = new Criptomoneda();
        List<Moneda> lista = c.listarMonedas(con, "sigla");  
            for (Moneda moneda : lista) {                
                System.out.println("Sigla: " + moneda.getSigla());            
            }

        // Solicitud de datos
        System.out.print("Ingresa la sigla de la criptomoneda a comprar: ");
        String sigla = ent.nextLine().trim().toUpperCase();

        // Chequeo que exista la cripto
        if (!c.existeMoneda(con, sigla)) {
            System.out.println("La criptomoneda ingresada no existe");
        } 

        // Continuo solicitando
        System.out.println("Fiat disponibles en la billetera para usar: ");
        List<Activo_Fiat> listaFiat = Activo_Fiat.listarActivosFiat(con, "sigla");
        for (Activo_Fiat activo : listaFiat) {
            System.out.println("Sigla: " + activo.getSigla() + ", Cantidad: " + activo.getCantidad());
        }
        System.out.println("Ingrese la moneda fiat con la que desea comprar: ");
        String siglaFiat = ent.nextLine().trim().toUpperCase();

        if(!Activo_Fiat.existeActivo(con, siglaFiat)){
            System.out.println("El activo no existe en tu billetera");
            return;
        }

        System.out.print("Ingresa la cantidad en " + siglaFiat + " que deseas comprar: ");
        double cantidad = ent.nextInt();

        // Metodo para pasar a dolar el fiat
        double precioActual = Fiat.getPrecioActual(con, siglaFiat); 
        double totalUSD = cantidad / precioActual;

        if (totalUSD <= 0) {
            System.out.println("Por favor ingrese un valor positivo.");
            return;
        }        
        ent.nextLine(); // Limpiar el buffer

        // Iniciar Transaccion
        Transaccion t = new Transaccion();
        t.comprarActivo2(con, sigla, siglaFiat, totalUSD);
    }
    
    public void realizarSwap(Scanner ent) {
        // Listado de criptomonedas
        System.out.println("Criptomonedas disponibles para swapear: ");
        List<Activo_Cripto> lista = Activo_Cripto.listarActivosCripto(con, "sigla");
        for (Activo_Cripto activo : lista) {
            System.out.println("Sigla: " + activo.getSigla() + ", Cantidad: " + activo.getCantidad());
        }

        // Solicito datos
        System.out.print("Ingresa la sigla de la criptomoneda a convertir: ");
        String siglaConvertir = ent.nextLine();

        if (!Activo_Cripto.existeActivo(con, siglaConvertir)){
            System.out.println("No tienes un activo con la sigla " +siglaConvertir);
            return;
        }

        System.out.print("Ingresa la cantidad a convertir: ");
        double cantidadConvertir = ent.nextDouble();
        ent.nextLine(); // Limpiar el buffer

        System.out.print("Ingresa la sigla de la criptomoneda esperada: ");
        String siglaEsperada = ent.nextLine();

        if (!Activo_Cripto.existeActivo(con, siglaEsperada)){
            System.out.println("No tienes un activo con la sigla " +siglaEsperada);
            return;
        }

        // Iniciar Transaccion
        Transaccion t = new Transaccion();
        t.realizarSwap(con, siglaConvertir, cantidadConvertir, siglaEsperada, ent);
    }
    
    public void ingresarMoneda(Scanner ent) {
        // Solicitud datos
        System.out.print("Ingresa el nombre de la moneda: ");
        String nombre = ent.nextLine();
        System.out.print("Ingresa la sigla de la moneda (Ej: BTC o ARG): ");
        String sigla = ent.nextLine();
        System.out.print("Ingresa el valor en dólares: ");
        double valor = ent.nextDouble();
        ent.nextLine(); // Limpiar buffer

        System.out.print("Ingresa el tipo de moneda (Criptomoneda o Fiat): ");
        String tipo = ent.nextLine();

        System.out.println("Se creara la moneda con los siguientes datos: " + nombre + ", " + sigla + ", " + valor + ", " + tipo  + " Desea continuar?: (S/N)");
        String continuar = ent.nextLine();

        // Confirmacion
        if (!continuar.equalsIgnoreCase("S")) {
            System.out.println("Operación cancelada.");
            return;
        } 
        
        // Si confirma, sigo por el chequeo
        if (tipo.equalsIgnoreCase("Criptomoneda")) {
            System.out.println("Ingresando una nueva criptomoneda...");

            // Genero volatilidad y stock aleatorio para criptos
            Random random = new Random();
            int stockAleatorio = random.nextInt(10000) + 0;
            double volatilidadAleatoria = random.nextInt(100) + 0;

            // Creo la criptomoneda y la inserto
            Criptomoneda nuevaCriptomoneda = new Criptomoneda(nombre, sigla, valor, volatilidadAleatoria, stockAleatorio);
            nuevaCriptomoneda.insertarMoneda(con, nuevaCriptomoneda);
        } 

        else if (tipo.equalsIgnoreCase("Fiat")) {
            System.out.println("Ingresando una nueva moneda FIAT...");

            // Creo la moneda fiat y la inserto
            Fiat nuevaFiat = new Fiat(nombre, sigla, valor);
            nuevaFiat.insertarMoneda(con, nuevaFiat);
        } 

        else {
            System.out.println("Tipo de moneda no válido. Solo se permiten Cripto o FIAT.");
        }
    }

    public void listarMonedas(Scanner ent) {
        System.out.println("¿Que moneda desea listar? (Criptomoneda/Fiat)");
        String tipo = ent.nextLine().trim().toLowerCase();
        System.out.print("¿Deseas ordenar por (Sigla/Valor)? ");
        String orden = ent.nextLine().trim().toLowerCase();
        
        if (!orden.equals("sigla") && !orden.equals("valor")) {
            System.out.println("Criterio de ordenamiento no válido. Ordenando por valor por defecto.");
            orden = "valor";
        }
        
        switch (tipo) {
            case "criptomoneda" -> {
                Criptomoneda criptomoneda = new Criptomoneda();
                List<Moneda> lista = criptomoneda.listarMonedas(con, orden);
                for (Moneda moneda : lista) {
                    System.out.println("Nombre: " + moneda.getNombre() + ", Sigla: " + moneda.getSigla() + ", Valor: " + moneda.getPrecioActual() + ", Volatilidad: " + moneda.getVolatilidad() + ", Stock: " + moneda.obtenerStock(con, moneda.getSigla()));
                }
            }
            case "fiat" -> {
                Fiat fiat = new Fiat();
                List<Moneda> lista = fiat.listarMonedas(con, orden);
                for (Moneda moneda : lista) {
                    System.out.println("Nombre: " + moneda.getNombre() + ", Sigla: " + moneda.getSigla() + ", Valor: " + moneda.getPrecioActual());
                }
            }
            default -> System.out.println("Tipo de moneda no válido.");
        }
    }

    public void listarStock(Scanner ent) {
        System.out.print("¿Deseas ordenar por (sigla/stock)? ");
        String orden = ent.nextLine().trim().toLowerCase();
        
        if (!orden.equals("sigla") && !orden.equals("stock")) {
            System.out.println("Criterio de ordenamiento no válido. Ordenando por stock por defecto.");
            orden = "stock";
        }
        
        List<Criptomoneda> lista = Criptomoneda.listarStock(con, orden);
        for (Criptomoneda criptomoneda : lista) {
            System.out.println("Nombre: " + criptomoneda.getNombre() + ", Sigla: " + criptomoneda.getSigla() + ", Valor: " + criptomoneda.getPrecioActual() + ", Volatilidad: " + criptomoneda.getVolatilidadAleatoria() + ", Stock: " + criptomoneda.getStock());
        }
    }

    public void depositar(Scanner ent) {
        System.out.println("Que desea depositar? (Criptomoneda/Fiat)");
        String tipo = ent.nextLine().trim().toLowerCase();
        
        if (!tipo.equals("criptomoneda") && !tipo.equals("fiat")) {
            System.out.println("Tipo de activo no válido. Solo se permiten criptomoneda o fiat.");
            return;
        }

        if (tipo.equals("criptomoneda")) {            
            Criptomoneda c = new Criptomoneda();            

            System.out.println("Criptomonedas disponibles para depositar: ");            
            List<Moneda> lista = c.listarMonedas(con, "sigla");  
            for (Moneda moneda : lista) {                
                System.out.println("Sigla: " + moneda.getSigla());            
            }

            System.out.print("Ingrese la sigla de la criptomoneda que desea depositar: ");            
            String sigla = ent.nextLine().trim().toUpperCase();  
            
            if (!c.existeMoneda(con, sigla)) {                
                System.out.println("La criptomoneda ingresada no existe.");                
                return;            
            }

            System.out.print("Ingrese la cantidad de " + sigla + " que desea depositar: ");            
            double cantidad = ent.nextDouble();                
            if (cantidad <= 0) {
                System.out.println("Por favor ingrese un valor positivo.");
                return;
            }
            

            ent.nextLine();  // Limpiar el buffer
            System.out.println("Desea confirmar el depósito de " + cantidad + " " + sigla + "? (S/N)");            
            String sino = ent.nextLine().trim().toUpperCase();    

            if (sino.equals("S")) {                
                Activo_Cripto.agregarCriptomoneda(sigla, cantidad, con);            
            } else {                
                System.out.println("Depósito cancelado.");            
            }        

        } else {
            Fiat f = new Fiat();
            System.out.println("Fiat disponibles para depositar: ");
            List<Moneda> lista = f.listarMonedas(con, "sigla");  
            for (Moneda moneda : lista) {                
                System.out.println("Sigla: " + moneda.getSigla());            
            }
    
            System.out.print("Ingrese la sigla del fiat que desea depositar: ");
            String sigla = ent.nextLine().trim().toUpperCase();

            if (!f.existeMoneda(con, sigla)) {
                System.out.println("El fiat ingresado no existe.");
                return;
            }
    
            System.out.print("Ingrese la cantidad de " + sigla + " que desea depositar: ");
            double cantidad = ent.nextInt();

            ent.nextLine();  // Limpiar el buffer

            System.out.println("Desea confirmar el depósito de " + cantidad + " " + sigla + "? (S/N)");
            String sino = ent.nextLine().trim().toUpperCase();
    
            if (sino.equals("S")) {
                Activo_Fiat.agregarFiat(sigla, cantidad, con);
            } else {
                 System.out.println("Depósito cancelado.");
            }
        }
    }

    public void listarActivos(Scanner ent) {
        System.out.println("Desea listar criptomonedas o fiat? (Criptomoneda/Fiat)");
        String tipo = ent.nextLine().trim().toLowerCase();
        if (!tipo.equals("criptomoneda") && !tipo.equals("fiat")) {
            System.out.println("Tipo de activo no válido. Solo se permiten criptomoneda o fiat.");
        }
        System.out.println("Desea ordenar por sigla o cantidad? (Sigla/Cantidad)");
        String orden = ent.nextLine().trim().toLowerCase();
        if (tipo.equals("criptomoneda")) {
            List<Activo_Cripto> lista = Activo_Cripto.listarActivosCripto(con, orden);
            for (Activo_Cripto activo : lista) {
                System.out.println("Sigla: " + activo.getSigla() + ", Cantidad: " + activo.getCantidad());
            }
        }
        else {
            List<Activo_Fiat> lista = Activo_Fiat.listarActivosFiat(con, orden);
            for (Activo_Fiat fiat : lista) {
                System.out.println("Sigla: " + fiat.getSigla() + ", Cantidad: " + fiat.getCantidad());
            }
        }
    }

     public void generarStock() {
        String querySelect = "SELECT sigla, stock FROM Moneda WHERE tipo = 'Criptomoneda'";
        String queryUpdate = "UPDATE Moneda SET stock = stock + ? WHERE sigla = ?";
        Random random = new Random();

        try (PreparedStatement selectStmt = con.prepareStatement(querySelect);
             ResultSet rs = selectStmt.executeQuery()) {

            while (rs.next()) {
                String sigla = rs.getString("sigla");
                int incrementoStock = random.nextInt(101) + 50;  // Genera entre 50 y 150 unidades adicionales

                try (PreparedStatement updateStmt = con.prepareStatement(queryUpdate)) {
                    updateStmt.setInt(1, incrementoStock);
                    updateStmt.setString(2, sigla);
                    updateStmt.executeUpdate();
                }
                System.out.println("Se han agregado " + incrementoStock + " unidades a la criptomoneda " + sigla);
            }

        } catch (SQLException e) {
            System.out.println("Error al generar stock aleatorio: " + e.getMessage());
        }
    }
}
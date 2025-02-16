import java.util.Scanner;

public class LineaProduccion {
    public static volatile boolean fin = false; // Bandera global

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Leer datos
        System.out.print("Número de productores: ");
        int numProductores = scanner.nextInt();
        System.out.print("Número de productos a generar: ");
        int numProductos = scanner.nextInt();
        System.out.print("Capacidad del buzón de revisión: ");
        int capacidadBuzon = scanner.nextInt();

        // Crear buzones y depósito
        Buzon buzonRevision = new Buzon(capacidadBuzon);
        Buzon buzonReproceso = new Buzon(Integer.MAX_VALUE);
        Deposito deposito = new Deposito();

        // Crear productores
        Productor[] productores = new Productor[numProductores];
        for (int i = 0; i < numProductores; i++) {
            productores[i] = new Productor(i + 1, buzonReproceso, buzonRevision);
            productores[i].start();
        }

        // Crear equipo de calidad
        EquipoCalidad[] equipoCalidad = new EquipoCalidad[numProductores];
        for (int i = 0; i < numProductores; i++) {
            equipoCalidad[i] = new EquipoCalidad(buzonRevision, buzonReproceso, deposito, numProductos);
            equipoCalidad[i].start();
        }

        // Esperar equipo de calidad
        for (EquipoCalidad eq : equipoCalidad) {
            try {
                eq.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Terminar productores correctamente
        for (Productor p : productores) {
           p.interrupt(); // Interrumpir hilos que puedan estar en espera
               }

        for (Productor p : productores) {
            try {
            p.join();
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
     }

System.out.println("Línea de producción finalizada.");

        scanner.close();
    }
}
import java.util.Scanner;

public class LineaProduccion {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Leer datos de la consola
        System.out.print("Número de productores: ");
        int numProductores = scanner.nextInt();
        System.out.print("Número de productos a generar: ");
        int numProductos = scanner.nextInt();
        System.out.print("Capacidad del buzón de revisión: ");
        int capacidadBuzon = scanner.nextInt();
        
        // Crear los buzones y el depósito
        Buzon buzonRevision = new Buzon(capacidadBuzon);
        Buzon buzonReproceso = new Buzon(Integer.MAX_VALUE); // Ilimitado
        Deposito deposito = new Deposito();

        // Crear y lanzar los threads de productores
        Productor[] productores = new Productor[numProductores];
        for (int i = 0; i < numProductores; i++) {
            productores[i] = new Productor(i + 1, buzonReproceso, buzonRevision);
            productores[i].start();
        }

        // Crear y lanzar los threads del equipo de calidad (mismo número que productores)
        EquipoCalidad[] equipoCalidad = new EquipoCalidad[numProductores];
        for (int i = 0; i < numProductores; i++) {
            equipoCalidad[i] = new EquipoCalidad(buzonRevision, buzonReproceso, deposito, numProductos);
            equipoCalidad[i].start();
        }

        // Esperar a que todos los hilos de calidad terminen
        for (EquipoCalidad eq : equipoCalidad) {
            try {
                eq.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Enviar la señal de terminación a los productores
        for (Productor p : productores) {
            p.terminar();
        }

        // Finalizar los hilos de productores
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
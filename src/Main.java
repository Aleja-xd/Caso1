import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;

public class Main {
    private List<Productor> productores;
    private List<EquipoCalidad> equipoCalidad;
    private Buzon buzonRevision;
    private Buzon buzonReproceso;
    private Deposito deposito;
    private CyclicBarrier barrera;

    public Main(int numProductores, int numProductos, int capacidadBuzon) {
        this.buzonRevision = new Buzon(capacidadBuzon);
        this.buzonReproceso = new Buzon(Integer.MAX_VALUE); // Sin límite de capacidad
        this.deposito = new Deposito();
        
        this.barrera = new CyclicBarrier(numProductores + numProductores, () -> {
            System.out.println("Todos los hilos han finalizado.");
        });

        this.productores = new ArrayList<>();
        for (int i = 0; i < numProductores; i++) {
            productores.add(new Productor(i, buzonReproceso, buzonRevision, barrera));
        }

        this.equipoCalidad = new ArrayList<>();
        for (int i = 0; i < numProductores; i++) {
            equipoCalidad.add(new EquipoCalidad(buzonRevision, buzonReproceso, deposito, numProductos, barrera));
        }
    }

    public void iniciarProduccion() {
        for (Productor p : productores) {
            p.start();
        }
        for (EquipoCalidad e : equipoCalidad) {
            e.start();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese número de productores y equipo de calidad: ");
        int numProductores = scanner.nextInt();
        System.out.print("Ingrese número total de productos a fabricar: ");
        int numProductos = scanner.nextInt();
        System.out.print("Ingrese capacidad del buzón de revisión: ");
        int capacidadBuzon = scanner.nextInt();
        scanner.close();
        
        Main produccion = new Main(numProductores, numProductos, capacidadBuzon);
        produccion.iniciarProduccion();
    }
}

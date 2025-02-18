import java.util.Random;

public class EquipoCalidad extends Thread {
    private final Buzon buzonRevision;
    private final Buzon buzonReproceso;
    private final Deposito deposito;
    private final int productosMaximos;
    private int productosFallidos = 0;
    private static final Random random = new Random();

    public EquipoCalidad(Buzon buzonRevision, Buzon buzonReproceso, Deposito deposito, int productosMaximos) {
        this.buzonRevision = buzonRevision;
        this.buzonReproceso = buzonReproceso;
        this.deposito = deposito;
        this.productosMaximos = productosMaximos;
    }

    @Override
public void run() {
    try {
        while (!LineaProduccion.fin) {
            Producto producto = buzonRevision.retirar();
            if (producto == null) continue;

            if (producto.getId() == -1) { 
                System.out.println("Equipo de Calidad recibe FIN y termina.");
                break;
            }

            if (random.nextInt(100) % 7 == 0 && productosFallidos < Math.floor(0.1 * productosMaximos)) {
                productosFallidos++;
                System.out.println("Equipo de Calidad rechaza " + producto);
                buzonReproceso.depositar(producto);
            } else {
                producto.setAprobado(true);
                System.out.println("Equipo de Calidad aprueba " + producto);
                deposito.almacenar(producto);
            }

            
            synchronized (LineaProduccion.class) {
                if (deposito.getTotalProductos() >= productosMaximos && !LineaProduccion.fin) {
                    System.out.println("Equipo de Calidad envía mensaje FIN.");
                    buzonReproceso.depositar(new Producto(-1)); // Producto especial FIN
                    LineaProduccion.fin = true;

                    // Notificar a todos
                    synchronized (buzonRevision) {
                        buzonRevision.notifyAll();
                    }
                    synchronized (buzonReproceso) {
                        buzonReproceso.notifyAll();
                    }
                }
            }
        }
    } catch (InterruptedException e) {
        System.out.println("Equipo de Calidad interrumpido.");
    }
 }
}
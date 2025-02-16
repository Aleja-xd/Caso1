import java.util.Random;

public class EquipoCalidad extends Thread {
    private Buzon buzonRevision;
    private Buzon buzonReproceso;
    private Deposito deposito;
    private int productosMaximos;
    private int productosFallidos = 0;
    private boolean continuar = true;
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
            while (continuar) {
                Producto producto = buzonRevision.retirar();

                // Evaluación de calidad (falla si es múltiplo de 7 y aún no se ha alcanzado el máximo de fallos)
                if (random.nextInt(100) % 7 == 0 && productosFallidos < (int) Math.floor(0.1 * productosMaximos)) {
                    productosFallidos++;
                    System.out.println("Equipo de Calidad rechaza " + producto);
                    buzonReproceso.depositar(producto);
                } else {
                    producto.setAprobado(true);
                    System.out.println("Equipo de Calidad aprueba " + producto);
                    deposito.almacenar(producto);
                }

                // Verifica si se ha alcanzado el límite de productos aprobados
                if (deposito.getTotalProductos() >= productosMaximos) {
                    System.out.println("Equipo de Calidad envía mensaje FIN.");
                    buzonReproceso.depositar(new Producto(-1)); // Producto con id -1 indica "FIN"
                    continuar = false;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Equipo de Calidad interrumpido.");
        }
    }
}

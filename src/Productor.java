
public class Productor extends Thread {
    private static int contadorId = 1;
    private final Buzon buzonReproceso;
    private final Buzon buzonRevision;
    private final int id;

    public Productor(int id, Buzon buzonReproceso, Buzon buzonRevision) {
        this.id = id;
        this.buzonReproceso = buzonReproceso;
        this.buzonRevision = buzonRevision;
    }

    @Override
public void run() {
    try {
        while (!LineaProduccion.fin) {
            Producto producto;

            synchronized (buzonReproceso) {
                if (!buzonReproceso.estaVacio()) {
                    producto = buzonReproceso.retirar();
                    if (producto.getId() == -1) {
                        System.out.println("Productor " + id + " recibe FIN y termina.");
                        break;
                    }
                    System.out.println("Productor " + id + " reprocesa " + producto);
                } else {
                    producto = new Producto(contadorId++);
                    System.out.println("Productor " + id + " genera " + producto);
                }
            }

            // Verificación adicional de FIN tras esperar
            if (!LineaProduccion.fin) {
                buzonRevision.depositar(producto);
                System.out.println("Productor " + id + " deposita en buzón de revisión: " + producto);
            } else {
                break;
            }
        }
    } catch (InterruptedException e) {
        System.out.println("Productor " + id + " interrumpido.");
    }
 }
}
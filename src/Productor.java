
public class Productor extends Thread {
    private static int contadorId = 1;
    private Buzon buzonReproceso;
    private Buzon buzonRevision;
    private int id;
    private boolean continuar = true;

    public Productor(int id, Buzon buzonReproceso, Buzon buzonRevision) {
        this.id = id;
        this.buzonReproceso = buzonReproceso;
        this.buzonRevision = buzonRevision;
    }

    @Override
    public void run() {
        try {
            while (continuar) {
                Producto producto;
                
                // Reprocesar si hay productos en el buzón de reproceso
                if (!buzonReproceso.estaVacio()) {
                    producto = buzonReproceso.retirar();
                    System.out.println("Productor " + id + " reprocesa " + producto);
                } else {
                    // Si no hay nada que reprocesar, generar un nuevo producto
                    producto = new Producto(contadorId++);
                    System.out.println("Productor " + id + " genera " + producto);
                }

                // Si el buzón de revisión está lleno, espera
                buzonRevision.depositar(producto);
                System.out.println("Productor " + id + " deposita en buzón de revisión: " + producto);
            }
        } catch (InterruptedException e) {
            System.out.println("Productor " + id + " interrumpido.");
        }
    }

    public void terminar() {
        this.continuar = false;
    }
}
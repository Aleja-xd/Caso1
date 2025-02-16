import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Productor extends Thread {
    private Buzon buzonReproceso;
    private Buzon buzonRevision;
    private int id;
    private boolean fin;
    private CyclicBarrier barrera;
    
    public Productor(int id, Buzon buzonReproceso, Buzon buzonRevision, CyclicBarrier barrera) {
        this.id = id;
        this.buzonReproceso = buzonReproceso;
        this.buzonRevision = buzonRevision;
        this.barrera = barrera;
        this.fin = false;
    }
    
    public boolean revisarReproceso() {
        synchronized (buzonReproceso) {
            return buzonReproceso.estaVacio();
        }
    }
    
    public Producto crearProducto(String s) {
        return new Producto(s);
    }

    @Override
    public void run() {
        Random rand = new Random();
        try {
            while (!fin) {
                if (revisarReproceso()) {
                    int idProd = rand.nextInt(10000000);
                    Producto p = crearProducto(String.valueOf(idProd));
                    synchronized (buzonRevision) {
                        while (buzonRevision.estaLleno()) {
                            buzonRevision.wait();
                        }
                        buzonRevision.depositar(p);
                        buzonRevision.notifyAll();
                    }
                    System.out.println("Productor " + id + " creó el producto " + idProd);
                } else {
                    Producto p;
                    synchronized (buzonReproceso) {
                        p = buzonReproceso.retirar();
                    }
                    if ("FIN".equals(p.getNombre())) {
                        fin = true;
                        System.out.println("Productor " + id + " ha terminado.");
                    } else {
                        synchronized (buzonRevision) {
                            while (buzonRevision.estaLleno()) {
                                buzonRevision.wait();
                            }
                            buzonRevision.depositar(p);
                            buzonRevision.notifyAll();
                        }
                        System.out.println("Productor " + id + " reprocesó el producto " + p.getNombre());
                    }
                }
            }
            barrera.await();
        } catch (Exception e) {
            System.out.println("Error en productor " + id);
            e.printStackTrace();
        }
    }
}

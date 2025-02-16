import java.util.concurrent.CyclicBarrier;

public class EquipoCalidad extends Thread {
    protected Buzon buzonReproceso;
    protected Buzon buzonRevision;
    protected Deposito deposito;
    protected int productosMaximos;
    protected int productosFallidos;
    private CyclicBarrier barrera;
    private boolean finEnviado = false;

    public EquipoCalidad(Buzon buzonRevision, Buzon buzonReproceso, Deposito deposito, int productosMaximos, CyclicBarrier barrera) {
        this.buzonRevision = buzonRevision;
        this.buzonReproceso = buzonReproceso;
        this.deposito = deposito;
        this.productosMaximos = productosMaximos;
        this.productosFallidos = 0;
        this.barrera = barrera;
    }

    @Override
    public void run() {
        try {
            while (productosMaximos > 0 || !buzonRevision.estaVacio()) {
                Producto p;
                synchronized (buzonRevision) {
                    while (buzonRevision.estaVacio()) {
                        buzonRevision.wait();
                    }
                    p = buzonRevision.retirar();
                    buzonRevision.notifyAll();
                }
                
                Thread.sleep(100);
                
                synchronized (p) {
                    p.evaluarCalidad();
                }
                
                if (p.isAprobado() || productosFallidos >= productosMaximos / 10) {
                    synchronized (deposito) {
                        deposito.almacenar(p);
                    }
                    System.out.println("[Equipo de Calidad] Aprobado y almacenado: " + p);
                } else {
                    productosFallidos++;
                    synchronized (buzonReproceso) {
                        buzonReproceso.depositar(p);
                        buzonReproceso.notifyAll();
                    }
                    System.out.println("[Equipo de Calidad] Rechazado y enviado a reproceso: " + p);
                }
                productosMaximos--;
            }

            synchronized (buzonReproceso) {
                if (!finEnviado) {
                    buzonReproceso.depositar(new Producto("FIN"));
                    buzonReproceso.notifyAll();
                    finEnviado = true;
                    System.out.println("[Equipo de Calidad] Se ha enviado el producto FIN.");
                }
            }
            
            barrera.await();
        } catch (Exception e) {
            System.out.println("Error en EquipoCalidad");
            e.printStackTrace();
        }
    }
}
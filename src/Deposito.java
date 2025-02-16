import java.util.LinkedList;
import java.util.Queue;

public class Deposito {
    private Queue<Producto> productos;

    public Deposito() {
        this.productos = new LinkedList<>();
    }

    public synchronized void almacenar(Producto producto) {
        productos.add(producto);
        notifyAll();
    }

    public synchronized Producto retirar() throws InterruptedException {
        while (productos.isEmpty()) {
            wait();
        }
        return productos.poll();
    }
}
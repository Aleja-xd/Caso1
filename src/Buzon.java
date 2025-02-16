import java.util.LinkedList;
import java.util.Queue;

public class Buzon {
    private Queue<Producto> productos;
    private int capacidad;

    public Buzon(int capacidad) {
        this.productos = new LinkedList<>();
        this.capacidad = capacidad;
    }

    public synchronized void depositar(Producto producto) throws InterruptedException {
        while (productos.size() == capacidad) {
            wait();
        }
        productos.add(producto);
        notifyAll();
    }

    public synchronized Producto retirar() throws InterruptedException {
        while (productos.isEmpty()) {
            wait();
        }
        Producto producto = productos.poll();
        notifyAll();
        return producto;
    }

    public synchronized boolean estaVacio() {
        return productos.isEmpty();
    }

    public synchronized boolean estaLleno() {
        return productos.size() >= capacidad;
    }
}

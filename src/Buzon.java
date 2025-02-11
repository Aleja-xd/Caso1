import java.util.LinkedList;
import java.util.Queue;

public class Buzon {
    private Queue<Producto> productos;
    private int capacidad;
     
    public Buzon(int capacidad) {
        this.capacidad = capacidad;
        this.productos = new LinkedList<>();
    }

    public synchronized void depositar(Producto p) throws InterruptedException {
        while (productos.size() >= capacidad) {
            wait(); 
        }
        productos.add(p);
        notifyAll(); 
    }
    
    public synchronized Producto retirar() throws InterruptedException {
        while (productos.isEmpty()) {
            wait(); 
        }
        Producto p = productos.poll();
        notifyAll(); 
        return p;
    }

    public synchronized boolean estaVacio() {
        return productos.isEmpty();
    }

    public synchronized boolean estaLleno() {
        return productos.size() >= capacidad;
    }
}

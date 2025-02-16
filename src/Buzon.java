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
        while (productos.size() >= capacidad && !LineaProduccion.fin) {
            wait(); // Espera si está lleno
        }
        if (!LineaProduccion.fin) {
            productos.add(p);
            notifyAll(); // Notifica a los consumidores
        }
    }

    public synchronized Producto retirar() throws InterruptedException {
        while (productos.isEmpty() && !LineaProduccion.fin) {
            wait(); // Espera si está vacío
        }
        Producto p = productos.poll();
        notifyAll(); // Notifica a los productores
        return p;
    }

    public synchronized boolean estaVacio() {
        return productos.isEmpty();
    }

    public synchronized boolean estaLleno() {
        return productos.size() >= capacidad;
    }
}
import java.util.ArrayList;
import java.util.List;

public class Deposito {
    private List<Producto> productosFinales = new ArrayList<>();

    public synchronized void almacenar(Producto p) {
        productosFinales.add(p);
        System.out.println("Producto almacenado en el depósito: " + p);
    }

    public synchronized int getTotalProductos() {
        return productosFinales.size();
    }
}

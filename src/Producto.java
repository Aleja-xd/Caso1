import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Producto {
    private String nombre;
    private boolean aprobado;

    public Producto(String nombre) {
        this.nombre = nombre;
        this.aprobado = false; // Por defecto, no aprobado
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public void evaluarCalidad() {
        int numero = (int) (Math.random() * 100) + 1;
        this.aprobado = numero % 7 != 0;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}


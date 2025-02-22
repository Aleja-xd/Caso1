public class Producto {
    private int id;
    private boolean aprobado; // Indica si pasó la revisión de calidad

    public Producto(int id) {
        this.id = id;
        this.aprobado = false; 
    }

    public int getId() {
        return id;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    @Override
    public String toString() {
        if (aprobado) {
            return "Producto " + id + " [Aprobado]";
        } else {
            return "Producto " + id + " [Rechazado]";
        }
    }
}

public class Producto {
    private int id;
    private boolean aprobado;

        public Producto(int id) {
            this.id = id;
            this.aprobado = false; // Por defecto, no aprobado
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
            return "Producto " + id + " [" + (aprobado ? "Aprobado" : "Rechazado") + "]";
        }
    
}

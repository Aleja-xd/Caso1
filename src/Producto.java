
public class Producto {
    protected String id;
    private boolean aprobado;

        public Producto(String id) {
            this.id = id;
            this.aprobado = false; // Por defecto, no aprobado
        }
    
        public String getId() {
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

public class EquipoCalidad {
    protected Buzon buzonReproceso;
    protected Buzon buzonRevision;
    protected Deposito deposito;
    protected int productosMaximos;
    protected int productosFallidos;

    public EquipoCalidad(Buzon buzonRevision, Buzon buzonReproceso, Deposito deposito, int productosMaximos) {
        this.buzonRevision = buzonRevision;
        this.buzonReproceso = buzonReproceso;
        this.deposito = deposito;
        this.productosMaximos = productosMaximos;
    }
}
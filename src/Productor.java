import java.util.Random;

public class Productor extends Thread{
	
    private Buzon buzonReproceso;
    private Buzon buzonRevision;
    private int id;
    private String fin;
    
    public Productor(int id, Buzon buzonReproceso, Buzon buzonRevision) {
    	
    	this.id = id;
		this.buzonReproceso = buzonReproceso;
		this.buzonRevision = buzonRevision;
		
	}
    
    public boolean Revisar(Buzon buzonReproceso) {
    	
    	boolean b = this.buzonReproceso.estaVacio();
    	return b;
    	
    }
    
    public Producto Crear(String s) {
    	
    	Producto p = new Producto(s);
    	return p;
    	
    }
	
	public void run() {
		
		Random rand = new Random();	
		
		while(fin != "FIN") {
			
			if(Revisar(this.buzonReproceso)) {
				
				int id = rand.nextInt(10000000);
				String idString = String.valueOf(id);
				Producto p = Crear(idString);
				
				while(this.buzonRevision.estaLleno()) {
					try {
						this.buzonRevision.wait();
					} catch (InterruptedException e) {
						System.out.println( "Error del productor " + this.id + " al esperar el Buzon de Revision." );
						e.printStackTrace();
					}
				}
				
				try {
					this.buzonRevision.depositar(p);
				} catch (InterruptedException e) {
					System.out.println( "Error del productor " + this.id + " al depositar el producto." );
					e.printStackTrace();
				}
				System.out.println( "El productor " + this.id + " Ha creado el producto con id " + id );
				
			}else {
				
				Producto p = null;
				try {
					p = this.buzonReproceso.retirar();
				} catch (InterruptedException e) {
					System.out.println( "Error del productor " + this.id + " al retirar el producto." );
					e.printStackTrace();
				}
				
				if(p.getId() == "FIN") {
					
					this.fin = "FIN";
					System.out.println("El productor " + this.id + " ha terminado de producir.");
					
				}else {
					
					while(this.buzonRevision.estaLleno()) {
						try {
							this.buzonRevision.wait();
						} catch (InterruptedException e) {
							System.out.println( "Error del productor " + this.id + " al esperar el Buzon de Revision." );
							e.printStackTrace();
						}
					}
					
					try {
						this.buzonRevision.depositar(p);
					} catch (InterruptedException e) {
						System.out.println( "Error del productor " + this.id + " al retirar el producto." );
						e.printStackTrace();
					}
					System.out.println( "El productor " + this.id + " Ha reprocesado el producto con id " + p.getId() );
					
				}
				
			}
			
		}
		
	}
	
}

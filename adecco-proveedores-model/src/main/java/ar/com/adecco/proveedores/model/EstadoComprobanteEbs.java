// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

public enum EstadoComprobanteEbs
{
    ERROR_FACTURACION("Error Facturaci\u00f3n"), 
    RETENIDA("Retenida"), 
    ERROR_PAGO("Error de Pago");
    
    private String nombre;
    
    public String getNombre() {
        return this.nombre;
    }
    
    private EstadoComprobanteEbs(final String nombre) {
        this.nombre = nombre;
    }
}

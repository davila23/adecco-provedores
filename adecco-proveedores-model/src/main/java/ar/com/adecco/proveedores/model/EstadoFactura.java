// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

public enum EstadoFactura
{
    INGRESADA("Ingresada"), 
    ENVIADA("Enviada"), 
    ACEPTADA("Aceptada"), 
    APROBADA("Aprobada"), 
    RECHAZADA("Rechazada"), 
    PAGADA("Pagada");
    
    private String nombre;
    
    public String getNombre() {
        return this.nombre;
    }
    
    private EstadoFactura(final String nombre) {
        this.nombre = nombre;
    }
}

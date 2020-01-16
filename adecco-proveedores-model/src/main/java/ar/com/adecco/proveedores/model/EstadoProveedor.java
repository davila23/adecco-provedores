// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

public enum EstadoProveedor
{
    INGRESADO("Ingresado"), 
    ENVIADO("Enviado"), 
    APROBADO_IMPUESTOS("Aprobado Impuestos"), 
    APROBADO_COMPRAS("Aprobado Compras"), 
    APROBADO_TESORERIA("Aprobado Tesoreria"), 
    APROBADO("Aprobado"), 
    ERROR_APROBACION("Error Aprobaci\u00f3n"), 
    RECHAZADO("Rechazado");
    
    private String nombre;
    
    public String getNombre() {
        return this.nombre;
    }
    
    private EstadoProveedor(final String nombre) {
        this.nombre = nombre;
    }
}

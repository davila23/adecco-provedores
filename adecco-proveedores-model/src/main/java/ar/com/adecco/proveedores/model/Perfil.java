// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

public enum Perfil
{
    SIN_PERFIL("Sin Perfil"), 
    PROVEEDOR("Proveedor"), 
    COMPRAS("Compras"), 
    IMPUESTOS("Impuestos"), 
    TESORERIA("Tesoreria"), 
    APROBADOR("Aprobador"), 
    ACEPTADOR("Aceptador"), 
    PAGO("Pago");
    
    private String nombre;
    
    public String getNombre() {
        return this.nombre;
    }
    
    private Perfil(final String nombre) {
        this.nombre = nombre;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

public enum CondicionIVA
{
    INSCRIPTO("Resp. Inscripto"), 
    MONOTRIBUTISTA("Monotributista"), 
    NO_RESPONSABLE("No Responsable"), 
    EXENTO("Sujeto Exento"), 
    CONSUMIDOR_FINAL("Consumidor Final");
    
    private String descripcion;
    
    public String getDescripcion() {
        return this.descripcion;
    }
    
    private CondicionIVA(final String descripcion) {
        this.descripcion = descripcion;
    }
}

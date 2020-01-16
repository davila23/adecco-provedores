// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

public enum CondicionGanancias
{
    INSCRIPTO("Resp. Inscripto"), 
    MONOTRIBUTISTA("Monotributista"), 
    EXENTO("Sujeto Exento"), 
    EXCLUIDO("Sujeto Excluido de Retenci\u00f3n");
    
    private String descripcion;
    
    public String getDescripcion() {
        return this.descripcion;
    }
    
    private CondicionGanancias(final String descripcion) {
        this.descripcion = descripcion;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

public enum CodigoAutorizacion
{
    CAE("C\u00f3digo de autorizaci\u00f3n electr\u00f3nico"), 
    CAI("C\u00f3digo de autorizaci\u00f3n de impresi\u00f3n");
    
    private String label;
    
    private CodigoAutorizacion(final String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return this.label;
    }
}

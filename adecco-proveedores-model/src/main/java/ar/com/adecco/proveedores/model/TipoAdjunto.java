// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

import java.io.Serializable;

public enum TipoAdjunto implements Serializable
{
    CUIT("CUIT"), 
    IIBB("IIBB"), 
    IMP_GANANCIAS("Impuesto de Ganancias"), 
    CER_CALIDAD("Certificado de Calidad"), 
    CONTRATOS("Contratos");
    
    private String tipo;
    
    public String getTipo() {
        return this.tipo;
    }
    
    private TipoAdjunto(final String tipo) {
        this.tipo = tipo;
    }
}

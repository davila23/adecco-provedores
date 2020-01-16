// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity(name = "ar.com.adecco.proveedores.model.Factura")
@DiscriminatorValue("FC")
public class Factura extends Comprobante implements Serializable
{
    private static final long serialVersionUID = -6685818720699873457L;
    private OrdenCompra ordenCompra;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PO_HEADER_ID")
    public OrdenCompra getOrdenCompra() {
        return this.ordenCompra;
    }
    
    public void setOrdenCompra(final OrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }
}

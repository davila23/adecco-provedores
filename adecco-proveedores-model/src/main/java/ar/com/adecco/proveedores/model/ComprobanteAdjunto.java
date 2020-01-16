// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.Basic;
import javax.persistence.Lob;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import ar.com.adecco.proveedores.model.base.EntidadAuditada;

@Entity
@Table(schema = "compras", name = "FACTURAADJUNTO")
public class ComprobanteAdjunto extends EntidadAuditada
{
    private static final long serialVersionUID = 4778268120467387711L;
    private String nombre;
    private long size;
    private String contentType;
    private byte[] data;
    private Comprobante comprobante;
    
    public ComprobanteAdjunto() {
    }
    
    public ComprobanteAdjunto(final long size, final String contentType, final byte[] data) {
        this.size = size;
        this.contentType = contentType;
        this.data = data;
    }
    
    @Column(name = "tamano")
    public long getSize() {
        return this.size;
    }
    
    public void setSize(final long size) {
        this.size = size;
    }
    
    public String getContentType() {
        return this.contentType;
    }
    
    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }
    
    @Lob
    @Column(length = Integer.MAX_VALUE)
    @Basic(fetch = FetchType.LAZY)
    public byte[] getData() {
        return this.data;
    }
    
    public void setData(final byte[] data) {
        this.data = data;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTURA_ID")
    public Comprobante getComprobante() {
        return this.comprobante;
    }
    
    public void setComprobante(final Comprobante comprobante) {
        this.comprobante = comprobante;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }
}

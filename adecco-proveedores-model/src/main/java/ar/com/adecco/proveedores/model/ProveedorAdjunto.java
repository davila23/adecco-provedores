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
import java.io.Serializable;
import ar.com.adecco.proveedores.model.base.EntidadAuditada;

@Entity
@Table(schema = "compras", name = "PROVEEDORADJUNTO")
public class ProveedorAdjunto extends EntidadAuditada implements Serializable
{
    private static final long serialVersionUID = 6124889155988950045L;
    private String nombre;
    private long size;
    private String contentType;
    private byte[] data;
    private String tipoArchivo;
    private Proveedor proveedor;
    
    public ProveedorAdjunto() {
    }
    
    public ProveedorAdjunto(final long size, final String contentType, final byte[] data) {
        this.size = size;
        this.contentType = contentType;
        this.data = data;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(final String nombre) {
        this.nombre = nombre;
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
    
    public String getTipoArchivo() {
        return this.tipoArchivo;
    }
    
    public void setTipoArchivo(final String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVEEDOR_ID", referencedColumnName = "ID")
    public Proveedor getProveedor() {
        return this.proveedor;
    }
    
    public void setProveedor(final Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}

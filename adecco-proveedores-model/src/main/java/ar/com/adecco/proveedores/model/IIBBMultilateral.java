// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import ar.com.adecco.dominio.ebs.fnd.Provincia;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.io.Serializable;
import ar.com.adecco.proveedores.model.base.EntidadAuditada;

@Entity
@Table(schema = "compras", name = "IIBBMULTILATERAL")
public class IIBBMultilateral extends EntidadAuditada implements Serializable
{
    private static final long serialVersionUID = 4356411912375542069L;
    private Provincia jurisdiccion;
    private boolean exento;
    private String porcentajeExclusion;
    private Proveedor proveedor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOOKUP_CODE")
    public Provincia getJurisdiccion() {
        return this.jurisdiccion;
    }
    
    public void setJurisdiccion(final Provincia jurisdiccion) {
        this.jurisdiccion = jurisdiccion;
    }
    
    public boolean isExento() {
        return this.exento;
    }
    
    public void setExento(final boolean exento) {
        this.exento = exento;
    }
    
    public String getPorcentajeExclusion() {
        return this.porcentajeExclusion;
    }
    
    public void setPorcentajeExclusion(final String porcentajeExclusion) {
        this.porcentajeExclusion = porcentajeExclusion;
    }
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROVEEDOR_ID", referencedColumnName = "ID", updatable = false)
    public Proveedor getProveedor() {
        return this.proveedor;
    }
    
    public void setProveedor(final Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}

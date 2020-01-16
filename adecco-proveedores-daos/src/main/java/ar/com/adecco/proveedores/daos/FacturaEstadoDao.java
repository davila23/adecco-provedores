// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import javax.persistence.NoResultException;
import java.util.List;
import ar.com.adecco.proveedores.model.Comprobante;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.inject.Named;
import ar.com.adecco.proveedores.model.FacturaEstado;

@Named
public class FacturaEstadoDao extends AbstractDao<FacturaEstado>
{
    private static final long serialVersionUID = 6752748871657582370L;
    
    @Override
    public Class<FacturaEstado> getClaseEntidad() {
        return FacturaEstado.class;
    }
    
    public void addFacturaEstado(final FacturaEstado facturaEstado) throws DaoException {
        try {
            this.agregarNoFlush(facturaEstado);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void removeFacturaEstado(final FacturaEstado facturaEstado) throws DaoException {
        try {
            this.eliminarNoFlush(facturaEstado);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<FacturaEstado> getFacturasEstadoByComprobante(final Comprobante comprobante) throws DaoException {
        try {
            return (List<FacturaEstado>)this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where e.factura = :factura order by id desc").setParameter("comprobante", (Object)comprobante).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public FacturaEstado getEstadoActualByComprobante(final Comprobante comprobante) throws DaoException {
        try {
            return (FacturaEstado)this.getEntityManager().createQuery("select fe from FacturaEstado fe where (fe.factura.id, fe.fechaCreacion) = (select fe2.factura.id, max(fe2.fechaCreacion) from FacturaEstado fe2 where fe2.factura = :comprobante group by fe2.factura.id)", (Class)FacturaEstado.class).setParameter("comprobante", (Object)comprobante).getSingleResult();
        }
        catch (NoResultException ex) {
            return null;
        }
    }
}

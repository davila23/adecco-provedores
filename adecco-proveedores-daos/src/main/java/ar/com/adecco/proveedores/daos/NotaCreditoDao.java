// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.inject.Named;
import ar.com.adecco.proveedores.model.NotaCredito;

@Named
public class NotaCreditoDao extends AbstractDao<NotaCredito>
{
    private static final long serialVersionUID = 550060121742976101L;
    
    @Override
    public Class<NotaCredito> getClaseEntidad() {
        return NotaCredito.class;
    }
    
    public NotaCredito getById(final long n, final boolean b) throws DaoException {
        try {
            if (!b) {
                return this.obtener(n);
            }
            return (NotaCredito)this.entityManager.createQuery("select nc from ar.com.adecco.proveedores.model.NotaCredito nc left join fetch nc.proveedor pr left join fetch nc.facturaAplicada left join fetch nc.lineas l left join fetch l.lineaAplicada la left join fetch la.lineaOrdenCompra laoc where nc.id = :id order by nc.id desc", (Class)NotaCredito.class).setParameter("id", (Object)n).getSingleResult();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
}

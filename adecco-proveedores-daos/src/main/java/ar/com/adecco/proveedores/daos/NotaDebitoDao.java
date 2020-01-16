// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.inject.Named;
import ar.com.adecco.proveedores.model.NotaDebito;

@Named
public class NotaDebitoDao extends AbstractDao<NotaDebito>
{
    private static final long serialVersionUID = 5500601742976101L;
    
    @Override
    public Class<NotaDebito> getClaseEntidad() {
        return NotaDebito.class;
    }
    
    public NotaDebito getById(final long n, final boolean b) throws DaoException {
        try {
            if (!b) {
                return this.obtener(n);
            }
            return (NotaDebito)this.entityManager.createQuery("select nd from ar.com.adecco.proveedores.model.NotaDebito nd left join fetch nd.proveedor pr left join fetch nd.facturaAplicada left join fetch nd.lineas l left join fetch l.lineaAplicada la left join fetch la.lineaOrdenCompra laoc where nd.id = :id order by nd.id desc", (Class)NotaDebito.class).setParameter("id", (Object)n).getSingleResult();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import java.util.List;
import ar.com.adecco.proveedores.model.Comprobante;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.inject.Named;
import ar.com.adecco.proveedores.model.ComprobanteAdjunto;

@Named
public class ComprobanteAdjuntoDao extends AbstractDao<ComprobanteAdjunto>
{
    private static final long serialVersionUID = 536830527977875262L;
    
    @Override
    public Class<ComprobanteAdjunto> getClaseEntidad() {
        return ComprobanteAdjunto.class;
    }
    
    public void addAdjunto(final ComprobanteAdjunto comprobanteAdjunto) throws DaoException {
        try {
            this.agregarNoFlush(comprobanteAdjunto);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void remove(final ComprobanteAdjunto comprobanteAdjunto) throws DaoException {
        try {
            this.eliminarNoFlush(comprobanteAdjunto);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<ComprobanteAdjunto> getAdjuntos(final Comprobante comprobante) throws DaoException {
        List resultList;
        try {
            resultList = this.entityManager.createQuery("select ca from ComprobanteAdjunto ca where ca.comprobante = :c").setParameter("c", (Object)comprobante).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<ComprobanteAdjunto>)resultList;
    }
}

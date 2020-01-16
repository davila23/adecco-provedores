// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import java.util.List;
import ar.com.adecco.dominio.ebs.po.LineaOrdenCompra;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.inject.Named;
import ar.com.adecco.proveedores.model.Linea;

@Named
public class LineaDao extends AbstractDao<Linea>
{
    private static final long serialVersionUID = -2091038415362050321L;
    
    public void addLinea(final Linea linea) throws DaoException {
        try {
            this.agregarNoFlush(linea);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void modifyLinea(final Linea linea) throws DaoException {
        try {
            this.modificarNoFlush(linea);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void removeLinea(final Linea linea) throws DaoException {
        try {
            this.eliminarNoFlush(linea);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<Linea> getLineasPorLineaOC(final LineaOrdenCompra lineaOrdenCompra) throws DaoException {
        List resultList;
        try {
            resultList = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where e.lineaOrdenCompra = :loc").setParameter("loc", (Object)lineaOrdenCompra).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<Linea>)resultList;
    }
    
    @Override
    public Class<Linea> getClaseEntidad() {
        return Linea.class;
    }
}

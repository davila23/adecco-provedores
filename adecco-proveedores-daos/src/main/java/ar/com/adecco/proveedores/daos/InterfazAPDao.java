// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.proveedores.model.Comprobante;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import java.util.Date;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.model.Proveedor;
import javax.inject.Named;
import ar.com.adecco.proveedores.model.InterfazAP;

@Named
public class InterfazAPDao extends AbstractDao<InterfazAP>
{
    private static final long serialVersionUID = 4049598611106668122L;
    
    @Override
    public Class<InterfazAP> getClaseEntidad() {
        return InterfazAP.class;
    }
    
    public void addProveedor(final Proveedor proveedor) throws DaoException {
        this.addProveedor(proveedor, null);
    }
    
    public void addProveedor(final Proveedor proveedor, final String error) throws DaoException {
        try {
            final InterfazAP interfazAP = new InterfazAP();
            interfazAP.setFecha(new Date());
            interfazAP.setError(error);
            interfazAP.setProveedor(proveedor);
            interfazAP.setEstadoProveedor(proveedor.getEstado());
            this.agregarNoFlush(interfazAP);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void addComprobante(final Comprobante factura) throws DaoException {
        try {
            final InterfazAP interfazAP = new InterfazAP();
            interfazAP.setFecha(new Date());
            interfazAP.setError("");
            interfazAP.setFactura(factura);
            interfazAP.setEstadoFactura(factura.getEstado());
            this.agregarNoFlush(interfazAP);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void addComprobante(final Comprobante factura, final String error) throws DaoException {
        try {
            final InterfazAP interfazAP = new InterfazAP();
            interfazAP.setFecha(new Date());
            interfazAP.setError(error);
            interfazAP.setFactura(factura);
            interfazAP.setEstadoFactura(factura.getEstado());
            this.agregarNoFlush(interfazAP);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
}

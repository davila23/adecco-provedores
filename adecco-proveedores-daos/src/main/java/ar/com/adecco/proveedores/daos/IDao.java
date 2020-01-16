// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.proveedores.daos.exception.DaoException;
import java.util.List;

public interface IDao<T>
{
    void agregar(final T p0);
    
    void modificar(final T p0);
    
    void eliminar(final T p0);
    
    List<T> listarTodos() throws DaoException;
    
    List<T> listar() throws DaoException;
}

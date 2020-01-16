// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.datamodel;

import java.util.Iterator;
import java.util.List;
//import org.primefaces.model.SelectableDataModel;
import javax.faces.model.ListDataModel;

public abstract class SimpleSelectableDataModel<T> extends ListDataModel<T> 
//implements SelectableDataModel<T>
{
    public SimpleSelectableDataModel() {
    }
    
    public SimpleSelectableDataModel(final List<T> list) {
        super(list);
    }
    
    public T getRowData(final String s) {
/*        for (final T next : (List<T>) this.getWrappedData()) {
            if (this.getRowKey(next).equals(s)) {
                return next;
            }
        }
        return null;
    }*/
    	return null;
    	}
}

// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.converter.selectitems;

import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Map;
import java.lang.reflect.Array;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectItem;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItemGroup;
import javax.faces.model.SelectItem;
import java.util.Iterator;
import javax.faces.component.UIInput;
import javax.faces.convert.Converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public final class SelectItemsUtils
{
    private SelectItemsUtils() {
    }
    
    public static Object findValueByStringConversion(final FacesContext facesContext, final UIComponent uiComponent, final String s, final Converter converter) {
        return findValueByStringConversion(facesContext, uiComponent, (Iterator)getSelectItems(facesContext, (UIInput)uiComponent).iterator(), s, converter);
    }
    
    private static Object findValueByStringConversion(final FacesContext facesContext, final UIComponent uiComponent, final Iterator<SelectItem> iterator, final String s, final Converter converter) {
        while (iterator.hasNext()) {
            final SelectItem selectItem = iterator.next();
            if (selectItem instanceof SelectItemGroup) {
                final SelectItem[] selectItems = ((SelectItemGroup)selectItem).getSelectItems();
                if (selectItems == null || selectItems.length == 0) {
                    continue;
                }
                final Object valueByStringConversion = findValueByStringConversion(facesContext, uiComponent, (Iterator)new ArrayIterator(selectItems), s, converter);
                if (valueByStringConversion != null) {
                    return valueByStringConversion;
                }
                continue;
            }
            else {
                if (!selectItem.isNoSelectionOption() && s.equals(converter.getAsString(facesContext, uiComponent, selectItem.getValue()))) {
                    return selectItem.getValue();
                }
                continue;
            }
        }
        return null;
    }
    
    public static List<Object> collectAllValuesFromSelectItems(final FacesContext facesContext, final UIComponent uiComponent) {
        final ArrayList<Object> list = new ArrayList<Object>();
        collect((Iterator)getSelectItems(facesContext, (UIInput)uiComponent).iterator(), (List)list);
        return list;
    }
    
    private static void collect(final Iterator<SelectItem> iterator, final List<Object> list) {
        while (iterator.hasNext()) {
            final SelectItem selectItem = iterator.next();
            if (selectItem instanceof SelectItemGroup) {
                final SelectItem[] selectItems = ((SelectItemGroup)selectItem).getSelectItems();
                if (selectItems == null || selectItems.length == 0) {
                    continue;
                }
                collect((Iterator)new ArrayIterator(selectItems), (List)list);
            }
            else {
                if (selectItem.isNoSelectionOption()) {
                    continue;
                }
                list.add(selectItem.getValue());
            }
        }
    }
    
    protected static List<SelectItem> getSelectItems(final FacesContext facesContext, final UIInput uiInput) {
        final ArrayList<SelectItem> list = new ArrayList<SelectItem>();
        for (final UIComponent uiComponent : uiInput.getChildren()) {
            if (uiComponent instanceof UISelectItem) {
                final UISelectItem uiSelectItem = (UISelectItem)uiComponent;
                final Object value = uiSelectItem.getValue();
                if (value == null) {
                    list.add(new SelectItem(uiSelectItem.getItemValue(), uiSelectItem.getItemLabel(), uiSelectItem.getItemDescription(), uiSelectItem.isItemDisabled(), uiSelectItem.isItemEscaped(), uiSelectItem.isNoSelectionOption()));
                }
                else {
                    list.add((SelectItem)value);
                }
            }
            else {
                if (!(uiComponent instanceof UISelectItems)) {
                    continue;
                }
                final UISelectItems uiSelectItems = (UISelectItems)uiComponent;
                final Object value2 = uiSelectItems.getValue();
                if (value2 == null) {
                    continue;
                }
                if (value2 instanceof SelectItem) {
                    list.add((SelectItem)value2);
                }
                else if (value2.getClass().isArray()) {
                    for (int i = 0; i < Array.getLength(value2); ++i) {
                        final Object value3 = Array.get(value2, i);
                        if (value3 instanceof SelectItem) {
                            list.add((SelectItem)value3);
                        }
                        else {
                            list.add(createSelectItem(facesContext, uiSelectItems, value3));
                        }
                    }
                }
                else if (value2 instanceof Map) {
                    final Map<Object, Object> map = (Map<Object, Object>) value2;
                    for (final Object next : map.keySet()) {
                        list.add(createSelectItem(facesContext, uiSelectItems, String.valueOf(next), map.get(next)));
                    }
                }
                else {
                    if (!(value2 instanceof Collection)) {
                        continue;
                    }
                    for (final SelectItem next2 : (Collection<SelectItem>)value2) {
                        if (next2 instanceof SelectItem) {
                            list.add(next2);
                        }
                        else {
                            list.add(createSelectItem(facesContext, uiSelectItems, (Object)next2));
                        }
                    }
                }
            }
        }
        return list;
    }
    
    protected static SelectItem createSelectItem(final FacesContext facesContext, final UISelectItems uiSelectItems, final Object o) {
        final String s = (String) uiSelectItems.getAttributes().get("var");
        if (s != null) {
            facesContext.getExternalContext().getRequestMap().put(s, o);
            final Object value = uiSelectItems.getAttributes().get("itemLabel");
            Object value2 = uiSelectItems.getAttributes().get("itemValue");
            final String s2 = (String) uiSelectItems.getAttributes().get("itemDescription");
            final Object value3 = uiSelectItems.getAttributes().get("itemDisabled");
            final Object value4 = uiSelectItems.getAttributes().get("itemLabelEscaped");
            final Object value5 = uiSelectItems.getAttributes().get("noSelectionOption");
            if (value2 == null) {
                value2 = o;
            }
            return new SelectItem(value2, (value == null) ? String.valueOf(o) : String.valueOf(value), s2, value3 != null && Boolean.valueOf(value3.toString()), value4 != null && Boolean.valueOf(value4.toString()), value5 != null && Boolean.valueOf(value5.toString()));
        }
        return new SelectItem(o, String.valueOf(o));
    }
    
    protected static SelectItem createSelectItem(final FacesContext facesContext, final UISelectItems uiSelectItems, final String s, final Object o) {
        final String s2 = (String) uiSelectItems.getAttributes().get("var");
        if (s2 != null) {
            facesContext.getExternalContext().getRequestMap().put(s2, o);
            return new SelectItem(o, s, (String)uiSelectItems.getAttributes().get("itemDescription"), Boolean.valueOf((Boolean) uiSelectItems.getAttributes().get("itemDisabled")), Boolean.valueOf((Boolean) uiSelectItems.getAttributes().get("itemLabelEscaped")), Boolean.valueOf((Boolean) uiSelectItems.getAttributes().get("noSelectionOption")));
        }
        return new SelectItem(o, s);
    }
    
    static class ArrayIterator implements Iterator<SelectItem>
    {
        private SelectItem[] items;
        private int index;
        
        public ArrayIterator(final SelectItem[] items) {
            this.index = 0;
            this.items = items;
        }
        
        @Override
        public boolean hasNext() {
            return this.index < this.items.length;
        }
        
        @Override
        public SelectItem next() {
            try {
                return this.items[this.index++];
            }
            catch (IndexOutOfBoundsException ex) {
                throw new NoSuchElementException();
            }
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

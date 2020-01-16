// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.commons.producers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Documented
public @interface BeanMapper {
}

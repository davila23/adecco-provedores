// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.commons.producers;

import javax.enterprise.inject.Produces;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

public class EntityManagerProducer
{
    @PersistenceContext(unitName = "proveedores-pu")
    @Produces
    private EntityManager entityManager;
}

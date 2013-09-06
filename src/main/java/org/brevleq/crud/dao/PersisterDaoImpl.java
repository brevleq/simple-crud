package org.brevleq.crud.dao;

import javax.persistence.EntityManager;

/**
 * Created with IntelliJ IDEA.
 * User: brevleq
 * Date: 16/02/13
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */
public class PersisterDaoImpl<T> extends ReaderDaoImpl<T> implements PersisterDao<T> {

    public PersisterDaoImpl(EntityManager entityManager, Class<T> clazz) {
        super(entityManager, clazz);
    }

    @Override
    public void create(T entity) throws Exception {
        errorMessage = "Some problems occurred when persisting the entity!";
        SimpleCreator creator = new SimpleCreator(entityManager);
        creator.persist(entity, SimpleCreator.USE_PERSIST);
        managedEntity = (T) creator.getManagedEntity();
    }

    @Override
    public void update(T entity) throws Exception {
        errorMessage = "Some problems occurred when updating the entity!";
        SimpleUpdater updater = new SimpleUpdater(entityManager);
        updater.update(entity, null);
        managedEntity = (T) updater.getManagedEntity();
    }

    @Override
    public void delete(T entity) throws Exception {
        SimpleDeleter deleter = new SimpleDeleter(entityManager);
        deleter.delete(entity);
    }
}

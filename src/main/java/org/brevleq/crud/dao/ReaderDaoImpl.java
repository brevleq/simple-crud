package org.brevleq.crud.dao;

import org.brevleq.crud.beans.ParameterBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: brevleq
 * Date: 17/12/12
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class ReaderDaoImpl<T> extends BaseDao<T> implements ReaderDao<T> {

    protected Class<T> clazz;

    public ReaderDaoImpl(EntityManager entityManager, Class<T> clazz) {
        super(entityManager);
        this.clazz = clazz;
    }

    @Override
    public List<T> readAll() {
        Query query = entityManager.createQuery(createReadAllQuery());
        limitTuplesQuantity(query);
        return query.getResultList();
    }

    private String createReadAllQuery() {
        StringBuilder builder = new StringBuilder("SELECT e FROM ");
        builder.append(clazz.getSimpleName());
        builder.append(" e");
        builder.append(createOrderSentence());
        return builder.toString();
    }

    private StringBuilder createOrderSentence() {
        StringBuilder builder = new StringBuilder();
        List<String> orderFields = retrieveOrderFields(clazz);
        int index = 0;
        while (index < orderFields.size()) {
            if (index == 0)
                builder.append(" order by e.");
            else
                builder.append(",");
            builder.append(orderFields.get(index));
            index++;
        }
        return builder;
    }

    @Override
    public List<T> readAll(int firstTupleIndex, int quantity) {
        this.firstTupleIndex = firstTupleIndex;
        this.tuplesQuantity = quantity;
        return readAll();
    }

    @Override
    public T readById(Serializable id) {
        if (id != null)
            return (T) entityManager.find(clazz, id);
        return null;
    }

    @Override
    public List<T> readByFilter(Object value, int filter, ParameterBean... parameters) {
        throw new UnsupportedOperationException("You should implement this operation!");
    }

    @Override
    public List<T> readByFilter(Object value, int filter, int firstTupleIndex, int quantity, ParameterBean... parameters) {
        this.firstTupleIndex = firstTupleIndex;
        this.tuplesQuantity = quantity;
        return readByFilter(value, filter, parameters);
    }

    @Override
    public int getTotalTuplesByFilter(Object valor, int filtro) {
        throw new UnsupportedOperationException("You should implement this operation");
    }

    @Override
    public int getTotalTuples() {
        Query query = entityManager.createQuery(createTotalTuplesQuery());
        return ((Number) query.getSingleResult()).intValue();
    }

    private String createTotalTuplesQuery() {
        StringBuilder builder = new StringBuilder("SELECT count(e) FROM ");
        builder.append(clazz.getSimpleName());
        builder.append(" e");
        return builder.toString();
    }
}

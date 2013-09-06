package org.brevleq.simplepersistence.dao;

import org.brevleq.simplepersistence.beans.ParameterBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: brevleq
 * Date: 17/12/12
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */
public interface ReaderDao<T> {

    List<T> readAll();

    List<T> readAll(int firstTupleIndex, int quantity);

    T readById(Serializable id);

    List<T> readByFilter(Object value, int filter, ParameterBean... parameters);

    List<T> readByFilter(Object value, int filter,int firstTupleIndex, int quantity, ParameterBean... parameters);

    int getTotalTuples();

    int getTotalTuplesByFilter(Object value, int filter);

    T getManagedEntity();

    String getErrorMessage();
}

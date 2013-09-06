package org.brevleq.crud.dao;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao<T> {

    protected EntityManager entityManager;
    protected String errorMessage;
    protected T managedEntity;
    protected Integer tuplesQuantity = null;
    protected Integer firstTupleIndex = null;

    protected BaseDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected void limitTuplesQuantity(Query query) {
        if (tuplesQuantity != null && firstTupleIndex != null) {
            query.setFirstResult(firstTupleIndex);
            if (firstTupleIndex % tuplesQuantity == 0)
                query.setMaxResults(tuplesQuantity);
            else
                query.setMaxResults(0);
            tuplesQuantity = null;
            firstTupleIndex = null;
        }
    }

    protected String createLikeParameter(String sentence) {
        StringBuilder builder = new StringBuilder();
        builder.append(sentence);
        while (builder.charAt(builder.length() - 1) == ' ')
            builder.deleteCharAt(builder.length() - 1);
        builder.append("%");
        return builder.toString();
    }

    protected void refreshEntitites(List entities) {
        for (Object entity : entities)
            entityManager.refresh(entity);
    }

    protected String retrieveIdField(Class clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].annotationType().equals(Id.class) || annotations[i].annotationType().equals(EmbeddedId.class)) {
                    return fieldName;
                }
            }
        }
        return null;
    }

    protected List<String> retrieveOrderFields(Class clazz) {
        List<String> orderFields = new ArrayList<String>();
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].annotationType().equals(OrderColumn.class)) {
                    orderFields.add(fieldName);
                }
            }
        }
        return orderFields;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public T getManagedEntity() {
        return managedEntity;
    }
}

package org.brevleq.simplepersistence.dao;

import com.hrgi.utilidades.beans.RecuperadorMetodos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;

/**
 * Arquivo criado em: 19/01/12
 */
public class SimpleDeleter extends BaseDao {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public SimpleDeleter(EntityManager entityManager) {
        super(entityManager);
    }

    public void delete(Object entidade) throws Exception {
        try {
            entityManager.clear();
            Object entidadeGerenciada = recuperarEntidadeGerenciada(entidade);
            entityManager.remove(entidadeGerenciada);
        } catch (Exception e) {
            throw e;
        }
    }

    private Object recuperarEntidadeGerenciada(Object entidade) {
        try {
            String propriedadeId = retrieveIdField(entidade.getClass());
            Method metodo = RecuperadorMetodos.recuperarMetodoGetPeloNomeDaPropriedade(entidade.getClass(), propriedadeId);
            Object id = metodo.invoke(entidade);
            return entityManager.find(entidade.getClass(), id);
        } catch (Exception e) {
            logger.error("Não foi possível recuperar a entidade gerenciada!", e);
            return null;
        }
    }
}

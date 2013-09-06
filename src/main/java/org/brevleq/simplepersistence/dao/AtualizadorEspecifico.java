package org.brevleq.simplepersistence.dao;

import com.hrgi.utilidades.beans.RecuperadorMetodos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * Arquivo criado em: 28/05/12
 */
public class AtualizadorEspecifico extends BaseDao {

    private String[] propriedades;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public AtualizadorEspecifico(EntityManager entityManager) {
        super(entityManager);
    }

    public void atualizar(Object entidade, String[] propriedades) throws Exception {
        this.propriedades = propriedades;
        if (!entityManager.contains(entidade)) {
            managedEntity = recuperarEntidadeGerenciada(entidade);
            entityManager.refresh(managedEntity);
            carregarEntidadeGerenciadaComNovosValores(entidade);
        } else {
            managedEntity = entidade;
        }
        entityManager.merge(managedEntity);
        entityManager.flush();
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

    private void carregarEntidadeGerenciadaComNovosValores(Object origem) {
        if (propriedades != null)
            copiarSomenteOEspecificado(origem);
    }

    private void copiarSomenteOEspecificado(Object objeto) {
        BeanWrapper origem = new BeanWrapperImpl(objeto);
        BeanWrapper destino = new BeanWrapperImpl(managedEntity);
        for (String propriedade : propriedades) {
            destino.setPropertyValue(propriedade, origem.getPropertyValue(propriedade));
        }
    }
}

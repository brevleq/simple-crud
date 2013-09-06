/*
 * Copyright (c) 2012.
 * Todos os direitos reservados à HR Gestão da Informação Ltda.
 * A cópia não autorizada, irá gerar punições previstas em lei.
 */

package org.brevleq.simplepersistence.dao;

import com.hrgi.utilidades.beans.RecuperadorMetodos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.Method;

public class SimpleUpdater extends BaseDao implements Serializable {


    private String[] propriedadesIgnoradas;
    private PersisterDao gerenciadorRelacionamentos;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public SimpleUpdater(EntityManager entityManager) {
        super(entityManager);
    }

    public void update(Object entidade, String[] propriedadesIgnoradas) throws Exception {
        this.propriedadesIgnoradas = propriedadesIgnoradas;
        entityManager.detach(entidade);
        managedEntity = recuperarEntidadeGerenciada(entidade);
        if (gerenciadorRelacionamentos != null) {
            gerenciadorRelacionamentos.removerRelacoes(managedEntity, entidade);
            gerenciadorRelacionamentos.criarRelacoes(managedEntity, entidade);
        }
        carregarEntidadeGerenciadaComNovosValores(entidade);
        entityManager.merge(entidade);
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

    private void carregarEntidadeGerenciadaComNovosValores(Object destino) {
        if (propriedadesIgnoradas != null)
            copiarSomenteOEspecificado(destino);
    }

    private void copiarSomenteOEspecificado(Object objeto) {
        BeanWrapper origem = new BeanWrapperImpl(managedEntity);
        BeanWrapper destino = new BeanWrapperImpl(objeto);
        for (String propriedade : propriedadesIgnoradas) {
            destino.setPropertyValue(propriedade, origem.getPropertyValue(propriedade));
        }
    }

    public void setGerenciadorRelacionamentos(PersisterDao gerenciadorRelacionamentos) {
        this.gerenciadorRelacionamentos = gerenciadorRelacionamentos;
    }
}

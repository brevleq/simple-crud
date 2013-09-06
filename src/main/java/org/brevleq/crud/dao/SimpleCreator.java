package org.brevleq.crud.dao;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class SimpleCreator extends BaseDao implements Serializable {

    public static final int USE_PERSIST = 1;
    public static final int USE_MERGE = 2;

    private PersisterDao gerenciadorRelacionamentos;

    public SimpleCreator(EntityManager entityManager) {
        super(entityManager);
    }

    public void persist(Object entidade, int tipoPersistencia) throws Exception {

        //todo: Encontre uma maneira de não precisar usar o merge!

        if (tipoPersistencia == USE_PERSIST) {
            entityManager.persist(entidade);
            managedEntity = entidade;
        } else if (tipoPersistencia == USE_MERGE) {
            managedEntity = entityManager.merge(entidade);
        }
        if (gerenciadorRelacionamentos != null) {
            gerenciadorRelacionamentos.criarRelacoes(null, entidade);
        }
    }

    public void setGerenciadorRelacionamentos(PersisterDao gerenciadorRelacionamentos) {
        this.gerenciadorRelacionamentos = gerenciadorRelacionamentos;
    }
}

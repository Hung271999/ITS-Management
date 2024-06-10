package com.sharp.vn.its.management.repositories;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

/**
 * The type Base jpa repository.
 *
 * @param <T> the type parameter
 * @param <Id> the type parameter
 */
public class BaseJpaRepositoryImpl<T, Id extends Serializable>
        extends SimpleJpaRepository<T, Id> implements BaseJpaRepository<T, Id> {

    /**
     * The constant DELETE_BATCH_QUERY_STRING.
     */
    public static final String DELETE_BATCH_QUERY_STRING = "DELETE FROM %s x WHERE x.id IN (?1)";

    /**
     * The constant QUERY_LIST_STRING.
     */
    public static final String QUERY_LIST_STRING = "SELECT x FROM %s x WHERE x.id IN (?1)";

    /**
     * The Entity information.
     */
    private final JpaEntityInformation<T, ?> entityInformation;

    /** The entity manager. */
    private final EntityManager entityManager;

    /**
     * Instantiates a new Base jpa repository.
     *
     * @param entityInformation the entity information
     * @param entityManager the entity manager
     */
    public BaseJpaRepositoryImpl(final JpaEntityInformation<T, ?> entityInformation,
            final EntityManager entityManager) {
        super(entityInformation, entityManager);

        // Keep the EntityManager around to used from the newly introduced methods.
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
    }

    /**
     * Delete batch int.
     *
     * @param ids the ids
     * @return the int
     */
    @Override
    public int deleteBatch(final Iterable<Id> ids) {
        Assert.notNull(ids, "The given Iterable of ids not be null!");

        if (!ids.iterator().hasNext()) {
            return 0;
        }

        // This is JPA query, it is secure usage
        // http://software-security.sans.org/developer-how-to/fix-sql-injection-in-java-persistence-api-jpa
        final Query query = entityManager.createQuery( //NOSONAR
                String.format(DELETE_BATCH_QUERY_STRING, entityInformation.getEntityName()));
        query.setParameter(1, ids);

        return query.executeUpdate();
    }

    /**
     * Refresh.
     *
     * @param <S> the type parameter
     * @param entity the entity
     */
    @Override
    public <S extends T> void refresh(final S entity) {
        entityManager.refresh(entity);
    }

    /**
     * Find all list.
     *
     * @param ids the ids
     * @return the list
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll(List<Id> ids) {
        Assert.notNull(ids, "The given List of ids not be null!");

        if (!ids.iterator().hasNext()) {
            return new ArrayList<>();
        }

        // This is JPA query, it is secure usage
        // http://software-security.sans.org/developer-how-to/fix-sql-injection-in-java-persistence-api-jpa
        final Query query = entityManager
                .createQuery(String.format(QUERY_LIST_STRING,
                        entityInformation.getEntityName())); //NOSONAR
        query.setParameter(1, ids);

        return query.getResultList();
    }

    /**
     * Save batch list.
     *
     * @param entities the entities
     * @return the list
     */
    @Override
    public List<T> saveBatch(Collection<T> entities) {
        final List<T> savedEntities = new ArrayList<>(entities.size());
        for (final T t : entities) {
            entityManager.persist(t);
            savedEntities.add(t);
        }
        // Flush a batch of inserts and release memory.
        entityManager.flush();
        entityManager.clear();
        return savedEntities;
    }
}

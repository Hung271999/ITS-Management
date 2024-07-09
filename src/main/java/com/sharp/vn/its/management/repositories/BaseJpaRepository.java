package com.sharp.vn.its.management.repositories;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The Interface BaseJpaRepository.
 *
 * @param <T>  the generic type
 * @param <ID> the generic type
 * @author <a href="mailto:developer@hitachiconsulting.com">phongtnguyen.jk</a>
 */
@NoRepositoryBean
public interface BaseJpaRepository<T, ID extends Serializable>
        extends JpaRepository<T, ID> { //NOSONAR

    /**
     * Delete batch collection of id.
     *
     * @param ids the collection of id
     * @return the number of deleted records
     */
    int deleteBatch(Iterable<ID> ids);

    /**
     * Refresh entity.
     *
     * @param <S>    the generic type
     * @param entity the entity
     */
    <S extends T> void refresh(S entity);

    /**
     * Find all by specification.
     *
     * @param specification the specification
     * @return the list
     */
    List<T> findAll(Specification<T> specification);

    /**
     * Find all by list of Ids.
     *
     * @param ids the Id list
     * @return the list
     */
    List<T> findAll(List<ID> ids);

    /**
     * Save batch list.
     *
     * @param entities the entities
     * @return the list
     */
    List<T> saveBatch(Collection<T> entities);
}



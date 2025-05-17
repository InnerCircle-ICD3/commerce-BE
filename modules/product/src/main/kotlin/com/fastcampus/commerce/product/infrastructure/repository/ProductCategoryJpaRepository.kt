package com.fastcampus.commerce.product.infrastructure.repository

import com.fastcampus.commerce.product.domain.entity.ProductCategory
import com.fastcampus.commerce.product.domain.model.CategoryInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductCategoryJpaRepository : JpaRepository<ProductCategory, Long> {
    @Query(
        """
    select new com.fastcampus.commerce.product.domain.model.CategoryInfo(
        p.id,
        g.id,
        g.title,
        c.id,
        c.name,
        c.sortOrder
    )
    from ProductCategory pc
        join pc.product p
        join pc.category c
        join c.group g
    where pc.isDeleted = false
    and p.id = :productId
    """,
    )
    fun findCategoriesByProductId(productId: Long): List<CategoryInfo>

    @Query(
        """
    select new com.fastcampus.commerce.product.domain.model.CategoryInfo(
        p.id,
        g.id,
        g.title,
        c.id,
        c.name,
        c.sortOrder
    )
    from ProductCategory pc
        join pc.product p
        join pc.category c
        join c.group g
    where pc.isDeleted = false
    and p.id in :productIds
    """,
    )
    fun findCategoriesByProductIds(productIds: List<Long>): List<CategoryInfo>
}

package com.fastcampus.commerce.product.infrastructure.repository

import com.fastcampus.commerce.product.application.dto.SearchProductQuery
import com.fastcampus.commerce.product.domain.entity.Product
import com.fastcampus.commerce.product.domain.entity.QInventory
import com.fastcampus.commerce.product.domain.entity.QProduct
import com.fastcampus.commerce.product.domain.entity.QProductCategory
import com.fastcampus.commerce.product.domain.entity.SellingStatus
import com.fastcampus.commerce.product.domain.model.ProductInfo
import com.fastcampus.commerce.product.domain.model.QProductInfo
import com.fastcampus.commerce.product.domain.repository.ProductRepository
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class ProductRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val productJpaRepository: ProductJpaRepository,
) : ProductRepository {
    override fun findById(productId: Long): Optional<Product> {
        return productJpaRepository.findById(productId)
    }

    override fun findProducts(query: SearchProductQuery, pageable: Pageable): Page<ProductInfo> {
        val product = QProduct.product
        val inventory = QInventory.inventory

        val whereCondition = createCondition(query)

        val content = queryFactory
            .select(
                QProductInfo(
                    product.id,
                    product.name,
                    product.price,
                    inventory.quantity,
                    product.thumbnail,
                    product.detailImage,
                    product.status,
                    product.createdAt,
                    product.updatedAt,
                ),
            )
            .from(product)
            .innerJoin(inventory).on(product.id.eq(inventory.product.id))
            .where(whereCondition)
            .orderBy(product.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(product.id.count())
            .from(product)
            .where(whereCondition)
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }
}

private fun createCondition(query: SearchProductQuery): BooleanBuilder {
    val product = QProduct.product
    val productCategory = QProductCategory.productCategory

    val whereCondition = BooleanBuilder()

    query.name?.let { whereCondition.and(product.name.containsIgnoreCase(it)) }
    query.status?.let { whereCondition.and(product.status.eq(SellingStatus.valueOf(it))) }

    val categoryIds = query.categoryIds

    val categoryCondition: BooleanExpression? = if (categoryIds.isNullOrEmpty()) {
        null
    } else {
        val subQuery = JPAExpressions.select(productCategory.product.id)
            .from(productCategory)
            .where(
                productCategory.category.id.`in`(categoryIds),
                productCategory.isDeleted.isFalse,
            )
            .groupBy(productCategory.product.id)
            .having(productCategory.category.id.countDistinct().eq(categoryIds.size.toLong()))

        product.id.`in`(subQuery)
    }

    categoryCondition?.let { whereCondition.and(it) }
    return whereCondition
}

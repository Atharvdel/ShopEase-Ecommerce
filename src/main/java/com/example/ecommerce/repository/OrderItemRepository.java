package com.example.ecommerce.repository;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

    @Query(value = "SELECT * FROM ORDER_ITEMS WHERE order_id = :orderId", nativeQuery = true)
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);

    @Query(value = "SELECT category_id, " +
            "(SELECT SUM(oi.total_price) FROM ORDER_ITEMS oi WHERE oi.product_id IN " +
            "(SELECT p2.product_id FROM PRODUCTS p2 WHERE p2.category_id = c.category_id)) as total_sales " +   //NESTED QUERY
            "FROM CATEGORIES c", nativeQuery = true)
    List<Object[]> getSalesByCategory();

    @Query(value = "SELECT EXISTS(SELECT 1 FROM ORDER_ITEMS oi " +                  //EXISTS
            "JOIN ORDERS o ON oi.order_id = o.order_id " +
            "WHERE o.customer_id = :customerId " +
            "AND oi.product_id = :productId)", nativeQuery = true)
    Long existsByCustomerIdAndProductId(@Param("customerId") Long customerId, @Param("productId") Long productId);

    @Query(value = "SELECT * FROM ORDER_ITEMS oi " +
            "WHERE oi.product_id IN (SELECT p.product_id FROM PRODUCTS p WHERE p.seller_id = :sellerId)", nativeQuery = true) //NESTED QUERY
    List<OrderItem> findOrderItemsBySellerId(@Param("sellerId") Long sellerId);

    @Procedure(procedureName = "get_total_sales_for_seller")                            //PROCEDURE
    BigDecimal getTotalSalesForSeller(@Param("sellerId") Long sellerId);

    @Query(value = "SELECT COUNT(DISTINCT oi.order_id) FROM ORDER_ITEMS oi " +
            "JOIN PRODUCTS p ON oi.product_id = p.product_id " +
            "WHERE p.seller_id = :sellerId", nativeQuery = true)
    Long getTotalOrdersForSeller(@Param("sellerId") Long sellerId);

    @Query(value = "CALL get_top_selling_products_for_seller(:sellerId)", nativeQuery = true)           //PROCEDURE
    List<Object[]> getTopSellingProductsForSeller(@Param("sellerId") Long sellerId);

    @Query(value = "SELECT DATE(o.time) as day, SUM(oi.total_price) FROM ORDER_ITEMS oi " +
            "JOIN ORDERS o ON oi.order_id = o.order_id " +
            "JOIN PRODUCTS p ON oi.product_id = p.product_id " +
            "WHERE p.seller_id = :sellerId " +
            "GROUP BY day ORDER BY day DESC LIMIT 30", nativeQuery = true)
    List<Object[]> getDailySalesForSeller(@Param("sellerId") Long sellerId);

    @Query(value = "CALL get_category_wise_sales_for_seller(:sellerId)", nativeQuery = true)        //PROCEDURE
    List<Object[]> getCategoryWiseSalesForSeller(@Param("sellerId") Long sellerId);

    @Query(value = "SELECT SUM(quantity) FROM ORDER_ITEMS WHERE product_id = " +
            "(SELECT product_id FROM PRODUCTS WHERE product_id = :productId)", nativeQuery = true)   //NESTED QUERY
    int sumQuantityByProductId(@Param("productId") Long productId);

    @Query(value = "SELECT SUM(oi.quantity) FROM ORDER_ITEMS oi " +
            "JOIN ORDERS o ON oi.order_id = o.order_id " +
            "WHERE oi.product_id = :productId " +
            "AND DATE(o.time) >= :startDate", nativeQuery = true)
    int getUnitsSoldForProductByFilter(
            @Param("productId") Long productId,
            @Param("startDate") LocalDate startDate
    );

    @Query(value = "SELECT SUM(quantity) FROM ORDER_ITEMS oi " +
            "JOIN ORDERS o ON oi.order_id = o.order_id " +
            "WHERE oi.product_id = :productId " +
            "AND DATE(o.time) BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    int getUnitsSoldForProductByFilter(
            @Param("productId") Long productId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    @Query(value = "SELECT SUM(oi.quantity) FROM ORDER_ITEMS oi " +
            "JOIN ORDERS o ON oi.order_id = o.order_id " +
            "WHERE oi.product_id = :productId " +
            "AND DATE(o.time) BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Integer findUnitsSoldByProductAndFilter(
            @Param("productId") Long productId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = "SELECT SUM(oi.total_price) FROM ORDER_ITEMS oi " +
            "JOIN ORDERS o ON oi.order_id = o.order_id " +
            "WHERE oi.product_id = :productId " +
            "AND DATE(o.time) BETWEEN :startDate AND :endDate", nativeQuery = true)
    BigDecimal getTotalSalesForProductByFilter(
            @Param("productId") Long productId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    @Query(value = "SELECT SUM(oi.total_price) FROM ORDER_ITEMS oi", nativeQuery = true)
    BigDecimal sumTotalSalesAllSellers();

    @Query(value = "SELECT COUNT(DISTINCT oi.order_id) FROM ORDER_ITEMS oi", nativeQuery = true)
    Long countTotalOrdersAllSellers();

    @Query(value = "SELECT s.business_name, SUM(oi.total_price) as total_sales " +
            "FROM ORDER_ITEMS oi " +
            "JOIN PRODUCTS p ON oi.product_id = p.product_id " +
            "JOIN SELLER s ON p.seller_id = s.id " +
            "GROUP BY s.id ORDER BY total_sales DESC LIMIT 5", nativeQuery = true)
    List<Object[]> findTopSellers();

    @Query(value = "SELECT c.name, SUM(oi.total_price) " +
            "FROM ORDER_ITEMS oi " +
            "JOIN PRODUCTS p ON oi.product_id = p.product_id " +
            "JOIN CATEGORIES c ON p.category_id = c.category_id " +
            "GROUP BY c.category_id", nativeQuery = true)
    List<Object[]> findCategoryWiseSalesAllSellers();

    @Query("SELECT p.productId, p.name, SUM(oi.quantity) " +
            "FROM OrderItem oi " +
            "JOIN oi.product p " +
            "WHERE p.stockQuantity > 0 " +  // Use stockQuantity field from Product
            "GROUP BY p.productId")
    List<Object[]> findActiveProducts();

    @Query("SELECT p.name, SUM(oi.quantity) AS totalSold " +
            "FROM OrderItem oi " +
            "JOIN Product p ON oi.product.productId = p.productId " +
            "GROUP BY p.productId " +
            "ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts();
}


//CREATE PROCEDURE get_category_wise_sales_for_seller(IN sellerId BIGINT)
//    -> BEGIN
//    ->     SELECT c.name, SUM(oi.total_price) AS total_sales
//    ->     FROM ORDER_ITEMS oi
//    ->     JOIN PRODUCTS p ON oi.product_id = p.product_id
//    ->     JOIN CATEGORIES c ON p.category_id = c.category_id
//    ->     WHERE p.seller_id = sellerId
//    ->     GROUP BY c.category_id, c.name;
//    -> END //


//CREATE PROCEDURE get_top_selling_products_for_seller(IN sellerId BIGINT)
//    -> BEGIN
//    ->     SELECT p.name, SUM(oi.quantity) AS total_sold
//    ->     FROM ORDER_ITEMS oi
//    ->     JOIN PRODUCTS p ON oi.product_id = p.product_id
//    ->     WHERE p.seller_id = sellerId
//    ->     GROUP BY p.product_id, p.name
//    ->     ORDER BY total_sold DESC
//    ->     LIMIT 5;
//        -> END //

//CREATE PROCEDURE get_total_sales_for_seller(IN sellerId BIGINT)
//    -> BEGIN
//    ->     SELECT SUM(oi.total_price) AS total_sales
//    ->     FROM ORDER_ITEMS oi
//    ->     JOIN PRODUCTS p ON oi.product_id = p.product_id
//    ->     WHERE p.seller_id = sellerId;
//    -> END //


//CREATE TRIGGER prevent_negative_price_before_update
//    -> BEFORE UPDATE ON products
//    -> FOR EACH ROW
//    -> BEGIN
//    ->     IF NEW.price < 0 THEN
//    ->         SET NEW.price = 0;
//        ->     END IF;
//    -> END$$


//CREATE TRIGGER prevent_negative_price_before_insert
//    -> BEFORE INSERT ON products
//    -> FOR EACH ROW
//    -> BEGIN
//    ->     IF NEW.price < 0 THEN
//    ->         SET NEW.price = 0;
//        ->     END IF;
//    -> END$$

//DELIMITER $$
//
//CREATE TRIGGER reduce_product_stock_after_order
//AFTER INSERT ON order_items
//FOR EACH ROW
//        BEGIN
//UPDATE products
//SET stock_quantity = stock_quantity - NEW.quantity
//WHERE product_id = NEW.product_id;
//END$$
//
//        DELIMITER ;

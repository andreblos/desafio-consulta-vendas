package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SellerSumaryDTO;
import com.devsuperior.dsmeta.entities.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(value = "SELECT obj FROM Sale obj JOIN FETCH obj.seller "
            + "WHERE UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%')) " +
            "AND obj.date BETWEEN :minDate AND :maxDate",
            countQuery = "SELECT COUNT(obj) FROM Sale obj JOIN obj.seller "
                    + "WHERE UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%')) " +
                    "AND obj.date BETWEEN :minDate AND :maxDate")
    Page<Sale> searchAll(String name, LocalDate minDate, LocalDate maxDate, Pageable pageable);

    @Query(value = "SELECT new  com.devsuperior.dsmeta.dto.SellerSumaryDTO( "
            + "obj.name, SUM(sa.amount)) "
            + "FROM Seller obj JOIN obj.sales sa WHERE sa.date BETWEEN :minDate AND :maxDate "
            + "GROUP BY obj.id, obj.name ORDER BY obj.name",
           countQuery = "SELECT COUNT(obj) FROM Seller obj JOIN obj.sales sa "
                   + "WHERE sa.date BETWEEN :minDate AND :maxDate ")
    Page<SellerSumaryDTO> sellerSummary(LocalDate minDate, LocalDate maxDate, Pageable pageable);
}

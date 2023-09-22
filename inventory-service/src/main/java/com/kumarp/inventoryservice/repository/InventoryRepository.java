package com.kumarp.inventoryservice.repository;

import com.kumarp.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    //Optional<Inventory> findBySkuCode(String skuCode); // get single object

    List<Inventory> findBySkuCodeIn(List<String> skuCode); // for getting the list of object

}

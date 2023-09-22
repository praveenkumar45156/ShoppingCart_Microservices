package com.kumarp.inventoryservice.service;

import com.kumarp.inventoryservice.dto.InventoryResponse;
import com.kumarp.inventoryservice.model.Inventory;
import com.kumarp.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly=true)
    @SneakyThrows
//    public boolean isInStock(List<String> skuCode){
//        return inventoryRepository.findBySkuCode(skuCode).isPresent();
//    }
    public List<InventoryResponse> isInStock(List<String> skuCode){
        log.info("Checking Inventory");
        // Adding below sleep to test the circuit break
        //log.info("Wait Started");
        //Thread.sleep ( 10000 );
        //log.info("Wait Ended");
        return inventoryRepository.findBySkuCodeIn(skuCode)
                .stream ()
                .map ( Inventory ->
                    InventoryResponse.builder ()
                            .skuCode ( Inventory.getSkuCode () )
                            .isInStock ( Inventory.getQuantity () > 0 )
                            .build ()
                 )
                .toList ();
    }
}

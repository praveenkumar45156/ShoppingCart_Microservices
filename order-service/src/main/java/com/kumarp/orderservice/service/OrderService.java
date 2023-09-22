package com.kumarp.orderservice.service;

import com.kumarp.orderservice.dto.InventoryResponse;
import com.kumarp.orderservice.dto.OrderLineItemsDto;
import com.kumarp.orderservice.dto.OrderRequest;
import com.kumarp.orderservice.event.OrderPlacedEvent;
import com.kumarp.orderservice.model.Order;
import com.kumarp.orderservice.model.OrderLineItems;
import com.kumarp.orderservice.repository.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    //private final WebClient webClient;
    private final WebClient.Builder webClientBuilder;
    private final ObservationRegistry observationRegistry;
    private final ApplicationEventPublisher applicationEventPublisher;
    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        // Collect the skuCodes form OrderLineItemsList
        List<String> skuCodes = order.getOrderLineItemsList ().stream ()
                .map( OrderLineItems::getSkuCode )
                .toList ();

        // Synchronous call
        // call inventory service and place the order if order isInStock
        // Call URL like "http://localhost:8082/api/inventory/skucode=iPhone13&skucode=iPhone13_red"
        /*InventoryResponse[] inventoryResponsesArray = webClient.build()
                .get ( )
                .uri ( "http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam ( "skuCode", skuCodes )
                                .build (  ) )
                .retrieve ( )
                .bodyToMono ( InventoryResponse[].class )
                .block ( );

        // inventoryResponsesArray should not be null
        //assert inventoryResponsesArray != null;
        // Check all skuCode is true or not
        boolean allProducts = Arrays.stream( inventoryResponsesArray )
                .allMatch ( InventoryResponse::isInStock );

        if(allProducts){
            orderRepository.save(order);
            return "Order Placed Successfully !!";
        }else{
            throw new IllegalArgumentException ( "Product is in not in stock. Please try again" );
        }*/

        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup",
                this.observationRegistry);
        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");
        return inventoryServiceObservation.observe(() -> {
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                // publish Order Placed Event
                applicationEventPublisher.publishEvent(new OrderPlacedEvent (this, order.getOrderNumber()));
                return "Order Placed";
            } else {
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }
        });

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        return orderLineItems;
    }
}

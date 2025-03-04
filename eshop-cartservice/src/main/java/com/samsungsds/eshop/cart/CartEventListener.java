package com.samsungsds.eshop.cart;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Service
public class CartEventListener {
    private final Logger logger = LoggerFactory.getLogger(CartEventListener.class);
    private final CartService cartService;

    public CartEventListener(CartService cartService){
        this.cartService = cartService;
    }

    @RabbitListener(queues = "eshop-queue")
    public void receiveMessage(final OrderPlaced orderPlaced){
        logger.info("empty Cart: {}", orderPlaced.getOrderId());
        cartService.emptyCart();
    }
}

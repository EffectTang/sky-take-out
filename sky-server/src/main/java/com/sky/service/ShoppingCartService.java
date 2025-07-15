package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

import java.util.List;

/**
 * @author starlord
 * @description
 * @create 2025/7/15
 */
public interface ShoppingCartService {


    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    List showShoppingCart();
}

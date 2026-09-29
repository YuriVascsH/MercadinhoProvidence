package br.com.mercadinhoprovidence.controller;

import java.math.BigDecimal;
import java.util.List;

import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.model.Employee;
import br.com.mercadinhoprovidence.model.ItemSell;
import br.com.mercadinhoprovidence.model.PaymentSell;
import br.com.mercadinhoprovidence.model.Product;
import br.com.mercadinhoprovidence.model.Sell;
import br.com.mercadinhoprovidence.service.SellService;

public class SellController {

    private final SellService sellService;
    private Employee employee;

    public SellController(SellService sellService) {
        this.sellService = sellService;
    }

    public void startNewSell(LoginResponseDto loginResponseDto) {
        sellService.startNewSell(loginResponseDto);

    }

    public ProductDto findProduct(String code) throws ProductNotExistOrNotFind {
        return SellService.findProduct(code);

    }

    public ItemSell addItem(Product product, BigDecimal quantity) {
        return sellService.addItem(product, quantity);
    }

    public boolean removeItemByNumber(int numberItem) {
        return sellService.removeItemByNumber(numberItem);
    }

    public void cancelCurrentSell() {
        sellService.cancelCurrentSell();
    }

    public boolean finalizeSell(List<PaymentSell> payments) {
        return sellService.finalizeSell(payments);
    }

    public Sell getCurrentSell() {
        return sellService.getCurrentSell();
    }
} 
package br.com.mercadinhoprovidence.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Representa um item individual lançado na Venda (Linha do Cupom).
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSell {

    private Integer idItemSell;
    
    private Integer numberItem;

    @Builder.Default
    private BigDecimal quantity = BigDecimal.ONE;

    @Builder.Default
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal itemDiscount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal totalItemValue = BigDecimal.ZERO;

    private Product product;

    /**
     * Calcula e atualiza o valor total do item com base na quantidade, 
     * preço unitário e desconto individual.
     */
    public void calculateTotal() {
        if (this.unitPrice == null) {
            this.unitPrice = BigDecimal.ZERO;
        }
        if (this.quantity == null) {
            this.quantity = BigDecimal.ZERO;
        }
        if (this.itemDiscount == null) {
            this.itemDiscount = BigDecimal.ZERO;
        }

        BigDecimal grossTotal = this.unitPrice.multiply(this.quantity);
        this.totalItemValue = grossTotal.subtract(this.itemDiscount);

        if (this.totalItemValue.compareTo(BigDecimal.ZERO) < 0) {
            this.totalItemValue = BigDecimal.ZERO;
        }
    }

    /**
     * Define o produto e já preenche o preço unitário com o preço de venda atual do produto.
     */
    public void setProduct(Product product) {
        this.product = product;
        if (product != null && product.getSalePrice() != null) {
            this.unitPrice = product.getSalePrice();
            calculateTotal();
        }
    }

    /**
     * Atualiza a quantidade do item e recalcular o subtotal.
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        calculateTotal();
    }
}
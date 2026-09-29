package br.com.mercadinhoprovidence.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.mercadinhoprovidence.model.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Integer id;
    private String name;
    private String barCode;
    private String description;
    private Category category;

    @Builder.Default
    private Boolean stockControl = true;

    @Builder.Default
    private BigDecimal costPrice = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal salePrice = BigDecimal.ZERO;

    private LocalDate expirationDate;

    @Builder.Default
    private BigDecimal stockQuantity = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    @Builder.Default
    private Boolean active = true;

    /**
     * Retorna o preço final de venda considerando o desconto aplicado ao cadastro do produto.
     */
    public BigDecimal getFinalPrice() {
        if (this.salePrice == null) {
            return BigDecimal.ZERO;
        }
        if (this.discount == null || this.discount.compareTo(BigDecimal.ZERO) <= 0) {
            return this.salePrice;
        }
        BigDecimal priceWithDiscount = this.salePrice.subtract(this.discount);
        return priceWithDiscount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : priceWithDiscount;
    }

    /**
     * Verifica se o produto é vendido por peso/balança com base na unidade da categoria.
     */
    public boolean isByWeight() {
        return this.category != null && "KG".equalsIgnoreCase(this.category.getUnit());
    }
}
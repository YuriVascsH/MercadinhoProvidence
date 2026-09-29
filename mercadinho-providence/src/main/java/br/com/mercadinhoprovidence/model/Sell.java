package br.com.mercadinhoprovidence.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Sell {

    private Integer idSell;

    @Builder.Default
    private LocalDateTime sellHour = LocalDateTime.now();

    private Integer idEmployeer;

    @Builder.Default
    private BigDecimal subTotalValue = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal totalValue = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal discountValue = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal valuePay = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal change = BigDecimal.ZERO;

    @Builder.Default
    private List<ItemSell> saleItens = new ArrayList<>();

    @Builder.Default
    private List<PaymentSell> paymentMethods = new ArrayList<>();

    public void addPayment(PaymentSell payment) {
        if (payment == null) {
            return;
        }
        this.paymentMethods.add(payment);
        recalcularPagamentos();
    }

    public void recalcularPagamentos() {
        this.valuePay = this.paymentMethods.stream()
                .map(PaymentSell::getValuePay)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (this.valuePay.compareTo(this.totalValue) > 0) {
            this.change = this.valuePay.subtract(this.totalValue);
        } else {
            this.change = BigDecimal.ZERO;
        }
    }

    public BigDecimal getSaldoRestante() {
        BigDecimal restante = this.totalValue.subtract(this.valuePay);
        return restante.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : restante;
    }

    public boolean isTotalmentePaga() {
        return this.valuePay.compareTo(this.totalValue) >= 0;
    }


    public void addItem(ItemSell item) {
        if (item == null) {
            return;
        }
        item.setNumberItem(this.saleItens.size() + 1);
        this.saleItens.add(item);
        recalcularTotais();
    }

    public boolean removeItemByNumber(int numberItem) {
        boolean removed = this.saleItens.removeIf(item -> item.getNumberItem() == numberItem);
        if (removed) {
            for (int i = 0; i < this.saleItens.size(); i++) {
                this.saleItens.get(i).setNumberItem(i + 1);
            }
            recalcularTotais();
        }
        return removed;
    }

    public void recalcularTotais() {
        this.subTotalValue = this.saleItens.stream()
                .map(ItemSell::getTotalItemValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalValue = this.subTotalValue.subtract(
                this.discountValue != null ? this.discountValue : BigDecimal.ZERO
        );

        if (this.totalValue.compareTo(BigDecimal.ZERO) < 0) {
            this.totalValue = BigDecimal.ZERO;
        }
        
        recalcularPagamentos();
    }

    public List<ItemSell> getSaleItens() {
        return Collections.unmodifiableList(this.saleItens);
    }

    public List<PaymentSell> getPaymentMethods() {
        return Collections.unmodifiableList(this.paymentMethods);
    }
}
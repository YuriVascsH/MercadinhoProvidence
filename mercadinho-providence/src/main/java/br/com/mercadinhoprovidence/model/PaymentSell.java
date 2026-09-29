package br.com.mercadinhoprovidence.model;

import java.math.BigDecimal;

import br.com.mercadinhoprovidence.model.enums.PaymentMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class PaymentSell {
    
    private Integer idPayment;
    private PaymentMethodEnum paymentMethod;
    private BigDecimal valuePay;
    
}

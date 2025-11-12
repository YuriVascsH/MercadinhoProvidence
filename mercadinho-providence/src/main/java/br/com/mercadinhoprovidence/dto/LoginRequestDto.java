package br.com.mercadinhoprovidence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    private Integer id;
    private String senha;
}

package br.com.mercadinhoprovidence.dto.login;

import br.com.mercadinhoprovidence.model.enums.JobTitle;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class LoginResponseDto {

    private String name;
    private Integer codeVerify;
    private JobTitle postion;

}

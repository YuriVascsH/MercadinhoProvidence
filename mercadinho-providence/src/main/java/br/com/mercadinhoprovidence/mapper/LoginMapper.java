package br.com.mercadinhoprovidence.mapper;

import br.com.mercadinhoprovidence.dto.login.LoginResponseDto;
import br.com.mercadinhoprovidence.model.Employee;

public class LoginMapper {

    private LoginMapper() {
        throw new UnsupportedOperationException("Está é uma classe utilitária e não deve ser instaciada.");
    }

   /**
    * Converte a classe funcionario para LoginResponseDto.
    *
    * @param funcionario que vem do metodo validarCredenciais.
    * @return O funcionário com os dados específicos.
    * */
    public static LoginResponseDto toLoginResponseDto(Employee employee) {
        if (employee == null) {
            return null;
        }

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setName(employee.getNome());
        loginResponseDto.setCodigoVerificador(employee.getCodigoVerificador());
        loginResponseDto.setCargo(employee.getCargo());
        return loginResponseDto;

    }
}

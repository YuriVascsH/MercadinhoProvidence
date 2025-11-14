package br.com.mercadinhoprovidence.controller;

import br.com.mercadinhoprovidence.Service.FuncionarioService;
import br.com.mercadinhoprovidence.dto.FuncionarioTableDto;

import java.util.List;

public class FuncionarioController {

    FuncionarioService funcionarioService = new FuncionarioService();

    public FuncionarioTableDto buscarPorCpf(String cpf) {
        return funcionarioService.buscarPorCpf(cpf);
    }

    public List<FuncionarioTableDto> listarTodos() {
        return funcionarioService.buscarTodosOsFuncionarios();
    }

    public Boolean deletarFuncionario(Integer idFuncionario) {
        return funcionarioService.deletarFuncionario(idFuncionario);
    }
}

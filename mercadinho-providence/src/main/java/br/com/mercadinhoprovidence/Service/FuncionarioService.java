package br.com.mercadinhoprovidence.Service;

import br.com.mercadinhoprovidence.dao.FuncionarioDao;
import br.com.mercadinhoprovidence.dto.FuncionarioTableDto;
import br.com.mercadinhoprovidence.model.Funcionario;

import java.util.List;

public class FuncionarioService {

    private final FuncionarioDao funcionarioDao = new FuncionarioDao();

    public FuncionarioTableDto buscarPorCpf(String cpf) {
        Funcionario funcionario = funcionarioDao.buscarPorCpf(cpf);
        if (funcionario == null) {
            return null;
        }
        FuncionarioTableDto funcionarioTableDto = new FuncionarioTableDto(funcionario);
        return funcionarioTableDto;
    }

    public List<FuncionarioTableDto> buscarTodosOsFuncionarios() {
        List<Funcionario> funcionarios = funcionarioDao.listarTodos();
        return funcionarios.stream().map(funcionario -> new FuncionarioTableDto()).toList();
    }

    public Boolean deletarFuncionario(Integer idFuncionario) {
        return funcionarioDao.deletar(idFuncionario);
    }


}

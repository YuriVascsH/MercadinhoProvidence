package br.com.mercadinhoprovidence.Service;

import br.com.mercadinhoprovidence.dao.FuncionarioDao;
import br.com.mercadinhoprovidence.dto.Funcionario.FuncionarioCreateDto;
import br.com.mercadinhoprovidence.dto.Funcionario.FuncionarioResponseDto;
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
        return new FuncionarioTableDto(funcionario);
    }

    public List<FuncionarioTableDto> buscarTodosOsFuncionarios() {
        List<Funcionario> funcionarios = funcionarioDao.listarTodos();
        return funcionarios.stream().map(funcionario -> new FuncionarioTableDto()).toList();
    }

    public boolean verificarCpfExistente(String cpf) {
        return funcionarioDao.verificarCpfExistente(cpf);
    }

    public FuncionarioResponseDto salvarFuncionario(FuncionarioCreateDto funcionarioCreateDto) {
        Funcionario funcionario = new Funcionario();
        funcionario.setCodigoVerificador(funcionarioCreateDto.getCodigoVerificador());
        funcionario.setCpf(funcionarioCreateDto.getCpf());
        funcionario.setNome(funcionarioCreateDto.getNome());
        funcionario.setDataNascimento(funcionarioCreateDto.getDataNascimento());
        funcionario.setTelefone(funcionarioCreateDto.getTelefone());
        funcionario.setEmail(funcionarioCreateDto.getEmail());
        funcionario.setEndereco(funcionarioCreateDto.getEndereco());
        funcionario.setDataAdmissao(funcionarioCreateDto.getDataAdmissao());
        funcionario.setCargo(funcionarioCreateDto.getCargo());
        funcionario.setSalario(funcionarioCreateDto.getSalario());
        funcionario.setSenha(funcionarioCreateDto.getSenha());
        funcionario.setAtivo(funcionarioCreateDto.getAtivo());
        funcionario.setUltimaVenda(funcionarioCreateDto.getUltimaVenda());

        Funcionario funcionarioSalvo = funcionarioDao.inserir(funcionario);

        return new FuncionarioResponseDto(funcionarioSalvo.getNome(), funcionarioSalvo.getCodigoVerificador());
    }

    public Boolean deletarFuncionario(Integer idFuncionario) {
        return funcionarioDao.deletar(idFuncionario);
    }


}

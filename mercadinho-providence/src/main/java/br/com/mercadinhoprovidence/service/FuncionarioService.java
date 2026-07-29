package br.com.mercadinhoprovidence.service;

import br.com.mercadinhoprovidence.dao.FuncionarioDao;
import br.com.mercadinhoprovidence.dto.funcionario.FuncionarioCreateDto;
import br.com.mercadinhoprovidence.dto.funcionario.FuncionarioResponseDto;
import br.com.mercadinhoprovidence.dto.funcionario.FuncionarioTableDto;
import br.com.mercadinhoprovidence.dto.funcionario.FuncionarioUpdateDto;
import br.com.mercadinhoprovidence.model.Funcionario;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FuncionarioService {

    private final FuncionarioDao funcionarioDao;

    public FuncionarioService() {
        this.funcionarioDao = new FuncionarioDao();
    }

    public FuncionarioService(FuncionarioDao funcionarioDao) {
        this.funcionarioDao = funcionarioDao;
    }

    public FuncionarioTableDto buscarPorCpf(String cpf) {
        Funcionario funcionario = funcionarioDao.buscarPorCpf(cpf);
        if (funcionario == null) {
            return null;
        }
        return new FuncionarioTableDto(funcionario);
    }

    public List<FuncionarioTableDto> buscarTodosOsFuncionarios() {
        List<Funcionario> funcionarios = funcionarioDao.listarTodos();
        return funcionarios.stream().map(FuncionarioTableDto::new).toList();
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

    public Funcionario buscarPeloId(Integer id) {
        return funcionarioDao.buscarPorId(id);
    }

    public Optional<FuncionarioResponseDto> atualizar(Integer id, FuncionarioUpdateDto funcionarioUpdateDto) throws SQLException {
        Funcionario funcionario = funcionarioDao.buscarPorId(id);
        funcionario.setNome(funcionarioUpdateDto.getNome());
        funcionario.setTelefone(funcionarioUpdateDto.getTelefone());
        funcionario.setEmail(funcionarioUpdateDto.getEmail());
        funcionario.setEndereco(funcionarioUpdateDto.getEndereco());
        funcionario.setCargo(funcionarioUpdateDto.getCargo());
        funcionario.setSalario(funcionarioUpdateDto.getSalario());
        funcionario.setAtivo(funcionarioUpdateDto.getAtivo());

        return funcionarioDao.atualizar(funcionario);
    }
}

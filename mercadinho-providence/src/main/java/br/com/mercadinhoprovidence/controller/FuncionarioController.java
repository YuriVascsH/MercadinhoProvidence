package br.com.mercadinhoprovidence.controller;

import br.com.mercadinhoprovidence.dto.funcionario.FuncionarioCreateDto;
import br.com.mercadinhoprovidence.dto.funcionario.FuncionarioResponseDto;
import br.com.mercadinhoprovidence.dto.funcionario.FuncionarioTableDto;
import br.com.mercadinhoprovidence.dto.funcionario.FuncionarioUpdateDto;
import br.com.mercadinhoprovidence.model.Funcionario;
import br.com.mercadinhoprovidence.service.FuncionarioService;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class FuncionarioController {

    private FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    /**
     * Função que busca um funcionario no banco a partir do cpf informado e retorana um FuncionarioTableDto
     *
     * @param cpf informado pelo user na barra de pesquisa
     *
     * @return o funcionarioTableDto contendo as informações cadastradas no banco
     * */
    public FuncionarioTableDto buscarPorCpf(String cpf) {
        return funcionarioService.buscarPorCpf(cpf);
    }

    /**
     * Função que buscca no banco retorna uma lista de funcionariosTableDto para popular a tabela da view
     *
     * @return uma lista contendo FuncionarioTableDto
     * */
    public List<FuncionarioTableDto> listarTodos() {
        return funcionarioService.buscarTodosOsFuncionarios();
    }

    /**
     * Função que retorna um valor booleano para deletar um funcionario do banco de dados
     * REVISAR
     * @param idFuncionario insreido ao ser clicado no botão de deletar
     *
     * @return valor booleano para disparar o alerta de sucesso
     * */
    public Boolean deletarFuncionario(Integer idFuncionario) {
        return funcionarioService.deletarFuncionario(idFuncionario);
    }

    /**
     * Função que retorna um FuncionarioResponseDto para a view, responsável pela verificação dos campos null dos atriubytos
     * Data de nascimento, Data de admissão e salário.
     *
     * @param funcionarioCreateDto inforamdo pelo user contendo os dados do funcionario a ser salvo no banco de dados
     *
     * @return funcionarioResponseDto contendo as informações de criação do funcionário
     */
    public FuncionarioResponseDto salvarFuncionario(FuncionarioCreateDto funcionarioCreateDto) {
        if (funcionarioCreateDto.getDataAdmissao() == null) {
            funcionarioCreateDto.setDataAdmissao(LocalDate.now());
        }
        if(funcionarioCreateDto.getDataNascimento() == null) {
            funcionarioCreateDto.setDataNascimento(LocalDate.of(2000, 1, 1));
        }
        if (funcionarioCreateDto.getSalario() == null) {
            funcionarioCreateDto.setSalario(BigDecimal.ZERO);
        }
        return funcionarioService.salvarFuncionario(funcionarioCreateDto);
    }

    /**
     * Funação que retorna um valor booleano para verificar se já existe um Funcionário cadastrado com o cpf informado
     * pelo user
     *
     * @param cpf valor informado pelo usuario para a verificação do cpf
     *
     * @return valor verdadeiro ou falso de acordo com o resultado da pesquisa
     */
    public boolean verificarCpfExistente(String cpf) {
        return funcionarioService.verificarCpfExistente(cpf);
    }
    /**
     * Função que busca um funcionario pelo seu id e retorna o funcionario correspondente
     *
     * @param id informado pelo funcionario
     * @return funcionario correspondente da busca pelo id
     */
    public Funcionario buscarPeloId(Integer id) {
        return funcionarioService.buscarPeloId(id);
    }

    /**
     * Função que atualiza os dados do funcionario
     *
     * @param id do funcionario informado pelo funcionario
     * @param funcionarioUpdateDto os dados do funcionario encapsulado
     *
     * @return um Optional contendo o funcionarioResponseDto
     */
    public Optional<FuncionarioResponseDto> atualizar(Integer id, FuncionarioUpdateDto funcionarioUpdateDto) throws SQLException {
        return funcionarioService.atualizar(id, funcionarioUpdateDto);
    }
}

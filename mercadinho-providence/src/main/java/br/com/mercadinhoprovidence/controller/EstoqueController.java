package br.com.mercadinhoprovidence.controller;

import java.util.List;

import br.com.mercadinhoprovidence.dao.EstoqueDao;
import br.com.mercadinhoprovidence.model.LoteEstoque;
import br.com.mercadinhoprovidence.service.EstoqueService;

public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController() {
        this.estoqueService = new EstoqueService(new EstoqueDao());
    }

    /**
     * Insere um novo estoque.
     *
     * @param estoque estoque a ser inserido
     */
    public void inserir(LoteEstoque estoque) {
        estoqueService.inserir(estoque);
    }

    /**
     * Lista todos os estoques cadastrados.
     *
     * @return lista de estoques
     */
    public List<LoteEstoque> listarTodos() {
        return estoqueService.listarTodos();
    }

    /**
     * Busca um estoque pelo ID.
     *
     * @param id identificador do estoque
     * @return estoque encontrado ou null
     */
    public LoteEstoque buscarPorId(Integer id) {
        return estoqueService.buscarPorId(id);
    }

    /**
     * Retorna a quantidade de produtos cadastrados no estoque.
     *
     * @param idEstoque identificador do estoque
     * @return quantidade de produtos
     */
    public int produtosCadastradosNoEstoque(Integer idEstoque) {
        return estoqueService.produtosCadastradosNoEstoque(idEstoque);
    }
}
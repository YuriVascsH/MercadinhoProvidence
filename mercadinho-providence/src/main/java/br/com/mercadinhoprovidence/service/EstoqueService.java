package br.com.mercadinhoprovidence.service;

import java.util.List;

import br.com.mercadinhoprovidence.dao.EstoqueDao;
import br.com.mercadinhoprovidence.model.LoteEstoque;

public class EstoqueService {

    private final EstoqueDao estoqueDao;

    public EstoqueService(EstoqueDao estoqueDao) {
        this.estoqueDao = estoqueDao;
    }

    /**
     * Insere um novo estoque.
     *
     * @param estoque estoque a ser inserido
     */
    public void inserir(LoteEstoque estoque) {
        if (estoque == null) {
            throw new IllegalArgumentException("O estoque não pode ser nulo.");
        }

        estoqueDao.inserir(estoque);
    }

    /**
     * Retorna todos os estoques cadastrados.
     *
     * @return lista de estoques
     */
    public List<LoteEstoque> listarTodos() {
        return estoqueDao.listarTodos();
    }

    /**
     * Busca um estoque pelo ID.
     *
     * @param id identificador do estoque
     * @return estoque encontrado ou null
     */
    public LoteEstoque buscarPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID do estoque deve ser válido.");
        }

        return estoqueDao.buscarPorId(id);
    }

    /**
     * Retorna a quantidade de produtos cadastrados no estoque.
     *
     * @param idEstoque identificador do estoque
     * @return quantidade de produtos
     */
    public int produtosCadastradosNoEstoque(Integer idEstoque) {
        if (idEstoque == null || idEstoque <= 0) {
            throw new IllegalArgumentException("O ID do estoque deve ser válido.");
        }

        return estoqueDao.produtosCadastradosNoEstoque(idEstoque);
    }
}
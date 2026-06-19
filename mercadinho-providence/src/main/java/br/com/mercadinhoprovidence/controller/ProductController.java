package br.com.mercadinhoprovidence.controller;

import java.util.List;

import br.com.mercadinhoprovidence.Service.ProductService;
import br.com.mercadinhoprovidence.dto.produto.ProductTableDto;
import br.com.mercadinhoprovidence.dto.produto.ProductUpdateDto;

public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService produtoService) {
        this.productService = produtoService;
    }

    /**
     *  Método responável por realizar a consulta de um produto pelo código de barras. 
     *  
     * @param code Código de barras fornecido pelo funcionário.
     * @return retorna um produto na tabela.
     */
    public ProductTableDto findByCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Código do produto não informado");
        }

        if(!code.matches("\\d+")) {
            throw new IllegalArgumentException("Código de barras deve conter apenas números."); 
        }
            
        Long codeLong = Long.parseLong(code.trim());
        return productService.findByCode(codeLong);
        
    }

    /**
     * Método responsável por relaizar a busca de todos os produtos cadastrados
     * 
     * @return retorna uma lista contendo todos os produtos
     */
    public List<ProductTableDto> findAll() {
        return productService.findAll();
    } 

    /**
     * Método responsável por atualizar as informações do produto.
     * 
     * @param id id do produto cadastrado no sistema;
     * @param produtoUpdateDto informações encapsulada passada pelo funcionário;
     * @return um Optional contendo a reposta de atualização.
     */
    public ProductTableDto update(Integer id, ProductUpdateDto produtoUpdateDto) {
        return productService.update(id, produtoUpdateDto);
    }

    /**
     * Método responsável por ativat/desativar um produto 
     * 
     * @param id do produto cadastrado no sistema
     * @param active boolean informando se ele está ativo ou desativado
     */
    public void updateStatus(Integer id, boolean active) {
        productService.updateStatus(id, active);
    }


}

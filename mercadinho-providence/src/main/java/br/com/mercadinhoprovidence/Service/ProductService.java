package br.com.mercadinhoprovidence.Service;

import java.math.BigDecimal;
import java.util.List;

import br.com.mercadinhoprovidence.dao.ProductDao;
import br.com.mercadinhoprovidence.dto.produto.ProductCreateDto;
import br.com.mercadinhoprovidence.dto.produto.ProductTableDto;
import br.com.mercadinhoprovidence.dto.produto.ProductUpdateDto;
import br.com.mercadinhoprovidence.exceptions.BusinessException;
import br.com.mercadinhoprovidence.mapper.ProductMapper;
import br.com.mercadinhoprovidence.model.Product;
import br.com.mercadinhoprovidence.model.enums.Categoria;

public class ProductService {

    private final ProductDao productDao;

    private static final String DESCRICAO_PADRAO = "Descrição não informada";

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * Métotodo reponsável por buscar o produto no banco de dados.
     * 
     * @param code Código recebido pelo controller;
     * @return o produto mapeado para ProductTableDto.
     */
    public ProductTableDto findByCode(Long code) {

        if (code == null || code <= 0) {
            throw new BusinessException("Código do produto fornecido inválido");
        }

        Product product = productDao.findByCode(code)
                .orElseThrown(() -> new EntityNotFoundException("Produto não encontrado"));

        return ProductMapper.toTableDto(product);
    }

    /**
     * Método responsável por realizar a busca do produso na vase de dados.
     * 
     * @return retorna uma lista contendo produtoTableDto
     */
    public List<ProductTableDto> findAll() {
        List<Product> products = productDao.findAll();
        return products.stream().map(ProductMapper::toTableDto).toList();
    }

    /**
     * Método responsável por realizar a atualização dos dados dos produtos.
     * 
     * @param id               do produto fornecido pelo funcionário.
     * @param productUpdateDto dados para atualização do produto
     * @return retorna o produto na tabela
     */
    public ProductTableDto update(Integer id, ProductUpdateDto productUpdateDto) {
        Product product = productDao.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado"));

        validateProductUpdatePrice(productUpdateDto);

        // updates
        product.setNome(productUpdateDto.getNome());

        if (productUpdateDto.getDescricao() != null) {
            if (productUpdateDto.getDescricao().strip().isEmpty()) {
                product.setDescricao(DESCRICAO_PADRAO);
            } else {
                product.setDescricao(productUpdateDto.getDescricao());
            }
        }
        //
        if (productUpdateDto.getCategoria() == Categoria.PESO) {
            product.setPrecoPorKg(productUpdateDto.getPrecoPorKg());
        } else {
            product.setPrecoUnitario(productUpdateDto.getPrecoUnitario());
        }

        // falta realizar essa última verificação aqui
        product.setControlaEstoque(productUpdateDto.getControlaEstoque());

        productDao.save(product);

        return ProductMapper.toTableDto(product);
    }

    /**
     * Método responsável por realizar o cadastro e as validações necessárias para o
     * registro do produto
     * 
     * @param productCreateDto Informações do produto.
     */
    public void createProduct(ProductCreateDto productCreateDto) {
        if (productDao.existsBycode(productCreateDto.getCodigoBarras())) {
            throw new BusinessException("Produto já cadastrado.");
        }

        Product product = ProductMapper.toProduct(productCreateDto);
        validateProduct(product);
        productDao.save(product);
        // Ainda estou em dúvida se vai retornar algo aqui
        return;

    }

    /**
     * Método responsável por alterar caracterísitica para ativo do produto
     * 
     * @param id identificador fornecido do produto.
     */
    public void activateProduct(Integer id) {
        Product product = productDao.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado"));

        if (Boolean.TRUE.equals(product.getAtivo())) {
            throw new BusinessException("Produto já está ativo");
        }

        product.setAtivo(true);
        productDao.save(product);
    }

    /**
     * Método reponsável por alterar característica do produto para desativado
     * 
     * @param id identificador fornecido do produto.
     */
    public void desactiveteProduct(Integer id) {
        Product product = productDao.findById(id)
                .orElseThrow(() -> new BusinessException("Produto não encontrado em nosso sistema"));

        product.setActive(false);
        productDao.save(product);

    }

    /**
     * Método auxiliar para a relazar a validação das informações do produto
     * 
     * @param product vindo da função de crete
     */
    private void validateProduct(Product product) {
        if (product.getCategoria() == Categoria.UNIDADE) {

            if (product.getPrecoUnitario() == null) {
                throw new BusinessException("Produto por unidade precisa de preço unitário");
            }

            validatePrice(product.getPrecoPorKg(), "Preço unitário deve ser maior que zero");

        }

        if (product.getCategoria() == Categoria.PESO) {

            if (product.getPrecoPorKg() == null) {
                throw new BusinessException("Produto por peso precisa de preço por Kg");
            }

            validatePrice(product.getPrecoPorKg(), "Preço por kg deve ser maior que zero");

        }

    }

    /**
     * Método responsável por realizar a verificação de valores
     * 
     * @param value   preço que será usado para comparar.
     * @param message Erro que será informado.
     */
    private void validatePrice(BigDecimal value, String message) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(message);
        }
    }

    /**
     * 
     * @param productUpdateDto
     */
    private void validateProductUpdatePrice(ProductUpdateDto productUpdateDto) {
        validatePrice(productUpdateDto.getPrecoPorKg(), "Preço unitário deve ser maior que zero");
        validatePrice(productUpdateDto.getPrecoUnitario(), "Preço por kg deve ser maior que zero");
        validateProductUpdate(productUpdateDto);
    }

    /**
     * 
     * @param productUpdateDto
     */
    private void validateProductUpdate(ProductUpdateDto productUpdateDto) {

        if (productUpdateDto.getNome() != null && productUpdateDto.getNome().strip().length() > 75) {
            throw new BusinessException("Nome ultrapassa o limite de 75 caracteres");
        }

        if (productUpdateDto.getNome().isEmpty()) {
            throw new BusinessException("Nome do produto não pode ser vazio");
        }

        if (productUpdateDto.getDescricao() != null && productUpdateDto.getDescricao().strip().length() > 150) {
            throw new BusinessException("Descrição ultrapassa o limite de 150 caracteres");
        }

        if (productUpdateDto.getCategoria() != null && (productUpdateDto.getCategoria() == Categoria.PESO
                && productUpdateDto.getCategoria() == Categoria.UNIDADE)) {
            throw new BusinessException("A categoria informada não é válida.");
        }
    }

}

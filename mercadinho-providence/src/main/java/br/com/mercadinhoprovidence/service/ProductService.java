// package br.com.mercadinhoprovidence.service;

// import java.math.BigDecimal;
// import java.util.List;

// import br.com.mercadinhoprovidence.dao.ProductDao;
// import br.com.mercadinhoprovidence.dto.produto.ProductCreateDto;
// import br.com.mercadinhoprovidence.dto.produto.ProductTableDto;
// import br.com.mercadinhoprovidence.dto.produto.ProductUpdateDto;
// import br.com.mercadinhoprovidence.exceptions.BusinessException;
// import br.com.mercadinhoprovidence.mapper.ProductMapper;
// import br.com.mercadinhoprovidence.model.Product;
// import br.com.mercadinhoprovidence.model.enums.Category;

// public class ProductService {

//     private final ProductDao productDao;

//     private static final String DESCRICAO_PADRAO = "Descrição não informada";

//     public ProductService(ProductDao productDao) {
//         this.productDao = productDao;
//     }

//     /**
//      * Método responsável por buscar o produto no banco de dados.
//      * 
//      * @param code Código recebido pelo controller;
//      * @return o produto mapeado para ProductTableDto.
//      */
//     public ProductTableDto findByCode(String code) {

//         if (code == null || code.isEmpty())
//             throw new BusinessException("Código do produto fornecido inválido");

//         Product product = productDao.findByCode(code)
//                 .orElseThrow(() -> new BusinessException("Produto não encontrado"));

//         return ProductMapper.toTableDto(product);
//     }

//     /**
//      * Método auxiliar para outras services (como LoteEstoque) buscarem o produto
//      * por ID.
//      */
//     public Product findById(Integer id) {
//         if (id == null || id <= 0) {
//             throw new BusinessException("ID do produto inválido.");
//         }
//         return productDao.findById(id)
//                 .orElseThrow(() -> new BusinessException("Produto não encontrado"));
//     }

//     /**
//      * Método responsável por realizar a busca do produto na base de dados.
//      * TODO: Alterar para paginação
//      * 
//      * @return retorna uma lista contendo produtoTableDto
//      */
//     public List<ProductTableDto> findAll() {
//         List<Product> products = productDao.findAll();
//         return products.stream().map(ProductMapper::toTableDto).toList();
//     }

//     /**
//      * Método responsável por realizar a atualização dos dados dos produtos.
//      * 
//      * @param id               do produto fornecido pelo funcionário.
//      * @param productUpdateDto dados para atualização do produto
//      * @return retorna o produto na tabela
//      */
//     public ProductTableDto update(Integer id, ProductUpdateDto productUpdateDto) {
//         Product product = productDao.findById(id)
//                 .orElseThrow(() -> new BusinessException("Produto não encontrado"));

//         validateProductFields(
//                 productUpdateDto.getNome(),
//                 productUpdateDto.getDescricao(),
//                 productUpdateDto.getCategoria());

//         // updates
//         product.setNome(productUpdateDto.getNome());

//         if (productUpdateDto.getDescricao() != null) {
//             if (productUpdateDto.getDescricao().strip().isEmpty()) {
//                 product.setDescricao(DESCRICAO_PADRAO);
//             } else {
//                 product.setDescricao(productUpdateDto.getDescricao());
//             }
//         }

//         if (productUpdateDto.getCategoria() != null) {
//             product.setCategoria(productUpdateDto.getCategoria());
//         }

//         if (productUpdateDto.getPreco() != null) {
//             validatePrice(productUpdateDto.getPreco());
//             product.setPrecoVenda(productUpdateDto.getPreco());
//         }

//         productDao.save(product);

//         return ProductMapper.toTableDto(product);
//     }

//     /**
//      * Método responsável por realizar o cadastro e as validações necessárias para o
//      * registro do produto
//      * 
//      * @param productCreateDto Informações do produto.
//      */
//     public void createProduct(ProductCreateDto productCreateDto) {
//         if (productDao.existsByCode(productCreateDto.getCodigoBarras())) 
//             throw new BusinessException("Produto já cadastrado.");
        

//         Product product = ProductMapper.toProduct(productCreateDto);

//         validateProductFields(
//                 productCreateDto.getNome(),
//                 productCreateDto.getDescricao(),
//                 productCreateDto.getCategoria());

//         validatePrice(product.getPrecoVenda());

//         product.setActive(true);
//         productDao.save(product);
//     }

//     /**
//      * Método responsável por alterar característica para ativo do produto
//      * 
//      * @param id identificador fornecido do produto.
//      */
//     public void activateProduct(Integer id) {
//         Product product = productDao.findById(id)
//                 .orElseThrow(() -> new BusinessException("Produto não encontrado"));

//         if (Boolean.TRUE.equals(product.getActive())) {
//             throw new BusinessException("Produto já está ativo");
//         }

//         product.setActive(true);
//         productDao.save(product);
//     }

//     /**
//      * Método responsável por alterar característica do produto para desativado
//      * 
//      * @param id identificador fornecido do produto.
//      */
//     public void deactivateProduct(Integer id) {
//         Product product = productDao.findById(id)
//                 .orElseThrow(() -> new BusinessException("Produto não encontrado em nosso sistema"));

//         product.setActive(false);
//         productDao.save(product);
//     }

//     /**
//      * Método responsável por realizar a verificação de valores
//      * 
//      * @param value preço que será usado para comparar.
//      */
//     private void validatePrice(BigDecimal value) {
//         if (value == null) {
//             throw new BusinessException("O preço do produto é obrigatório.");
//         }
//         if (value.compareTo(BigDecimal.ZERO) <= 0) {
//             throw new BusinessException("O preço do produto deve ser maior que zero.");
//         }
//     }

//     /*
//      * Método genérico para validar as regras de negócio de campos de texto e
//      * categoria,
//      * reutilizado tanto na criação quanto na atualização.
//      */
//     private void validateProductFields(String nome, String descricao, Category categoria) {

//         if (nome != null) {
//             if (nome.strip().isEmpty()) {
//                 throw new BusinessException("Nome do produto não pode ser vazio");
//             }
//             if (nome.strip().length() > 75) {
//                 throw new BusinessException("Nome ultrapassa o limite de 75 caracteres");
//             }
//         } else {
//             throw new BusinessException("Nome do produto é obrigatório");
//         }

//         // Validações da Descrição (opcional, mas se informada, deve respeitar o limite)
//         if (descricao != null && descricao.strip().length() > 150) {
//             throw new BusinessException("Descrição ultrapassa o limite de 150 caracteres");
//         }

//         // Validações da Categoria
//         if (categoria != null && (categoria != Category.PESO && categoria != Category.UNIDADE)) {
//             throw new BusinessException("A categoria informada não é válida.");
//         }
//     }

//     /**
//      * Método que ativa ou desativa o produto de acordo com o booleano recebido.
//      * 
//      * @param id     identificador do produto
//      * @param active status desejado
//      */
//     public void updateStatus(Integer id, boolean active) {
//         if (active) {
//             activateProduct(id);
//         } else {
//             deactivateProduct(id);
//         }
//     }
// }
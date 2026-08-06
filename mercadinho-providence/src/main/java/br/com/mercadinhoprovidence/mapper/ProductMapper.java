package br.com.mercadinhoprovidence.mapper;

import br.com.mercadinhoprovidence.dto.produto.ProductCreateDto;
import br.com.mercadinhoprovidence.dto.produto.ProductTableDto;
import br.com.mercadinhoprovidence.dto.produto.ProductUpdateDto;
import br.com.mercadinhoprovidence.model.Product;

public final class ProductMapper {

	private ProductMapper() {
	}

	/**
	 * Método responsável por realizar a conversão para produto da tabela
	 * 
	 * @param product informações do produto
	 * @return um produto formatado para a classe ProductTableDto
	 */
	public static ProductTableDto toTableDto(Product product) {
		return new ProductTableDto(product.getIdProduto(), product.getNome(), product.getCodigoDeBarras(),
				product.getCategoria(), product.getControlaEstoque(), product.getPrecoVenda(), product.getValidade(),
				product.getQuantOuPesoEmEstoque());
	}

	/**
	 * Método para realização da conversão de ProductCreateDto para Product
	 * 
	 * @param productCreateDto Informações contidas do produto
	 * @return Um produto formatado e com suas informações
	 */
	public static Product toProduct(ProductCreateDto productCreateDto) {
		return Product.builder().nome(productCreateDto.getNome()).codigoDeBarras(productCreateDto.getCodigoBarras())
				.descricao(productCreateDto.getDescricao()).categoria(productCreateDto.getCategoria())
				.controlaEstoque(productCreateDto.getControlaEstoque()).precoVenda(productCreateDto.getPrecoVenda())
				.validade(productCreateDto.getValidade())
				.quantOuPesoEmEstoque(productCreateDto.getQuantOuPesoEmEstoque()).build();
	}

	public static void updateProductFromDto(ProductUpdateDto dto, Product product) {

		if (dto.getNome() != null)
			product.setNome(dto.getNome());

		if (dto.getCodigoBarras() != null)
			product.setCodigoDeBarras(dto.getCodigoBarras());

		if (dto.getDescricao() != null)
			product.setDescricao(dto.getDescricao());

		if (dto.getCategoria() != null)
			product.setCategoria(dto.getCategoria());

		if (dto.getControlaEstoque() != null)
			product.setControlaEstoque(dto.getControlaEstoque());

		if (dto.getPrecoVenda() != null)
			product.setPrecoVenda(dto.getPrecoVenda());

		if (dto.getValidade() != null)
			product.setValidade(dto.getValidade());

		if (dto.getQuantOuPesoEmEstoque() != null)
			product.setQuantOuPesoEmEstoque(dto.getQuantOuPesoEmEstoque());
	}

}

package pl.edu.vistula.FirstRestAPI.product.service;

import org.springframework.stereotype.Service;
import pl.edu.vistula.FirstRestAPI.product.api.request.ProductRequest;
import pl.edu.vistula.FirstRestAPI.product.api.request.UpdateProductRequest;
import pl.edu.vistula.FirstRestAPI.product.api.response.ProductResponse;
import pl.edu.vistula.FirstRestAPI.product.domain.Product;
import pl.edu.vistula.FirstRestAPI.product.repository.ProductRepository;
import pl.edu.vistula.FirstRestAPI.product.support.ProductExceptionSupplier;
import pl.edu.vistula.FirstRestAPI.product.support.ProductMapper;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse create(ProductRequest productRequest) {
        Product product = productRepository.save(productMapper.toProduct(productRequest));
        return productMapper.toProductResponse(product);
    }

    public ProductResponse find(Long id) {
        Product product = findProduct(id);
        return productMapper.toProductResponse(product);
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse update(Long id, UpdateProductRequest updateProductRequest) {
        Product product = findProduct(id);
        Product updatedProduct = productMapper.toProduct(product, updateProductRequest);
        return productMapper.toProductResponse(productRepository.save(updatedProduct));
    }

    public void delete(Long id) {
        Product product = findProduct(id);
        productRepository.delete(product);
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductExceptionSupplier.productNotFound(id));
    }
}
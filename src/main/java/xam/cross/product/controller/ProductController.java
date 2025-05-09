package xam.cross.product.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import xam.cross.product.dto.ProductDto;
import xam.cross.product.entity.ProductEntity;
import xam.cross.product.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Value("${INSTANCE_ID:666}")
    String instanceId;

    @GetMapping("/ping/hello")
    public String hello() {
        return String.format("Hello from instance %s", instanceId);
    }

    @GetMapping
    public List<ProductDto> getAllProducts(HttpServletRequest request) {
        return productRepository.findAll().stream()
                .map(entity -> {
                    ProductDto productDto = new ProductDto();
                    productDto.setId(entity.getId());
                    productDto.setName(entity.getName());
                    productDto.setDescription(entity.getDescription());
                    productDto.setPrice(entity.getPrice());
                    productDto.setInventory(entity.getInventory());
                    return productDto;
                })
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(final @PathVariable String id) {
        Optional<ProductEntity> optionalEntity = productRepository.findById(id);
        if (optionalEntity.isPresent()) {
            ProductEntity productEntity = optionalEntity.get();
            ProductDto productDto = new ProductDto();
            productDto.setId(productEntity.getId());
            productDto.setName(productEntity.getName());
            productDto.setDescription(productEntity.getDescription());
            productDto.setPrice(productEntity.getPrice());
            productDto.setInventory(productEntity.getInventory());
            return ResponseEntity.ok().body(productDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Transactional
    public ProductDto createProduct(@RequestBody ProductDto product) {
        ProductEntity entity = new ProductEntity(product.getName(), product.getDescription(), product.getPrice());
        entity.setInventory(product.getInventory());
        productRepository.save(entity);
        product.setId(entity.getId());
        return product;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deleteProductById(final @PathVariable String id) {
        productRepository.deleteById(id);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<ProductDto> updateProduct(final @PathVariable String id, final @RequestBody ProductDto updatedProduct) {
        Optional<ProductEntity> optionalEntity = productRepository.findById(id);
        if (optionalEntity.isPresent()) {
            ProductEntity productEntity = optionalEntity.get();
            productEntity.setName(updatedProduct.getName() != null ? updatedProduct.getName() : productEntity.getName());
            productEntity.setDescription(updatedProduct.getDescription() != null ? updatedProduct.getDescription() : productEntity.getDescription());
            productEntity.setPrice(updatedProduct.getPrice() == 0 ? productEntity.getPrice() : updatedProduct.getPrice());
            productEntity.setInventory(updatedProduct.getInventory() == 0 ? productEntity.getInventory() : updatedProduct.getInventory());
            productRepository.save(productEntity);
            ProductDto dto = new ProductDto();
            dto.setId(id);
            dto.setName(productEntity.getName());
            dto.setDescription(productEntity.getDescription());
            dto.setPrice(productEntity.getPrice());
            dto.setInventory(productEntity.getInventory());
            return ResponseEntity.ok().body(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

package xam.cross.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import xam.cross.product.entity.ProductEntity;

@Repository
public interface ProductRepository extends MongoRepository<ProductEntity, String> {

}

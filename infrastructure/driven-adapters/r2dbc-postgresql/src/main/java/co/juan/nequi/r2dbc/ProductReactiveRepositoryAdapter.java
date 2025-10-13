package co.juan.nequi.r2dbc;

import co.juan.nequi.model.product.Product;
import co.juan.nequi.model.product.gateways.ProductRepository;
import co.juan.nequi.r2dbc.entity.ProductEntity;
import co.juan.nequi.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ProductReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Product,
        ProductEntity,
        Long,
        ProductReactiveRepository
        > implements ProductRepository {
    public ProductReactiveRepositoryAdapter(ProductReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Product.class));
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        return save(product);
    }

    @Override
    public Mono<Product> findProductByName(String name) {
        return repository.findByName(name);
    }
}

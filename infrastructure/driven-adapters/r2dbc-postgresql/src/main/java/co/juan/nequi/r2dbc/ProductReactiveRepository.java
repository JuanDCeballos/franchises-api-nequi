package co.juan.nequi.r2dbc;

import co.juan.nequi.model.product.Product;
import co.juan.nequi.r2dbc.entity.ProductEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductReactiveRepository extends ReactiveCrudRepository<ProductEntity, Long>,
        ReactiveQueryByExampleExecutor<ProductEntity> {

    Mono<Product> findByName(String name);
}

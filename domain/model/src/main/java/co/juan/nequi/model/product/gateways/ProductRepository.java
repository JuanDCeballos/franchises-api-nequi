package co.juan.nequi.model.product.gateways;

import co.juan.nequi.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<Product> saveProduct(Product product);

    Mono<Product> findProductByName(String name);
}

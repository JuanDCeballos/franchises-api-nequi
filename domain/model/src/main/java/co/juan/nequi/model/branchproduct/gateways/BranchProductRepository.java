package co.juan.nequi.model.branchproduct.gateways;

import co.juan.nequi.model.branchproduct.BranchProduct;
import reactor.core.publisher.Mono;

public interface BranchProductRepository {

    Mono<BranchProduct> saveBranchProduct(BranchProduct branchProduct);
}

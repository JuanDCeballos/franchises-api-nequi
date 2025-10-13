package co.juan.nequi.r2dbc;

import co.juan.nequi.model.branchproduct.BranchProduct;
import co.juan.nequi.model.branchproduct.gateways.BranchProductRepository;
import co.juan.nequi.r2dbc.entity.BranchProductEntity;
import co.juan.nequi.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class BranchProductReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        BranchProduct,
        BranchProductEntity,
        Long,
        BranchProductReactiveRepository
        > implements BranchProductRepository {
    public BranchProductReactiveRepositoryAdapter(BranchProductReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, BranchProduct.class));
    }

    @Override
    public Mono<BranchProduct> saveBranchProduct(BranchProduct branchProduct) {
        return save(branchProduct);
    }
}

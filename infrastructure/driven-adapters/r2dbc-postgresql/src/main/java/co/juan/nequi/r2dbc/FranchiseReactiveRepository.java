package co.juan.nequi.r2dbc;

import co.juan.nequi.dto.TopStockPerBranchDto;
import co.juan.nequi.r2dbc.entity.FranchiseEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseReactiveRepository extends ReactiveCrudRepository<FranchiseEntity, Long>,
        ReactiveQueryByExampleExecutor<FranchiseEntity> {

    Mono<Boolean> existsById(Long idFranchise);

    @Query("""
                WITH RankedProducts AS (
                    SELECT
                        b.id AS idBranch,
                        b.name AS branchName,
                        p.id AS idProduct,
                        p.name AS productName,
                        bp.stock,
                        ROW_NUMBER() OVER(PARTITION BY b.id ORDER BY bp.stock DESC) as rn
                    FROM branches b
                    JOIN branch_product bp ON b.id = bp.id_branch
                    JOIN products p ON bp.id_product = p.id
                    WHERE b.id_franchise = :idFranchise
                )
                SELECT idBranch, branchName, idProduct, productName, stock
                FROM RankedProducts
                WHERE rn = 1
            """)
    Flux<TopStockPerBranchDto> findTopStockProductByBranchForFranchise(Long idFranchise);
}

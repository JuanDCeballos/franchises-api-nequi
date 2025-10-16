package co.juan.nequi.config;

import co.juan.nequi.model.branch.gateways.BranchRepository;
import co.juan.nequi.model.branchproduct.gateways.BranchProductRepository;
import co.juan.nequi.model.franchise.gateways.FranchiseRepository;
import co.juan.nequi.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }

        @Bean
        public FranchiseRepository franchiseRepository() {
            return Mockito.mock(FranchiseRepository.class);
        }

        @Bean
        public BranchRepository branchRepository() {
            return Mockito.mock(BranchRepository.class);
        }

        @Bean
        public ProductRepository productRepository() {
            return Mockito.mock(ProductRepository.class);
        }

        @Bean
        public BranchProductRepository branchProductRepository() {
            return Mockito.mock(BranchProductRepository.class);
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}
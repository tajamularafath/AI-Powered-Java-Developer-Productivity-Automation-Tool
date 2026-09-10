package com.aiproductivity.java_dev_tool.service;

import com.aiproductivity.java_dev_tool.dto.request.DirectCodeGenRequest;
import com.aiproductivity.java_dev_tool.dto.request.EntityFieldDefinition;
import com.aiproductivity.java_dev_tool.dto.response.GeneratedArtifactDto;
import com.aiproductivity.java_dev_tool.entity.ArtifactType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CodeGenerationServiceTest {

    private CodeGenerationService codeGenService;
    private List<EntityFieldDefinition> sampleFields;

    @BeforeEach
    void setUp() {
        codeGenService = new CodeGenerationService();
        sampleFields = List.of(
                new EntityFieldDefinition("title", "String", false, false, "@NotBlank", "Product title", "Sample Title"),
                new EntityFieldDefinition("sku", "String", false, false, "@NotBlank", "Unique SKU", "SKU-1234"),
                new EntityFieldDefinition("price", "BigDecimal", false, false, "@PositiveOrZero", "Product price", "49.99"),
                new EntityFieldDefinition("stock", "Integer", false, false, "@Min(0)", "Stock inventory", "100")
        );
        // Mark sku as unique
        sampleFields.get(1).setUnique(true);
    }

    @Test
    @DisplayName("Should generate valid JPA Entity class with annotations and fields")
    void testGenerateEntityCode() {
        String code = codeGenService.generateEntityCode("Product", "com.example.inventory", sampleFields);

        assertThat(code).contains("package com.example.inventory.entity;");
        assertThat(code).contains("@Entity");
        assertThat(code).contains("@Table(name = \"products\")");
        assertThat(code).contains("@Id");
        assertThat(code).contains("@GeneratedValue(strategy = GenerationType.IDENTITY)");
        assertThat(code).contains("private Long id;");
        assertThat(code).contains("private String title;");
        assertThat(code).contains("private BigDecimal price;");
        assertThat(code).contains("private Integer stock;");
        assertThat(code).contains("public Long getId()");
        assertThat(code).contains("public void setTitle(String title)");
        assertThat(code).contains("public boolean equals(Object o)");
        assertThat(code).contains("public int hashCode()");
    }

    @Test
    @DisplayName("Should generate valid Request and Response DTOs")
    void testGenerateDtos() {
        String reqDto = codeGenService.generateRequestDtoCode("Product", "com.example.inventory", sampleFields);
        assertThat(reqDto).contains("public class ProductRequestDto");
        assertThat(reqDto).contains("private String title;");
        assertThat(reqDto).contains("@NotBlank");

        String resDto = codeGenService.generateResponseDtoCode("Product", "com.example.inventory", sampleFields);
        assertThat(resDto).contains("public class ProductResponseDto");
        assertThat(resDto).contains("public static ProductResponseDto fromEntity(Product entity)");
    }

    @Test
    @DisplayName("Should generate Spring Data JPA Repository with unique derived query")
    void testGenerateRepositoryCode() {
        String repoCode = codeGenService.generateRepositoryCode("Product", "com.example.inventory", sampleFields);

        assertThat(repoCode).contains("public interface ProductRepository extends JpaRepository<Product, Long>");
        assertThat(repoCode).contains("Optional<Product> findBySku(String sku);");
        assertThat(repoCode).contains("boolean existsBySku(String sku);");
        assertThat(repoCode).contains("Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);");
    }

    @Test
    @DisplayName("Should generate transactional Service Interface and Implementation")
    void testGenerateServiceCode() {
        String srvInterface = codeGenService.generateServiceCode("Product", "com.example.inventory", sampleFields);
        assertThat(srvInterface).contains("public interface ProductService");
        assertThat(srvInterface).contains("ProductResponseDto create(ProductRequestDto requestDto);");
        assertThat(srvInterface).contains("ProductResponseDto findById(Long id);");

        String srvImpl = codeGenService.generateServiceImplCode("Product", "com.example.inventory", sampleFields);
        assertThat(srvImpl).contains("@Service");
        assertThat(srvImpl).contains("@Transactional");
        assertThat(srvImpl).contains("public class ProductServiceImpl implements ProductService");
        assertThat(srvImpl).contains("private final ProductRepository productRepository;");
        assertThat(srvImpl).contains("@Transactional(readOnly = true)");
    }

    @Test
    @DisplayName("Should generate REST Controller with proper status codes and mappings")
    void testGenerateControllerCode() {
        String ctrlCode = codeGenService.generateControllerCode("Product", "com.example.inventory", sampleFields);

        assertThat(ctrlCode).contains("@RestController");
        assertThat(ctrlCode).contains("@RequestMapping(\"/api/v1/products\")");
        assertThat(ctrlCode).contains("@PostMapping");
        assertThat(ctrlCode).contains("HttpStatus.CREATED");
        assertThat(ctrlCode).contains("@GetMapping(\"/{id}\")");
        assertThat(ctrlCode).contains("@PutMapping(\"/{id}\")");
        assertThat(ctrlCode).contains("@DeleteMapping(\"/{id}\")");
    }

    @Test
    @DisplayName("Should generate MySQL DDL script with InnoDB and UTF-8 charset")
    void testGenerateDdlScript() {
        String ddl = codeGenService.generateDdlScript("Product", sampleFields);

        assertThat(ddl).contains("CREATE TABLE IF NOT EXISTS `products`");
        assertThat(ddl).contains("`id` BIGINT NOT NULL AUTO_INCREMENT");
        assertThat(ddl).contains("`sku` VARCHAR(255) NOT NULL UNIQUE");
        assertThat(ddl).contains("`price` DECIMAL(19, 4) NOT NULL");
        assertThat(ddl).contains("`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");
        assertThat(ddl).contains("ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
    }

    @Test
    @DisplayName("Should generate single artifact on demand via DirectCodeGenRequest")
    void testGenerateArtifactDirect() {
        DirectCodeGenRequest req = new DirectCodeGenRequest("Invoice", "com.example.billing", ArtifactType.CONTROLLER, sampleFields);
        GeneratedArtifactDto artifact = codeGenService.generateArtifact(req);

        assertThat(artifact.getArtifactType()).isEqualTo(ArtifactType.CONTROLLER);
        assertThat(artifact.getFileName()).isEqualTo("InvoiceController.java");
        assertThat(artifact.getContent()).contains("@RestController");
        assertThat(artifact.getLineCount()).isGreaterThan(20);
    }
}

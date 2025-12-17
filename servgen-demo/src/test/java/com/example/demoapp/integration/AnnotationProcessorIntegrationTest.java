//=========================================================|
//  Copyright © Valdemar Støvring Storgaard, December 2025.|
//=========================================================|

package io.servgen.demo.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test that verifies annotation processing works end-to-end
 */
public class AnnotationProcessorIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldGenerateBaseServiceFromAnnotation() throws IOException {
        // Given: Source code with @ServGen annotation
        String testServiceCode = """
            package test;
            
            import io.servgen.annotation.ServGen;
            
            @ServGen(entity = test.TestEntity.class, repository = test.TestRepository.class)
            public class TestService extends BaseTestService {
                public TestService(test.TestRepository repository) {
                    super(repository);
                }
            }
            """;

        String testEntityCode = """
            package test;
            public class TestEntity {
                private Long id;
                public Long getId() { return id; }
                public void setId(Long id) { this.id = id; }
            }
            """;

        String testRepositoryCode = """
            package test;
            import java.util.List;
            import java.util.Optional;
            public interface TestRepository {
                Optional<TestEntity> findById(Long id);
                List<TestEntity> findAll();
                TestEntity save(TestEntity entity);
                void deleteById(Long id);
                boolean existsById(Long id);
            }
            """;

        // Create source files
        Path sourceDir = tempDir.resolve("src");
        Files.createDirectories(sourceDir);
        Files.writeString(sourceDir.resolve("TestService.java"), testServiceCode);
        Files.writeString(sourceDir.resolve("TestEntity.java"), testEntityCode);
        Files.writeString(sourceDir.resolve("TestRepository.java"), testRepositoryCode);

        // When: Compile with annotation processor
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        
        Path outputDir = tempDir.resolve("output");
        Files.createDirectories(outputDir);
        
        // Get classpath (simplified - in real test you'd include all dependencies)
        String classPath = System.getProperty("java.class.path");
        
        List<String> options = Arrays.asList(
            "-d", outputDir.toString(),
            "-cp", classPath,
            "-s", outputDir.toString()
        );

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(
            Arrays.asList(
                sourceDir.resolve("TestEntity.java").toFile(),
                sourceDir.resolve("TestRepository.java").toFile(),
                sourceDir.resolve("TestService.java").toFile()
            )
        );

        boolean success = compiler.getTask(null, fileManager, null, options, null, compilationUnits).call();

        // Then: Verify compilation succeeded and base class was generated
        assertTrue(success, "Compilation should succeed");
        
        Path generatedBaseService = outputDir.resolve("test").resolve("BaseTestService.java");
        assertTrue(Files.exists(generatedBaseService), "BaseTestService should be generated");
        
        String generatedContent = Files.readString(generatedBaseService);
        assertAll(
            () -> assertTrue(generatedContent.contains("public abstract class BaseTestService")),
            () -> assertTrue(generatedContent.contains("public TestEntity findById(Long id)")),
            () -> assertTrue(generatedContent.contains("public TestEntity save(TestEntity entity)")),
            () -> assertTrue(generatedContent.contains("public List<TestEntity> findAll()")),
            () -> assertTrue(generatedContent.contains("public void deleteById(Long id)")),
            () -> assertTrue(generatedContent.contains("public boolean existsById(Long id)"))
        );

        fileManager.close();
    }
}
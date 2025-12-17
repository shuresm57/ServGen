// |==========================================================|
// |  Copyright © Valdemar Støvring Storgaard, December 2025  |
// |==========================================================|

//=========================================================|
//  Copyright © Valdemar Støvring Storgaard, December 2025.|
//=========================================================|

package org.example.processor;

import org.example.autocrud.AutoCrudService;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.Set;

/**
 * Annotation processor responsible for generating base CRUD service
 * classes for types annotated with {@link org.example.autocrud.AutoCrudService}.
 *
 * <p>For each annotated service class, this processor generates an
 * abstract base class named {@code Base<OriginalServiceName>} that:
 * <ul>
 *   <li>Contains a protected final repository field</li>
 *   <li>Provides common CRUD methods such as:
 *     {@code findById}, {@code findAll}, {@code save},
 *     {@code existsById}, and {@code deleteById}</li>
 * </ul>
 *
 * This approach eliminates the need for repetitive boilerplate code,
 * allowing the concrete service class to focus solely on application-specific business logic.
 *
 *@see org.example.autocrud.AutoCrudService
 *@see javax.annotation.processing.AbstractProcessor
 *
*/

@SupportedAnnotationTypes("org.example.autocrud.AutoCrudService")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class AutoCrudServiceProcessor extends AbstractProcessor {

    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv){
        super.init(processingEnv);
        this.elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {

        for (Element element : roundEnv.getElementsAnnotatedWith(AutoCrudService.class)) {

            if (element.getKind() != ElementKind.CLASS) {
                continue;
            }

            TypeElement serviceClass = (TypeElement) element;
            AutoCrudService annotation = element.getAnnotation(AutoCrudService.class);

            // Handle MirroredTypeException when accessing Class values
            String entityClassName = null;
            String repositoryClassName = null;
            
            try {
                Class<?> entityClass = annotation.entity();
                entityClassName = entityClass.getCanonicalName();
            } catch (javax.lang.model.type.MirroredTypeException ex) {
                entityClassName = ex.getTypeMirror().toString();
            }
            
            try {
                Class<?> repositoryClass = annotation.repository();
                repositoryClassName = repositoryClass.getCanonicalName();
            } catch (javax.lang.model.type.MirroredTypeException ex) {
                repositoryClassName = ex.getTypeMirror().toString();
            }

            TypeElement entityType = elementUtils.getTypeElement(entityClassName);
            TypeElement repositoryType = elementUtils.getTypeElement(repositoryClassName);

            generateBaseService(serviceClass, entityType, repositoryType);
        }

        return true;
    }

    private void generateBaseService(TypeElement serviceClass, TypeElement entityType,
                                    TypeElement repositoryType) {

        ClassName entityClass = ClassName.get(entityType);
        ClassName repositoryClass = ClassName.get(repositoryType);
        
        String packageName = elementUtils.getPackageOf(serviceClass).getQualifiedName().toString();
        String originalClassName = serviceClass.getSimpleName().toString();
        String baseClassName = "Base" + originalClassName;

        FieldSpec repositoryField = FieldSpec.builder(
                        repositoryClass,
                        "repository",
                        Modifier.PROTECTED, Modifier.FINAL)
                .build();

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(repositoryClass, "repository")
                .addStatement("this.repository = repository")
                .build();

        MethodSpec findById = MethodSpec.methodBuilder("findById")
                .addModifiers(Modifier.PUBLIC)
                .returns(entityClass)
                .addParameter(Long.class, "id")
                .addStatement("return repository.findById(id).orElse(null)")
                .build();

        MethodSpec existsById = MethodSpec.methodBuilder("existsById")
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(Long.class, "id")
                .addStatement("return repository.existsById(id)")
                .build();

        MethodSpec deleteById = MethodSpec.methodBuilder("deleteById")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Long.class, "id")
                .addStatement("repository.deleteById(id)")
                .build();

        MethodSpec save = MethodSpec.methodBuilder("save")
                .addModifiers(Modifier.PUBLIC)
                .returns(entityClass)
                .addParameter(entityClass, "entity")
                .addStatement("return repository.save(entity)")
                .build();

        MethodSpec findAll = MethodSpec.methodBuilder("findAll")
                .addModifiers(Modifier.PUBLIC)
                .returns(
                        ParameterizedTypeName.get(
                                ClassName.get(java.util.List.class),
                                entityClass
                        )
                )
                .addStatement("return repository.findAll()")
                .build();

        TypeSpec baseServiceClass = TypeSpec.classBuilder(baseClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addField(repositoryField)
                .addMethod(constructor)
                .addMethod(findById)
                .addMethod(existsById)
                .addMethod(deleteById)
                .addMethod(save)
                .addMethod(findAll)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, baseServiceClass)
                .build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

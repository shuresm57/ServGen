# ServGen

Generate service boilerplate at compile-time instead of writing it by hand. For proof of this working, there is a 
Spring Boot demo application with tests of all the generic methods and integration tests.

## Before ServGen:
```java
@Service
public class CarService {
    private final CarRepository repository;
    
    public CarService(CarRepository repository) {
        this.repository = repository;
    }
    
    public Car findById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public Car save(Car car) {
        return repository.save(car);
    }
    
    public List<Car> findAll() {
        return repository.findAll();
    }
    
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
    
    // Your actual business logic
    public List<Car> findAllRedCars() {
        return findAll().stream()
                .filter(car -> "red".equalsIgnoreCase(car.getColor()))
                .collect(Collectors.toList());
    }
}
```

## After ServGen:
```java
@Service
@ServGen(entity = Car.class, repository = CarRepository.class)
public class CarService extends BaseCarService {
    public CarService(CarRepository repository) {
        super(repository);
    }
    
    // Just write your business logic
    public List<Car> findAllRedCars() {
        return findAll().stream()
                .filter(car -> "red".equalsIgnoreCase(car.getColor()))
                .collect(Collectors.toList());
    }
}
```

The CRUD methods get generated at compile-time, without reflection and runtime overhead.

## Installation

Add these to your `pom.xml`:

```xml
<dependencies>
    <!-- Runtime annotation -->
    <dependency>
        <groupId>io.servgen</groupId>
        <artifactId>servgen-annotation</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    
    <!-- Compile-time processor -->
    <dependency>
        <groupId>io.servgen</groupId>
        <artifactId>servgen-processor</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## Usage

1. Create your entity and repository like normal
2. Annotate your service class with `@ServGen`
3. Extend the generated base class
4. Add your custom business methods

```java
@Entity
public class Car {
    @Id @GeneratedValue
    private Long id;
    private String color;
    private int regNo;
    // getters/setters
}

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}

@Service
@ServGen(entity = Car.class, repository = CarRepository.class)
public class CarService extends BaseCarService {
    public CarService(CarRepository repository) {
        super(repository);
    }
}
```

## How It Works

ServGen is an annotation processor that runs during compilation. When it finds `@ServGen`, it generates an abstract base class with standard CRUD methods. Your service extends this base class and inherits all the methods.

The generated `BaseCarService` looks something like:
```java
public abstract class BaseCarService {
    protected final CarRepository repository;
    
    public BaseCarService(CarRepository repository) {
        this.repository = repository;
    }
    
    public Car findById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public Car save(Car entity) {
        return repository.save(entity);
    }
    
    // ... other CRUD methods
}
```

## Generated Methods

Every `@ServGen` service gets these methods for free:

- `findById(Long id)` - Find entity by ID
- `save(T entity)` - Save or update entity
- `findAll()` - Get all entities
- `deleteById(Long id)` - Delete by ID
- `existsById(Long id)` - Check if entity exists

## Why Not Just Spring Data JPA?

Spring Data JPA gives you repository methods. ServGen gives you service layer methods. Sometimes you want business logic in your service that's more than just repository calls.

Also, this way you can mock your service in tests without dealing with JPA complexity.

## Requirements

- Java 21+
- Spring Boot 3.x
- Maven or Gradle

## License

Copyright © Valdemar Støvring Storgaard, December 2025
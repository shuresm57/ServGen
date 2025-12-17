//=========================================================|
//  Copyright © Valdemar Støvring Storgaard, December 2025.|
//=========================================================|

package io.servgen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a service class for which a base CRUD service implementation
 * should be automatically generated at compile time.
 *
 * <p>The annotated service class is expected to extend the generated
 *  base class and provide any custom business logic.
 *
 *  <pre>{@code
 *  @AutoCrudService(entity = User.class, repository = UserRepository.class)
 *  public class UserService extends BaseUserService {
 *  }
 *  }</pre>
 *
 *  @see AutoCrudService
 *
*/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ServGen {

    Class<?> entity();
    Class<?> repository();

}

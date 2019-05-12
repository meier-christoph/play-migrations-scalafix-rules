## Migration Rules

### MigrateControllers

Migrate Play Controllers away from global state and to more D.I. using `ControllerComponents`.

This rule should handle most common use cases (see examples in input) i.e. when Play's `Controller`
trait is present. However, if you did create a custom base class which extends `Controller`
and you only use that class in your application, then you need to configure said base class
in the settings.

```.scalafix.conf
MigrateControllers.controllerClasses = [
  MyBaseController
]
```



### Controller Changes

Using DI and ControllerComponents instead of Controller

### ExecutionContext

Using DI for ExecutionContext

### I18n Messages

Ensure all Action have an implicit request

### Use new Cache API

Play v2.6.x introduces a new API for caching

## Migration Rules

This module contains scalafix rules for migrating play v2.5.x to play v2.6.x

While these rules won't cover the entire migration process, they should help in the process.
Some rules are complex (e.g. MigrateControllers) and will likely not cover all use cases or 
generate code that may not compile. If that's the case you will need to finish the migration
manually.

See the official migration guide for more info :
https://www.playframework.com/documentation/2.8.x/Migration26

### MigrateControllers

Using DI and ControllerComponents instead of Controller.

Note: we opted for the `BaseController` style, `AbstractController` is not supported.

[Guide](https://www.playframework.com/documentation/2.8.x/Migration26#Scala-Controller-changes)

### MigrateActions

Ensure all Action have an implicit request.

Note: we don't add `I18nSupport` to the controller, we only prepare the `Action` blocks.

[Guide](https://www.playframework.com/documentation/2.8.x/MessagesMigration26#I18nSupport-Implicit-Conversion)

### MigrateExecutionContext

Using DI for ExecutionContext.

[Guide](https://www.playframework.com/documentation/2.8.x/Migration26#play.api.libs.concurrent.Execution-is-deprecated)

### MigrateCacheApi

The `CacheApi` has been replaced with `SyncCacheApi`.

Note: sometimes using the `AsyncCacheApi` has additional benefits but this would 
      require some refactoring which is out of scope for this rule.

[Guide](https://www.playframework.com/documentation/2.8.x/CacheMigration26)

### MigrateConfiguration

Changes `Configuration` getter for optional values.

Note: default values should be defined in `reference.conf` files but this is 
      out of scope for this rule.

Warning: your code may compile after using this rule, but your app will still fail 
         at runtime because of a missing conf, please be careful and make sure
         you are doing proper testing post migration.

[Guide](https://www.playframework.com/documentation/2.8.x/Migration26#Scala-Configuration-API)

### MigrateWSClient

Changes the methods in `WSRequest` and `WSResponse` that have been renamed and deprecated.
to the new ones.

### MigrateJsLookup

Revert changes on the behavior of `apply(i: Int)` on an `JsArray` which 
will now throw an exception. Replace it with `\(i: Int)` to keep the
same behavior as before.

[Guide](https://www.playframework.com/documentation/2.8.x/Migration26#Play-JSON-API-changes)

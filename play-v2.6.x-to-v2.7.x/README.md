## Migration Rules

This module contains scalafix rules for migrating play v2.6.x to play v2.7.x

While these rules won't cover the entire migration process, they should help in the process.
Some rules are complex (e.g. MigrateControllers) and will likely not cover all use cases or 
generate code that may not compile. If that's the case you will need to finish the migration
manually.

See the official migration guide for more info :
https://www.playframework.com/documentation/2.8.x/Migration27

### MigrateLogger

Using named Logger instead of application Logger.

[Guide](https://www.playframework.com/documentation/2.8.x/Migration27#Static-Logger-singletons-deprecated)

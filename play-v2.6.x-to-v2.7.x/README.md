## Migration Rules

This module contains scalafix rules for migrating play v2.6 to play v2.7

Compared to the migration for play v2.6 this one is much easier.
Also, v2.7 did not last very long before it was replaced with v2.8
and from our experience we could just upgrade straight to v2.8 without 
any changes.

See the official migration guide for more info :
https://www.playframework.com/documentation/2.8.x/Migration27

### MigrateLogger

Using named Logger instead of application Logger.

[Guide](https://www.playframework.com/documentation/2.8.x/Migration27#Static-Logger-singletons-deprecated)

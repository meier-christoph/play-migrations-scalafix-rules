## Migration Rules

This module contains scalafix rules for migrating play v2.3 to play v2.4

Full disclaimer, this migration is not easy, and it is even harder to automate with scalafix.

See the official migration guide for more info :
https://www.playframework.com/documentation/2.8.x/Migration24

### MigrateInjectControllers

This rule will only migrate controllers and leave everything else as is. 
This should be enough to configure the router, change the `build.sbt`, and
the configurations in order to get the app running again.

Still, you will have a considerable amount of technical depth since
`WS`, `DB`, `Cache`, etc. will still run on global state, but you
could migrate those step by step over time.

### MigrateInjectAll

This rule will _try_ to migrate all objects that have well known global
state into injected classes.
At the same time, it will write all migrated types into a file such that you can 
run the same rule again, this time with custom types and repeat the process.
After a few iterations, you should have migrated all objects to classes using 
DI (hopefully).

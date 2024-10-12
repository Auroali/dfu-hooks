# ! This is a work in progress, and may not work !
## Using in a project
There are two entrypoints provided currently.

`dfuhooks-item-components` provides an entrypoint into minecraft's ItemStackComponentizationFix class. This entrypoint allows you to move legacy item nbt data to the new components system.

`dfuhooks-modify-schemas` provides an entrypoint into the `build` method in the Schemas class. This allows you to register custom schemas (may not work properly), get existing schemas, and add new fixers.

# Harpy Vents

A Fabric mod that adds a keyed vent hatch for **The Harpy Express: Extended** mansion maps.

Anyone holding a `wathe:lockpick` or a `noellesroles:master_key` can open a vent. The role alone does not grant access. Moving through vents is not implemented yet. See [docs/gameplay-options.md](docs/gameplay-options.md) for the options being considered.

## Requirements

| Component | Version |
|---|---|
| Minecraft | 1.21.1 |
| Fabric Loader | ≥ 0.19.5 |
| Fabric API | 0.116.17+1.21.1 |
| WATHE | 1.4.1-1.21.1 |
| NoellesRoles | 1.7.5-h1.4 |
| Java | 21 |

## Building

```bash
./gradlew build
```

The mod JAR is written to `build/libs/`. Run the tests with `./gradlew test`.

## Docs

- [installed-pack.md](docs/installed-pack.md): inventory of the installed modpack
- [integration-research.md](docs/integration-research.md): how vents hook into WATHE and NoellesRoles
- [gameplay-options.md](docs/gameplay-options.md): design options for vent transport
- [verification-plan.md](docs/verification-plan.md): how to test the mod in game

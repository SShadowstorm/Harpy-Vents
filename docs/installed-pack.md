# Installed Harpy Express Extended inventory

Inspected on 14 October 2026 using read-only filesystem access, ZIP metadata, language assets, block tags, and launcher version metadata. No installed mod, configuration, world, or account data was changed. The mod JARs were not executed.

## Local installation

Profile folder:

`%APPDATA%\ModrinthApp\profiles\The Harpy Express_ Extended`

The `mods` folder contains 49 JARs. The exact Modrinth **modpack release number** was not established from the filesystem; the component versions below are independently confirmed. Do not confuse the Wathe Extended mod version with the pack release number.

| Component | Installed version | Evidence |
|---|---|---|
| Minecraft | 1.21.1 | Launcher manifest ID `1.21.1-0.19.5`, mod dependency metadata |
| Fabric Loader | 0.19.5 | Launcher manifest library `net.fabricmc:fabric-loader:0.19.5` |
| Fabric API | 0.116.17+1.21.1 | `fabric-api-0.116.17+1.21.1.jar` |
| WATHE | 1.4.1-1.21.1 | `fabric.mod.json` in `wathe-1.4.1-1.21.1.jar` |
| Wathe Extended | 4.0.0-beta.1-h1.4 | `fabric.mod.json` in `WatheExtended-4.0.0-beta.1-h1.4.jar` |
| NoellesRoles | 1.7.5-h1.4 | `fabric.mod.json` in `noellesroles-1.7.5-h1.4.jar` |
| HarpyModLoader | 1.2.5-h1.4 | `fabric.mod.json` in `harpymodloader-1.2.5-h1.4.jar` |
| KinsWathe | 1.6.3 | `fabric.mod.json` in `kinswathe-1.6.3.jar` |
| Starry Express | 1.3.2 | `fabric.mod.json` in `starry-express-1.3.2.jar` |
| Stupid Express | 2026.10.1-h1.4 | `fabric.mod.json` in `stupid-express-2026.10.1-h1.4.jar` |
| Wathe Extra Items | 1.0.7 | `fabric.mod.json` in `wathe-extra-items-1.21.1-1.0.7.jar` |
| WathExtras | 1.0.3 | `fabric.mod.json` in `wathextras-1.0.3.jar` |
| Wathe Blood | 1.1.5 | `fabric.mod.json` in `wathe_blood-1.1.5+1.21.1.jar` |

Launcher manifest:

`%APPDATA%\ModrinthApp\meta\versions\1.21.1-0.19.5\1.21.1-0.19.5.json`

The manifest specifies Java major version 21 and Fabric's `KnotClient` main class.

## Existing key items

| Item | Identifier from assets/language namespace | Provider | Relevant class |
|---|---|---|---|
| Lockpick | `wathe:lockpick` | WATHE | `dev.doctor4t.wathe.item.LockpickItem` |
| Master Key | `noellesroles:master_key` | NoellesRoles | `org.agmas.noellesroles.item.MasterKeyItem` |

Evidence for the lockpick includes `assets/wathe/models/item/lockpick.json`, `assets/wathe/textures/item/lockpick.png`, and language key `item.wathe.lockpick`. Evidence for the Master Key includes corresponding files under `assets/noellesroles/` and `item.noellesroles.master_key`. Registry classes available for bytecode verification are `dev.doctor4t.wathe.index.WatheItems` and `org.agmas.noellesroles.ModItems`.

The installed English guidebook describes the lockpick as opening locked doors and allowing sneak-use to temporarily jam doors. The locally configured Wathe Extended values are price 50 and cooldown 180 seconds, in `config/watheextended/server.json` lines 56–57.

The Starry Express guidebook describes the Conductor as carrying the Master Key, with keys dropped on death and usable by killers who pick them up. Therefore, allowing **either item** should not be described as strictly killer-only access. Item possession and role permission are different rules and should be decided explicitly if restrictions change later.

The local `config/noellesroles.json5` has `playerCountToMakeConducterKeyVisible: 10`. The guidebook and settings describe Master Keys appearing as lockpicks in smaller games. Access checks must use the registered item identity, never the displayed name or texture.

Related NoellesRoles classes already patch door behavior:

- `org.agmas.noellesroles.mixin.host.MasterKeySmallDoorMixin`
- `org.agmas.noellesroles.mixin.host.MasterKeyTrainDoorMixin`

## Existing vent assets and code

WATHE **already includes vents**. Their exact current interaction rules require bytecode or source inspection, but the following registered identifiers are explicitly listed in the built-in block tags:

| Hatch | Shaft |
|---|---|
| `wathe:dark_steel_vent_hatch` | `wathe:dark_steel_vent_shaft` |
| `wathe:stainless_steel_vent_hatch` | `wathe:stainless_steel_vent_shaft` |
| `wathe:tarnished_gold_vent_hatch` | `wathe:tarnished_gold_vent_shaft` |

Tag entries: `data/wathe/tags/block/vent_hatches.json` and `data/wathe/tags/block/vent_shafts.json`.

Each family includes blockstates, block models, item models, textures, and loot tables. Hatch models include open and closed states. Shaft models include junctions, sides, and openings. WATHE also includes vent shaft step and break sounds.

Code targets for further analysis:

- `dev.doctor4t.wathe.block.VentHatchBlock`
- `dev.doctor4t.wathe.block.VentShaftBlock`
- `dev.doctor4t.wathe.index.WatheBlocks`
- `dev.doctor4t.wathe.api.event.AllowPlayerOpenLockedDoor`
- `dev.doctor4t.wathe.util.AdventureUsable`

This finding may simplify the requested add-on: a new vent can potentially reference the existing WATHE models and sound assets through its dependency. Whether reusing these classes is suitable depends on the inspected behavior and compatibility needs.

## Java development runtime

Modrinth's installed runtime is:

`%APPDATA%\ModrinthApp\meta\java_versions\zulu21.52.203-ca-jre21.0.12.1-win_x64`

Its `release` file identifies Azul Zulu Java 21.0.12.1, Windows x86_64. `bin/java.exe` exists; `bin/javac.exe` does not. This is a **JRE**, not a complete development JDK. No `java` or `javac` was found on PATH, and no `javac.exe` was found in the inspected Program Files, user `.jdks`, Local Programs, or Modrinth runtime paths. A portable Java 21 JDK is needed to compile the add-on unless another JDK is supplied.

## Distribution metadata

The installed metadata declares WATHE `ARR`, NoellesRoles `LGPL-3.0`, and Wathe Extended `GPL-3.0`. An add-on should use the installed mods as dependencies and reference their existing resources rather than copying or redistributing their JARs or assets. These strings are metadata observations, not a complete legal interpretation.

## SHA-256 evidence

All files below are in the profile's `mods` folder.

| File | SHA-256 |
|---|---|
| `wathe-1.4.1-1.21.1.jar` | `79245F0351C1567BA08CC419D0DACF73819C2D3CF6DC9A7CD5406399F69F76B3` |
| `WatheExtended-4.0.0-beta.1-h1.4.jar` | `4510EDFDA339C7ABDA6828BBE5CD5DD47D6CA8D28BACF91E693620D791AA8D71` |
| `noellesroles-1.7.5-h1.4.jar` | `7A2029DE432566582AB0509F024B7F751D836EF55CAB567EE226F18EBD896200` |
| `harpymodloader-1.2.5-h1.4.jar` | `06C0BAF83BFCEE2877191D4F4B7C34E667B608F2D21F97C672E2AA2635D31B19` |
| `fabric-api-0.116.17+1.21.1.jar` | `79AC44B40780ACBD884B34C50BE1E39AF682847E5F5CB3B1FDDEEAA768DCE800` |

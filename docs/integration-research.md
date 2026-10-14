# Harpy Express vent integration research

Inspected 2026-10-14. This is source research, not a claim of an in-game test.

## Target established from the installed pack

The inventory specialist found this installed Modrinth profile:

`%APPDATA%\ModrinthApp\profiles\The Harpy Express_ Extended`

| Component | Installed version reported by inventory |
| --- | --- |
| Minecraft | 1.21.1 |
| Loader family | Fabric |
| WATHE | 1.4.1-1.21.1 |
| Wathe Extended | 4.0.0-beta.1-h1.4 |
| Noelle's Roles | 1.7.5-h1.4 |

The installed beta matters: older stable-pack pages describe the 1.3 generation. Build against the installed 1.4 generation instead.

Three public repositories were downloaded read-only into `work/references/` for inspection:

- WATHE: commit `edaade2f927e7b5b2fd1bb824dee404544552fed`.
- Wathe Extended: commit `97698d2c74a9f8081a4d121d0ee7de0065d63669`.
- Noelle's Roles: commit `e7a6ee24d86c22a6af260a32d7c2a4ad94f9cfe0`.

Source versions match those installed versions, except Extended's build configuration compiles against WATHE 1.4 while the installed WATHE is 1.4.1.

## Existing items: exact identities and behavior

| Item | Registry ID | Public constant |
| --- | --- | --- |
| Lockpick | `wathe:lockpick` | `dev.doctor4t.wathe.index.WatheItems.LOCKPICK` |
| Master Key | `noellesroles:master_key` | `org.agmas.noellesroles.ModItems.MASTER_KEY` |

Both have a maximum stack size of one. Neither is consumed or damaged by ordinary door opening in the inspected source. The lockpick's default three-minute cooldown is applied to **sneak-use door jamming**, not ordinary unlocking. Extended sets this cooldown through its configurable `lockpickCooldown`, default 180 seconds, and updates the WATHE cooldown map. A vent should not accidentally apply or clear that existing jamming cooldown.

The lockpick implements WATHE's `AdventureUsable` marker. Its `useOnBlock` handles jamming a `SmallDoorBlock`; actual ordinary unlocking is in the door's own `onUse` method. Vanilla doors and arbitrary custom blocks do not automatically gain that behavior.

The Master Key is registered as a plain `Item`. A `MasterKeyItem` class exists in the repository and jar, but **that class is not used by the actual item registration**. Its ordinary door access is supplied by Noelle's Roles mixins into `TrainDoorBlock.onUse` and `SmallDoorBlock.onUse`, expanding their lockpick check to accept the master key.

The Conductor receives a master key on role assignment. The key is marked to drop on death. Therefore item-based access intentionally allows a Conductor or someone who recovered the key to use the vent; it does not establish that a player is a killer. This could preserve social ambiguity, but needs to be an explicit gameplay decision.

Sources: [WATHE item registration](https://github.com/doctor4t/Wathe/blob/edaade2f927e7b5b2fd1bb824dee404544552fed/src/main/java/dev/doctor4t/wathe/index/WatheItems.java), [lockpick behavior](https://github.com/doctor4t/Wathe/blob/edaade2f927e7b5b2fd1bb824dee404544552fed/src/main/java/dev/doctor4t/wathe/item/LockpickItem.java), [master-key registration](https://github.com/agmass/NoellesRoles/blob/e7a6ee24d86c22a6af260a32d7c2a4ad94f9cfe0/src/main/java/org/agmas/noellesroles/ModItems.java), [train-door compatibility](https://github.com/agmass/NoellesRoles/blob/e7a6ee24d86c22a6af260a32d7c2a4ad94f9cfe0/src/main/java/org/agmas/noellesroles/mixin/host/MasterKeyTrainDoorMixin.java), [key assignment and death drop](https://github.com/agmass/NoellesRoles/blob/e7a6ee24d86c22a6af260a32d7c2a4ad94f9cfe0/src/main/java/org/agmas/noellesroles/Noellesroles.java).

## Existing vents are useful assets, but not the requested mechanic

WATHE already registers six vent blocks with block items:

- `wathe:stainless_steel_vent_hatch`
- `wathe:dark_steel_vent_hatch`
- `wathe:tarnished_gold_vent_hatch`
- `wathe:stainless_steel_vent_shaft`
- `wathe:dark_steel_vent_shaft`
- `wathe:tarnished_gold_vent_shaft`

`VentHatchBlock` supports floor, wall, and ceiling attachment, horizontal facing, and an `OPEN` property. Its `onUse` simply toggles that property and plays a copper-trapdoor sound. There is no lock, item, role, transit, or round-state check.

`VentShaftBlock` joins its visible faces to adjacent shafts of the same block type. Its outline and raycast shapes are full cubes; it supplies no alternate collision override and its settings do not disable collision. Source inspection therefore indicates decorative solid shafts, not a ready-made crawlable duct network. This should be confirmed in a disposable game world before planning to use those blocks as traversable tunnels.

A new vent block can use a new namespace and refer to existing WATHE model resources at runtime, retaining WATHE as an installed dependency. It need not package WATHE's textures or source. Reusing the existing hatch class directly introduces the protection issue below.

Sources: [vent registration](https://github.com/doctor4t/Wathe/blob/edaade2f927e7b5b2fd1bb824dee404544552fed/src/main/java/dev/doctor4t/wathe/index/WatheBlocks.java), [hatch](https://github.com/doctor4t/Wathe/blob/edaade2f927e7b5b2fd1bb824dee404544552fed/src/main/java/dev/doctor4t/wathe/block/VentHatchBlock.java), [shaft](https://github.com/doctor4t/Wathe/blob/edaade2f927e7b5b2fd1bb824dee404544552fed/src/main/java/dev/doctor4t/wathe/block/VentShaftBlock.java).

## World protection is a concrete compatibility constraint

Extended explicitly classifies every `instanceof VentHatchBlock` as protected. With world protection enabled, its server interaction mixin blocks protected blocks inside the configured lobby, ready, and play areas. The play-area exception permits certain lights, doors, ornaments, and buttons, **not vent hatches**. A subclass of the WATHE hatch also matches that protected classification.

The same mixin blocks block-item placement into these areas, with no creative-player exception in the inspected method. Map builders may need to disable world protection while placing map blocks.

Recommended implementation: a narrowly scoped standalone vent class with its own locked interaction, or a deliberate compatibility allowance for exactly the new vent block. Avoid broad changes to protection of unrelated blocks. Test with protection enabled and disabled in each relevant map area.

Sources: [protected block classification](https://github.com/rezelyn/Wathe-Extended/blob/97698d2c74a9f8081a4d121d0ee7de0065d63669/src/main/java/cat/rezelyn/watheextended/block/ProtectedBlocks.java), [interaction enforcement](https://github.com/rezelyn/Wathe-Extended/blob/97698d2c74a9f8081a4d121d0ee7de0065d63669/src/main/java/cat/rezelyn/watheextended/mixin/game/BlockInteractionProtectionMixin.java).

## Verified role and lifecycle hooks

| Need | Hook / behavior |
| --- | --- |
| Get game state | `GameWorldComponent.KEY.get(world)` |
| Check strictly active play | `getGameStatus() == GameWorldComponent.GameStatus.ACTIVE` |
| Check assigned role | `getRole(player)` returns nullable `Role` |
| Optional killer capability | `canUseKillerFeatures(player)` delegates to role `canUseKiller()` |
| Existing permission event | `AllowPlayerOpenLockedDoor.EVENT.invoker().allowOpen(player)` |
| After roles initialized | `GameEvents.ON_FINISH_INITIALIZE.register((world, game) -> ...)` |
| After round finalized | `GameEvents.ON_FINISH_FINALIZE.register((world, game) -> ...)` |

`isRunning()` also includes STOPPING. A transit mechanic that must stop immediately when the round ends should use the explicit ACTIVE check.

`GameFunctions.isPlayerAliveAndSurvival(player)` only checks that the player exists and is neither spectator nor creative. Despite its name, it does not perform a separate health/death test. Combine appropriate checks in new code.

`AllowPlayerOpenLockedDoor` is a general additive permission event, not a test for possession of the specified two items. The current master-key implementation does not use that event. Calling it would grant any other add-on's door exemptions to vents too, which is broader than the user's request. Prefer exact item identity for the initial vent requirement.

The after-initialize event occurs after the map reset is **queued**, not necessarily after map blocks are pasted. WATHE attempts that reset after a ten-tick countdown, retrying when needed. Persistent open-state or linkage metadata should account for the actual template/paste mechanism. Keep dynamic transit state separate and clear it after finalization; retain map-builder configuration.

WATHE's current game loop kills living players below `playArea.minY` as falling from the train. A physical duct area placed under the mansion, or an off-map transit room, must remain within suitable map bounds. Spectator bounds and other map effects should be tested as part of mansion configuration.

Sources: [event API](https://github.com/doctor4t/Wathe/blob/edaade2f927e7b5b2fd1bb824dee404544552fed/src/main/java/dev/doctor4t/wathe/api/event/GameEvents.java), [door permission API](https://github.com/doctor4t/Wathe/blob/edaade2f927e7b5b2fd1bb824dee404544552fed/src/main/java/dev/doctor4t/wathe/api/event/AllowPlayerOpenLockedDoor.java), [game component](https://github.com/doctor4t/Wathe/blob/edaade2f927e7b5b2fd1bb824dee404544552fed/src/main/java/dev/doctor4t/wathe/cca/GameWorldComponent.java), [initialization, reset, finalization](https://github.com/doctor4t/Wathe/blob/edaade2f927e7b5b2fd1bb824dee404544552fed/src/main/java/dev/doctor4t/wathe/game/GameFunctions.java).

## Build baseline

Use Java 21, Minecraft 1.21.1, and Yarn mappings `1.21.1+build.3`. Extended's current build uses Fabric Loader `0.18.4`, Fabric API `0.116.8+1.21.1`, and Cardinal Components API `6.1.1`. WATHE uses Ratatouille `1.4.3-1.21.1`.

WATHE's published dependency pattern is `dev.doctor4t:wathe:<version>` at `https://maven.ladysnake.org/releases`. Extended uses `dev.doctor4t:wathe:1.4-1.21.1`; resolve and pin the installed `1.4.1-1.21.1` when available. Noelle's Roles is available through the Modrinth Maven pattern `maven.modrinth:noelles-roles-tmm:1.7.5-h1.4` at `https://api.modrinth.com/maven`. Artifact availability was not tested in this research step.

The minimal vent addon can avoid a compiled dependency on Noelle's Roles by resolving `noellesroles:master_key` through Minecraft's item registry. Do not use display-name comparisons. Direct WATHE lifecycle access needs WATHE and the relevant CCA types on the development classpath.

Sources: [WATHE build properties](https://github.com/doctor4t/Wathe/blob/edaade2f927e7b5b2fd1bb824dee404544552fed/gradle.properties), [Extended build properties](https://github.com/rezelyn/Wathe-Extended/blob/97698d2c74a9f8081a4d121d0ee7de0065d63669/gradle.properties), [Extended dependencies](https://github.com/rezelyn/Wathe-Extended/blob/97698d2c74a9f8081a4d121d0ee7de0065d63669/build.gradle).

## Licensing observations

WATHE's repository license is All Rights Reserved. Its official Modrinth page explicitly provides addon API guidance. Treat WATHE as a dependency and write the new mechanic independently; do not bundle its source or assets in the addon.

Extended's repository contains GPL-3.0 text and its Modrinth page says GPL-3.0-or-later. Noelle's Roles has inconsistent metadata: its current repository license includes an LGPL v3-or-later notice, whereas Modrinth says GPL-3.0-only. There is no need to copy either project's implementation to recognize their item IDs. Record and resolve the discrepancy before redistributing copied source.

Sources: [WATHE license](https://github.com/doctor4t/Wathe/blob/edaade2f927e7b5b2fd1bb824dee404544552fed/LICENSE), [official addon guidance](https://modrinth.com/mod/wathe), [Extended license](https://github.com/rezelyn/Wathe-Extended/blob/97698d2c74a9f8081a4d121d0ee7de0065d63669/LICENSE), [Noelle's repository license](https://github.com/agmass/NoellesRoles/blob/e7a6ee24d86c22a6af260a32d7c2a4ad94f9cfe0/LICENSE.txt), [Noelle's Modrinth listing](https://modrinth.com/mod/noelles-roles-tmm).

## Recommended first milestone

Create one new placeable vent block/item with server-authoritative opening by the existing lockpick or master key. Preserve those items' consumption and cooldown behavior. Confirm both hand handling and adventure-mode interactions. Make it possible to close an open vent without trapping players. Keep transport behavior undecided until the mansion layout and balance goal are agreed.

Physical passages best preserve the train roof's travel time, position, and pursuit. Teleport links are easier to fit into an existing build, but need travel delay, predictable linked exits, and audible entry/exit to avoid an instant escape mechanic. A small physical service corridor with several locked hatches is the closest replacement for the roof if the map can accommodate it.

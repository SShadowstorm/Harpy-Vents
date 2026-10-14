# Compatibility review and verification plan

Reviewed 14 October 2026 against the installed Minecraft 1.21.1 / Fabric Harpy Express Extended profile and the source revisions recorded in `integration-research.md`. This document distinguishes source/bytecode evidence from tests that still need an actual game session.

## Confirmed integration constraints

- The access rule is possession of `wathe:lockpick` or `noellesroles:master_key`. A qualifying civilian is intentionally allowed; role alone does not grant access. Match registry identity, not displayed names, textures, lore, or item class names.
- The installed NoellesRoles `ModItems` bytecode registers the Master Key as a plain Minecraft `Item`. Its named `MasterKeyItem` class is not the registered implementation. Door access comes from mixins into WATHE doors. A new vent needs its own item check.
- The installed WATHE `LockpickItem` bytecode restricts its special action to `SmallDoorBlock`. Sneaking jams that door and applies the item's existing cooldown. Ordinary door unlocking neither consumes nor damages the lockpick. A vent should neither apply nor clear that jamming cooldown.
- WATHE's `AdventureUsable` marker enables item-use behavior in Adventure mode. A vent implemented through block interaction can handle a plain Master Key without changing the item or granting wider world-editing permission.
- Extended's protection mixin runs at the head of server block interaction. It blocks `VentHatchBlock` and `TrapdoorBlock` instances in protected play, ready, and lobby areas. A subclass still matches. An independent vent class avoids this classification; changing unrelated protection rules is unnecessary.
- Extended separately blocks block-item placement and all breaking within protected areas. Builders must temporarily disable the relevant protection to place or remove map vents. Holding an unrelated block item can also cause an interaction to fail before the vent receives it.
- WATHE's door permission event grants additive permissions from other mods. Calling it would widen the agreed access rule and is unnecessary.
- A simple block `open` property is included in normal world saving and WATHE's map-template cloning. Reset the template's vents to closed before saving it. A vent outside the reset area will retain its state unless a separate reset rule is added.
- `ON_FINISH_INITIALIZE` occurs before the queued map paste necessarily completes. The paste begins after a ten-tick countdown and can retry. Avoid assuming that event means blocks are already reset.
- WATHE kills active non-creative/non-spectator players below `playArea.minY`. Physical passages under a mansion must remain within the configured play area; visual geography does not override that boundary.

## Interaction and multiplayer review points

Use the server's current block state as authority and send normal block notifications. A client can report a successful interaction for hand animation, but should not independently commit a conflicting state change. Stop the interaction after one successful toggle so a single click does not open and immediately close through a second hand or fallback callback.

Minecraft has separate item-on-block and default-block-use paths. The new block should explicitly support the intended hand policy. Sneaking with held items normally bypasses ordinary block use; verify and document this behavior instead of accidentally adding a second lockpick action. A lockpick's existing jamming cooldown may block later item use while ordinary block interaction can still occur: preserve and verify the existing cooldown rather than invent a new one.

A closed hatch checks the key when it opens. An already open physical aperture does not stop an unkeyed player walking through. This is expected for the initial hatch prototype and must not be presented as role-exclusive transport. Likewise, a free close action is a convenience rule, not proof that a player who loses a key inside a future sealed network can escape. Passage escape rules remain a separate design decision.

## Focused game test matrix

Run these in a disposable copy of the installed pack and a test world. The source review alone does not mark them passed.

| Test | Expected result |
| --- | --- |
| Launch client and dedicated server with addon plus matching pack | Both load without registry, mixin, missing-class, or resource errors |
| Locate or give the Vent block item | Exactly one new placeable Vent is available; both existing keys remain the original items |
| Place each supported orientation | Model, outline, collision, attachment, and open state agree; no invisible full-cube barrier |
| Survival and Adventure: main-hand lockpick | Closed vent opens once; item count and damage remain unchanged |
| Survival and Adventure: main-hand Master Key | Same opening behavior, including when the pack visually disguises the key as a lockpick |
| Off-hand qualifying item; main hand empty or unrelated item | Follows the implemented/documented hand rule, with no double toggle; check a block item separately because Extended can intercept it |
| Empty hands, ordinary key, crowbar, renamed item | Closed vent remains closed; no placement or other item action accidentally bypasses the check |
| Civilian with a real qualifying item | Opens the vent; killer role is not required |
| Killer without a qualifying item | Cannot open the vent solely because of role |
| Creative player without a key | Follows the documented builder policy; do not silently grant an unspecified gameplay exemption |
| Spectator | Cannot change the vent state |
| Close an open vent | Follows the documented close rule; clients agree on the state |
| Sneak-use each key | No door jamming, consumption, duplicated toggle, or unexpected cooldown mutation |
| Lockpick already cooling down after jamming a real door | Existing cooldown keeps counting normally; vent behavior matches the documented block-use policy |
| Redstone power and neighbor updates | Cannot open a closed keyed vent through an unintended trapdoor or support-update path |
| Two clients, including simultaneous use and high latency | Each accepted server interaction changes state once; both clients converge on the server's state |
| World protection on in play/ready/lobby area | Intended keyed vent interaction works; placement and breaking remain subject to Extended's existing protection |
| Save, unload/reload chunk, restart server | Vent state persists correctly; no custom player/session state is left behind |
| Start next round from a closed template | Vent is restored closed after the actual map paste; any vent outside template bounds is understood |
| Walk through an open vent without a key | Physical movement behavior is acknowledged; opening permission is not an invisible transit barrier |

## Release readiness

Compilation and artifact inspection establish that the addon builds and packages. They do not establish that its model renders correctly or that the complete pack permits every interaction. Report an untested prototype honestly until at least the matching-pack client/server launch, Adventure key checks, two-client synchronization, and protected-area checks above have been exercised.

The initial addon should not patch the train roof, teleport players, consume keys, change roles, or modify the user's installed profile merely to validate the new hatch. Movement design and installation into a live community world are subsequent steps.

## Evidence locations

- `work/references/wathe/src/main/java/dev/doctor4t/wathe/item/LockpickItem.java`
- `work/references/wathe/src/main/java/dev/doctor4t/wathe/mixin/ItemStackMixin.java`
- `work/references/wathe/src/main/java/dev/doctor4t/wathe/block/SmallDoorBlock.java`
- `work/references/wathe/src/main/java/dev/doctor4t/wathe/game/GameFunctions.java`
- `work/references/wathe/src/main/java/dev/doctor4t/wathe/cca/GameWorldComponent.java`
- `work/references/noelles-roles/src/main/java/org/agmas/noellesroles/ModItems.java`
- `work/references/noelles-roles/src/main/java/org/agmas/noellesroles/mixin/host/MasterKeySmallDoorMixin.java`
- `work/references/wathe-extended/src/main/java/cat/rezelyn/watheextended/block/ProtectedBlocks.java`
- `work/references/wathe-extended/src/main/java/cat/rezelyn/watheextended/mixin/game/BlockInteractionProtectionMixin.java`
- Installed `wathe-1.4.1-1.21.1.jar` and `noellesroles-1.7.5-h1.4.jar`, read with portable JDK 21 `javap` without executing the mods.

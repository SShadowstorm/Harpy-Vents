# Mansion vent gameplay options

## Confirmed scope

- Add a placeable vent using the existing Extended pack as its dependency.
- Anyone holding `wathe:lockpick` **or** `noellesroles:master_key` may open it. Role alone does not grant access. A civilian with a qualifying item is intentionally eligible.
- Transport remains undecided. The initial keyed hatch should not silently commit the project to teleportation or a particular duct layout.
- Existing pack inventory and item evidence are recorded in [installed-pack.md](installed-pack.md).

## Design judgment: start with physical routes

My recommendation is short, physically connected service passages behind walls, under floors, or above ceilings. They give the mansion an alternate route with a readable entrance, actual travel time, and opportunities to hear or intercept someone. That makes a useful first candidate for replacing the train rooftop's role in your games.

The assumption worth testing is that the rooftop's main value is alternative movement. If its real value is escaping unnoticed or observing players, an enclosed duct could remove those advantages. Build one route between two rooms and playtest that purpose before building a mansion-wide network.

| Option | Strength | Main cost | When to choose it |
|---|---|---|---|
| Physical passages | Spatially understandable; travel and pursuit create decisions | Needs usable space and careful collision/headroom; can become a camping corridor | The mansion layout can reserve service space |
| Teleport between linked vents | Easy to fit into an existing building | Instant travel may erase pursuit and make exits hard to predict; needs destination and timing rules | The building cannot accommodate passages |
| Hybrid: physical local passages with a few linked transfers | Keeps local movement while connecting distant wings | Adds two movement rules, extra setup, and transfer edge cases | Only if physical routes prove too restrictive |

A simple service passage can be more practical than forcing Minecraft players through a tiny crawl tube. Validate movement and camera clearance before choosing narrow geometry. Do not assume a decorative shaft is traversable because its model looks hollow.

## Decisions to preserve for playtesting

These are proposals, not approved gameplay rules or implemented behavior.

- **Sound:** Try a local latch sound and modest movement noise. Nearby players should have a chance to react without every vent action announcing its location across the mansion. Test how the server's voice chat behaves through walls and at any linked transfer.
- **Travel time:** Physical movement supplies a natural cost. If transfers are used later, test a brief, interruptible entry action before introducing instant escapes. Avoid choosing fixed timing before comparing it with walking between the same rooms.
- **Counterplay:** Position entrances where approaching them can sometimes be observed. Give a useful alternate route, but avoid a network that lets a player reach every important room without exposure.
- **Civilian and recovered keys:** Item-based access is confirmed. Expect a qualifying item to change hands. Losing a key while inside must have an intentional escape rule; a free exit from the inside or a designated emergency exit would avoid trapping players, but either is an additional rule to approve.
- **Open hatch versus permitted entry:** Checking the item when a hatch opens does not stop an unkeyed player following through. Decide whether that tailgating is part of the game. Preventing it would require a separate passage rule and goes beyond the confirmed opening requirement.
- **Camping:** Avoid long dead ends and single unavoidable chokepoints. A small loop or alternate exit can reduce the value of waiting inside one doorway. Whether combat is allowed inside has a large effect on this balance.
- **Map constraints:** Reserve passage space, test player-sized clearance, avoid exits into occupied blocks, and make links explicit if transfers are introduced. Keep exits outside inaccessible or round-critical spaces unless deliberately designed otherwise.
- **Round reset and respawn:** Decide which hatches reset closed, how occupants return to the round's normal spawn/reset flow, and what happens if a destination is removed or blocked. Store map connections separately from temporary round state.
- **Lockpick cost:** Do not introduce consumption, damage, cooldowns, or permanent unlocking merely because a lockpick is used. Preserve both inventory items in the first access prototype; discuss the eventual cost after checking how often players need vents. Repeated consumption could make the master key substantially more valuable than the lockpick.

## Next design question

**Should players be able to encounter and fight one another inside the vent network?**

That answer will clarify whether the vents should behave like additional rooms and corridors, or a more protected travel mechanic.

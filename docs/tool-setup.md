# Tool setup and actual REA use

Completed on 14 October 2026 for this project.

## Installed

- REA npm package `rea-agents@6.4.0`, installed locally with dependency lifecycle scripts disabled and optional instrumentation omitted.
- Runtime: `<codex-workspace>/work/tools/rea-runtime/node_modules/rea-agents/`.
- Source reference: `work/tools/rea`, commit `fb752d229391a8c4d65b6dd970e3eb6053affa9a` from [morluto/rea](https://github.com/morluto/rea).
- REA registered in the user's Codex configuration using its official setup tool. Existing configuration was preserved; setup created `config.toml.rea.backup` beside the configuration.
- The registration launches the installed local package through the existing Node 24.19.0 executable. It does not download a new version on each launch. Keep the runtime directory in place while this registration is used.
- A portable Eclipse Temurin Java 21 JDK was downloaded into `work/tools/jdk21/jdk-21.0.12.1+1` for compilation and `javap` bytecode inspection. No system Java/PATH replacement was needed.

REA npm integrity: `sha512-Vj1zWfSlna5bphGoaoI/TcVBuYvze9u+OyPC3Shz8YkBqaCTqB4COnnRoX7XDwMpwI24ZlFEel2yEj0k8LTWEQ==`.

JDK archive SHA-256 verified against the Adoptium API: `f9d6e191ab098c0d416e7d588a24420a8621cd2f4720dab2459b8b7b2d2d8b4e`.

## Verified through MCP

REA's production stdio diagnostic passed initialization, all 170 advertised tools, all six prompts, server identity, and a `binary_session` request. This checks the MCP connection; it does not assert that optional native-analysis engines are installed.

After the chat resumed, REA was available directly in the tool catalog. The live registered MCP server was used to open, inventory and close the actual installed WATHE, Noelle's Roles and Wathe Extended JARs.

| Artifact | Archive nodes | Entry occurrences |
|---|---:|---:|
| WATHE 1.4.1 | 2,056 | 2,160 |
| Noelle's Roles 1.7.5-h1.4 | 205 | 205 |
| Wathe Extended 4.0.0-beta.1-h1.4 | 865 | 890 |

The filtered evidence, original archive hashes, evidence identifiers and coverage are retained in [rea-evidence.json](rea-evidence.json). These counts describe archive structure, not game features or functions analyzed.

## Scope and limits

The supplied [MindStudio article](https://www.mindstudio.ai/blog/reverse-engineer-anything-mcp) was reviewed alongside REA's own implementation and documentation. See [rea-research.md](rea-research.md) for the findings.

REA established archive identities and relevant class/resource paths. Published source and Java's `javap -c -p` supplied the behavior evidence for the installed vent hatch, lockpick, master-key registration/mixin, and Extended protection. The inspection focused on integration points needed by this add-on, not reconstruction of the entire game.

REA 6.4.0's Android provider does not accept ordinary Minecraft JARs. Optional Hopper, Ghidra, IDA and JADX providers were therefore not installed. Some Java class headers share magic bytes with native formats; archive labels alone are not JVM semantic analysis.

An attempted provider-neutral snapshot export was unavailable for archive-only analysis because REA requires a concrete analysis-provider profile for that snapshot format. The separately saved filtered evidence is the record used here; no successful snapshot export is claimed.

The project used seven specialist roles in waves: coordination/setup, REA review, installed-pack inventory, mod integration research, implementation, gameplay design, and compatibility review. Four concurrent-agent slots were available. Interrupted implementation work was resumed from the saved research.

Official setup references: [REA installation](https://github.com/morluto/rea/blob/main/docs/installation.md), [Codex MCP configuration](https://learn.chatgpt.com/docs/extend/mcp?surface=cli), [Adoptium archive installation](https://adoptium.net/installation).

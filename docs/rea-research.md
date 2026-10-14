# REA assessment for the mansion vent add-on

Reviewed 14 October 2026. Repository inspected at commit `fb752d229391a8c4d65b6dd970e3eb6053affa9a`; its package version is `6.4.0`. Installation and actual analysis results are recorded separately by the project setup work.

## Conclusion

REA can help inspect the installed mod JARs as archives and record the exact files and hashes being examined. Its current Java-related adapter is for Android APKs; it is not a general Minecraft Java decompiler. Use REA for artifact evidence and use published mod source or a JVM-specific tool for item and interaction logic. There is no task benefit in installing Hopper, Ghidra or the Android engine just to build this vent add-on.

## What the supplied article establishes

The [MindStudio article](https://www.mindstudio.ai/blog/reverse-engineer-anything-mcp) describes REA as an interface between agents and existing analysis tools. Its central point is reasonable: inspecting a program produces evidence, which an agent still needs to interpret, implement and test. The Preview comparison is an informal anecdote, not a reproducible benchmark supplied by the page. Its claim that setup needs no further configuration omits provider prerequisites, client registration and restart requirements. It contains product marketing and predictions about software businesses; these do not establish capabilities relevant to Minecraft.

## Installation facts

The official npm package is `rea-agents`, and its executable is `rea`. Node must satisfy `^22.19.0 || ^24.11.0 || >=26.0.0`; this machine's Node 24.19.0 meets that requirement. The supported stdio server entry is `node <package>/scripts/rea.mjs mcp`. These facts are verified in the [package metadata](https://github.com/morluto/rea/blob/fb752d229391a8c4d65b6dd970e3eb6053affa9a/package.json) and [dispatcher](https://github.com/morluto/rea/blob/fb752d229391a8c4d65b6dd970e3eb6053affa9a/scripts/rea.mjs).

Use a scoped setup preview for Codex, then apply that reviewed scope. `--skill=false` omits the additional agent instructions. Setup backs up configuration, pins the package version and adds a startup timeout. A new registration is not automatically live in an already-running chat; restarting/reconnecting the client or using a direct stdio MCP client is needed. `doctor` checks configuration and prerequisites, not a live app connection. The repository can have features newer than the published package despite displaying the same version string. Use the installed server's `tools/list` schemas as authority. [Installation guide](https://github.com/morluto/rea/blob/fb752d229391a8c4d65b6dd970e3eb6053affa9a/docs/installation.md).

## Relevant tools and exact limits

| Tool | Use here |
|---|---|
| `binary_session {}` | Inspect current server identity and operation availability. |
| `open_binary {"path":"<absolute mod.jar path>"}` | Select the actual JAR; ZIP magic routes it to artifact analysis. |
| `inspect_artifact {}` | Inventory entries and digests without executing the mod. |
| `extract_artifact {}` | Extract regular files into a new temporary directory chosen by REA. |
| `inspect_analysis_view` | Page retained inventory when supported by the installed schema. |
| `close_binary {}` | Close the selected target and release its session resources. |

`inventory_artifact` is an internal primitive, not a public MCP tool in this source revision. Extraction does not decompile `.class` bytecode. JARs are recognized as generic ZIP archives, not a dedicated Java semantic format. Sources: [artifact contracts](https://github.com/morluto/rea/blob/fb752d229391a8c4d65b6dd970e3eb6053affa9a/src/contracts/artifactToolContracts.ts), [target resolver](https://github.com/morluto/rea/blob/fb752d229391a8c4d65b6dd970e3eb6053affa9a/src/application/BinaryTargetResolver.ts), [MCP contracts](https://github.com/morluto/rea/blob/fb752d229391a8c4d65b6dd970e3eb6053affa9a/docs/mcp-contracts.md).

The JADX adapter explicitly rejects any target whose format is not `apk`. It requires an external headless JADX JAR and full JDK; having Java installed alone does not supply it. [Provider check](https://github.com/morluto/rea/blob/fb752d229391a8c4d65b6dd970e3eb6053affa9a/src/android/JadxProvider.ts), [Android guide](https://github.com/morluto/rea/blob/fb752d229391a8c4d65b6dd970e3eb6053affa9a/docs/android-analysis.md).

The Windows Ghidra adapter is experimental and admits native x86/x64 PE targets and certain raw images. Minecraft mod JARs are outside that boundary. [Windows guide](https://github.com/morluto/rea/blob/fb752d229391a8c4d65b6dd970e3eb6053affa9a/docs/windows-ghidra-p0.md).

## Focus of the game investigation

Identify the exact installed mod versions, loader, Java/Minecraft versions, registered lockpick/master-key IDs, item consumption and cooldown rules, and door-access interaction hooks. Build a separate add-on that registers the vent and recognizes those existing items. Analyze only the relevant integration points; reconstructing the whole game would add work without answering the vent-design questions.

## Audit boundary

The package metadata, launch dispatcher, setup documentation and artifact/JADX selection code were reviewed. The dispatcher loads the packaged CLI or MCP runtime; its source-only prepare hook installs Husky when the source and dependency are present. This is a targeted review, not a security certification of the entire repository or dependencies. Installing with lifecycle scripts disabled and optional runtime instrumentation omitted fits this static archive task. No commercial analysis engine license is supplied by REA.

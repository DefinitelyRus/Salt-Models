# Mod Changelog

A chronological record of all changes, updates, and additions made to this project.

## Guidelines

- The logs must be sorted reverse-chronologically (nearest-first).
- Descriptions and changes must be written in plain English (minimize techno-jargon).
- Don't be too concise but don't be too verbose either.
- Changelogs are not always 1:1 with Git changes.
- Use the format template.
- *TBD*

## Format Template

```markdown
### 12/31/2099

An optional short description of the changes made on this day should be written in place of this text. It should be no more than 1-3 sentences long. The description may be skipped if the changes are minimal and self-explanatory.

Changes:

- <Change 1>
- <Change 2>
- [...]
```

## Logs

### 7/16/2026

Initial project setup. Rebranded the mod from the NeoForge template to Salt Models, and added the first set of custom blocks: four giant forge anvil variants (Giant, Jungle, Frost, and Golden Forges) and their shared invisible part block.

Changes:

- Rebranded mod ID, name, version, group ID, and author in `gradle.properties`
- Added the main mod class `SaltModels.java`
- Added `CustomAnvils.java` to register all custom blocks and items
- Added `CustomAnvilsTab.java` to register the mod's creative tab
- Added `GiantAnvilBlock.java` for multi-block giant anvil behavior
- Added `GiantAnvilPartBlock.java` for the invisible filler part block
- Added `log.java`, a logging utility ported from the EZBM C# project
- Added blockstate files for each forge variant and the anvil part
- Added block models for each forge variant (Giant, Jungle, Frost, Golden)
- Added item models for each forge variant
- Added the `en_us.json` language file
- Added anvil block tag data under `data/minecraft/tags/blocks/`
- Added texture assets for block faces
- Removed the NeoForge template placeholder files (`ExampleMod.java`, `Config.java`, `en_us.json` from examplemod)

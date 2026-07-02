# Copy Extra By QingHao

JetBrains / IntelliJ IDEA plugin for copying editor selections with file path, line range, and line numbers.

## Features

Editor popup menu under `Copy Extra`:

- `Copy Full Path and Line Range Only`
- `Copy Relative Path Line Range Only`
- `Copy Full Path Line Range Selected`
- `Copy Full Path Line Numbers Selected`
- `Copy Relative Path Line Range Selected`
- `Copy Relative Path Line Numbers Selected`
- `Copy Full Path Line Range`
- `Copy Full Path Line Numbers`
- `Copy Relative Path Line Range`
- `Copy Relative Path Line Numbers`

Project view popup menu under `Copy Extra`:

- `Copy With Relative Path`
- `Copy With Full Path`

## Shortcut

- Windows/Linux: `Ctrl+Shift+C`
- macOS: `Cmd+Shift+C`

In the editor this triggers `Copy Full Path Line Range Selected`.

## Build

```bash
./gradlew build
```

## Packaging

```bash
./gradlew buildPlugin
```

Plugin metadata is defined in `src/main/resources/META-INF/plugin.xml`.

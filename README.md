# Copy Extra By QingHao

JetBrains / IntelliJ IDEA plugin for copying editor selections with file path, line range, and line numbers.

## Features

Editor popup menu under `Copy Extra`:

- `Copy Full Path and Line Range Only`
- `Copy Relative Path Line Range Only`
- `Copy Full Path Line Range Scope Selected`
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

In the editor this triggers `Copy Full Path Line Range Scope Selected`.

`Copy Full Path Line Range Scope Selected` copies the selected code text only, while the header line range expands around the selection. The before/after line counts are configurable in `Settings | Tools | Copy Extra` and default to 5 lines before and 5 lines after the selection.

## Settings

Open `Settings | Tools | Copy Extra`:

- `Windows copy path format`: Windows-only full path output format.
- `Path prefix`: Prefix used for copied folders in Project View. Default: `Path:`.
- `File prefix`: Prefix used before copied file paths. Default: `File:`.
- `File suffix`: Suffix used after editor line ranges. Default: `行`.
- `Scope selected before lines`: Number of lines before the selection included in the `Copy Full Path Line Range Scope Selected` header. Default: `5`.
- `After lines`: Number of lines after the selection included in the `Copy Full Path Line Range Scope Selected` header. Default: `5`.

On Linux, macOS, and Unix, path format uses the default IDE path format.

## Build

```bash
./gradlew build
```

## Packaging

```bash
./gradlew buildPlugin
```

Plugin metadata is defined in `src/main/resources/META-INF/plugin.xml`.

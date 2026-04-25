package gdnwxf;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.EmptyClipboardOwner;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

class CopyWithLineNumbersHelper {
    enum CopyType {
        COPY_WITH_LINE_NUMBERS_WITH_FILE_NAME_AND_LINE_RANGE,
        COPY_WITH_FULL_FILE_PATH_AND_LINE_RANGE_ONLY,
        COPY_WITH_RELATIVE_FILE_PATH_AND_LINE_RANGE,
        COPY_WITH_RELATIVE_FILE_PATH_AND_LINE_RANGE_ONLY,
        COPY_WITH_RELATIVE_FILE_PATH_AND_LINE_RANGE_SELECTED,
        COPY_WITH_LINE_NUMBERS_WITH_FULL_FILE_PATH,
        COPY_WITH_LINE_NUMBERS_WITH_RELATIVE_FILE_PATH,
        COPY_WITH_LINE_NUMBERS_WITH_RELATIVE_FILE_PATH_SELECTED,
        COPY_WITH_LINE_NUMBERS_WITH_FULL_FILE_PATH_SELECTED,
        COPY_WITH_LINE_NUMBERS_WITH_FULL_FILE_PATH_AND_LINE_RANGE_SELECTED,
        COPY_WITH_FULL_FILE_PATH_SELECTED_ONLY,
        COPY_WITH_RELATIVE_FILE_PATH_SELECTED_ONLY,
        COPY_WITH_FULL_FILE_PATH_AND_LINE_RANGE_SELECTED,
    }

    /**
     * @param project 当前项目，计算相对路径时使用
     * @param editor 当前编辑器，用于读取选区和文档内容
     * @param copyWithLineNumbers 复制模式，决定是否附带文件信息
     * @param virtualFile 当前文件，用于输出文件名或路径
     * @returns 无返回值，直接将结果写入系统剪贴板
     * @description 按选区覆盖到的实际代码行复制内容，并根据复制模式决定是否附带文件信息和行号。
     */
    static void copyWithLineNumbers(@Nullable Project project, Editor editor, CopyType copyWithLineNumbers, VirtualFile virtualFile) {
        SelectionModel selectionModel = editor.getSelectionModel();
        Document document = editor.getDocument();
        int selectionStartOffset = selectionModel.getSelectionStart();
        int startLine = document.getLineNumber(selectionStartOffset);
        // 根据选中文本实际覆盖的行数计算结束行，避免整行复制时把下一行误算进来。
        int endLine = resolveSelectedEndLine(document, selectionStartOffset, selectionModel.getSelectedText());

        int lineStartOffset = document.getLineStartOffset(startLine);
        int lineEndOffset = document.getLineEndOffset(endLine);

        String text = document.getText(new TextRange(lineStartOffset, lineEndOffset));
        String[] lines = text.split("\n");

        StringBuilder sb = new StringBuilder();
        String path = virtualFile.getPath();
        // 统一计算相对路径，避免多个复制模式重复拼装相同逻辑。
        String relativePath = resolveRelativeFilePath(project, path);
        switch (copyWithLineNumbers) {
            case COPY_WITH_LINE_NUMBERS_WITH_FILE_NAME_AND_LINE_RANGE:
                sb.append("File: ")
                        .append(path)
                        .append(":")
                        .append(startLine + 1)
                        .append("-")
                        .append(endLine + 1)
                        .append("\n");
                break;
            case COPY_WITH_FULL_FILE_PATH_AND_LINE_RANGE_ONLY:
                sb.append("File: ")
                        .append(path)
                        .append(":")
                        .append(startLine + 1)
                        .append("-")
                        .append(endLine + 1)
                        .append("\n");
                break;
            case COPY_WITH_RELATIVE_FILE_PATH_AND_LINE_RANGE:
                sb.append("File: ")
                        .append(relativePath)
                        .append(":")
                        .append(startLine + 1)
                        .append("-")
                        .append(endLine + 1)
                        .append("\n");
                break;
            case COPY_WITH_RELATIVE_FILE_PATH_AND_LINE_RANGE_ONLY:
                sb.append("File: ")
                        .append(relativePath)
                        .append(":")
                        .append(startLine + 1)
                        .append("-")
                        .append(endLine + 1)
                        .append("\n");
                break;
            case COPY_WITH_RELATIVE_FILE_PATH_AND_LINE_RANGE_SELECTED:
                sb.append("File: ")
                        .append(relativePath)
                        .append(":")
                        .append(startLine + 1)
                        .append("-")
                        .append(endLine + 1)
                        .append("\n");
                break;
            case COPY_WITH_LINE_NUMBERS_WITH_FULL_FILE_PATH:
                sb.append("File: ")
                        .append(path)
                        .append(":")
                        .append(startLine + 1)
                        .append("-")
                        .append(endLine + 1)
                        .append("\n");
                break;
            case COPY_WITH_LINE_NUMBERS_WITH_RELATIVE_FILE_PATH:
                sb.append("File: ")
                        .append(relativePath)
                        .append(":")
                        .append(startLine + 1)
                        .append("-")
                        .append(endLine + 1)
                        .append("\n");
                break;
            case COPY_WITH_LINE_NUMBERS_WITH_RELATIVE_FILE_PATH_SELECTED:
                sb.append("File: ")
                        .append(relativePath)
                        .append(":")
                        .append(startLine + 1)
                        .append("-")
                        .append(endLine + 1)
                        .append("\n");
                break;
            case COPY_WITH_LINE_NUMBERS_WITH_FULL_FILE_PATH_SELECTED:
                sb.append("File: ")
                        .append(path)
                        .append(":")
                        .append(startLine + 1)
                        .append("-")
                        .append(endLine + 1)
                        .append("\n");
                break;
            case COPY_WITH_LINE_NUMBERS_WITH_FULL_FILE_PATH_AND_LINE_RANGE_SELECTED:
                sb.append("File: ")
                        .append(path)
                        .append(":")
                        .append(startLine + 1)
                        .append("-")
                        .append(endLine + 1)
                        .append("\n");
                break;
            case COPY_WITH_FULL_FILE_PATH_AND_LINE_RANGE_SELECTED:
                sb.append("File: ")
                        .append(path)
                        .append(":")
                        .append(startLine + 1)
                        .append("-")
                        .append(endLine + 1)
                        .append("\n");
                break;
            case COPY_WITH_FULL_FILE_PATH_SELECTED_ONLY:
                break;
            case COPY_WITH_RELATIVE_FILE_PATH_SELECTED_ONLY:
                break;
        }
        if (copyWithLineNumbers == CopyType.COPY_WITH_LINE_NUMBERS_WITH_FILE_NAME_AND_LINE_RANGE
                || copyWithLineNumbers == CopyType.COPY_WITH_RELATIVE_FILE_PATH_AND_LINE_RANGE) {
            // 该模式要求正文保持原始格式，不为每一行追加行号。
            sb.append(text);
            final Clipboard clipBoard = editor.getComponent().getToolkit().getSystemClipboard();
            clipBoard.setContents(new StringSelection(sb.toString()), EmptyClipboardOwner.INSTANCE);
            return;
        }
        if (copyWithLineNumbers == CopyType.COPY_WITH_RELATIVE_FILE_PATH_AND_LINE_RANGE_SELECTED
                || copyWithLineNumbers == CopyType.COPY_WITH_FULL_FILE_PATH_AND_LINE_RANGE_SELECTED) {
            // 该模式输出文件头和行号范围，并复制选中的原始代码。
            String selectedText = selectionModel.getSelectedText();
            if (selectedText != null) {
                sb.append(selectedText);
            }
            final Clipboard clipBoard = editor.getComponent().getToolkit().getSystemClipboard();
            clipBoard.setContents(new StringSelection(sb.toString()), EmptyClipboardOwner.INSTANCE);
            return;
        }
        if (copyWithLineNumbers == CopyType.COPY_WITH_FULL_FILE_PATH_AND_LINE_RANGE_ONLY
                || copyWithLineNumbers == CopyType.COPY_WITH_RELATIVE_FILE_PATH_AND_LINE_RANGE_ONLY) {
            // 该模式只输出文件头，不追加正文内容。
            final Clipboard clipBoard = editor.getComponent().getToolkit().getSystemClipboard();
            clipBoard.setContents(new StringSelection(sb.toString()), EmptyClipboardOwner.INSTANCE);
            return;
        }
        if (copyWithLineNumbers == CopyType.COPY_WITH_LINE_NUMBERS_WITH_FULL_FILE_PATH_SELECTED
                || copyWithLineNumbers == CopyType.COPY_WITH_LINE_NUMBERS_WITH_RELATIVE_FILE_PATH_SELECTED
                || copyWithLineNumbers == CopyType.COPY_WITH_LINE_NUMBERS_WITH_FULL_FILE_PATH_AND_LINE_RANGE_SELECTED) {
            // 该模式只复制选中部分，并为每行添加行号。
            String selectedText = selectionModel.getSelectedText();
            if (selectedText != null && !selectedText.isEmpty()) {
                String[] selectedLines = selectedText.split("\n", -1);
                for (int i = 0; i < selectedLines.length; i++) {
                    sb.append(String.format("%5d: %s\n", startLine + i + 1, selectedLines[i]));
                }
            }
            final Clipboard clipBoard = editor.getComponent().getToolkit().getSystemClipboard();
            clipBoard.setContents(new StringSelection(sb.toString()), EmptyClipboardOwner.INSTANCE);
            return;
        }
        if (copyWithLineNumbers == CopyType.COPY_WITH_FULL_FILE_PATH_SELECTED_ONLY
                || copyWithLineNumbers == CopyType.COPY_WITH_RELATIVE_FILE_PATH_SELECTED_ONLY) {
            // 该模式只复制选中部分，不加任何前缀和行号。
            String selectedText = selectionModel.getSelectedText();
            if (selectedText != null) {
                final Clipboard clipBoard = editor.getComponent().getToolkit().getSystemClipboard();
                clipBoard.setContents(new StringSelection(selectedText), EmptyClipboardOwner.INSTANCE);
            }
            return;
        }
        for (int i = 0; i < lines.length; i++) {
            sb.append(String.format("%5d: %s\n", startLine + i + 1, lines[i]));
        }

        final Clipboard clipBoard = editor.getComponent().getToolkit().getSystemClipboard();
        clipBoard.setContents(new StringSelection(sb.toString()), EmptyClipboardOwner.INSTANCE);
    }

    /**
     * @param document 当前编辑器文档
     * @param selectionStartOffset 选区起始偏移量
     * @param selectedText 当前选中的文本内容
     * @returns 选区最后一个实际命中的字符所在行号
     * @description 基于选中文本实际包含的换行数量计算结束行，避免整行选择附带行尾换行符时误带下一行。
     * @example 选择第 10 行全文且选区包含末尾换行符时，应该返回第 10 行而不是第 11 行。
     */
    private static int resolveSelectedEndLine(Document document, int selectionStartOffset, @Nullable String selectedText) {
        int startLine = document.getLineNumber(selectionStartOffset);
        if (selectedText == null || selectedText.isEmpty()) {
            return startLine;
        }

        // 先移除选区尾部仅用于跨行的换行符，再统计真正命中的行数。
        String textWithoutTrailingLineBreak = trimTrailingLineBreak(selectedText);
        if (textWithoutTrailingLineBreak.isEmpty()) {
            return startLine;
        }

        // 统计文本中的换行数量，每个换行代表额外覆盖一行。
        int selectedLineBreakCount = countLineBreak(textWithoutTrailingLineBreak);
        return Math.min(document.getLineCount() - 1, startLine + selectedLineBreakCount);
    }

    /**
     * @param text 选中的原始文本
     * @returns 移除尾部换行符后的文本
     * @description 去掉选区末尾因为“整行选择”附带的换行符，避免将下一行误判为有效内容。
     */
    private static String trimTrailingLineBreak(String text) {
        int endIndex = text.length();
        while (endIndex > 0) {
            char currentChar = text.charAt(endIndex - 1);
            if (currentChar != '\n' && currentChar != '\r') {
                break;
            }
            endIndex--;
        }
        return text.substring(0, endIndex);
    }

    /**
     * @param text 已去除尾部冗余换行符的文本
     * @returns 文本中的逻辑换行数量
     * @description 兼容 \n 和 \r\n，统计选区实际跨越了多少行。
     * @example 文本为 a\nb\nc 时返回 2，表示覆盖了 3 行。
     */
    private static int countLineBreak(String text) {
        int lineBreakCount = 0;
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (currentChar == '\n') {
                lineBreakCount++;
                continue;
            }
            if (currentChar == '\r') {
                lineBreakCount++;
                if (i + 1 < text.length() && text.charAt(i + 1) == '\n') {
                    i++;
                }
            }
        }
        return lineBreakCount;
    }

    /**
     * @param project 当前项目，用于获取项目根目录
     * @param path 当前文件绝对路径
     * @returns 项目内文件返回相对路径，无法计算时回退为原始绝对路径
     * @description 为需要展示相对路径的复制模式统一生成输出路径，避免分支内重复判断空项目和空根目录。
     * @example 项目根目录为 D:\abc\CopyWithLineNumbers，文件路径为 D:\abc\CopyWithLineNumbers\src\main\App.java 时返回 src\main\App.java。
     */
    static String resolveRelativeFilePath(@Nullable Project project, String path) {
        if (project == null) {
            return path;
        }

        String projectBasePath = project.getBasePath();
        if (projectBasePath == null) {
            return path;
        }

        try {
            // 仅当文件真实位于项目根目录内时才转换为相对路径，项目外文件保留绝对路径。
            Path normalizedProjectBasePath = Paths.get(Objects.requireNonNull(projectBasePath)).normalize();
            // 统一标准化当前文件路径，避免因为路径分隔符或冗余片段导致 startsWith 判断失真。
            Path normalizedFilePath = Paths.get(path).normalize();
            if (!normalizedFilePath.startsWith(normalizedProjectBasePath)) {
                return path;
            }
            // 生成项目内文件的相对路径，用于相对路径复制模式输出更短的结果。
            return normalizedProjectBasePath.relativize(normalizedFilePath).toString();
        } catch (InvalidPathException exception) {
            return path;
        }
    }
}

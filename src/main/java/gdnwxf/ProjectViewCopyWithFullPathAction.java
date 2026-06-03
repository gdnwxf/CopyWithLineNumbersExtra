package gdnwxf;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.EmptyClipboardOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ProjectViewCopyWithFullPathAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile[] files = resolveFiles(e);
        if (files == null || files.length == 0) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < files.length; i++) {
            if (i > 0) {
                sb.append("\n");
            }
            // 文件显示 File: 前缀，文件夹显示 Path: 前缀，再追加绝对路径。
            sb.append(files[i].isDirectory() ? "Path: " : "File: ");
            sb.append(files[i].getPath());
        }

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(sb.toString()), EmptyClipboardOwner.INSTANCE);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        // 仅在 Project View 存在有效选中项、且当前不在编辑器上下文时启用，避免与编辑器快捷键冲突。
        boolean hasProjectViewSelection = resolveFiles(e) != null;
        boolean isEditorContext = e.getData(CommonDataKeys.EDITOR) != null;
        e.getPresentation().setEnabledAndVisible(hasProjectViewSelection && !isEditorContext);
    }

    /**
     * @description 优先取多选数组，单选时 VIRTUAL_FILE_ARRAY 可能为 null，回退到 VIRTUAL_FILE。
     */
    @Nullable
    private static VirtualFile[] resolveFiles(@NotNull AnActionEvent e) {
        VirtualFile[] files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        if (files != null && files.length > 0) {
            return files;
        }
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        return file != null ? new VirtualFile[]{file} : null;
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}

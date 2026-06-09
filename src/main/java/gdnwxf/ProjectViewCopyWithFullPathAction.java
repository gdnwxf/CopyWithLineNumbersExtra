package gdnwxf;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.EmptyClipboardOwner;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ProjectViewCopyWithFullPathAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        var files = CopyWithLineNumbersHelper.resolveProjectViewFiles(e);
        if (files == null || files.length == 0) {
            return;
        }

        String text = CopyWithLineNumbersHelper.buildProjectViewPathText(project, files, false);

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(text), EmptyClipboardOwner.INSTANCE);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        // Project View 弹窗内按选中项启用；非弹窗场景仍避开编辑器上下文，避免与编辑器快捷键冲突。
        boolean hasProjectViewSelection = CopyWithLineNumbersHelper.resolveProjectViewFiles(e) != null;
        boolean isProjectViewPopup = ActionPlaces.PROJECT_VIEW_POPUP.equals(e.getPlace());
        boolean isEditorContext = e.getData(CommonDataKeys.EDITOR) != null;
        e.getPresentation().setEnabledAndVisible(hasProjectViewSelection && (isProjectViewPopup || !isEditorContext));
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}

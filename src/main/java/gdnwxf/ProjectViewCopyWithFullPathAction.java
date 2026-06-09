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
        // 该 action 只注册到 Project View 弹窗；启用条件不依赖节点数据源，避免目录节点因数据 key 差异置灰。
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}

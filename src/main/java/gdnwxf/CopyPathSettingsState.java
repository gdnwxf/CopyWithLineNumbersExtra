package gdnwxf;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;

@Service(Service.Level.APP)
@State(name = "CopyExtraSettings", storages = @Storage("copyExtraSettings.xml"))
public final class CopyPathSettingsState implements PersistentStateComponent<CopyPathSettingsState.StateData> {
    static final String DEFAULT_FILE_PREFIX = "File:";
    static final String DEFAULT_FILE_SUFFIX = "行";
    static final String DEFAULT_PATH_PREFIX = "Path:";

    private StateData stateData = new StateData();

    public static CopyPathSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(CopyPathSettingsState.class);
    }

    @Override
    public @NotNull StateData getState() {
        return stateData;
    }

    @Override
    public void loadState(@NotNull StateData state) {
        stateData = state;
    }

    WindowsCopyPathStyle getWindowsCopyPathStyle() {
        if (!SystemInfo.isWindows) {
            return WindowsCopyPathStyle.DEFAULT;
        }
        if (stateData.windowsCopyPathStyle == null) {
            return WindowsCopyPathStyle.DEFAULT;
        }
        return stateData.windowsCopyPathStyle;
    }

    void setWindowsCopyPathStyle(WindowsCopyPathStyle windowsCopyPathStyle) {
        stateData.windowsCopyPathStyle = windowsCopyPathStyle == null
                ? WindowsCopyPathStyle.DEFAULT
                : windowsCopyPathStyle;
    }

    String getFilePrefix() {
        return stateData.filePrefix == null ? DEFAULT_FILE_PREFIX : stateData.filePrefix;
    }

    void setFilePrefix(String filePrefix) {
        stateData.filePrefix = filePrefix == null ? DEFAULT_FILE_PREFIX : filePrefix;
    }

    String getFileSuffix() {
        return stateData.fileSuffix == null ? DEFAULT_FILE_SUFFIX : stateData.fileSuffix;
    }

    void setFileSuffix(String fileSuffix) {
        stateData.fileSuffix = fileSuffix == null ? DEFAULT_FILE_SUFFIX : fileSuffix;
    }

    String getPathPrefix() {
        return stateData.pathPrefix == null ? DEFAULT_PATH_PREFIX : stateData.pathPrefix;
    }

    void setPathPrefix(String pathPrefix) {
        stateData.pathPrefix = pathPrefix == null ? DEFAULT_PATH_PREFIX : pathPrefix;
    }

    public static final class StateData {
        public WindowsCopyPathStyle windowsCopyPathStyle = WindowsCopyPathStyle.DEFAULT;
        public String filePrefix = DEFAULT_FILE_PREFIX;
        public String fileSuffix = DEFAULT_FILE_SUFFIX;
        public String pathPrefix = DEFAULT_PATH_PREFIX;
    }

    public enum WindowsCopyPathStyle {
        DEFAULT("Default", "D:\\llama.cpp"),
        WSL("WSL", "/mnt/d/llama.cpp"),
        UNIX("Unix", "D:/llama.cpp"),
        CYGWIN("Cygwin", "/cygdrive/d/llama.cpp"),
        MSYS("MSYS / MSYS2", "/d/llama.cpp"),
        GIT_BASH("Git Bash", "/d/llama.cpp");

        private final String label;
        private final String example;

        WindowsCopyPathStyle(String label, String example) {
            this.label = label;
            this.example = example;
        }

        @Override
        public String toString() {
            return label + " (" + example + ")";
        }
    }
}

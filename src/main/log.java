import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Provides enhanced logging capabilities with contextual information and stack tracing.
 */
public class log
{
    /**
     * Defines the severity levels for log messages.
     */
    public enum Mode
    {
        /**
         * A standard informational message.
         */
        Message,
        /**
         * A warning indicating a potential issue that doesn't halt execution.
         */
        Warning,
        /**
         * An error message indicating a failure.
         */
        Error
    }

    // region Static Logging

    /**
     * Logs a trace message with contextual information to the console.
     *
     * @param message    The message to log.
     * @param printAs    Specifies the severity level of the message.
     * @param frameDepth The number of stack frames to skip.
     * @param printTrace Whether to print the full trace.
     */
    public static void Message(
        String message,
        Mode printAs,
        int frameDepth,
        boolean printTrace)
    {
        StackTraceElement[] frames = new Throwable().getStackTrace();

        if (frames == null || frames.length == 0)
        {
            throw new IllegalStateException(
                "No stack frames available for trace logging."
            );
        }

        StackTraceElement[] relevantFrames = filterFrames(frames, frameDepth);
        int depth = 0;

        for (StackTraceElement frame : relevantFrames)
        {
            String fqName = frame.getClassName();
            String methodName = frame.getMethodName();

            StringBuilder indentBuilder = new StringBuilder();
            if (depth == 0)
            {
                indentBuilder.append("\n");
            }
            for (int i = 0; i < depth; i++)
            {
                indentBuilder.append(" ");
            }
            String indent = indentBuilder.toString();
            String prefix;

            if (fqName != null && methodName != null)
            {
                int lastDot = fqName.lastIndexOf('.');
                String className = lastDot >= 0 
                    ? fqName.substring(lastDot + 1) 
                    : fqName;

                boolean isTopLevelMain = "main".equals(methodName) 
                    || "<Main>$".equals(methodName);
                if (isTopLevelMain)
                {
                    methodName = "MAIN";
                }

                int frameLine = frame.getLineNumber();

                String locationInfo = frameLine > 0
                    ? className + "." + methodName + ":" + frameLine
                    : className + "." + methodName + ":?";

                prefix = indent + "[" + locationInfo + "]";
            }
            else
            {
                String fileName = frame.getFileName();
                int frameLine = frame.getLineNumber();
                if (fileName == null)
                {
                    fileName = "UNKNOWN_FILE";
                }
                String lineStr = frameLine > 0 
                    ? String.valueOf(frameLine) 
                    : "?";
                String fileLineInfo = fileName + " @ line " + lineStr;
                prefix = indent + "[" + fileLineInfo + "]";
            }

            boolean isLastFrame = depth == relevantFrames.length - 1;
            if (isLastFrame)
            {
                String insert = "";
                switch (printAs)
                {
                    case Warning:
                        insert = "WARN: ";
                        break;
                    case Error:
                        insert = "ERROR: ";
                        break;
                    default:
                        insert = "";
                        break;
                }

                System.out.println(prefix + " " + insert + message);
            }
            else if (printTrace)
            {
                System.out.println(prefix);
            }

            depth++;
        }
    }

    /**
     * Logs an informational message without tracing the call stack.
     * <br><br>
     * Example:
     * <pre>
     * log.Me("Operation started.");
     * </pre>
     *
     * @param message The message to log.
     */
    public static void Me(String message)
    {
        Me(message, true, false);
    }

    /**
     * Logs an informational message without tracing the call stack.
     * <br><br>
     * Example:
     * <pre>
     * log.Me("Operation started.", true);
     * </pre>
     *
     * @param message The message to log.
     * @param enabled Whether logging is enabled.
     */
    public static void Me(String message, boolean enabled)
    {
        Me(message, enabled, false);
    }

    /**
     * Logs an informational message without tracing the call stack.
     * <br><br>
     * Example:
     * <pre>
     * log.Me("Operation started.", true, false);
     * </pre>
     *
     * @param message    The message to log.
     * @param enabled    Whether logging is enabled.
     * @param printTrace Whether to print call stack frames.
     */
    public static void Me(String message, boolean enabled, boolean printTrace)
    {
        if (!enabled) return;
        String safeMsg = message == null ? "" : message;
        Message(safeMsg, Mode.Message, 2, printTrace);
    }

    /**
     * Logs an informational message generated by a factory function.
     * <br><br>
     * Example:
     * <pre>
     * log.Me(() -&gt; "Count: " + list.size());
     * </pre>
     *
     * @param messageFactory The supplier returning the log message.
     */
    public static void Me(Supplier<String> messageFactory)
    {
        Me(messageFactory, true, false);
    }

    /**
     * Logs an informational message generated by a factory function.
     * <br><br>
     * Example:
     * <pre>
     * log.Me(() -&gt; "Count: " + list.size(), true);
     * </pre>
     *
     * @param messageFactory The supplier returning the log message.
     * @param enabled        Whether logging is enabled.
     */
    public static void Me(Supplier<String> messageFactory, boolean enabled)
    {
        Me(messageFactory, enabled, false);
    }

    /**
     * Logs an informational message generated by a factory function.
     * <br><br>
     * Example:
     * <pre>
     * log.Me(() -&gt; "Count: " + list.size(), true, false);
     * </pre>
     *
     * @param messageFactory The supplier returning the log message.
     * @param enabled        Whether logging is enabled.
     * @param printTrace     Whether to print call stack frames.
     */
    public static void Me(
        Supplier<String> messageFactory,
        boolean enabled,
        boolean printTrace)
    {
        if (!enabled) return;
        String msg = messageFactory.get();
        Message(msg, Mode.Message, 2, printTrace);
    }

    /**
     * Logs a warning message and optionally traces the call stack.
     * <br><br>
     * Example:
     * <pre>
     * log.Warn("Disk space low.");
     * </pre>
     *
     * @param message The warning message to log.
     */
    public static void Warn(String message)
    {
        Warn(message, true, false);
    }

    /**
     * Logs a warning message and optionally traces the call stack.
     * <br><br>
     * Example:
     * <pre>
     * log.Warn("Disk space low.", true);
     * </pre>
     *
     * @param message The warning message to log.
     * @param enabled Whether logging is enabled.
     */
    public static void Warn(String message, boolean enabled)
    {
        Warn(message, enabled, false);
    }

    /**
     * Logs a warning message and optionally traces the call stack.
     * <br><br>
     * Example:
     * <pre>
     * log.Warn("Disk space low.", true, false);
     * </pre>
     *
     * @param message    The warning message to log.
     * @param enabled    Whether logging is enabled.
     * @param printTrace Whether to print call stack frames.
     */
    public static void Warn(
        String message,
        boolean enabled,
        boolean printTrace)
    {
        if (!enabled) return;
        String safeMsg = message == null ? "" : message;
        Message(safeMsg, Mode.Warning, 2, printTrace);
    }

    /**
     * Logs a warning message generated by a factory function.
     * <br><br>
     * Example:
     * <pre>
     * log.Warn(() -&gt; "Connection timeout.");
     * </pre>
     *
     * @param messageFactory The supplier returning the warning message.
     */
    public static void Warn(Supplier<String> messageFactory)
    {
        Warn(messageFactory, true, false);
    }

    /**
     * Logs a warning message generated by a factory function.
     * <br><br>
     * Example:
     * <pre>
     * log.Warn(() -&gt; "Connection timeout.", true);
     * </pre>
     *
     * @param messageFactory The supplier returning the warning message.
     * @param enabled        Whether logging is enabled.
     */
    public static void Warn(Supplier<String> messageFactory, boolean enabled)
    {
        Warn(messageFactory, enabled, false);
    }

    /**
     * Logs a warning message generated by a factory function.
     * <br><br>
     * Example:
     * <pre>
     * log.Warn(() -&gt; "Connection timeout.", true, false);
     * </pre>
     *
     * @param messageFactory The supplier returning the warning message.
     * @param enabled        Whether logging is enabled.
     * @param printTrace     Whether to print call stack frames.
     */
    public static void Warn(
        Supplier<String> messageFactory,
        boolean enabled,
        boolean printTrace)
    {
        if (!enabled) return;
        String msg = messageFactory.get();
        Message(msg, Mode.Warning, 2, printTrace);
    }

    /**
     * Logs an error message and optionally traces the call stack.
     * <br><br>
     * Example:
     * <pre>
     * log.Err("Database connection failed.");
     * </pre>
     *
     * @param message The error message to log.
     */
    public static void Err(String message)
    {
        Err(message, true, false);
    }

    /**
     * Logs an error message and optionally traces the call stack.
     * <br><br>
     * Example:
     * <pre>
     * log.Err("Database connection failed.", true);
     * </pre>
     *
     * @param message The error message to log.
     * @param enabled Whether logging is enabled.
     */
    public static void Err(String message, boolean enabled)
    {
        Err(message, enabled, false);
    }

    /**
     * Logs an error message and optionally traces the call stack.
     * <br><br>
     * Example:
     * <pre>
     * log.Err("Database connection failed.", true, false);
     * </pre>
     *
     * @param message    The error message to log.
     * @param enabled    Whether logging is enabled.
     * @param printTrace Whether to print call stack frames.
     */
    public static void Err(String message, boolean enabled, boolean printTrace)
    {
        if (!enabled) return;
        String safeMsg = message == null ? "" : message;
        Message(safeMsg, Mode.Error, 2, printTrace);
    }

    /**
     * Logs an error message generated by a factory function.
     * <br><br>
     * Example:
     * <pre>
     * log.Err(() -&gt; "Transaction failed.");
     * </pre>
     *
     * @param messageFactory The supplier returning the error message.
     */
    public static void Err(Supplier<String> messageFactory)
    {
        Err(messageFactory, true, false);
    }

    /**
     * Logs an error message generated by a factory function.
     * <br><br>
     * Example:
     * <pre>
     * log.Err(() -&gt; "Transaction failed.", true);
     * </pre>
     *
     * @param messageFactory The supplier returning the error message.
     * @param enabled        Whether logging is enabled.
     */
    public static void Err(Supplier<String> messageFactory, boolean enabled)
    {
        Err(messageFactory, enabled, false);
    }

    /**
     * Logs an error message generated by a factory function.
     * <br><br>
     * Example:
     * <pre>
     * log.Err(() -&gt; "Transaction failed.", true, false);
     * </pre>
     *
     * @param messageFactory The supplier returning the error message.
     * @param enabled        Whether logging is enabled.
     * @param printTrace     Whether to print call stack frames.
     */
    public static void Err(
        Supplier<String> messageFactory,
        boolean enabled,
        boolean printTrace)
    {
        if (!enabled) return;
        String msg = messageFactory.get();
        Message(msg, Mode.Error, 2, printTrace);
    }

    /**
     * Filters out stack frames belonging to system, library, or logging classes.
     *
     * @param frames     The original array of stack frames.
     * @param frameDepth The depth to start filtering from.
     * @return An array of filtered stack frames.
     */
    private static StackTraceElement[] filterFrames(
        StackTraceElement[] frames,
        int frameDepth)
    {
        List<StackTraceElement> filtered = new ArrayList<>();
        for (int i = frameDepth; i < frames.length; i++)
        {
            StackTraceElement frame = frames[i];
            String className = frame.getClassName();

            if (className == null)
            {
                continue;
            }

            boolean isSystem = className.startsWith("java.")
                || className.startsWith("javax.")
                || className.startsWith("sun.")
                || className.startsWith("com.sun.")
                || className.startsWith("jdk.");

            boolean isLogger = className.equals("log")
                || className.endsWith(".log")
                || className.equals("Log")
                || className.endsWith(".Log");

            if (!isSystem && !isLogger)
            {
                filtered.add(frame);
            }
        }

        Collections.reverse(filtered);
        return filtered.toArray(new StackTraceElement[0]);
    }

    // endregion
}

package ru.vsu.cs.p_p_v.kriegspiel.client.cli;

public class ConsoleColor {
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    // https://stackoverflow.com/questions/1448858/how-to-color-system-out-println-output
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_DEFAULT = "\u001B[39m";
    public static final String ANSI_DEFAULT_BACKGROUND = "\u001B[49m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static final String ANSI_DARK_BLUE = "\u001B[38;2;20;106;203m";
    public static final String ANSI_DARK_ORANGE = "\u001B[38;2;189;112;12m";
    public static final String ANSI_DARK_MAGENTA = "\u001B[38;2;139;0;139m";
    public static final String ANSI_LIGHT_GRAY_BACKGROUND = "\u001B[48;2;232;232;232m";
    public static final String ANSI_GRAY_BACKGROUND = "\u001B[48;2;195;195;195m";
}

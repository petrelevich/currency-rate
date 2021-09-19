package ru.cbrrate.services.processors;

public enum CmdRegistry {
    START("/start", "messageTextProcessorStart");

    private final String cmd;
    private final String handlerName;

    CmdRegistry(String cmd, String handlerName) {
        this.cmd = cmd;
        this.handlerName = handlerName;
    }

    public String getCmd() {
        return cmd;
    }

    public String getHandlerName() {
        return handlerName;
    }
}

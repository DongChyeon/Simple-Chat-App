package com.dongchyeon.simplechatapp.model;

public class MessageData {
    private String type;
    private String from;
    private String to;
    private String content;
    private long sendTime;

    public MessageData(String type, String from, String to, String content, long sendTime) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.content = content;
        this.sendTime = sendTime;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getFrom() { return from; }

    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }

    public void setTo(String to) { this.to = to; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public long getSendTime() { return sendTime; }

    public void setSendTime(long sendTime) { this.sendTime = sendTime; }

    @Override
    public String toString() {
        return "MessageData{" +
                "type='" + type + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}

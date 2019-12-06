package id.ac.ui.cs.williamrumanta.directnotification;

import androidx.annotation.NonNull;

public class UserMessage {
    private String sender;
    private String message;

    public UserMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    @NonNull
    @Override
    public String toString() {
        return sender + ": " + message;
    }

    public String getSender() {
        return this.sender;
    }
    public String getMessage() {
        return this.message;
    }
}

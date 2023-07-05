package mza.thy.reminder;

public record EmailDepositReminder(String text) {

    String getText() {
        return text;
    }
}

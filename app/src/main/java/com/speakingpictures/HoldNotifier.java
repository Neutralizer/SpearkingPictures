package com.speakingpictures;

public class HoldNotifier {

    private int holdDuration;

    private boolean isPressed = false;
    private boolean isHeld = false;


    /**
     * Manages an internal timer
     *
     * @param holdDuration duration that the action must be held - in ms
     */
    HoldNotifier(int holdDuration) {//TODO decide if the timer will be external or internal
        this.holdDuration = holdDuration;
    }

    public int getHoldDuration() {
        return holdDuration;
    }

    /**
     * when the timer sets hold = true, check if the button is still pressed //TODO check if this is still needed
     *
     * @return
     */
    public boolean getIsPressed() {
        return this.isPressed;
    }

    /**
     * used on action_down
     */
    public void isPressed() {
        this.isPressed = true;
    }

    public void setHeld(boolean held) {
        isHeld = held;
    }

    public boolean isHeld() {
        return isHeld;
    }

    /**
     * used on action_up
     */
    public void holdReleased() {
        this.isPressed = false;
        this.isHeld = false;
    }

}

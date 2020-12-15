public class Event {

    //attributes============================================

    String trigger; //three values: "button", "walk", or "none" (maybe more in the future for quests/contingent triggers)
                    //button means when the player is facing the event and presses the interact button
                    //walk means it will be triggered as soon as the player walks on the tile with this event

    String eventType; //MapMove, Shop, Dialogue, Quest, Combat, Treasure, or none


    //only use these two ints for a MapMove object
    int newXPositionForPlayer;
    int newYPositionForPlayer;

    //constructors============================================
    public Event() {
        //not implemented yet
        setTrigger("none");
        setEventType("none");
        newXPositionForPlayer = -1; //meaning this Event is not a MapMove event
        newYPositionForPlayer = -1; //meaning this Event is not a MapMove event
    }

    public Event(String trigger, String eventType) {
        setTrigger(trigger);
        setEventType(eventType);
    }



    //getters and setters============================================

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    //methods that will only be used by subclasses
    //this is not an abstract superclass but you're really not supposed to use it
    //it's just non-abstract so that all tiles can have an event object even if there isn't an event on it
    public String getMapToLoad() {
        System.out.println("This Event is not a MapMove event, therefore this method does nothing");
        return "none";
    }

    public void setNewXPositionForPlayer(int newXPositionForPlayer) {
        this.newXPositionForPlayer = newXPositionForPlayer;
    }

    public int getNewXPositionForPlayer() {
        return newXPositionForPlayer;
    }

    public void setNewYPositionForPlayer(int newYPositionForPlayer) {
        this.newYPositionForPlayer = newYPositionForPlayer;
    }

    public int getNewYPositionForPlayer() {
        return newYPositionForPlayer;
    }

    public void setNewXY(int x, int y) {
        setNewXPositionForPlayer(x);
        setNewYPositionForPlayer(y);
    }

    //other methods============================================

    public void destroyEvent() {
        //System.out.println("generic Event destroyed");
        setEventType(null);
        setTrigger(null);
        //System.gc();
    }


}

public class Event {

    //attributes============================================

    String trigger; //three values: "button", "walk", or "none" (maybe more in the future for quests/contingent triggers)
                    //button means when the player is facing the event and presses the interact button
                    //walk means it will be triggered as soon as the player walks on the tile with this event

    String eventType; //MapMove, Shop, Dialogue, Quest, Combat, Treasure, or none

    //constructors============================================
    public Event() {
        //not implemented yet
        setTrigger("none");
        setEventType("none");
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

    //other methods============================================

    public void destroyEvent() {
        System.out.println("not implemented yet");
    }
}

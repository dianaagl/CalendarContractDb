package com.learning.calendarcontractdb;

/**
 * Created by Диана on 12.06.2017.
 */
public class Event {
    private Long id;
    private String title;
    private Long dtstart;
    private Long dtend;
    private String duration;
    private String rrule;
    private String rdate;
    private String eventPlace;
    private String calendar_id;
    private String description;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dtstart=" + dtstart +
                ", dtend=" + dtend +
                ", duration='" + duration + '\'' +
                ", rrule='" + rrule + '\'' +
                ", rdate='" + rdate + '\'' +
                ", eventPlace='" + eventPlace + '\'' +
                ", calendar_id='" + calendar_id + '\'' +
                '}';
    }

   @Override
   public boolean equals(Object o) {
       if (this == o) return true;
       if (!(o instanceof Event)) return false;

       Event event = (Event) o;

       if (id != null ? !id.equals(event.id) : event.id != null) return false;
       if (title != null ? !title.equals(event.title) : event.title != null) return false;
       if (dtstart != null ? !dtstart.equals(event.dtstart) : event.dtstart != null) return false;
       if (dtend != null ? !dtend.equals(event.dtend) : event.dtend != null) return false;
       if (duration != null ? !duration.equals(event.duration) : event.duration != null) return false;
       if (rrule != null ? !rrule.equals(event.rrule) : event.rrule != null) return false;
       if (rdate != null ? !rdate.equals(event.rdate) : event.rdate != null) return false;
       if (eventPlace != null ? !eventPlace.equals(event.eventPlace) : event.eventPlace != null) return false;
       if (calendar_id != null ? !calendar_id.equals(event.calendar_id) : event.calendar_id != null) return false;
       return description != null ? description.equals(event.description) : event.description == null;
   }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (dtstart != null ? dtstart.hashCode() : 0);
        result = 31 * result + (dtend != null ? dtend.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (rrule != null ? rrule.hashCode() : 0);
        result = 31 * result + (rdate != null ? rdate.hashCode() : 0);
        result = 31 * result + (eventPlace != null ? eventPlace.hashCode() : 0);
        result = 31 * result + (calendar_id != null ? calendar_id.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public Long getDtstart() {
        return dtstart;
    }

    public void setDtstart(Long dtstart) {
        this.dtstart = dtstart;
    }

    public Long getDtend() {
        return dtend;
    }

    public void setDtend(Long dtend) {
        this.dtend = dtend;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRrule() {
        return rrule;
    }

    public void setRrule(String rrule) {
        this.rrule = rrule;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public String getCalendar_id() {
        return calendar_id;
    }

    public void setCalendar_id(String calendar_id) {
        this.calendar_id = calendar_id;
    }


}

package com.nibou.niboucustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class EventResponseModel extends ErrorResponseModel implements Serializable {

    @SerializedName("past_events")
    private PastEvents past_events;

    public PastEvents getPast_events() {
        return past_events;
    }

    public void setPast_events(PastEvents past_events) {
        this.past_events = past_events;
    }

    public class PastEvents implements Serializable {

        @SerializedName("list")
        @Expose
        private ArrayList<EventList> list;

        @SerializedName("limit")
        @Expose
        private int limit;

        @SerializedName("n")
        @Expose
        private int n;

        @SerializedName("num_rows")
        @Expose
        private String numRows;

        public ArrayList<EventList> getList() {
            return list;
        }

        public void setList(ArrayList<EventList> list) {
            this.list = list;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public String getNumRows() {
            return numRows;
        }

        public void setNumRows(String numRows) {
            this.numRows = numRows;
        }
    }

    public class EventList implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("display")
        @Expose
        private String display;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("participants")
        @Expose
        private String participants;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("imgs")
        @Expose
        private ArrayList<Img> imgs;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getParticipants() {
            return participants;
        }

        public void setParticipants(String participants) {
            this.participants = participants;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public ArrayList<Img> getImgs() {
            return imgs;
        }

        public void setImgs(ArrayList<Img> imgs) {
            this.imgs = imgs;
        }
    }

    public class Img implements Serializable{

        @SerializedName("n")
        @Expose
        private int n;

        @SerializedName("img")
        @Expose
        private String img;

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}

package com.nibou.niboucustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListResponseModel extends ErrorResponseModel implements Serializable {

    @SerializedName("brands")
    private Model brands;

    @SerializedName("models")
    private Model models;

    @SerializedName("colors")
    private Model colors;

    @SerializedName("users")
    private Model users;

    @SerializedName("pages")
    private Model pages;

    @SerializedName("past_events")
    private Model past_events;

    @SerializedName("events")
    private Model events;

    @SerializedName("user")
    private Model user;

    @SerializedName("cars")
    private Model cars;

    @SerializedName("checkpoints")
    private Model checkpoints;

    public Model getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(Model checkpoints) {
        this.checkpoints = checkpoints;
    }

    public Model getEvents() {
        return events;
    }

    public void setEvents(Model events) {
        this.events = events;
    }

    public Model getUser() {
        return user;
    }

    public void setUser(Model user) {
        this.user = user;
    }

    public Model getCars() {
        return cars;
    }

    public void setCars(Model cars) {
        this.cars = cars;
    }

    public Model getColors() {
        return colors;
    }

    public void setColors(Model colors) {
        this.colors = colors;
    }

    public Model getUsers() {
        return users;
    }

    public void setUsers(Model users) {
        this.users = users;
    }

    public Model getPast_events() {
        return past_events;
    }

    public void setPast_events(Model past_events) {
        this.past_events = past_events;
    }

    public Model getPages() {
        return pages;
    }

    public void setPages(Model pages) {
        this.pages = pages;
    }

    public Model getBrands() {
        return brands;
    }

    public void setBrands(Model brands) {
        this.brands = brands;
    }

    public Model getModels() {
        return models;
    }

    public void setModels(Model models) {
        this.models = models;
    }

    public class Model implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("list")
        @Expose
        private ArrayList<ModelList> list;

        @SerializedName("limit")
        @Expose
        private int limit;

        @SerializedName("n")
        @Expose
        private int n;

        @SerializedName("num_rows")
        @Expose
        private String numRows;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ArrayList<ModelList> getList() {
            return list;
        }

        public void setList(ArrayList<ModelList> list) {
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

    public static class ModelList implements Serializable {

        @SerializedName("users")
        @Expose
        private ArrayList<ArrayItem> users;

        @SerializedName("brands")
        @Expose
        private ArrayList<ArrayItem> brands;

        @SerializedName("partners")
        @Expose
        private ArrayList<ArrayItem> partners;

        @SerializedName("event")
        @Expose
        private String event;

        @SerializedName("hours")
        @Expose
        private String hours;

        @SerializedName("minutes")
        @Expose
        private String minutes;

        @SerializedName("lat")
        @Expose
        private String lat;

        @SerializedName("lng")
        @Expose
        private String lng;

        @SerializedName("checked")
        @Expose
        private String checked;

        @SerializedName("user")
        @Expose
        private String user;
        @SerializedName("brand")
        @Expose
        private String brand;
        @SerializedName("model")
        @Expose
        private String model;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("model_name")
        @Expose
        private String modelName;
        @SerializedName("model_img")
        @Expose
        private String modelImg;
        @SerializedName("brand_name")
        @Expose
        private String brandName;
        @SerializedName("brand_img")
        @Expose
        private String brandImg;
        @SerializedName("color_name")
        @Expose
        private String colorName;

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("updated")
        @Expose
        private String updated;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("expiry")
        @Expose
        private String expiry;

        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("tiers")
        @Expose
        private String tiers;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("img")
        @Expose
        private String img;
        @SerializedName("docs")
        @Expose
        private List<Img> docs = null;
        @SerializedName("licenses")
        @Expose
        private List<Img> licenses = null;

        @SerializedName("text")
        @Expose
        private String text;

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

        @SerializedName("start")
        @Expose
        private String start;

        @SerializedName("end")
        @Expose
        private String end;

        @SerializedName("limit_guests")
        @Expose
        private String limit_guests;

        @SerializedName("fee")
        @Expose
        private String fee;

        @SerializedName("reservation")
        @Expose
        private Integer reservation;

        @SerializedName("imgs")
        @Expose
        private ArrayList<Img> imgs;


        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public String getMinutes() {
            return minutes;
        }

        public void setMinutes(String minutes) {
            this.minutes = minutes;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getChecked() {
            return checked;
        }

        public void setChecked(String checked) {
            this.checked = checked;
        }

        public Integer getReservation() {
            return reservation;
        }

        public void setReservation(Integer reservation) {
            this.reservation = reservation;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public String getLimit_guests() {
            return limit_guests;
        }

        public void setLimit_guests(String limit_guests) {
            this.limit_guests = limit_guests;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public ArrayList<ArrayItem> getUsers() {
            return users;
        }

        public void setUsers(ArrayList<ArrayItem> users) {
            this.users = users;
        }

        public ArrayList<ArrayItem> getBrands() {
            return brands;
        }

        public void setBrands(ArrayList<ArrayItem> brands) {
            this.brands = brands;
        }

        public ArrayList<ArrayItem> getPartners() {
            return partners;
        }

        public void setPartners(ArrayList<ArrayItem> partners) {
            this.partners = partners;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getModelImg() {
            return modelImg;
        }

        public void setModelImg(String modelImg) {
            this.modelImg = modelImg;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getBrandImg() {
            return brandImg;
        }

        public void setBrandImg(String brandImg) {
            this.brandImg = brandImg;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
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


        public ArrayList<Img> getImgs() {
            return imgs;
        }

        public void setImgs(ArrayList<Img> imgs) {
            this.imgs = imgs;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getTiers() {
            return tiers;
        }

        public void setTiers(String tiers) {
            this.tiers = tiers;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public List<Img> getDocs() {
            return docs;
        }

        public void setDocs(List<Img> docs) {
            this.docs = docs;
        }

        public List<Img> getLicenses() {
            return licenses;
        }

        public void setLicenses(List<Img> licenses) {
            this.licenses = licenses;
        }
    }

    public static class ArrayItem implements Serializable {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("model_name")
        @Expose
        private String modelName;

        @SerializedName("brand_name")
        @Expose
        private String brandName;

        @SerializedName("img")
        @Expose
        private String img;

        @SerializedName("guests")
        @Expose
        private String guests;

        public String getGuests() {
            return guests;
        }

        public void setGuests(String guests) {
            this.guests = guests;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }

    public static class Img implements Serializable {

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

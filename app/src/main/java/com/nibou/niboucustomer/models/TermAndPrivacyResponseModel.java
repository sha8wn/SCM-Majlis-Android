package com.nibou.niboucustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TermAndPrivacyResponseModel extends ErrorResponseModel implements Serializable {

    @SerializedName("pages")
    private Pages pages;

    public Pages getPages() {
        return pages;
    }

    public void setPages(Pages pages) {
        this.pages = pages;
    }

    public class Pages {

        @SerializedName("list")
        @Expose
        private ArrayList<PageList> list;

        @SerializedName("limit")
        @Expose
        private int limit;

        @SerializedName("n")
        @Expose
        private int n;

        @SerializedName("num_rows")
        @Expose
        private String numRows;

        public ArrayList<PageList> getList() {
            return list;
        }

        public void setList(ArrayList<PageList> list) {
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

    public class PageList {

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
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}

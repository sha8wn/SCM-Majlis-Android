package com.nibou.niboucustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ListResponseModel extends ErrorResponseModel implements Serializable {

    @SerializedName("brands")
    private Model brands;

    @SerializedName("models")
    private Model models;

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

    public class ModelList implements Serializable {

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

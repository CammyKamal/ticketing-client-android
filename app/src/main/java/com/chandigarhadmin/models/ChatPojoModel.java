package com.chandigarhadmin.models;


import java.util.ArrayList;
import java.util.List;


public class ChatPojoModel {
    private List<BranchesModel> departmentModelResponse;

    public boolean isAlignRight() {
        return alignRight;
    }

    public void setAlignRight(boolean alignRight) {
        this.alignRight = alignRight;
    }

    private boolean alignRight;

    private String type;

    public List<BranchesModel> getDepartmentResponse() {
        return departmentModelResponse;
    }


    public void setDepartmentResponse(List<BranchesModel> departmentModelResponse) {
        this.departmentModelResponse = departmentModelResponse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    private String input;
}
package com.calhacks.agks.database;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NutrientInfo {
    private int nutrientId;
    private String nutrientName;
    private float nutrientAmount;

    public NutrientInfo() {}
    public NutrientInfo(int nutrientId, String nutrientName, float nutrientAmount) {
        this.nutrientId = nutrientId;
        this.nutrientName = nutrientName;
        this.nutrientAmount = nutrientAmount;
    }

    public int getNutrientId() {
        return nutrientId;
    }

    public void setNutrientId(int nutrientId) {
        this.nutrientId = nutrientId;
    }

    public String getNutrientName() {
        return nutrientName;
    }

    public void setNutrientName(String nutrientName) {
        this.nutrientName = nutrientName;
    }

    public float getNutrientAmount() {
        return nutrientAmount;
    }

    public void setNutrientAmount(float nutrientAmount) {
        this.nutrientAmount = nutrientAmount;
    }
}

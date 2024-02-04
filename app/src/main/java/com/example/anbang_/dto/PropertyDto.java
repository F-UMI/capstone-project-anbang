package com.example.anbang_.dto;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "property")
public class PropertyDto {
    @PrimaryKey(autoGenerate = true)
    private Long propertyId;

    @ColumnInfo(name = "propertyName")
    private String propertyName;

    @ColumnInfo(name = "bargainerName")
    private String bargainerName;

    @ColumnInfo(name = "listingCreationDate")
    private String listingCreationDate;

    @ColumnInfo(name = "propertyType")
    private String propertyType;

    @ColumnInfo(name = "streetNameAddress")
    private String propertyAddress;

    @ColumnInfo(name = "detailedAddress")
    private String detailedAddress;

    @ColumnInfo(name = "propertySize")
    private String propertySize;

    @ColumnInfo(name = "numberOfRooms")
    private String numberOfRooms;

    @ColumnInfo(name = "typeOfPropertyTransaction")
    private String typeOfPropertyTransaction;

    @ColumnInfo(name = "propertyPrice")
    private String propertyPrice;

    @ColumnInfo(name = "maintenanceCost")
    private String maintenanceCost;

    @ColumnInfo(name = "availableMoveInDate")
    private String availableMoveInDate;

    public PropertyDto(Long propertyId, String propertyName, String bargainerName, String listingCreationDate, String propertyType, String propertyAddress, String detailedAddress, String propertySize, String numberOfRooms, String typeOfPropertyTransaction, String propertyPrice, String maintenanceCost, String availableMoveInDate) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.bargainerName = bargainerName;
        this.listingCreationDate = listingCreationDate;
        this.propertyType = propertyType;
        this.propertyAddress = propertyAddress;
        this.detailedAddress = detailedAddress;
        this.propertySize = propertySize;
        this.numberOfRooms = numberOfRooms;
        this.typeOfPropertyTransaction = typeOfPropertyTransaction;
        this.propertyPrice = propertyPrice;
        this.maintenanceCost = maintenanceCost;
        this.availableMoveInDate = availableMoveInDate;
    }

    public PropertyDto() {
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getBargainerName() {
        return bargainerName;
    }

    public void setBargainerName(String bargainerName) {
        this.bargainerName = bargainerName;
    }

    public String getListingCreationDate() {
        return listingCreationDate;
    }

    public void setListingCreationDate(String listingCreationDate) {
        this.listingCreationDate = listingCreationDate;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getPropertySize() {
        return propertySize;
    }

    public void setPropertySize(String propertySize) {
        this.propertySize = propertySize;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getTypeOfPropertyTransaction() {
        return typeOfPropertyTransaction;
    }

    public void setTypeOfPropertyTransaction(String typeOfPropertyTransaction) {
        this.typeOfPropertyTransaction = typeOfPropertyTransaction;
    }

    public String getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(String propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public String getMaintenanceCost() {
        return maintenanceCost;
    }

    public void setMaintenanceCost(String maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public String getAvailableMoveInDate() {
        return availableMoveInDate;
    }

    public void setAvailableMoveInDate(String availableMoveInDate) {
        this.availableMoveInDate = availableMoveInDate;
    }

    @Override
    public String toString() {
        return "PropertyDto{" +
                "propertyId=" + propertyId +
                ", propertyName='" + propertyName + '\'' +
                ", bargainerName='" + bargainerName + '\'' +
                ", listingCreationDate='" + listingCreationDate + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", propertyAddress='" + propertyAddress + '\'' +
                ", detailedAddress='" + detailedAddress + '\'' +
                ", propertySize='" + propertySize + '\'' +
                ", numberOfRooms='" + numberOfRooms + '\'' +
                ", typeOfPropertyTransaction='" + typeOfPropertyTransaction + '\'' +
                ", propertyPrice='" + propertyPrice + '\'' +
                ", maintenanceCost='" + maintenanceCost + '\'' +
                ", availableMoveInDate='" + availableMoveInDate + '\'' +
                '}';
    }
}


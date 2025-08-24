package org.accify.dto;


import lombok.Data;

@Data
public class Consignment {

    private String consignmentNo;
    private String date;
    private String fromLocation;
    private String toLocation;
    private Party consignor;
    private Party consignee;
    private String truckNo;
    private String mobile;
    private String gstin;
    private String description;
    private double goodsValue;
    private double amountPaid;

    @Data
    public static class Party {
        private String name;
        private String address;
    }
}

package com.handaebong.handaebong.model;

import android.app.Activity;

public class chat_message_model {
    private android.app.Activity Activity;
    private String Id;
    private String Category;
    private String Type;
    private String Message;
    private String UserName;
    private String DeviceId;
    private String CreatedAt;
    private String First_Id;
    private String First_Type;
    private String First_Message;
    private String First_UserName;
    private String First_DeviceId;
    private String First_CreatedAt;
    private Boolean Bol_exist;
    private Boolean Bol_linedate;
    private Boolean Bol_linetime;
    public chat_message_model(android.app.Activity activity, String id, String category, String type, String message, String userName, String deviceId, String createdAt, String first_Id, String first_Type, String first_Message, String first_UserName, String first_DeviceId, String first_CreatedAt, Boolean bol_exist,Boolean bol_linedate, Boolean bol_linetime){
        this.Activity = activity;
        this.Id = id;
        this.Category = category;
        this.Type = type;
        this.Message = message;
        this.UserName = userName;
        this.DeviceId = deviceId;
        this.CreatedAt = createdAt;
        this.First_Id = first_Id;
        this.First_Type = first_Type;
        this.First_Message = first_Message;
        this.First_UserName = first_UserName;
        this.First_DeviceId = first_DeviceId;
        this.First_CreatedAt = first_CreatedAt;
        this.Bol_exist = bol_exist;
        this.Bol_linedate = bol_linedate;
        this.Bol_linetime = bol_linetime;
    }

    public android.app.Activity getActivity() {
        return Activity;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public Boolean getBol_exist() {
        return Bol_exist;
    }

    public String getId() {
        return Id;
    }

    public String getCategory() {
        return Category;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public String getFirst_CreatedAt() {
        return First_CreatedAt;
    }

    public String getFirst_DeviceId() {
        return First_DeviceId;
    }

    public String getFirst_Id() {
        return First_Id;
    }

    public String getFirst_Message() {
        return First_Message;
    }

    public String getFirst_Type() {
        return First_Type;
    }

    public String getFirst_UserName() {
        return First_UserName;
    }

    public String getMessage() {
        return Message;
    }

    public String getType() {
        return Type;
    }

    public String getUserName() {
        return UserName;
    }

    public Boolean getBol_linedate() {
        return Bol_linedate;
    }

    public Boolean getBol_linetime() {
        return Bol_linetime;
    }
}

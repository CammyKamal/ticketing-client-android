package com.chandigarhadmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harendrasinghbisht on 23/09/17.
 */

public class CreateTicketResponse implements Parcelable{
    @SerializedName("attachments")
    @Expose
    private List<Object> attachments = null;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("comments")
    @Expose
    private List<Object> comments = null;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("reporter")
    @Expose
    private String reporter;
    @SerializedName("asignee")
    @Expose
    private String asignee;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("unique_id")
    @Expose
    private UniqueId uniqueId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("watcher_list")
    @Expose
    private List<Object> watcherList = null;

    protected CreateTicketResponse(Parcel in) {
        branch = in.readString();
        createdAt = in.readString();
        description = in.readString();
        id = in.readString();
        key = in.readString();
        priority = in.readString();
        reporter = in.readString();
        asignee = in.readString();
        source = in.readString();
        status = in.readString();
        subject = in.readString();
        tags = in.createStringArrayList();
        uniqueId = in.readParcelable(UniqueId.class.getClassLoader());
        updatedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(branch);
        dest.writeString(createdAt);
        dest.writeString(description);
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(priority);
        dest.writeString(reporter);
        dest.writeString(asignee);
        dest.writeString(source);
        dest.writeString(status);
        dest.writeString(subject);
        dest.writeStringList(tags);
        dest.writeParcelable(uniqueId, flags);
        dest.writeString(updatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CreateTicketResponse> CREATOR = new Creator<CreateTicketResponse>() {
        @Override
        public CreateTicketResponse createFromParcel(Parcel in) {
            return new CreateTicketResponse(in);
        }

        @Override
        public CreateTicketResponse[] newArray(int size) {
            return new CreateTicketResponse[size];
        }
    };

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public List<Object> getComments() {
        return comments;
    }

    public void setComments(List<Object> comments) {
        this.comments = comments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getAsignee() {
        return asignee;
    }

    public void setAsignee(String asignee) {
        this.asignee = asignee;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public UniqueId getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UniqueId uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Object> getWatcherList() {
        return watcherList;
    }

    public void setWatcherList(List<Object> watcherList) {
        this.watcherList = watcherList;
    }

}

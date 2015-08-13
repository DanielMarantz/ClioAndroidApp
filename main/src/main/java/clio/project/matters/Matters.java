package clio.project.matters;

import java.io.Serializable;

public class Matters implements Serializable {

    private String displayName;
    private String clientName;
    private String description;
    private String openDate;
    private String status;

    public Matters(String displayName, String clientName, String description, String openDate, String status) {
        super();
        this.displayName = displayName;
        this.clientName = clientName;
        this.description = description;
        this.openDate = openDate;
        this.status = status;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

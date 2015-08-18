package clio.project.matters;

/**
 * Created by Daniel Marantz on 15/08/15.
 *
 * Matters is a class modeling a Matter. With Getters and Setters
 * of Matter attributes.
 */
public class Matters {

    private String displayName;
    private String clientName;
    private String description;
    private String openDate;
    private String status;

    /**
     * No-Args constructor
     */
    public Matters(){
    }

    /**
     * Constructor of a Matter.
     *
     * @param displayName Display name of a Matter.
     * @param clientName  Client name of a Matter.
     * @param description Description of a Matter.
     * @param openDate    Open date of a Matter.
     * @param status      Status of a Matter.
     */
    public Matters(String displayName, String clientName, String description, String openDate, String status) {
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

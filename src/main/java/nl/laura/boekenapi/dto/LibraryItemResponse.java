package nl.laura.boekenapi.dto;

public class LibraryItemResponse {
    private Long id;
    private String locationCode;
    private Integer totalCopies;
    private Integer availableCopies;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }
    public Integer getTotalCopies() { return totalCopies; }
    public void setTotalCopies(Integer totalCopies) { this.totalCopies = totalCopies; }
    public Integer getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(Integer availableCopies) { this.availableCopies = availableCopies; }
}

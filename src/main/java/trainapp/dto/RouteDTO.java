package trainapp.dto;

public class RouteDTO {
    private Long id;
    private String source;
    private String destination;
    private int distanceKm;

    public RouteDTO() {}

    public RouteDTO(Long id, String source, String destination, int distanceKm) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.distanceKm = distanceKm;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public int getDistanceKm() {
        return distanceKm;
    }
    public void setDistanceKm(int distanceKm) {
        this.distanceKm = distanceKm;
    }

}
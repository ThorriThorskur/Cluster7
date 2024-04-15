package Interface;

import java.util.Date;
import java.util.UUID;

public abstract class InterfaceBooking extends InterfaceService {
    public enum ServiceType {
        Flight, Hotel, DayTrip
    }

    private UUID bookingId;
    private ServiceType serviceType;
    private long serviceId;
    private Date bookingDate;

    public InterfaceBooking(ServiceType serviceType, long serviceId, Date bookingDate) {
        this.bookingId = UUID.randomUUID();
        this.serviceType = serviceType;
        this.serviceId = serviceId;
        this.bookingDate = bookingDate;
    }

    public UUID getBookingId() { return bookingId; }
    public ServiceType getServiceType() { return serviceType; }
    public long getServiceId() { return serviceId; }
    public Date getBookingDate() { return bookingDate; }

    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }
}


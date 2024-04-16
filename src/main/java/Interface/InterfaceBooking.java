package Interface;

import java.util.UUID;

public abstract class InterfaceBooking {
    public enum ServiceType {
        Flight, Hotel, Tour
    }

    private UUID bookingId;
    private ServiceType serviceType;

    public InterfaceBooking(ServiceType serviceType) {
        this.bookingId = UUID.randomUUID();
        this.serviceType = serviceType;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }
}


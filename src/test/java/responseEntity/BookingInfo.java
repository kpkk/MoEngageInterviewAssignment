package responseEntity;

import requestEntity.Bookingdates;
import requestEntity.RequestEntityBuilder;

public class BookingInfo {


    private Number bookingid;
    private Booking bookingDetails;

    public Number getBookingid() {
        return bookingid;
    }

    public void setBookingId(int bookingId) {
        this.bookingid = bookingId;
    }

    public Booking getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDeatils(Booking bookingDeatils) {
        this.bookingDetails = bookingDetails;
    }




}

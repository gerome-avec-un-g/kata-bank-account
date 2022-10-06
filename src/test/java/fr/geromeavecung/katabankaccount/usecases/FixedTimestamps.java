package fr.geromeavecung.katabankaccount.usecases;

import fr.geromeavecung.katabankaccount.businessdomain.account.Timestamp;
import fr.geromeavecung.katabankaccount.businessdomain.account.Timestamps;

import java.time.LocalDateTime;

public class FixedTimestamps implements Timestamps {

    private Timestamp timestamp = new Timestamp(LocalDateTime.parse("2022-01-01T00:00:00"));

    @Override
    public Timestamp now() {
        return timestamp;
    }

    public void setTimestamp(String value) {
        this.timestamp = new Timestamp(LocalDateTime.parse(value));
    }
}

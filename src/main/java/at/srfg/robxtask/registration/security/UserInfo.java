package at.srfg.robxtask.registration.security;

public class UserInfo {

    String ublPartyID;
    String ublPersonID;

    public String getUblPartyID() {
        return ublPartyID;
    }

    public void setUblPartyID(String ublPartyID) {
        this.ublPartyID = ublPartyID;
    }

    public String getUblPersonID() {
        return ublPersonID;
    }

    public void setUblPersonID(String ublPersonID) {
        this.ublPersonID = ublPersonID;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "ublPartyID='" + ublPartyID + '\'' +
                ", ublPersonID='" + ublPersonID + '\'' +
                '}';
    }
}

import java.time.LocalDate;

 class Member {
    private final String personalNumber;
    private final LocalDate lastPaymentDate;

    public Member(String personalNumber, LocalDate lastPaymentDate) {
        this.personalNumber = personalNumber;
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public LocalDate getLastPaymentDate() {
        return lastPaymentDate;
    }
}


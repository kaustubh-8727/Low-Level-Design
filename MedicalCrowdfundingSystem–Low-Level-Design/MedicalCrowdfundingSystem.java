
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


enum PatientStatus {
    CRITICAL,
    STABLE,
    GOOD
}

enum CampaignStatus {
    ACTIVE,
    COMPLETED,
    FAILED
}

enum DonationStatus {
    SUCCESS,
    FAILED
}

class User {
    private String userId;
    private String userName;
    private String email;
    private String phone;
    private String userType;

    public User(String userName, String email, String phone, String userType) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.userId = UUID.randomUUID().toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

class Patient {
    private String patientId;
    private String disease;
    private String description;
    private int durationInDays;
    private String medicalHistory;
    private String hospitalId;
    private PatientStatus status;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public PatientStatus getStatus() {
        return status;
    }

    public void setStatus(PatientStatus status) {
        this.status = status;
    }
}

class Campaign {
    private String campaignId;
    private String patientId;
    private double targetAmount;
    private double currentAmount;
    private CampaignStatus status;
    private Date startDate;
    private Date endDate;

    public Campaign(String campaignId, String patientId, double targetAmount,
                    double currentAmount, CampaignStatus status,
                    Date startDate, Date endDate) {
        this.campaignId = campaignId;
        this.patientId = patientId;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public String getPatientId() {
        return patientId;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

class Donation {
    private String donorId;
    private String patientId;
    private String campaignId;
    private double amount;
    private boolean isAnonymous;
    private Date timestamp;
    private DonationStatus status;

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setDonationStatus(DonationStatus status) {
        this.status = status;
    }
}

class CampaignService {
    private Map<String, Campaign> campaignMap = new HashMap<>();

    public Campaign createCampaign(String patientId, double targetAmount, int durationInDays) {

        String campaignId = UUID.randomUUID().toString();
        Date startDate = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE, durationInDays);
        Date endDate = cal.getTime();

        Campaign campaign = new Campaign(
                campaignId,
                patientId,
                targetAmount,
                0.0,
                CampaignStatus.ACTIVE,
                startDate,
                endDate
        );

        campaignMap.put(campaignId, campaign);

        return campaign;
    }

    public void updateCampaign(Campaign campaign) throws Exception {
        String campaignId = campaign.getCampaignId();
        if(!campaignMap.containsKey(campaignId)) {
            throw new Exception("campaignId is not valid");
        }

        campaignMap.put(campaignId, campaign);
    }

    public void deleteCampaign(String campaignId) throws Exception {
        if(!campaignMap.containsKey(campaignId)) {
            throw new Exception("campaignId is not valid");
        }
        campaignMap.remove(campaignId);
    }

    public void closeCampaign(String campaignId) throws Exception {
        if(!campaignMap.containsKey(campaignId)) {
            throw new Exception("campaignId is not valid");
        }
        
        Campaign campaign = campaignMap.get(campaignId);
        campaign.setStatus(CampaignStatus.COMPLETED);
    }

    public Campaign getCampaign(String campaignId) {
        return campaignMap.get(campaignId);
    }
}

class DonationService {
    private Map<String, List<Donation>> donationsMap = new HashMap<>();
    private PaymentService paymentService;
    private CampaignService campaignService;
    private CampaignTrackingService trackingService;
    private NotificationService notificationService;

    public DonationService(PaymentService paymentService,
                           CampaignService campaignService,
                           CampaignTrackingService trackingService,
                           NotificationService notificationService) {
        this.paymentService = paymentService;
        this.campaignService = campaignService;
        this.trackingService = trackingService;
        this.notificationService = notificationService;
    }

    public void makeDonation(Donation donation) throws Exception {

        Campaign campaign = campaignService.getCampaign(donation.getCampaignId());
        if (campaign == null || campaign.getStatus() != CampaignStatus.ACTIVE) {
            throw new IllegalStateException("Invalid or inactive campaign");
        }

        if (donation.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        boolean success = paymentService.makePayment(donation.getAmount());

        if (success) {
            donation.setDonationStatus(DonationStatus.SUCCESS);

            donationsMap
                .computeIfAbsent(donation.getCampaignId(), k -> new ArrayList<>())
                .add(donation);

            trackingService.updateCampaignAmount(
                donation.getCampaignId(),
                donation.getAmount()
            );

            // notificationService.notifyDonationSuccess(donation);

        } else {
            donation.setDonationStatus(DonationStatus.FAILED);
        }
    }
}

interface PaymentService {
    public boolean makePayment(double amount);
}

class UPIPayment implements PaymentService {

    public boolean makePayment(double amount) {
        // perform payment processing
        return true;
    }
}

class CardPayment implements PaymentService {

    public boolean makePayment(double amount) {
        // perform payment processing
        return true;
    }
}

class CampaignTrackingService {
    private CampaignService campaignService;

    public CampaignTrackingService(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    public void updateCampaignAmount(String campaignId, double amount) throws Exception {

        Campaign campaign = campaignService.getCampaign(campaignId);

        if (campaign == null) {
            throw new IllegalStateException("Campaign not found");
        }

        synchronized (campaign) {
            double updatedAmount = campaign.getCurrentAmount() + amount;
            campaign.setCurrentAmount(updatedAmount);

            if (updatedAmount >= campaign.getTargetAmount()) {
                campaignService.closeCampaign(campaignId);
            }
        }
    }
}

interface Notifier {
    public void notifyUser(User user, String message);
}

class EmailNotifier implements Notifier {

    @Override
    public void notifyUser(User user, String message) {
        System.out.println("Email sent to " + user.getEmail() + ": " + message);
    }
}

class SMSNotifier implements Notifier {

    @Override
    public void notifyUser(User user, String message) {
        System.out.println("SMS sent to " + user.getPhone() + ": " + message);
    }
}

class PushNotifier implements Notifier {

    @Override
    public void notifyUser(User user, String message) {
        System.out.println("Push notification sent to user " + user.getUserId() + ": " + message);
    }
}

class NotificationService {

    private List<Notifier> notifiers;

    public NotificationService(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    public void notifyAll(User user, String message) {
        for (Notifier notifier : notifiers) {
            notifier.notifyUser(user, message);
        }
    }

    public void notifyDonationSuccess(User user) {
        for (Notifier notifier : notifiers) {
            notifier.notifyUser(user, new String("donation successful"));
        }
    }
}

class MedicalCrowdfundingSystem {
    public static void main(String[] args) throws Exception {

        User patientUser = new User("Rahul", "rahul@gmail.com", "9999999999", "PATIENT");
        User donorUser = new User("Amit", "amit@gmail.com", "8888888888", "DONOR");

        Patient patient = new Patient();
        patient.setPatientId(patientUser.getUserId());
        patient.setDisease("fever");
        patient.setDescription("mild fever 102 degree");
        patient.setDurationInDays(60);
        patient.setMedicalHistory("No prior major illness");
        patient.setHospitalId("HOSP123");
        patient.setStatus(PatientStatus.CRITICAL);

        CampaignService campaignService = new CampaignService();
        CampaignTrackingService trackingService = new CampaignTrackingService(campaignService);

        PaymentService paymentService = new UPIPayment();

        List<Notifier> notifiers = List.of(
                new EmailNotifier(),
                new SMSNotifier(),
                new PushNotifier()
        );
        NotificationService notificationService = new NotificationService(notifiers);

        DonationService donationService = new DonationService(
                paymentService,
                campaignService,
                trackingService,
                notificationService
        );

        Campaign campaign = campaignService.createCampaign(
                patient.getPatientId(),
                10000.0,
                30
        );

        System.out.println("Campaign created with ID: " + campaign.getCampaignId());

        Donation donation = new Donation();
        donation.setDonorId(donorUser.getUserId());
        donation.setPatientId(patient.getPatientId());
        donation.setCampaignId(campaign.getCampaignId());
        donation.setAmount(5000.0);
        donation.setAnonymous(false);
        donation.setTimestamp(new Date());

        donationService.makeDonation(donation);

        Campaign updatedCampaign = campaignService.getCampaign(campaign.getCampaignId());

        System.out.println("Updated Campaign Amount: " + updatedCampaign.getCurrentAmount());
        System.out.println("Campaign Status: " + updatedCampaign.getStatus());

        Donation donation2 = new Donation();
        donation2.setDonorId(donorUser.getUserId());
        donation2.setPatientId(patient.getPatientId());
        donation2.setCampaignId(campaign.getCampaignId());
        donation2.setAmount(6000.0);
        donation2.setAnonymous(true);
        donation2.setTimestamp(new Date());

        donationService.makeDonation(donation2);

        updatedCampaign = campaignService.getCampaign(campaign.getCampaignId());

        System.out.println("Final Campaign Amount: " + updatedCampaign.getCurrentAmount());
        System.out.println("Final Campaign Status: " + updatedCampaign.getStatus());
    }
}

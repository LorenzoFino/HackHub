package unicam.hackhub.infrastructure.services.payment;

import org.springframework.stereotype.Component;

/**
 * Mock implementation of the external payment system.
 * In production this would integrate with a real payment provider.
 * Used to award the prize money to the winning team.
 */
@Component
public class MockPaymentAdapter {

    /**
     * Simulates a payment to the winning team.
     *
     * @param teamName  name of the winning team
     * @param amount    prize amount in euros
     * @return true if payment succeeded, false otherwise
     */
    public boolean processPayment(String teamName, Double amount) {
        if (teamName == null || amount == null || amount <= 0) {
            System.out.println("[PAYMENT] Invalid payment request for team: " + teamName);
            return false;
        }

        System.out.println("[PAYMENT] Processing payment of €" + amount +
                " to team: " + teamName);

        // Simulate successful payment
        System.out.println("[PAYMENT] Payment successful for team: " + teamName);
        return true;
    }
}
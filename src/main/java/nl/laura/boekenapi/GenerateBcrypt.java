package nl.laura.boekenapi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateBcrypt {
    public static void main(String[] args) {
        String raw = args.length > 0 ? args[0] : "secret";
        String hash = new BCryptPasswordEncoder().encode(raw);
        System.out.println("RAW: " + raw);
        System.out.println("HASH: " + hash);
    }
}


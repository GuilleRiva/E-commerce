package com.ecommerce.ecommerce.devtools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        String[] users = {
                "Rusorodriguez50",
                "GodoyErik4",
                "Alvarez16",
                "TobiRamirez2",
                "Prieto20",
                "RomanVega6",
                "Alan8",
                "Fattori24",
                "AlanLescano",
                "NicoOroz21",
                "RodriguezMorta7",
                "TomasMolina27",
                "EmiViveros29",
                "MaxiRomero9",
                "CrisFerreira",
                "CesarCarranza1",
                "CrisFerreira",
                "GuilleRiva7",
                "Torres9",
                "Breitenbruch25",
                "Lamendola16",
                "TomasDurso25",
                "Martinez4",
                "Delossantos3",
                "Ferrari6",
                "ASanchez",
                "Tesuri18",
                "Bajamich9",
                "NicoCastro11",
                "Nicola10",
                "Pulga7",
                "LocoDiaz18",
                "Coronel37",
                "Cabrera29"
        };

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        for (String user : users){
            String hashedPassword = encoder.encode(user);
            System.out.println("Username " + user);
            System.out.println("Hashed " + hashedPassword);
            System.out.println("---------------------------");
        }
    }
}

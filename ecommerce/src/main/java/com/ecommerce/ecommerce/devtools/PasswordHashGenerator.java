package com.ecommerce.ecommerce.devtools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {

        String[] users = {
                "Ruso50",
                "ErikGodoy4",
                "RamirezTobi2",
                "AlvarezFran16",
                "RomanVega6",
                "Prieto20",
                "Alan8",
                "Fattori24",
                "Lescano10",
                "RomeroMaxi9",
                "GuilleRiva7",
                "Molina27",
                "Durso25",
                "Martinez4",
                "Ferrari6",
                "Matidls3",
                "Sanchez5",
                "Bajamich9",
                "CoronelMateo37",
                "PulgaRodriguez7",
                "Carranza1",
                "Breitenbruch25",
                "EliasTorres9",
                "PomboJoaquin6",
                "GeroRivera7",
                "Sepulveda11",
                "jappertKevin14",
                "TobioFer2",
                "PochoInsua6",
                "IvanTapia23",
                "ToroMorales9",
                "NicoBlandi37",
                "FaustoGrillo37",
                "Menossi15",
                "FacuQuignon23",
                "SantiLongo5",
                "ChinoZelarrayan10",
                "UvitaFer22",
                "UliSanchez12",
                "Passerini9",
                "FrancoJara29",
                "Marchesin25",
                "LeanBrey12",
                "Advincula17",
                "ChapuLema2",
                "NicoFigal4",
                "Lauty40",
                "Ayrton32",
                "MarcosRojo6",
                "Blanco23",
                "CheloSarachi3",
                "TotoBelmonte30",
                "Delgado43",
                "NachoMiramon14",
                "Alarcon15",
                "AnderHerrera21",
                "Palacios8",
                "KevinZenon22",
                "AlanVelasco20",
                "ChangoZeballos7",
                "BraianAguirre33",
                "Jansonpibe11",
                "Labestia16",
                "EdiCavani10",
                "caboGimenez10",
                "LautaroRivero19",
                "Cufre3",
                "pupaHeredia12",
                "GastonVeron10",
                "LuisAngulo7",
                "Cabral29",
                "NachoArce1",
                "MiltonCeliz8",
                "Sanchez35",
                "JonyHerrera9"

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

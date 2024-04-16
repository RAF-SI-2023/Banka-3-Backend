Feature: Trestiranje contact kontrolera - integracioni testovi

  Scenario: Admin hoce da vidi sve kontakte i da zatim jedan promeni, a jedan da obrise
    Given Admin logovan
    When Admin zatrazi sve kontakte i jedan obrise onaj sa id "1", a drugi id "2" promeni myName "dugovanje epsu"
    Then Adminu se vracaju svi kontakti, osim onog obrisanog sa id "1", i izmenjeni kontakt

  Scenario: Korisnik pravi novi kontakt
    Given Korisnik logovan
    When Korisnik kreira kontakt userId "3", myName "Placanje teretane", name "Zikica Zikic", accountNumber "33333333333"
    Then Korisniku userId "3" se vracaju svi njegovi kontakti, zajedno sa ovim novim
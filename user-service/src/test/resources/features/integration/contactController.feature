Feature: Trestiranje contact kontrolera - integracioni testovi

  Scenario: Korisnik pravi novi kontakt
    When Korisnik kreira kontakt userId "1", myName "Zika dinarksi", name "Zikica Zikic", accountNumber "1122334455667788"
    Then Korisnik menja kontakt predhodno napravljen, menja myName u "Zika eurski"

  Scenario: Admin hoce da vidi sve kontakte i da zatim jedan promeni, a jedan da obrise
    Given Admin logovan
    When Amin trazi sve kontakte od usera sa id "1"
    Then Admin brise kontakt


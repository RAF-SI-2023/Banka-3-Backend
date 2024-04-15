Feature: Trestiranje company account kontrolera - integracioni testovi

  Scenario: Admin zeli da vidi sve company account-e
    Given Admin je ulogovan
    When Admin salje zahtev za sve company account-e
    Then Sistem vraca sve company account-e

  Scenario: Admin zeli da vidi jedan company account po kompanijama
    When Admin salje zahtev za company account sa companyId-jem "1"
    Then Sistem vraca company account sa companyId-jem "1"

  Scenario: Admin zeli da vidi jedan company account po broju naloga
    When Admin salje zahtev za company account sa brojem "1010101010101010"
    Then Sistem vraca company account sa brojem "1010101010101010"

  Scenario: Dodavanje company account-a
      When Kreiramo novi company nalog id "1"
      Then Povlacimo nalog "1" iz baze i proveravamo da li je kreiran po companyId-ju

  Scenario: Brisanje company account-a
    When Admin salje zahtev za brisanje company account-a sa id-jem "1"
    Then Sistem brise company account sa brojem "1010101010101010"

  Scenario: Provera balansa company account-a
    When Slanje zahteva za proveru balansa
    Then Sistem vraca odgovor u zavisnosti od toga da li ima dovoljno sredstava

  Scenario: Rezervacija novca na company account-u
    When Slanje zahteva za rezervaciju novca
    Then Sistem vraca odgovor u zavisnosti od toga da li je rezervacija uspesna

  Scenario: Uklanjanje rezervacije novca na company account-u
    When Slanje zahteva za uklanjanje rezervacije novca
    Then Sistem vraca odgovor u zavisnosti od toga da li je uklanjanje rezervacije uspesno

  Scenario: Dodavanje novca na company account
    When Slanje zahteva za dodavanje novca
    Then Sistem vraca odgovor u zavisnosti od toga da li je dodavanje uspesno

  Scenario: Uzimanje novca sa company account-a
    When Slanje zahteva za uklanjanje uzimanje novca
    Then Sistem vraca odgovor u zavisnosti od toga da li je uzimanje uspesno


Feature: Testiranje creditTransaction kontrolera - integracioni testovi

  Scenario: Kreiranje kredit transakcije
    When Korisnik kreira kredit transakciju
    Then Kredit transakcija je kreirana
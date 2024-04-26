Feature: E2e test kredit od strane korisnika
  Scenario: Kredit od strane korisnika
    Given Korisnik se loguje na aplikaciju sa email "jristic3620rn@raf.rs" i sifrom "user1234"
    And korisnik cekira balans na svom "EUR" racunu
    When korisnik salje zahtev za kredit
    Then Zaposleni se loguje na aplikaciju i odobrava zahtev
    And korisnik cekira balans
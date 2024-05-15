Feature: E2e test za podizanje para sa kartice
  Scenario: Podizanje para sa kartice
    Given korisnik se loguje "jristic3620rn@raf.rs" i lozinkom "user1234"
    And korisnik cekira balans na dinarskom racunu
    When korisnik se loguje preko kartice
    And korisnik podize pare sa kartice
    Then korisnik cekira balans na dinarskom racunu nakon podizanja
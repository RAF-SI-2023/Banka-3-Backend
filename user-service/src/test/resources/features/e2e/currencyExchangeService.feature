Feature: E2e test menjacnica
  Scenario: Menjacnica
    Given korisnik se loguje "jristic3620rn@raf.rs" i lozinkom "user1234"
    And korisnik cekira dinarski racun
    And korisnik cekira euro racun
    And provera stanja dinarskog i euro racuna banke
    When korisnik izvrsava konverziju novca
    Then provera stanja dinarskog i euro racuna banke nako konverzije
    And korisnik cekira balans na euro i dinarskom racunu

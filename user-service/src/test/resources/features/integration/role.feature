Feature:
  Scenario: Pregled svig rola
    Given zaposleni se loguje na sistem
    When korisnik zeli da pretrazi sve role
    Then sistem prikazuje sve role

  Scenario: Pregled role po imenu
    When korisnik zeli da pretrazi role po imenu
    Then sistem prikazuje role sa zadatim imenom "ROLE_ADMIN"
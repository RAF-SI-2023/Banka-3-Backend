Feature:
  Scenario: Pregled svig permisija
    Given radnik se loguje na sistem
    When korisnik zeli da pretrazi svih permisija
    Then sistem prikazuje sve permisije

  Scenario: Pregled permisije po imenu
    When korisnik zeli da pretrazi permmisije po imenu
    Then sistem prikazuje permisije sa zadatim imenom "CAN_TRADE"
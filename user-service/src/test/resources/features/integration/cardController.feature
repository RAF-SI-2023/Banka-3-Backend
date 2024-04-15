Feature: Testiranje card kontrolera - integracioni testovi

  Scenario: Povlacenje svih kartica korisnika
    Given Ulogovani smo kao admin
    When Korisnik sa id-em "1" povuce sve svoje kartice
    Then Vrati sve kartice korisnika sa id-em "1"

  Scenario: Deaktivacija kartice korisnika
    When Korisnik deaktivira karticu sa id-em "1"
    Then Kartica sa id-em "1" je deaktivirana

  Scenario: Aktivacija kartice korisnika
    When Korisnik aktivira karticu sa id-em "1"
    Then Kartica sa id-em "1" je aktivirana

#    --------------------?????
  Scenario: Kreiranje kartice korisnika
    When Korisnik sa id-em "1" kreira karticu
    Then Kartica je kreirana za korisnika sa id-em "1"

  Scenario: Podizanje gotovine
    When Korisnik dize gotovinu
    Then Sistem vraca potvrdan response

  Scenario: Logovanje kartice korisnika
    When Korisnik loguje karticu
    Then Sistem vraca ulogovanu karticu

  Scenario: Depozit novca
    When Korisnik uplati novac
    Then Sistem vraca potvrdan response
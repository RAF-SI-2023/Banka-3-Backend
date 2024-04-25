Feature: Trestiranje company kontrolera - integracioni testovi

  Scenario: kompanija menja sifru i ponovo se loguje
    Given kompanija se loguje sa sa emailom "exchange@gmail.com" i lozinkom "exchange1234"
    When kompanija zeli da promeni lozinku u "exchange1234"
    Then kompanija se ponovo loguje

  Scenario: Admin se loguje, kreira novu kompaniju i setuje da su aktivni
    Given admin se loguje sa emailom "admin@admin.com" i lozinkom "admin1234"
    When admin kreira novu kompaniju sa emailom "newComp@gmail.com" i lozinkom "new1234"
    Then nova kompanija je aktivana

    Scenario: dohvatanje svih kompanija
      When admin dohvati sve kompanije
      Then admin provaerava da li je kompanija aktivana

  Scenario: dohvatanje kompanije po id-u
    When admin dohvati kompaniju po id-u
    Then admin setuje da je kompanije neaktivna
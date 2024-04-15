Feature: Trestiranje employee kontrolera - integracioni testovi

  Scenario: Kreiranje novog zaposlenog
    Given logovali smo se kao administrator
    When kreiramo zaposlenog sa imenom "Mladen" i prezimenom "Matic", emailom "cehd1234@gmail.com", username "mladen", jmbg "1234567891234", datum rodjenja "123421512", adresa "Bulevar kralja Aleksandra 73", telefon "0651234567",pol "M", rola "2"
    Then povlacimo zaposlenog iz baze i proveravamo da li je kreiran, pretraga po emailu "mmatic11021rn@raf.rs"

  Scenario: Brisanje korisnika koji je prethodno napravljen
    When obrisemo korisnika sa id-em "3"
    Then korisnik sa id-em "3" je obrisan
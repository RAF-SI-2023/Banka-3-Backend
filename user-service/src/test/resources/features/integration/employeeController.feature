Feature: Trestiranje employee kontrolera - integracioni testovi

  Scenario: Kreiranje novog zaposlenog
    Given logovali smo se kao administrator
    When kreiramo zaposlenog sa imenom "Mladen" i prezimenom "Matic", emailom "cehd1234@gmail.com", username "mladen", jmbg "1234567891234", datum rodjenja "123421512", adresa "Bulevar kralja Aleksandra 73", telefon "065123456",pol "M", rola "ROLE_CREDIT_OFFICER"
    Then povlacimo zaposlenog iz baze i proveravamo da li je kreiran, pretraga po emailu "cehd1234@gmail.com"

  Scenario: Brisanje korisnika koji je prethodno napravljen
    When obrisemo korisnika sa id-em "3"
    Then korisnik sa id-em "3" je obrisan


  Scenario: Setovanje lozinke zaposlenom
    When setujemo sifru zaposlenom sa emailom "cehd1234@gmail.com"
    Then proveravamo logovanje zaposlenog sa novom sifrom

  Scenario: Izmene na zaposlenom i provera izmena po email
    When promena adrese zaposlenom sa id-em "3" u "Marka Mrkovica 11"
    Then proveravamo zaposlenog po emailu da li mu je adresa "Marka Mrkovica 11"

  Scenario: Izmene na zaposlenom i provera izmena po username
    When promena adrese zaposlenom sa id-em "3" u "Marka Mrkovica 11"
    Then proveravamo zaposlenog po username-u da li mu je adresa "Marka Mrkovica 11"

  Scenario: Izmene na zaposlenom i provera izmena po search
    When promena adrese zaposlenom sa id-em "3" u "Marka Mrkovica 11"
    Then proveravamo zaposlenog po search-u da li mu je adresa "Marka Mrkovica 11"

  Scenario: Zaposleni je zatrazio sve korisnike
    When zahtev za pregled korisnika
    Then zaposleni dobija listu svih korisnika

#  Scenario: Zaposleni je zatrazio sve  Exchange zaposlene
#    When zahtev za pregled korisnika
#    Then zaposleni dobija listu svih Exchange zaposlenih

    Scenario: Zaposleni se registruje
        When zaposleni se registruje sa emailom "mmatic11021rn@raf.rs"
        Then neuspesno logovanje i neuspesno brisanje
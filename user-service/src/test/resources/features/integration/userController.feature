Feature: Trestiranje user kontrolera - integracioni testovi

  Scenario: Kreiranje novog korisnika
    Given logovali smo se kao admin da bi kreirali korisnika
    When kreiramo korsisnika sa imenom "Pera" i prezimenom "Peric" i emailom "cehd1234@gmail.com" i lozinkom "damir1234", jmbg "1234567891234"
    Then povlacimo korisnika sa emailom "cehd1234@gmail.com"

  Scenario: birsanje korisnika banke od strane zaposlenog
    When zaposleni banke brise korisnika sa id-em "1"
    Then korisnik sa id-em "1" nije aktivan

    Scenario: Logovanje korisnika na sistem
      Given korisnik sa emailom "pera@user.com" i lozinkom "user1234"
      When korisnik setuje sifru "pera1234" i email "pera@user.com"
      Then korisnik se opet loguje "pera@user.com" i lozinkom "pera1234"

    Scenario: Reset passworda i logovanje
      When korisnik resetuje sifru "pera1234" i email "pera@user.com"
      Then korisnik se opet loguje "pera@user.com" i lozinkom "pera1234"

    Scenario: Izmene podataka korisnika
      When promena adrese korisniku sa id-em "2" u "Marka Mrkovica 11"
      Then korisnik sa id-em "2" ima adresu "Marka Mrkovica 11"

    Scenario: Izmene podataka korisnika i pretrazujemo ga po emailu
      When promena adrese korisniku sa id-em "2" u "Marka Mrkovica 11"
      Then korisnik sa emailom "pera@user.com" ima adresu "Marka Mrkovica 11"

    Scenario: Zaposleni pretrazuje sve korisnike
      When zaposleni zeli da vidi sve korisnike
      Then zaposleni vidi sve korisnike

    Scenario: Zaposleni pretrazuje korisnika po odredjenim vrednostima
      When zaposleni zeli da vidi korisnika sa odrednjenim vrednostima
      Then zaposleni pretrazuje korisnika po odredjenim vrednostima

    Scenario: Korisnik se registruje
      When korisnik se registruje sa emailom "dceh9121rn@raf.rs"
      Then neuspesno logovanje korisika
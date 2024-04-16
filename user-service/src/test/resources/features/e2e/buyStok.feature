Feature: E2e test za kupovinu stock-ova na berzi
    Scenario: Kupovina stock-ova na berzi
      Given Korisnik se loguje na aplikaciju kao supervisor sa emailom "salter3@salter.com" i lozinkom "admin1234"
      And preverava balans na racunu
      And pregleda listu svih stock-ova
      When korisnik izabere stock sa liste za kupovinu i unese kolicinu
      Then proverava da li je stock dodat u listu kupljenih stock-ova
      And proverava da li je balans na racunu smanjen za odgovarajucu sumu

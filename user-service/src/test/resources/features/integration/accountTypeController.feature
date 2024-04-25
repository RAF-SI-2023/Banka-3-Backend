Feature: Testiranje userAccount type kontrolera - integracioni testovi

  Scenario: Povlacenje svih tipova naloga
    Given Korisnik je ulogovan
    When Korisnik zatrazi sve tipove naloga
    Then Sistem vraca sve tipove naloga
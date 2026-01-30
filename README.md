Compose tilanhallinta tarkoittaa sitä että UI seuraa suoiraan muuttuvia stateja. Kun state muuttuu, compose päivittää ruudun automaattisesti, jolloin näytöllä aina ajankohtainen tieto.
Remember tallentaa arvon vain niin pitkäksi aikaa kun composable on olemassa. Jos composable poistuu ja se luodaan uudestaan, arvo katoaa. ViewModel sen sijaan säilyttää tilan koko activityn ajan ja UI päivittyy automaattisesti, kun viewModelin tila muuttuu.


MVVM on arkkitehtuurimalli, joka erottaa vastuut eri kerroksiin, jotta koodi pysyy selkeänä, helposti laajennettavana ja testattavana.
StateFlow sisältää aina yhden nykyisen arvon, lähettää tämän arvon heti kaikille kuuntelijoille, lähettää uuden arvon aina kun tila muuttuu.

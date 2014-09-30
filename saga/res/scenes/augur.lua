local hero1 = getHero(0).getName()
local hero2 = getHero(1).getName()

speak(hero1, "Who are you?")
speak("Augur", "That's not important. Nobody passes in or out of Hero unless by my master's command.")
speak(hero2, "Then who is your master?")
speak("Augur", "That's not important. You ask too many questions.")
speak("David", "Look out!")

battle('party_bossHero', false)

speak("Augur", "This world is ancient, pathetic, backwards, yet...")
sceneSwitch('augur_dead', true)

speak("David", "Thanks again for your help.")
speak(hero1, "Now let's see if the path to Hero is open.")

local hero1 = getHero(0).getName()
local hero2 = getHero(1).getName()
local hero3 = getHero(2).getName()
local hero4 = getHero(3).getName()

speak("David", "Help!")
speak(hero2, "You're not a monster!")
speak("David", "Of course not! You have to get me out of here!")
speak(hero4, "We'll try, but who are you?")
speak("David", "It's a long story, but I'm after the artifacts of Hero. Except this place is swarming with bandits and monsters! They have the key here somewhere.")

sceneSwitch('david01', true)

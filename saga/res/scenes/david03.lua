local hero1 = getHero(0).getName()
local hero3 = getHero(2).getName()

speak("David", "Hah... Hah...")
speak(hero1, "David? What are you doing here?")
speak("David", "I swam through this pitch-black cave, I almost drowned, and I was scorched all over by burning-hot geysers.")
speak(hero3, "Maybe we didn't need these artifacts after all...")
speak("David", "But now I'm trapped in this cave, and the door ahead won't open. I think there's someone else here too... someone unfriendly.")
speak(hero1, "We'll get you to safety.")

pathEvent('david', 'hero')
addMember('chara_david')
sceneSwitch('recruited_david', true)

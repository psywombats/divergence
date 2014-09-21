local hero1 = getHero(0).getName()
local hero2 = getHero(1).getName()
local hero3 = getHero(2).getName()
local hero4 = getHero(3).getName()

speak("David", "I'm free! May I ask your name?")
speak(hero1, hero1 .. ".");
speak("David", "My name's David. I'm a professional artifact hunter!")
speak(hero3, "Then you're also searching for the lost city?")
speak("David", "Of course! It looks like you must be too if you have the airseed!")
speak(hero1, "Airseed? You mean this helmet?")
speak("David", "Try it on! Even if you go underwater, you'll still be able to breathe. It must be the airseed.")
speak(hero2, "For a magical artifact it sure looks awfully strange...")
speak("David", "Artifacts come in many shapes and sizes. In fact, here's the artifact I already found... it's a sending stone! I have two, so you can keep one as a reward for freeing me.")

playSound('get')
addItem('key_artifactRadio')
speak("Received the RADIO.")

speak("BORG", "It's one of the elder's sending stones! Where did you get this?")
speak("David", "A woman named Janine gave it to me a long time ago. She said she knew the way to Hero already and didn't need it anymore.")
speak(hero1, "Janine!")

sceneSwitch('borg_leaves_shrine', true)
face('hero', 'SOUTH')

speak("BORG", "That's some good news. " .. hero1 .. ", I'd better go tell Elder we've found his sending stone.")
speak(hero1, "Thanks for your help BORG, we'll go ahead and look for the other two artifacts.")
speak("David", "Not if I find them first! See you around!")

pathEvent('david', 'exit')

speak("BORG", "Goodbye, " .. hero1 .. "!")

pathEvent('borg', 'exit')
sceneSwitch('borg_leaves_shrine', false)

speak(hero4, "Now what?")
speak(hero1, "We have two artifacts, we just need two more.")
sceneSwitch('shrine_done', true)

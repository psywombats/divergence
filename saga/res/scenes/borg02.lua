local hero = getHero(0).getName()
local friend1 = getHero(1).getName()
local friend2 = getHero(2).getName()

speak(friend1, "Are you ready?")
speak(hero, "Yes, we're off!")
speak("????", "Wait!")

sceneSwitch('opening_borg_visible', true)
face('hero', 'NORTH')
path('borg', 20, 27)
face('borg', 'SOUTH')
wait(.4)

speak("BORG", "I'll come with you! It's dangerous out there.")
speak(hero, "Thanks BORG.")

pathEvent("friend1", "hero", false)
pathEvent("friend2", "hero", false)
pathEvent("friend3", "hero", false)
pathEvent("borg", "hero")

sceneSwitch('opening_borg_visible', false)
sceneSwitch('opening_finished', true)
addMember('chara_borg')

speak(hero, "Now let's find the way to Hero!")

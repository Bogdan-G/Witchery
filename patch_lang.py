import re

with open('src/main/resources/assets/witchery/lang/en_US.lang', 'r') as f:
    content = f.read()

new_rituals_list = """[br]> [url=ritualreparo Reparo][br]> [url=ritualidentify Identify][br]> [url=ritualaparecium Aparecium][br]> [url=ritualvociferador Vociferador][br]> [url=rituallumosmaxima Lumos Maxima][br]> [url=ritualdimensionalanchor Dimensional Anchor][br]> [url=ritualempaticlink Empatic Link][br]> [url=ritualherbivicus Herbivicus][br]> [url=ritualmagicalprison Magical Prison]"""

content = content.replace(
    "[br]> [url=ritualfind Find Structure]",
    "[br]> [url=ritualfind Find Structure]" + new_rituals_list
)

new_rituals_text = """
witchery:cauldronbook.ritualreparo=[h1 Ritual: Reparo]Repair damaged items. Draw a line of 5 white chalk:[br][img=witchery:textures/gui/circles_tinywhite.png|center|top|32|32][br]Drop a Diamond and Spectral Dust.
witchery:cauldronbook.ritualidentify=[h1 Ritual: Identify]Identify an unknown item. Draw a polygon of 4 white chalk:[br][img=witchery:textures/gui/circles_tinywhite.png|center|top|32|32][br]Drop an Eye of Ender and Exhale of the Horned One.
witchery:cauldronbook.ritualaparecium=[h1 Ritual: Aparecium]Reveal hidden things. Draw a polygon of 3 white chalk:[br][img=witchery:textures/gui/circles_tinywhite.png|center|top|32|32][br]Drop a Golden Carrot and Tear of the Goddess.
witchery:cauldronbook.ritualvociferador=[h1 Ritual: Vociferador]Create a howler. Draw a line of 5 white chalk:[br][img=witchery:textures/gui/circles_tinywhite.png|center|top|32|32][br]Drop a Jukebox and Mandrake Root.
witchery:cauldronbook.rituallumosmaxima=[h1 Ritual: Lumos Maxima]Create a powerful light. Draw a polygon of 5 white chalk:[br][img=witchery:textures/gui/circles_tinywhite.png|center|top|32|32][br]Drop Glowstone Dust and Breath of the Goddess.
witchery:cauldronbook.ritualdimensionalanchor=[h1 Ritual: Dimensional Anchor]Anchor a dimension. Draw a polygon of 5 red chalk:[br][img=witchery:textures/gui/circles_tinyred.png|center|top|32|32][br]Drop an Ender Pearl, Obsidian, and Charged Attuned Stone.
witchery:cauldronbook.ritualempaticlink=[h1 Ritual: Empatic Link]Create an empathic link. Draw a polygon of 6 infernal chalk:[br][img=witchery:textures/gui/circles_tinyinfernal.png|center|top|32|32][br]Drop a Golden Apple, Drop of Luck, and Brew of Love.
witchery:cauldronbook.ritualherbivicus=[h1 Ritual: Herbivicus]Accelerate plant growth. Draw a polygon of 4 white chalk:[br][img=witchery:textures/gui/circles_tinywhite.png|center|top|32|32][br]Drop Bonemeal and Mutandis Extremis.
witchery:cauldronbook.ritualmagicalprison=[h1 Ritual: Magical Prison]Trap an entity. Draw a polygon of 4 red chalk:[br][img=witchery:textures/gui/circles_tinyred.png|center|top|32|32][br]Drop a Web and Soul Sand.
"""

content = content.replace(
    "witchery:cauldronbook.ritualfind=[h1 Ritual: Find Structure]Summon a spirit that flys towards the closest village (or nether fortress). Draw a 3x3 white-chalk circle:[br][img=witchery:textures/gui/circles_tinywhite.png|center|top|32|32][br]Drop a Subdued Spirit or Attuned Stone in the center and wait.",
    "witchery:cauldronbook.ritualfind=[h1 Ritual: Find Structure]Summon a spirit that flys towards the closest village (or nether fortress). Draw a 3x3 white-chalk circle:[br][img=witchery:textures/gui/circles_tinywhite.png|center|top|32|32][br]Drop a Subdued Spirit or Attuned Stone in the center and wait." + new_rituals_text
)

with open('src/main/resources/assets/witchery/lang/en_US.lang', 'w') as f:
    f.write(content)

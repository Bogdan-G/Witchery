import re

file_path = "/Users/brianchirinos/Documents/WitcheryRepo/src/main/resources/assets/witchery/lang/en_US.lang"

with open(file_path, "r") as f:
    lines = f.readlines()

new_lines = []
for line in lines:
    if line.startswith("witchery:cauldronbook.level2_9="):
        line = line.strip() + "[br][stack=glass_bottle][url=spectralsight|middle Spectral Sight]\n"
    elif line.startswith("witchery:cauldronbook.level4_6="):
        line = line.strip() + "[br][stack=name_tag][url=comprehension|middle Comprehension][br][stack=packed_ice][url=glaciate|middle Glaciate][br][stack=sandstone][url=glasswork|middle Glasswork]\n"
    elif line.startswith("witchery:cauldronbook.level5_2="):
        line = line.strip() + "[br][stack=melon][url=lifesteal|middle Lifesteal][br][stack=fire_charge][url=berserk|middle Berserk]\n"
    elif line.startswith("witchery:cauldronbook.level6_2="):
        line = line.strip() + "[br][stack=glass_pane][url=phasewalk|middle Phasewalk][br][stack=experience_bottle][url=manasiphon|middle Mana Siphon][br][stack=stonebrick][url=petrify|middle Petrify]\n"
    elif line.startswith("witchery:cauldronbook.level8="):
        line = line.strip() + "[br][stack=clock][url=soultether|middle Soul Tether]\n"
    
    new_lines.append(line)

# Now we must append the actual localized names for these keys if they are not already there
# and also the descriptions for the book pages!
descriptions = """
witchery:cauldronbook.spectralsight=[h1 Effect: Spectral Sight]Grants the ability to see spectral and invisible entities.
witchery:cauldronbook.comprehension=[h1 Effect: Comprehension]Allows you to understand languages you normally wouldn't.
witchery:cauldronbook.glaciate=[h1 Effect: Glaciate]Freezes water into ice and lava into obsidian within a large radius.
witchery:cauldronbook.glasswork=[h1 Effect: Glasswork]Turns sand into glass within a radius.
witchery:cauldronbook.lifesteal=[h1 Effect: Lifesteal]Drains health from enemies and heals the caster.
witchery:cauldronbook.berserk=[h1 Effect: Berserk]Greatly increases damage dealt but lowers defense.
witchery:cauldronbook.phasewalk=[h1 Effect: Phasewalk]Allows the imbiber to pass through solid obstacles temporarily.
witchery:cauldronbook.manasiphon=[h1 Effect: Mana Siphon]Drains magical energy from targets to restore the caster's.
witchery:cauldronbook.petrify=[h1 Effect: Petrify]Roots the target to the spot and turns their skin to stone.
witchery:cauldronbook.soultether=[h1 Effect: Soul Tether]Tethers the target's soul to yours, pulling them to you across dimensions.
"""

# Let's add them at the end of the file
new_lines.append(descriptions)

with open(file_path, "w") as f:
    f.writelines(new_lines)

print("en_US.lang updated successfully!")

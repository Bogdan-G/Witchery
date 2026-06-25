import re

file_path = "/Users/brianchirinos/Documents/WitcheryRepo/src/main/java/com/emoniph/witchery/infusion/infusions/symbols/EffectRegistry.java"

with open(file_path, "r") as f:
    content = f.read()

# Pattern 1: if (mop.typeOfHit -> if (mop != null && mop.typeOfHit
content = re.sub(r'if \(\s*mop\.typeOfHit', 'if (mop != null && mop.typeOfHit', content)
content = re.sub(r'else if \(\s*mop\.typeOfHit', 'else if (mop != null && mop.typeOfHit', content)

# Pattern 2: int cx = mop.typeOfHit == -> if (mop == null) return; int cx = mop.typeOfHit == 
content = re.sub(r'(int cx = mop\.typeOfHit == MovingObjectPosition\.MovingObjectType\.BLOCK \? mop\.blockX : \(int\)spell\.posX;)', r'if (mop == null) return;\n                \1', content)

with open(file_path, "w") as f:
    f.write(content)
print("Done")

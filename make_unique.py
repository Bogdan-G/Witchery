import re
import sys

def make_unique(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Find all StrokeSets
    matches = list(re.finditer(r'new\s+StrokeSet\s*\(\s*(\d+)\s*,\s*new\s+byte\s*\[\s*\]\s*\{([^}]+)\}\s*\)', content))
    
    strokes = []
    for m in matches:
        level = int(m.group(1))
        bytes_str = m.group(2)
        stroke_list = [int(x.strip()) for x in bytes_str.replace('(byte)', '').split(',')]
        strokes.append((m, stroke_list))
        
    prefixes = set()
    
    for i in range(len(strokes)):
        for j in range(len(strokes)):
            if i == j: continue
            str1 = strokes[i][1]
            str2 = strokes[j][1]
            
            if len(str1) < len(str2):
                if str2[:len(str1)] == str1:
                    prefixes.add(i)

    # We will modify the shorter strokes by appending one stroke to them.
    # We will append `(byte)3` (which is typically 'Left') or something else if it creates another conflict.
    # Let's just append `(byte)X` where X is the opposite of the next byte in the longer spell, 
    # but actually just appending a byte is fine as long as we check if it conflicts.
    
    all_stroke_tuples = {tuple(s[1]) for s in strokes}
    
    replacements = []
    for idx in prefixes:
        m, s = strokes[idx]
        
        # Try appending 0, 1, 2, 3 until we find one that is not a prefix of anything, and doesn't exist.
        for candidate in [0, 1, 2, 3]:
            new_stroke = s + [candidate]
            # check if new_stroke is a prefix of any other
            is_prefix = False
            for j in range(len(strokes)):
                if idx == j: continue
                s2 = strokes[j][1]
                if len(new_stroke) <= len(s2) and s2[:len(new_stroke)] == new_stroke:
                    is_prefix = True
                    break
            
            if not is_prefix and tuple(new_stroke) not in all_stroke_tuples:
                replacements.append((m, new_stroke))
                all_stroke_tuples.add(tuple(new_stroke))
                all_stroke_tuples.remove(tuple(s))
                break

    # Now we apply replacements in reverse order of match position to not mess up offsets.
    replacements.sort(key=lambda x: x[0].start(), reverse=True)
    
    for m, new_stroke in replacements:
        bytes_str = ",".join(f"(byte){b}" for b in new_stroke)
        replacement_str = f"new StrokeSet({m.group(1)}, new byte[]{{{bytes_str}}})"
        content = content[:m.start()] + replacement_str + content[m.end():]
        
    with open(filepath, 'w') as f:
        f.write(content)

    print(f"Made {len(replacements)} strokes unique.")

if __name__ == "__main__":
    make_unique(sys.argv[1])

import re
import sys

def parse_strokesets(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Find all SymbolEffect additions.
    # We will look for new StrokeSet(..., new byte[]{...})
    strokes = []
    
    # Regex to match new StrokeSet(level, new byte[]{(byte)X, (byte)Y, ...})
    # We want to keep track of the spell name if possible, or just the byte arrays.
    
    # Actually, we can just find all new byte[]{(byte)X, ...} inside StrokeSet
    matches = re.finditer(r'new\s+StrokeSet\s*\(\s*(\d+)\s*,\s*new\s+byte\s*\[\s*\]\s*\{([^}]+)\}\s*\)', content)
    
    for m in matches:
        level = int(m.group(1))
        bytes_str = m.group(2)
        # Extract numbers
        nums = re.findall(r'\d+', bytes_str)
        # The first number in each (byte)X is the stroke direction.
        # Wait, the regex \d+ will capture the stroke numbers.
        # Let's just strip (byte) and spaces.
        stroke_list = [int(x.strip()) for x in bytes_str.replace('(byte)', '').split(',')]
        
        strokes.append((m.group(0), stroke_list, m.start(), m.end()))
        
    return strokes

def main():
    filepath = sys.argv[1]
    strokes = parse_strokesets(filepath)
    
    prefixes = []
    
    for i in range(len(strokes)):
        for j in range(len(strokes)):
            if i == j:
                continue
            
            str1 = strokes[i][1]
            str2 = strokes[j][1]
            
            if len(str1) < len(str2):
                if str2[:len(str1)] == str1:
                    prefixes.append((strokes[i], strokes[j]))
                    
    # Print out the prefixes to modify.
    seen = set()
    for p1, p2 in prefixes:
        # We only need to modify p1 (the shorter one) to make it not a prefix, OR p2.
        # The user wants them unique. Usually changing the last stroke of the shorter one, 
        # or just adding a stroke to the shorter one.
        # Actually we should just print them out.
        if tuple(p1[1]) not in seen:
            print(f"Prefix: {p1[1]} (Found in {p1[0]})")
            print(f"  Is prefix of: {p2[1]}")
            seen.add(tuple(p1[1]))

if __name__ == "__main__":
    main()

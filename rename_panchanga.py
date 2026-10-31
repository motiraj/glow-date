import os
import re

project_dir = 'C:/Users/gauta/AndroidStudioProjects/BSADDateConverterAgeCalculator/app/src/main'

# Files to rename
file_renames = [
    ('java/com/example/bs_addateconverteragecalculator/data/PanchangCalculator.kt', 'java/com/example/bs_addateconverteragecalculator/data/PanchangaCalculator.kt'),
    ('java/com/example/bs_addateconverteragecalculator/data/PanchangSyncService.kt', 'java/com/example/bs_addateconverteragecalculator/data/PanchangaSyncService.kt'),
    ('java/com/example/bs_addateconverteragecalculator/ui/panchang', 'java/com/example/bs_addateconverteragecalculator/ui/panchanga'),
    ('java/com/example/bs_addateconverteragecalculator/ui/panchanga/PanchangScreen.kt', 'java/com/example/bs_addateconverteragecalculator/ui/panchanga/PanchangaScreen.kt')
]

for old, new in file_renames:
    old_path = os.path.join(project_dir, old)
    new_path = os.path.join(project_dir, new)
    if os.path.exists(old_path):
        os.rename(old_path, new_path)
        print(f"Renamed {old_path} to {new_path}")

# Now replace text in all kt and xml files
for root, dirs, files in os.walk(project_dir):
    for file in files:
        if file.endswith('.kt') or file.endswith('.xml'):
            filepath = os.path.join(root, file)
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()

            # Replace occurrences
            new_content = content.replace('Panchang', 'Panchanga')
            new_content = new_content.replace('panchang', 'panchanga')
            new_content = new_content.replace('PANCHANG', 'PANCHANGA')

            # Fix double replacements just in case it was already Panchanga -> Panchangaa
            new_content = new_content.replace('Panchangaa', 'Panchanga')
            new_content = new_content.replace('panchangaa', 'panchanga')

            if new_content != content:
                with open(filepath, 'w', encoding='utf-8') as f:
                    f.write(new_content)
                print(f"Updated {filepath}")

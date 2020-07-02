DIC = {'asd': 'hello', 'grade': 3}
asdf="asdf{asd} {grade}"
print(asdf.format_map(DIC))
print(asdf.format(**DIC))
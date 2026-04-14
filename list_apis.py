import json

with open('openapi.json', encoding='utf-8') as f:
    d = json.load(f)

for path, methods in d.get('paths', {}).items():
    for method, op in methods.items():
        if method not in ['get', 'post', 'put', 'delete', 'patch']: continue
        print(f"{method.upper()} {path} - {op.get('summary')}")

import json
import requests

with open('openapi.json', encoding='utf-8') as f:
    d = json.load(f)

# 1. Add securitySchemes
if 'components' not in d:
    d['components'] = {}

d['components']['securitySchemes'] = {
    "bearerAuth": {
        "type": "http",
        "scheme": "bearer",
        "bearerFormat": "JWT"
    }
}

# Endpoints that are PUBLIC (no auth required)
public_endpoints = {
    ('post', '/api/v1/users/login'),
    ('post', '/api/v1/users/register'),
    ('get', '/api/v1/products'),
    ('get', '/api/v1/products/{id}'),
    ('get', '/api/v1/scenarios'),
    ('get', '/api/v1/scenarios/{id}'),
}

# 2. Fix requestBody of GET /api/v1/products and security of all paths
paths = d.get('paths', {})
for path, methods in paths.items():
    for method, op in methods.items():
        if method not in ['get', 'post', 'put', 'delete', 'patch']: continue
        
        # Issue 1: remove requestBody from GET /api/v1/products
        if path == '/api/v1/products' and method == 'get':
            if 'requestBody' in op:
                del op['requestBody']
        
        # Also remove requestBody from any other GET request if it matches the same issue
        if method == 'get':
            if 'requestBody' in op:
                del op['requestBody']
        
        # Issue 2: fix security
        if (method, path) in public_endpoints:
            op['security'] = []
        else:
            op['security'] = [{"bearerAuth": []}]

with open('openapi_patched.json', 'w', encoding='utf-8') as f:
    json.dump(d, f, ensure_ascii=False, indent=2)

# Post back to Apifox
url = 'https://api.apifox.com/v1/projects/8090070/import-openapi?locale=zh-CN'
headers = {
    'Authorization': 'Bearer afxp_7ae1d9og8KlOA7Wr1L3G0H9NsivGAdPadXH9',
    'X-Apifox-Api-Version': '2024-03-28',
    'Content-Type': 'application/json'
}
import_data = {
    "input": json.dumps(d),
    "options": {
        "targetEndpointFolderId": 0,
        "targetSchemaFolderId": 0,
        "endpointOverwriteBehavior": "OVERWRITE_EXISTING",
        "schemaOverwriteBehavior": "OVERWRITE_EXISTING"
    }
}
r = requests.post(url, headers=headers, json=import_data)
print(r.status_code)
print(r.text)

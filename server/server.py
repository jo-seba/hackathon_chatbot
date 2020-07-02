from http.server import HTTPServer, BaseHTTPRequestHandler
from urllib.parse import urlparse, parse_qs
from io import BytesIO
import json

class SimpleHTTPRequestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        # [1] 직접 url 파라미터 받아보기
        # print(self.path)
        # if '?' in self.path:
        #     urls = self.path.split('?')
        #     path = urls[0]
        #     qs = urls[1].split('&')
        #     print(path, qs)

        # [2] url parse 사용으로 파라미터 받기
        result = urlparse(self.path)
        params = parse_qs(result.query)
        print(params)
        print(params['id'][0])
        print(params['password'][0])

        self.send_response(200)
        self.send_header('Content-Type', 'text/html; charset=utf-8')
        self.end_headers()
        self.wfile.write('<h1>안녕하세요</h1>'.encode('utf-8'))

    #def do_GET(self):
    #    self.send_response(200)
    #    self.end_headers()
    #    self.wfile.write(b'Hello, world!')

    def do_POST(self):
        content_length = int(self.headers['Content-Length'])
        body = self.rfile.read(content_length)
        ubody = body.decode('ascii')
        print(ubody)
        
        # json data
        json_data = json.loads(ubody)
        print(json_data)
        
        # ordinary data
        #params = parse_qs(ubody)
        #print(params)
        
        self.send_response(200)
        self.end_headers()
        response = BytesIO()
        response.write(b'This is POST request.')
        response.write(b'Received: ')
        response.write(body)
        self.wfile.write(response.getvalue())

def main():
    IP = '192.168.0.6'
    PORT = 8080
    httpApp = HTTPServer((IP, PORT), SimpleHTTPRequestHandler)
    print(f'Server running on http://{IP}:{PORT}/')
    httpApp.serve_forever()

if __name__ == "__main__":
    main()
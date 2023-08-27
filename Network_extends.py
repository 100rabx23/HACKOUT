import socket
import threading

class MeshNode:
    def __init__(self, ip, port):
        self.ip = ip
        self.port = port
        self.peers = []
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server_socket.bind((self.ip, self.port))
        self.server_socket.listen(5)
        self.listen_thread = threading.Thread(target=self.listen_for_connections)
        self.listen_thread.start()

    def listen_for_connections(self):
        while True:
            client_socket, _ = self.server_socket.accept()
            self.peers.append(client_socket)
            threading.Thread(target=self.handle_peer, args=(client_socket,)).start()

    def handle_peer(self, client_socket):
        while True:
            data = client_socket.recv(1024)
            if not data:
                self.peers.remove(client_socket)
                client_socket.close()
                break
            self.broadcast(data, sender=client_socket)

    def broadcast(self, data, sender):
        for peer_socket in self.peers:
            if peer_socket != sender:
                try:
                    peer_socket.send(data)
                except:
                    self.peers.remove(peer_socket)
                    peer_socket.close()

if __name__ == "__main__":
    node = MeshNode('127.0.0.1', 12345)

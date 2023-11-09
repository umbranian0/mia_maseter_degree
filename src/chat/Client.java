package chat;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
	/*
	
	private final Thread runner;
	private Socket socket;
	private Chat chat;

	public Client(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		runner = new Thread(this);
		runner.start();
	}

	public void run() {
		DataInputStream din = new DataInputStream(socket.getInputStream());
		try {
			do {
				String line = din.readLine();
				if ((line != null)) {
					chat.addLine(line);

				}

			} while (line != null);
			din.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	*/

}

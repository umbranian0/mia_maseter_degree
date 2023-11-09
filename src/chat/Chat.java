package chat;
import java.util.concurrent.CopyOnWriteArrayList;

public class Chat {
	private CopyOnWriteArrayList<String> list;
	public Chat() {
		// TODO Auto-generated constructor stub
		list = new CopyOnWriteArrayList<>();
	}

	public synchronized void addLine(String line) {
		list.add(line);
		if(list.size()>50)
			list.remove(0);
	}
	
	public synchronized CopyOnWriteArrayList<String> getList(){
		return list;
	}

}

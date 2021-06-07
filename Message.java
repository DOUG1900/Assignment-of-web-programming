public class Message {
	int totalLength;
	int commandID;
	//对应四个消息类型，
	//1: registration request msg
	//2: registration response msg
	//3: login request msg
	//4: login response msg
	
	Message(int len, int ID){
		this.totalLength = len;
		this.commandID = ID;
	}
	Message(){
		this.totalLength = 0;
		this.commandID = 0;
	}
	
	public int getLength(){
		return this.totalLength;
	}
	
	public int getID(){
		return this.commandID;
	}

}

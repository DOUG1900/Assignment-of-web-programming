public class ReqMsg extends Message{ //注册请求消息
	OctetString userName;
	OctetString passWord;
	ReqMsg(int ID, int len, String username, String passwd){
		super();
		this.commandID = ID;
		this.totalLength = len;
		this.userName = new OctetString(20, username);
		this.passWord = new OctetString(30, passwd);
	}
	ReqMsg(){
		super();
		this.commandID = 0;
		this.totalLength = 58;
		this.userName = null;
		this.passWord = null;
	}
	
	public String getUsername(){
		return this.userName.getValue();
	}
	
	public String getPassword(){
		return this.passWord.getValue();
	}
	public byte[] getAll(){ //将所有部分合并成一个byte[]
		byte[] btstream = new byte[this.totalLength];
		byte[] bt1 = new byte[4]; //消息头第一部分，总长度
		byte[] bt2 = new byte[4]; //消息头第二部分，ID
		byte[] bt3 = new byte[20]; //username
		byte[] bt4 = new byte[30]; //passwd
		
		ByteStream  bs = new ByteStream();  //将各部分信息转化为字节数组
		bs.Int2Byte(bt1, this.totalLength);
		bs.Int2Byte(bt2, this.commandID);
		bt3 = this.getUsername().getBytes();
		bt4 = this.getPassword().getBytes();
		
		btstream = bs.ConnectBytes(bt1, bt2);  //合并成一个字节数组
		btstream = bs.ConnectBytes(btstream, bt3);
		btstream = bs.ConnectBytes(btstream, bt4);
		
		return btstream;
	}
	
}
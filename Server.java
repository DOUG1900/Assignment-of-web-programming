import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.Date;


public class Server {
	
	class Worker implements Runnable {
		//为连入的客户端打开的套接口
		Socket socket;
		DataOutputStream dos;
		Worker(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			int p1 = 4, p2 = 8, p3 = 28;
			try {
				//打开sock输入输出流
				BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
				DataInputStream dis = new DataInputStream(bis);
				ByteStream bs = new ByteStream();
				byte[] len = new byte[4];  //totalLength
				byte[] ID = new byte[4];   //commandID
				byte[] username = new byte[20];  //username
				byte[] password = new byte[30];   //passwd
				byte[] bytes = new byte[58];
				if(dis.read(bytes) != -1){
					bs.Split(bytes, len, p1, ID, p2, username, p3, password);
				}
				int length = bs.Byte2Int(len);
				int id = bs.Byte2Int(ID);
				String temp1[] = new String(username).split("\0");  //读到\0说明字符串已读完，剩余为空白填补
				String usernm = temp1[0];
				String temp2[] = new String(password).split("\0");
				String passwd = temp2[0];
				RespMsg response = new RespMsg();
				response.totalLength = length;
				response.commandID = id + 1;
				String filename = "../User/src/user/userinfo.txt";
				String logfile = "../User/src/user/log.txt";
				String stat_dsp;
				Map info = readfile(filename);
				if(id == 1){ //注册
					if(info.get(usernm) == null){
						//Thread.sleep(20000);
						writefile(filename, usernm, passwd);
						stat_dsp = new String("Successfully Register");
						response.description = new OctetString(64, stat_dsp);
						response.status = new OctetString(1, "1");
					}
					else {
						stat_dsp = new String("The Username Exists");
						response.description = new OctetString(64, stat_dsp);
						response.status = new OctetString(1, "0");
					}
				}
				else if(id == 3){ //登录验证
					if(info.get(usernm) == null){
						stat_dsp = new String("The User Dose not Exists");
						response.description = new OctetString(64, stat_dsp);
						response.status = new OctetString(1, "0");
					}
					else if (info.get(usernm).equals(String.valueOf(passwd.hashCode()))){
						stat_dsp = new String("Successfully Log in");
						response.description = new OctetString(64, stat_dsp);
						response.status = new OctetString(1, "1");
					}
					else{
						stat_dsp = new String("Password Error");
						response.description = new OctetString(64, stat_dsp);
						response.status = new OctetString(1, "0");
					}
				}
				else {
					stat_dsp = new String("Option Information Error");
					response.description = new OctetString(64, stat_dsp);
				}
				//回复客户端信息
				//Thread.sleep(20000);
				msgprint(logfile, id, stat_dsp);
				DataOutputStream dos = new DataOutputStream(this.socket.getOutputStream());
				byte[] respmsg = response.getAll();
				dos.write(respmsg);
				
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
	
	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.launch();
	}
	
	
	public static void msgprint(String filename, int id, String stat_dsp)throws IOException{ 
		BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
		try{
			String msglog = new String();
			String op;
			if(id == 1){
				op = new String("Register");
			}
			else if(id == 3){
				op = new String("Log in");
			}
			else{
				op = new String("Error");
			}
			msglog = "[" + (new Date()) + "] " + op + ": "+ stat_dsp;
			System.out.println(msglog);
            out.write(msglog + "\n");
        } catch (IOException e) {
        	System.out.println(e.getMessage());
        } finally{
        	out.close();
        }	
		
	}
	
	public static Map readfile(String filename) throws IOException { 
		//读用户信息文件，返回一个用户名与密码哈希值的映射表Map
		FileInputStream inputStream = new FileInputStream(filename);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String[] strvalue;
		Map userinfo = new HashMap();
		String str;
		try{
			while((str = bufferedReader.readLine()) != null)
			{
				strvalue = str.split("\t");
				userinfo.put(strvalue[0], strvalue[1]);
			}
			return userinfo;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}finally{
			inputStream.close();
			bufferedReader.close();
		}
	}
	
	public static void writefile(String filename, String username, String passwd)throws IOException{
		//写用户信息文件，保存新的用户名和密码，写入的是密码的哈希值
		BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
		try{
			String result = username+'\t'+passwd.hashCode()+'\n';
            out.write(result);
        } catch (IOException e) {
        	System.out.println(e.getMessage());
        } finally{
        	out.close();
        }		
	}
	
	
	
	void launch() throws IOException {
		int portNumber = 12001;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			
			while(true){
				Socket socket = serverSocket.accept();
				(new Thread(new Worker(socket))).start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally{
			serverSocket.close();
		}		
	}
}


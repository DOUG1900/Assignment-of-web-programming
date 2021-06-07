public class ByteStream {
	byte bytes[];  //byte数组，保存内容
	int length;    //长度
	ByteStream(){
		this.length = 0;
		this.bytes = null;
	}
	
	ByteStream(int len, int num){
		bytes = new byte[len];
		Int2Byte(this.bytes,num);
	}
	
	ByteStream(int len, String str){
		bytes = new byte[len];
		this.bytes = str.getBytes();
	}
	
	public byte[] ConnectBytes(byte[] bt1, byte[] bt2){
		byte[] result = new byte[bt1.length + bt2.length];
		System.arraycopy(bt1, 0, result, 0, bt1.length);  
        System.arraycopy(bt2, 0, result, bt1.length, bt2.length); 
		
        return result;
	}
	
	public void Split(byte[] bt, byte[] bt1, int p1, byte[] bt2, int p2, byte[] bt3, int p3, byte[] bt4){
		//按照不同的消息格式分割消息内容，bt为原比特流数据，bt1-bt4用来顺序保存各段数据，p1-p3是三个分割点的位置
		for(int i = p1-1; i >= 0; i--){
			bt1[i] = bt[i];
		}
		for(int i = p2-1; i >= p1; i--){
			bt2[i-p1] = bt[i];
		}
		for(int i = p3-1; i >= p2; i--){
			bt3[i-p2] = bt[i];
		}
		for(int i = bt.length-1; i >= p3; i--){
			bt4[i-p3] = bt[i];
		}
	}
	
	public void Int2Byte(byte[] bytes, int temp) { //将int数转化为byte
        bytes[0] = (byte)(temp >>> 24) ;//bytes[0]表示一个int值的最高8位
        bytes[1] = (byte)(temp >>> 16);//bytes[1]表示一个int值的接下来的8位
        bytes[2] = (byte)(temp >>> 8);//bytes[2]表示一个int值的再接下来的8位
        bytes[3] = (byte)(temp);      //bytes[3]表示一个int值的最低8位
    }
	public int Byte2Int(byte[] bytes) {  // 将byte转化为int
        return (bytes[0] << 24)           | //还原int值最高8位
                ((bytes[1] & 0xff) << 16) | //还原int值接下来的8位
                ((bytes[2] & 0xff) << 8 ) |//还原int值再接下来的8位
                (bytes[3] & 0xff);         //还原int值的最低8位
    }

}

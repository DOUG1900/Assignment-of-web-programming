public class OctetString {
	int length;
	char value[];
	
	OctetString() {
		this.length = 128;
		this.value = null;
	}
	
	OctetString(int len, String str) {
		this.length = len;
		this.value = new char[this.length];
		
		for(int i = 0; i < str.length(); i++) {
			this.value[i] = str.charAt(i);
		}
		for(int i = str.length(); i < this.length; i++) {
			this.value[i] = '\0';
		}
		
	}
	
	public int getLength(){
		return this.length;
	}
	
	public String getValue(){
		return String.valueOf(value);
	}
}
